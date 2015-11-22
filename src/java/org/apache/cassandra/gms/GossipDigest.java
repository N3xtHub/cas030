
/**
 * Contains information about a specified list of EndPoints and the largest version 
 * of the state they have generated as known by the local endpoint.
 */

public class GossipDigest implements Comparable<GossipDigest>
{
    private static ICompactSerializer<GossipDigest> serializer_ = new GossipDigestSerializer();
    
    EndPoint endPoint_;
    int generation_;
    int maxVersion_;

    public static ICompactSerializer<GossipDigest> serializer()
    {
        return serializer_;
    }
    
    public int compareTo(GossipDigest gDigest)
    {
        if ( generation_ != gDigest.generation_ )
            return ( generation_ - gDigest.generation_ );
        return (maxVersion_ - gDigest.maxVersion_);
    }
}
