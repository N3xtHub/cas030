
/**
 * This message gets sent out as a result of the receipt of a GossipDigestSynMessage by an
 * endpoint. This is the 2 stage of the 3 way messaging in the Gossip protocol.
 * 
 */

class GossipDigestAckMessage
{    
    List<GossipDigest> gDigestList_ = new ArrayList<GossipDigest>();
    Map<EndPoint, EndPointState> epStateMap_ = new HashMap<EndPoint, EndPointState>();
       
    GossipDigestAckMessage(List<GossipDigest> gDigestList, Map<EndPoint, EndPointState> epStateMap)
    {
        gDigestList_ = gDigestList;
        epStateMap_ = epStateMap;
    }
    
    void addGossipDigest(EndPoint ep, int generation, int version)
    {
        gDigestList_.add( new GossipDigest(ep, generation, version) );
    }
    
    List<GossipDigest> getGossipDigestList()
    {
        return gDigestList_;
    }
    
    Map<EndPoint, EndPointState> getEndPointStateMap()
    {
        return epStateMap_;
    }
}