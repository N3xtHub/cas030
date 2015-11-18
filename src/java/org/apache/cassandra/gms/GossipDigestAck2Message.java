

/**
 * This message gets sent out as a result of the receipt of a GossipDigestAckMessage. This the 
 * last stage of the 3 way messaging of the Gossip protocol.
 *  
 */

class GossipDigestAck2Message
{
    private static  ICompactSerializer<GossipDigestAck2Message> serializer_;
    static
    {
        serializer_ = new GossipDigestAck2MessageSerializer();
    }
    
    Map<EndPoint, EndPointState> epStateMap_ = new HashMap<EndPoint, EndPointState>();

    public static ICompactSerializer<GossipDigestAck2Message> serializer()
    {
        return serializer_;
    }
    
    GossipDigestAck2Message(Map<EndPoint, EndPointState> epStateMap)
    {
        epStateMap_ = epStateMap;
    }
        
    Map<EndPoint, EndPointState> getEndPointStateMap()
    {
         return epStateMap_;
    }
}

class GossipDigestAck2MessageSerializer implements ICompactSerializer<GossipDigestAck2Message>
{
    public void serialize(GossipDigestAck2Message gDigestAck2Message, DataOutputStream dos) throws IOException
    {
        /* Use the EndPointState */
        EndPointStatesSerializationHelper.serialize(gDigestAck2Message.epStateMap_, dos);
    }

    public GossipDigestAck2Message deserialize(DataInputStream dis) throws IOException
    {
        Map<EndPoint, EndPointState> epStateMap = EndPointStatesSerializationHelper.deserialize(dis);
        return new GossipDigestAck2Message(epStateMap);        
    }
}

