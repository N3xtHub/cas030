
/**
 * This encapsulates information of the list of 
 * ranges that a target node requires in order to 
 * be bootstrapped. This will be bundled in a 
 * BootstrapMetadataMessage and sent to nodes that
 * are going to handoff the data.
*/
class BootstrapMetadata
{
    private static ICompactSerializer<BootstrapMetadata> serializer_
        = new BootstrapMetadataSerializer();
    
    protected EndPoint target_;
    protected List<Range> ranges_;
}



