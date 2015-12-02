
/**
 * Contains information about a specified list of EndPoints and the largest version 
 * of the state they have generated as known by the local endpoint.
 */

public class GossipDigest implements Comparable<GossipDigest>
{
    EndPoint endPoint_;
    int generation_;
    int maxVersion_;
    
    public int compareTo(GossipDigest gDigest)
    {
        if ( generation_ != gDigest.generation_ )
            return ( generation_ - gDigest.generation_ );
        return (maxVersion_ - gDigest.maxVersion_);
    }
}
