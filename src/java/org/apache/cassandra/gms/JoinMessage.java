
class JoinMessage
{
    private static ICompactSerializer<JoinMessage> serializer_;
    static
    {
        serializer_ = new JoinMessageSerializer();
    }
    
    static ICompactSerializer<JoinMessage> serializer()
    {
        return serializer_;
    }
    
    String clusterId_;
    
    JoinMessage(String clusterId)
    {
        clusterId_ = clusterId;
    }
}

class JoinMessageSerializer implements ICompactSerializer<JoinMessage>
{
    public void serialize(JoinMessage joinMessage, DataOutputStream dos) throws IOException
    {    
        dos.writeUTF(joinMessage.clusterId_);         
    }

    public JoinMessage deserialize(DataInputStream dis) throws IOException
    {
        String clusterId = dis.readUTF();
        return new JoinMessage(clusterId);
    }
}
