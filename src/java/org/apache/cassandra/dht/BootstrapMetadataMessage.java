

/**
 * This class encapsulates the message that needs to be sent
 * to nodes that handoff data. The message contains information
 * about the node to be bootstrapped and the ranges with which
 * it needs to be bootstrapped.
*/
class BootstrapMetadataMessage
{
    private static ICompactSerializer<BootstrapMetadataMessage> serializer_ = new BootstrapMetadataMessageSerializer();
    

    protected static Message makeBootstrapMetadataMessage(BootstrapMetadataMessage bsMetadataMessage) 
    {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream( bos );
        BootstrapMetadataMessage.serializer().serialize(bsMetadataMessage, dos);
        return new Message( StorageService.getLocalStorageEndPoint(), "", StorageService.bsMetadataVerbHandler_, bos.toByteArray() );
    }        
    
    protected BootstrapMetadata[] bsMetadata_ = new BootstrapMetadata[0];
}

