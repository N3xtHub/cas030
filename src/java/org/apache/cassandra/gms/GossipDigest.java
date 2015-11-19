
/**
 * Contains information about a specified list of EndPoints and the largest version 
 * of the state they have generated as known by the local endpoint.
 */

public class GossipDigest implements Comparable<GossipDigest>
{
    private static ICompactSerializer<GossipDigest> serializer_;
    static
    {
        serializer_ = new GossipDigestSerializer();
    }
    
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

class GossipDigestSerializer implements ICompactSerializer<GossipDigest>
{       
    public void serialize(GossipDigest gDigest, DataOutputStream dos) throws IOException
    {        
        CompactEndPointSerializationHelper.serialize(gDigest.endPoint_, dos);
        dos.writeInt(gDigest.generation_);
        dos.writeInt(gDigest.maxVersion_);
    }

    public GossipDigest deserialize(DataInputStream dis) throws IOException
    {
        EndPoint endPoint = CompactEndPointSerializationHelper.deserialize(dis);
        int generation = dis.readInt();
        int version = dis.readInt();
        return new GossipDigest(endPoint, generation, version);
    }
}
