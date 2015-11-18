
/**
 * This message gets sent out as a result of the receipt of a GossipDigestSynMessage by an
 * endpoint. This is the 2 stage of the 3 way messaging in the Gossip protocol.
 * 
 */

class GossipDigestAckMessage
{
    private static ICompactSerializer<GossipDigestAckMessage> serializer_;
    static
    {
        serializer_ = new GossipDigestAckMessageSerializer();
    }
    
    List<GossipDigest> gDigestList_ = new ArrayList<GossipDigest>();
    Map<EndPoint, EndPointState> epStateMap_ = new HashMap<EndPoint, EndPointState>();
    
    static ICompactSerializer<GossipDigestAckMessage> serializer()
    {
        return serializer_;
    }
    
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

class GossipDigestAckMessageSerializer implements ICompactSerializer<GossipDigestAckMessage>
{
    public void serialize(GossipDigestAckMessage gDigestAckMessage, DataOutputStream dos) throws IOException
    {
        /* Use the helper to serialize the GossipDigestList */
        boolean bContinue = GossipDigestSerializationHelper.serialize(gDigestAckMessage.gDigestList_, dos);
        dos.writeBoolean(bContinue);
        /* Use the EndPointState */
        if ( bContinue )
        {
            EndPointStatesSerializationHelper.serialize(gDigestAckMessage.epStateMap_, dos);            
        }
    }

    public GossipDigestAckMessage deserialize(DataInputStream dis) throws IOException
    {
        Map<EndPoint, EndPointState> epStateMap = new HashMap<EndPoint, EndPointState>();
        List<GossipDigest> gDigestList = GossipDigestSerializationHelper.deserialize(dis);                
        boolean bContinue = dis.readBoolean();

        if ( bContinue )
        {
            epStateMap = EndPointStatesSerializationHelper.deserialize(dis);                                    
        }
        return new GossipDigestAckMessage(gDigestList, epStateMap);
    }
}