
public class SliceReadCommand extends ReadCommand
{
    /* for a slice of a standard column, cFC should only be the CF name.
       for a supercolumn slice, it will be CF:supercolumn. */
    public final String columnFamilyColumn;
    public final int start;
    public final int count;

    public SliceReadCommand(String table, String key, String columnFamilyColumn, int start, int count)
    {
        super(table, key, CMD_TYPE_GET_SLICE);
        this.columnFamilyColumn = columnFamilyColumn;
        this.start = start;
        this.count = count;
    }

    @Override
    public String getColumnFamilyName()
    {
        return RowMutation.getColumnAndColumnFamily(columnFamilyColumn)[0];
    }

    @Override
    public ReadCommand copy()
    {
        ReadCommand readCommand = new SliceReadCommand(table, key, columnFamilyColumn, start, count);
        readCommand.setDigestQuery(isDigestQuery());
        return readCommand;
    }
    
    @Override
    public Row getRow(Table table) throws IOException
    {
        return table.getRow(key, columnFamilyColumn, start, count);
    }

    @Override
    public String toString()
    {
        return "GetSliceReadMessage(" +
               "table='" + table + '\'' +
               ", key='" + key + '\'' +
               ", columnFamily='" + columnFamilyColumn + '\'' +
               ", start='" + start + '\'' +
               ", count='" + count + '\'' +
               ')';
    }
}

class SliceReadCommandSerializer extends ReadCommandSerializer
{
    @Override
    public void serialize(ReadCommand rm, DataOutputStream dos) throws IOException
    {
        SliceReadCommand realRM = (SliceReadCommand)rm;
        dos.writeBoolean(realRM.isDigestQuery());
        dos.writeUTF(realRM.table);
        dos.writeUTF(realRM.key);
        dos.writeUTF(realRM.columnFamilyColumn);
        dos.writeInt(realRM.start);
        dos.writeInt(realRM.count);
    }

    @Override
    public ReadCommand deserialize(DataInputStream dis) throws IOException
    {
        boolean isDigest = dis.readBoolean();
        String table = dis.readUTF();
        String key = dis.readUTF();
        String columnFamily = dis.readUTF();
        int start = dis.readInt();
        int count = dis.readInt();
        
        SliceReadCommand rm = new SliceReadCommand(table, key, columnFamily, start, count);
        rm.setDigestQuery(isDigest);
        return rm;
    }
}
