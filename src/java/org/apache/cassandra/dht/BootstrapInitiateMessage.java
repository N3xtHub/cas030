
public class BootstrapInitiateMessage implements Serializable
{
    private static ICompactSerializer<BootstrapInitiateMessage> serializer_;
    
    static
    {
        serializer_ = new BootstrapInitiateMessageSerializer();
    }
    
    public static ICompactSerializer<BootstrapInitiateMessage> serializer()
    {
        return serializer_;
    }
    
    public static Message makeBootstrapInitiateMessage(BootstrapInitiateMessage biMessage) throws IOException
    {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream( bos );
        BootstrapInitiateMessage.serializer().serialize(biMessage, dos);
        return new Message( StorageService.getLocalStorageEndPoint(), "", StorageService.bootStrapInitiateVerbHandler_, bos.toByteArray() );
    }
    
    protected StreamContextManager.StreamContext[] streamContexts_ = new StreamContextManager.StreamContext[0];
   
    public BootstrapInitiateMessage(StreamContextManager.StreamContext[] streamContexts)
    {
        streamContexts_ = streamContexts;
    }
    
    public StreamContextManager.StreamContext[] getStreamContext()
    {
        return streamContexts_;
    }
}

class BootstrapInitiateMessageSerializer implements ICompactSerializer<BootstrapInitiateMessage>
{
    public void serialize(BootstrapInitiateMessage bim, DataOutputStream dos) throws IOException
    {
        dos.writeInt(bim.streamContexts_.length);
        for ( StreamContextManager.StreamContext streamContext : bim.streamContexts_ )
        {
            StreamContextManager.StreamContext.serializer().serialize(streamContext, dos);
        }
    }
    
    public BootstrapInitiateMessage deserialize(DataInputStream dis) throws IOException
    {
        int size = dis.readInt();
        StreamContextManager.StreamContext[] streamContexts = new StreamContextManager.StreamContext[0];
        if ( size > 0 )
        {
            streamContexts = new StreamContextManager.StreamContext[size];
            for ( int i = 0; i < size; ++i )
            {
                streamContexts[i] = StreamContextManager.StreamContext.serializer().deserialize(dis);
            }
        }
        return new BootstrapInitiateMessage(streamContexts);
    }
}

