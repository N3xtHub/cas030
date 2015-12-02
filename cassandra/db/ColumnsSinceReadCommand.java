
public class ColumnsSinceReadCommand extends ReadCommand
{
    public final String columnFamily;
    public final long sinceTimestamp;

    public ColumnsSinceReadCommand(String table, String key, String columnFamily, long sinceTimestamp)
    {
        super(table, key, CMD_TYPE_GET_COLUMNS_SINCE);
        this.columnFamily = columnFamily;
        this.sinceTimestamp = sinceTimestamp;
    }

    @Override
    public String getColumnFamilyName()
    {
        return columnFamily;
    }

    @Override
    public ReadCommand copy()
    {
        ReadCommand readCommand= new ColumnsSinceReadCommand(table, key, columnFamily, sinceTimestamp);
        readCommand.setDigestQuery(isDigestQuery());
        return readCommand;
    }

    @Override
    public Row getRow(Table table) throws IOException
    {        
        return table.getRow(key, columnFamily, sinceTimestamp);
    }

    @Override
    public String toString()
    {
        return "GetColumnsSinceMessage(" +
               "table='" + table + '\'' +
               ", key='" + key + '\'' +
               ", columnFamily='" + columnFamily + '\'' +
               ", sinceTimestamp='" + sinceTimestamp + '\'' +
               ')';
    }

}

class ColumnsSinceReadCommandSerializer extends ReadCommandSerializer
{
    @Override
    public void serialize(ReadCommand rm, DataOutputStream dos) throws IOException
    {
        ColumnsSinceReadCommand realRM = (ColumnsSinceReadCommand)rm;
        dos.writeBoolean(realRM.isDigestQuery());
        dos.writeUTF(realRM.table);
        dos.writeUTF(realRM.key);
        dos.writeUTF(realRM.columnFamily);
        dos.writeLong(realRM.sinceTimestamp);
    }

    @Override
    public ReadCommand deserialize(DataInputStream dis) throws IOException
    {
        boolean isDigest = dis.readBoolean();
        String table = dis.readUTF();
        String key = dis.readUTF();
        String columnFamily = dis.readUTF();
        long sinceTimestamp = dis.readLong();

        ColumnsSinceReadCommand rm = new ColumnsSinceReadCommand(table, key, columnFamily, sinceTimestamp);
        rm.setDigestQuery(isDigest);
        return rm;
    }
}
