
/**
 * This is the first message that gets sent out as a start of the Gossip protocol in a 
 * round. 
 */

class GossipDigestSynMessage
{    
    String clusterId_;
    List<GossipDigest> gDigests_ = new ArrayList<GossipDigest>();
 
    public GossipDigestSynMessage(String clusterId, List<GossipDigest> gDigests)
    {      
        clusterId_ = clusterId;
        gDigests_ = gDigests;
    }
    
    List<GossipDigest> getGossipDigests()
    {
        return gDigests_;
    }
}





