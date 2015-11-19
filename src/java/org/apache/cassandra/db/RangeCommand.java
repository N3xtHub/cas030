
public class RangeCommand
{
    private static RangeCommandSerializer serializer = new RangeCommandSerializer();

    public final String table;
    public final String startWith;
    public final String stopAt;
    public final int maxResults;

    public RangeCommand(String table, String startWith, String stopAt, int maxResults)
    {
        this.table = table;
        this.startWith = startWith;
        this.stopAt = stopAt;
        this.maxResults = maxResults;
    }

    public Message getMessage() throws IOException
    {
        DataOutputBuffer dob = new DataOutputBuffer();
        serializer.serialize(this, dob);
        return new Message(StorageService.getLocalStorageEndPoint(),
                           StorageService.readStage_,
                           StorageService.rangeVerbHandler_,
                           Arrays.copyOf(dob.getData(), dob.getLength()));
    }

    public static RangeCommand read(Message message) throws IOException
    {
        byte[] bytes = message.getMessageBody();
        DataInputBuffer dib = new DataInputBuffer();
        dib.reset(bytes, bytes.length);
        return serializer.deserialize(new DataInputStream(dib));
    }

    public String toString()
    {
        return "RangeCommand(" +
               "table='" + table + '\'' +
               ", startWith='" + startWith + '\'' +
               ", stopAt='" + stopAt + '\'' +
               ", maxResults=" + maxResults +
               ')';
    }
}

class RangeCommandSerializer implements ICompactSerializer<RangeCommand>
{
    public void serialize(RangeCommand command, DataOutputStream dos) throws IOException
    {
        dos.writeUTF(command.table);
        dos.writeUTF(command.startWith);
        dos.writeUTF(command.stopAt);
        dos.writeInt(command.maxResults);
    }

    public RangeCommand deserialize(DataInputStream dis) throws IOException
    {
        return new RangeCommand(dis.readUTF(), dis.readUTF(), dis.readUTF(), dis.readInt());
    }
}
