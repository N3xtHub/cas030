

/**
 * This message gets sent out as a result of the receipt of a GossipDigestAckMessage. This the 
 * last stage of the 3 way messaging of the Gossip protocol.
 *  
 */

class GossipDigestAck2Message
{
    Map<EndPoint, EndPointState> epStateMap_ = new HashMap<EndPoint, EndPointState>();
}


