

public abstract class ReadCommand
{
    public static final String DO_REPAIR = "READ-REPAIR";
    public static final byte CMD_TYPE_GET_ROW=1;
    public static final byte CMD_TYPE_GET_COLUMN=2;
    public static final byte CMD_TYPE_GET_SLICE_BY_NAMES=3;
    public static final byte CMD_TYPE_GET_COLUMNS_SINCE=4;
    public static final byte CMD_TYPE_GET_SLICE=5;
    public static final String EMPTY_CF = "";
    
    private static ReadCommandSerializer serializer = new ReadCommandSerializer();

    public static ReadCommandSerializer serializer()
    {
        return serializer;
    }

    public Message makeReadMessage() throws IOException
    {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);
        ReadCommand.serializer().serialize(this, dos);
        return new Message(StorageService.getLocalStorageEndPoint(), StorageService.readStage_, StorageService.readVerbHandler_, bos.toByteArray());
    }

    public final String table;
    public final String key;
    private boolean isDigestQuery = false;    
    protected final byte commandType;

    protected ReadCommand(String table, String key, byte cmdType)
    {
        this.table = table;
        this.key = key;
        this.commandType = cmdType;
    }
    
    public boolean isDigestQuery()
    {
        return isDigestQuery;
    }

    public void setDigestQuery(boolean isDigestQuery)
    {
        this.isDigestQuery = isDigestQuery;
    }

    public abstract String getColumnFamilyName();
    
    public abstract ReadCommand copy();

    public abstract Row getRow(Table table) throws IOException;
}

class ReadCommandSerializer implements ICompactSerializer<ReadCommand>
{
    private static final Map<Byte, ReadCommandSerializer> CMD_SERIALIZER_MAP = new HashMap<Byte, ReadCommandSerializer>(); 
    static 
    {
        CMD_SERIALIZER_MAP.put(ReadCommand.CMD_TYPE_GET_ROW, new RowReadCommandSerializer());
        CMD_SERIALIZER_MAP.put(ReadCommand.CMD_TYPE_GET_COLUMN, new ColumnReadCommandSerializer());
        CMD_SERIALIZER_MAP.put(ReadCommand.CMD_TYPE_GET_SLICE_BY_NAMES, new SliceByNamesReadCommandSerializer());
        CMD_SERIALIZER_MAP.put(ReadCommand.CMD_TYPE_GET_COLUMNS_SINCE, new ColumnsSinceReadCommandSerializer());
        CMD_SERIALIZER_MAP.put(ReadCommand.CMD_TYPE_GET_SLICE, new SliceReadCommandSerializer());
    }


    public void serialize(ReadCommand rm, DataOutputStream dos) throws IOException
    {
        dos.writeByte(rm.commandType);
        ReadCommandSerializer ser = CMD_SERIALIZER_MAP.get(rm.commandType);
        ser.serialize(rm, dos);
    }

    public ReadCommand deserialize(DataInputStream dis) throws IOException
    {
        byte msgType = dis.readByte();
        return CMD_SERIALIZER_MAP.get(msgType).deserialize(dis);
    }
        
}
