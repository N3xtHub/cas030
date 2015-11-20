

/**
 * This class encapsulates the message that needs to be sent
 * to nodes that handoff data. The message contains information
 * about the node to be bootstrapped and the ranges with which
 * it needs to be bootstrapped.
*/
class BootstrapMetadataMessage
{
    private static ICompactSerializer<BootstrapMetadataMessage> serializer_;
    static
    {
        serializer_ = new BootstrapMetadataMessageSerializer();
    }
    
    protected static ICompactSerializer<BootstrapMetadataMessage> serializer()
    {
        return serializer_;
    }
    
    protected static Message makeBootstrapMetadataMessage(BootstrapMetadataMessage bsMetadataMessage) 
    {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream( bos );
        BootstrapMetadataMessage.serializer().serialize(bsMetadataMessage, dos);
        return new Message( StorageService.getLocalStorageEndPoint(), "", StorageService.bsMetadataVerbHandler_, bos.toByteArray() );
    }        
    
    protected BootstrapMetadata[] bsMetadata_ = new BootstrapMetadata[0];
    
    BootstrapMetadataMessage(BootstrapMetadata[] bsMetadata)
    {
        bsMetadata_ = bsMetadata;
    }
}

class BootstrapMetadataMessageSerializer implements ICompactSerializer<BootstrapMetadataMessage>
{
    public void serialize(BootstrapMetadataMessage bsMetadataMessage, DataOutputStream dos) 
    {
        BootstrapMetadata[] bsMetadata = bsMetadataMessage.bsMetadata_;
        int size = (bsMetadata == null) ? 0 : bsMetadata.length;
        dos.writeInt(size);
        for ( BootstrapMetadata bsmd : bsMetadata )
        {
            BootstrapMetadata.serializer().serialize(bsmd, dos);
        }
    }

    public BootstrapMetadataMessage deserialize(DataInputStream dis) 
    {            
        int size = dis.readInt();
        BootstrapMetadata[] bsMetadata = new BootstrapMetadata[size];
        for ( int i = 0; i < size; ++i )
        {
            bsMetadata[i] = BootstrapMetadata.serializer().deserialize(dis);
        }
        return new BootstrapMetadataMessage(bsMetadata);
    }
}
