
public class RowReadCommand extends ReadCommand
{
    public RowReadCommand(String table, String key)
    {
        super(table, key, CMD_TYPE_GET_ROW);
    }

    @Override
    public String getColumnFamilyName()
    {
        return null;
    }

    @Override
    public ReadCommand copy()
    {
        ReadCommand readCommand= new RowReadCommand(table, key);
        readCommand.setDigestQuery(isDigestQuery());
        return readCommand;
    }

    @Override
    public Row getRow(Table table) throws IOException    
    {
        return table.get(key);
    }

    @Override
    public String toString()
    {
        return "GetColumnReadMessage(" +
               "table='" + table + '\'' +
               ", key='" + key + '\'' +
               ')';
    }

}

class RowReadCommandSerializer extends ReadCommandSerializer
{
    @Override
    public void serialize(ReadCommand rm, DataOutputStream dos) throws IOException
    { 
        RowReadCommand realRM = (RowReadCommand)rm;
        dos.writeBoolean(realRM.isDigestQuery());
        dos.writeUTF(realRM.table);
        dos.writeUTF(realRM.key);
    }

    @Override
    public ReadCommand deserialize(DataInputStream dis) throws IOException
    {
        boolean isDigest = dis.readBoolean();
        String table = dis.readUTF();
        String key = dis.readUTF();
        RowReadCommand rm = new RowReadCommand(table, key);
        rm.setDigestQuery(isDigest);
        return rm;
    }
}
