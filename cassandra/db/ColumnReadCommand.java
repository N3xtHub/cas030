
public class ColumnReadCommand extends ReadCommand
{
    public final String columnFamilyColumn;

    public ColumnReadCommand(String table, String key, String columnFamilyColumn)
    {
        super(table, key, CMD_TYPE_GET_COLUMN);
        this.columnFamilyColumn = columnFamilyColumn;
    }

    @Override
    public String getColumnFamilyName()
    {
        String[] values = RowMutation.getColumnAndColumnFamily(columnFamilyColumn);
        return values[0];
    }

    @Override
    public ReadCommand copy()
    {
        ReadCommand readCommand= new ColumnReadCommand(table, key, columnFamilyColumn);
        readCommand.setDigestQuery(isDigestQuery());
        return readCommand;
    }

    @Override
    public Row getRow(Table table) throws IOException    
    {
        return table.getRow(key, columnFamilyColumn);
    }

    @Override
    public String toString()
    {
        return "GetColumnReadMessage(" +
               "table='" + table + '\'' +
               ", key='" + key + '\'' +
               ", columnFamilyColumn='" + columnFamilyColumn + '\'' +
               ')';
    }
}

class ColumnReadCommandSerializer extends ReadCommandSerializer
{
    @Override
    public void serialize(ReadCommand rm, DataOutputStream dos) throws IOException
    { 
        ColumnReadCommand realRM = (ColumnReadCommand)rm;
        dos.writeBoolean(realRM.isDigestQuery());
        dos.writeUTF(realRM.table);
        dos.writeUTF(realRM.key);
        dos.writeUTF(realRM.columnFamilyColumn);
    }

    @Override
    public ReadCommand deserialize(DataInputStream dis) throws IOException
    {
        boolean isDigest = dis.readBoolean();
        String table = dis.readUTF();
        String key = dis.readUTF();
        String columnFamily_column = dis.readUTF();
        ColumnReadCommand rm = new ColumnReadCommand(table, key, columnFamily_column);
        rm.setDigestQuery(isDigest);
        return rm;
    }
}
