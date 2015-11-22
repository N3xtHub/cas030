
/**
 * This is the first message that gets sent out as a start of the Gossip protocol in a 
 * round. 
 */

class GossipDigestSynMessage
{
    private static ICompactSerializer<GossipDigestSynMessage> serializer_;
    static
    {
        serializer_ = new GossipDigestSynMessageSerializer();
    }
    
    String clusterId_;
    List<GossipDigest> gDigests_ = new ArrayList<GossipDigest>();

    public static ICompactSerializer<GossipDigestSynMessage> serializer()
    {
        return serializer_;
    }
 
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

class GossipDigestSerializationHelper
{
    private static Logger logger_ = Logger.getLogger(GossipDigestSerializationHelper.class);
    
    static boolean serialize(List<GossipDigest> gDigestList, DataOutputStream dos) throws IOException
    {
        boolean bVal = true;
        int size = gDigestList.size();                        
        dos.writeInt(size);
        
        int estimate = 0;            
        for ( GossipDigest gDigest : gDigestList )
        {
            if ( Gossiper.MAX_GOSSIP_PACKET_SIZE - dos.size() < estimate )
            {
                logger_.info("@@@@ Breaking out to respect the MTU size in GD @@@@");
                bVal = false;
                break;
            }
            int pre = dos.size();               
            GossipDigest.serializer().serialize( gDigest, dos );
            int post = dos.size();
            estimate = post - pre;
        }
        return bVal;
    }

    static List<GossipDigest> deserialize(DataInputStream dis) throws IOException
    {
        int size = dis.readInt();            
        List<GossipDigest> gDigests = new ArrayList<GossipDigest>();
        
        for ( int i = 0; i < size; ++i )
        {
            if ( dis.available() == 0 )
            {
                logger_.info("Remaining bytes zero. Stopping deserialization of GossipDigests.");
                break;
            }
                            
            GossipDigest gDigest = GossipDigest.serializer().deserialize(dis);                
            gDigests.add( gDigest );                
        }        
        return gDigests;
    }
}

class EndPointStatesSerializationHelper
{
    private static Log4jLogger logger_ = new Log4jLogger(EndPointStatesSerializationHelper.class.getName());
    
    static boolean serialize(Map<EndPoint, EndPointState> epStateMap, DataOutputStream dos) throws IOException
    {
        boolean bVal = true;
        int estimate = 0;                
        int size = epStateMap.size();
        dos.writeInt(size);
    
        Set<EndPoint> eps = epStateMap.keySet();
        for( EndPoint ep : eps )
        {
            if ( Gossiper.MAX_GOSSIP_PACKET_SIZE - dos.size() < estimate )
            {
                bVal = false;
                break;
            }
    
            int pre = dos.size();
            CompactEndPointSerializationHelper.serialize(ep, dos);
            EndPointState epState = epStateMap.get(ep);            
            EndPointState.serializer().serialize(epState, dos);
            int post = dos.size();
            estimate = post - pre;
        }
        return bVal;
    }

    static Map<EndPoint, EndPointState> deserialize(DataInputStream dis) throws IOException
    {
        int size = dis.readInt();            
        Map<EndPoint, EndPointState> epStateMap = new HashMap<EndPoint, EndPointState>();
        
        for ( int i = 0; i < size; ++i )
        {
            if ( dis.available() == 0 )
            {
                break;
            }
            // int length = dis.readInt();            
            EndPoint ep = CompactEndPointSerializationHelper.deserialize(dis);
            EndPointState epState = EndPointState.serializer().deserialize(dis);            
            epStateMap.put(ep, epState);
        }        
        return epStateMap;
    }
}

class GossipDigestSynMessageSerializer implements ICompactSerializer<GossipDigestSynMessage>
{   
    public void serialize(GossipDigestSynMessage gDigestSynMessage, DataOutputStream dos) throws IOException
    {    
        dos.writeUTF(gDigestSynMessage.clusterId_);
        GossipDigestSerializationHelper.serialize(gDigestSynMessage.gDigests_, dos);
    }

    public GossipDigestSynMessage deserialize(DataInputStream dis) throws IOException
    {
        String clusterId = dis.readUTF();
        List<GossipDigest> gDigests = GossipDigestSerializationHelper.deserialize(dis);
        return new GossipDigestSynMessage(clusterId, gDigests);
    }

}

