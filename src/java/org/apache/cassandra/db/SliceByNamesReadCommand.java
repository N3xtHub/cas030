
public class SliceByNamesReadCommand extends ReadCommand
{
    public final String columnFamily;
    public final List<String> columnNames;

    public SliceByNamesReadCommand(String table, String key, String columnFamily, List<String> columnNames)
    {
        super(table, key, CMD_TYPE_GET_SLICE_BY_NAMES);
        this.columnFamily = columnFamily;
        this.columnNames = Collections.unmodifiableList(columnNames);
    }

    @Override
    public String getColumnFamilyName()
    {
        return columnFamily;
    }

    @Override
    public ReadCommand copy()
    {
        ReadCommand readCommand= new SliceByNamesReadCommand(table, key, columnFamily, columnNames);
        readCommand.setDigestQuery(isDigestQuery());
        return readCommand;
    }
    
    @Override
    public Row getRow(Table table) throws IOException
    {        
        return table.getRow(key, columnFamily, columnNames);
    }

    @Override
    public String toString()
    {
        return "GetSliceByNamesReadMessage(" +
               "table='" + table + '\'' +
               ", key='" + key + '\'' +
               ", columnFamily='" + columnFamily + '\'' +
               ", columns=[" + StringUtils.join(columnNames, ", ") + "]" +
               ')';
    }

}

class SliceByNamesReadCommandSerializer extends ReadCommandSerializer
{
    @Override
    public void serialize(ReadCommand rm, DataOutputStream dos) throws IOException
    {
        SliceByNamesReadCommand realRM = (SliceByNamesReadCommand)rm;
        dos.writeBoolean(realRM.isDigestQuery());
        dos.writeUTF(realRM.table);
        dos.writeUTF(realRM.key);
        dos.writeUTF(realRM.columnFamily);
        dos.writeInt(realRM.columnNames.size());
        if (realRM.columnNames.size() > 0)
        {
            for (String cName : realRM.columnNames)
            {
                dos.writeInt(cName.getBytes().length);
                dos.write(cName.getBytes());
            }
        }
    }

    @Override
    public ReadCommand deserialize(DataInputStream dis) throws IOException
    {
        boolean isDigest = dis.readBoolean();
        String table = dis.readUTF();
        String key = dis.readUTF();
        String columnFamily = dis.readUTF();

        int size = dis.readInt();
        List<String> columns = new ArrayList<String>();
        for (int i = 0; i < size; ++i)
        {
            byte[] bytes = new byte[dis.readInt()];
            dis.readFully(bytes);
            columns.add(new String(bytes));
        }
        SliceByNamesReadCommand rm = new SliceByNamesReadCommand(table, key, columnFamily, columns);
        rm.setDigestQuery(isDigest);
        return rm;
    }
}
