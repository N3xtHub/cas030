
public class Header implements java.io.Serializable
{
    static final long serialVersionUID = -3194851946523170022L;
    private static ICompactSerializer<Header> serializer_;
    private static AtomicInteger idGen_ = new AtomicInteger(0);
    
    static
    {
        serializer_ = new HeaderSerializer();        
    }
    
    static ICompactSerializer<Header> serializer()
    {
        return serializer_;
    }

    private EndPoint from_;
    private String type_;
    private String verb_;
    private String messageId_;
    protected Map<String, byte[]> details_ = new Hashtable<String, byte[]>();
    
    Header(String id, EndPoint from, String messageType, String verb)
    {
        messageId_ = id;
        from_ = from;
        type_ = messageType;
        verb_ = verb;        
    }
    
    Header(String id, EndPoint from, String messageType, String verb, Map<String, byte[]> details)
    {
        this(id, from, messageType, verb);
        details_ = details;
    }

    Header(EndPoint from, String messageType, String verb)
    {
        messageId_ = Integer.toString(idGen_.incrementAndGet());
        from_ = from;
        type_ = messageType;
        verb_ = verb;
    }        

    EndPoint getFrom()
    {
        return from_;
    }

    String getMessageType()
    {
        return type_;
    }

    String getVerb()
    {
        return verb_;
    }

    String getMessageId()
    {
        return messageId_;
    }

    void setMessageId(String id)
    {
        messageId_ = id;
    }
    
    void setMessageType(String type)
    {
        type_ = type;
    }
    
    void setMessageVerb(String verb)
    {
        verb_ = verb;
    }
    
    byte[] getDetail(Object key)
    {
        return details_.get(key);
    }
    
    void removeDetail(Object key)
    {
        details_.remove(key);
    }
    
    void addDetail(String key, byte[] value)
    {
        details_.put(key, value);
    }
    
    Map<String, byte[]> getDetails()
    {
        return details_;
    }
}

class HeaderSerializer implements ICompactSerializer<Header>
{
    public void serialize(Header t, DataOutputStream dos) throws IOException
    {           
        dos.writeUTF(t.getMessageId());
        CompactEndPointSerializationHelper.serialize(t.getFrom(), dos);
        dos.writeUTF(t.getMessageType());
        dos.writeUTF( t.getVerb() );
        
        /* Serialize the message header */
        int size = t.details_.size();
        dos.writeInt(size);
        Set<String> keys = t.details_.keySet();
        
        for( String key : keys )
        {
            dos.writeUTF(key);
            byte[] value = t.details_.get(key);
            dos.writeInt(value.length);
            dos.write(value);
        }
    }

    public Header deserialize(DataInputStream dis) throws IOException
    {
        String id = dis.readUTF();
        EndPoint from = CompactEndPointSerializationHelper.deserialize(dis);
        String type = dis.readUTF();
        String verb = dis.readUTF();
        
        /* Deserializing the message header */
        int size = dis.readInt();
        Map<String, byte[]> details = new Hashtable<String, byte[]>(size);
        for ( int i = 0; i < size; ++i )
        {
            String key = dis.readUTF();
            int length = dis.readInt();
            byte[] bytes = new byte[length];
            dis.readFully(bytes);
            details.put(key, bytes);
        }
        
        return new Header(id, from, type, verb, details);
    }
}


