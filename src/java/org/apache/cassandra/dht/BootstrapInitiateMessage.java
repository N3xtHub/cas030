
public class BootstrapInitiateMessage implements Serializable
{
    private static ICompactSerializer<BootstrapInitiateMessage> serializer_ = new BootstrapInitiateMessageSerializer();
    
    public static Message makeBootstrapInitiateMessage(BootstrapInitiateMessage biMessage) 
    {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream( bos );
        BootstrapInitiateMessage.serializer().serialize(biMessage, dos);
        return new Message( StorageService.getLocalStorageEndPoint(), "", StorageService.bootStrapInitiateVerbHandler_, bos.toByteArray() );
    }
    
    protected StreamContextManager.StreamContext[] streamContexts_ = new StreamContextManager.StreamContext[0];
}


