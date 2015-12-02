/*
 * An instance of this class represents an update to a table. 
 * This is written to the CommitLog to be replayed on recovery. It
 * contains enough information to be written to a SSTable to 
 * capture events that happened before some catastrophe.
 *
 */
class CommitLogEntry
{    
    private static ICompactSerializer<CommitLogEntry> serializer_;
    static
    {
        serializer_ = new CommitLogEntrySerializer();
    }
    
    static ICompactSerializer<CommitLogEntry> serializer()
    {
        return serializer_;
    }    
    
    private int length_;
    private byte[] value_ = new byte[0];
    
    CommitLogEntry()
    {
    }
    
    CommitLogEntry(byte[] value)
    {
        this(value, 0);
    }
    
    CommitLogEntry(byte[] value, int length)
    {
        value_ = value;   
        length_ = length;
    }
    
    void value(byte[] bytes)
    {
        value_ = bytes;
    }
    
    byte[] value()
    {
        return value_;
    }
    
    void length(int size)
    {
        length_ = size;
    }
    
    int length()
    {
        return length_;
    }
}

class CommitLogEntrySerializer implements ICompactSerializer<CommitLogEntry>
{
    public void serialize(CommitLogEntry logEntry, DataOutputStream dos) throws IOException
    {    
        int length = logEntry.length();
        dos.writeInt(length);
        dos.write(logEntry.value(), 0, length);           
    }
    
    public CommitLogEntry deserialize(DataInputStream dis) throws IOException
    {        
        byte[] value = new byte[dis.readInt()];
        dis.readFully(value);        
        return new CommitLogEntry(value);
    }
}

