
/**
 * This encapsulates information of the list of 
 * ranges that a target node requires in order to 
 * be bootstrapped. This will be bundled in a 
 * BootstrapMetadataMessage and sent to nodes that
 * are going to handoff the data.
*/
class BootstrapMetadata
{
    private static ICompactSerializer<BootstrapMetadata> serializer_;
    static
    {
        serializer_ = new BootstrapMetadataSerializer();
    }
    
    protected static ICompactSerializer<BootstrapMetadata> serializer()
    {
        return serializer_;
    }
    
    protected EndPoint target_;
    protected List<Range> ranges_;
    
    BootstrapMetadata(EndPoint target, List<Range> ranges)
    {
        target_ = target;
        ranges_ = ranges;
    }
    
    public String toString()
    {
        StringBuilder sb = new StringBuilder("");
        sb.append(target_);
        sb.append("------->");
        for ( Range range : ranges_ )
        {
            sb.append(range);
            sb.append(" ");
        }
        return sb.toString();
    }
}

class BootstrapMetadataSerializer implements ICompactSerializer<BootstrapMetadata>
{
    public void serialize(BootstrapMetadata bsMetadata, DataOutputStream dos) throws IOException
    {
        CompactEndPointSerializationHelper.serialize(bsMetadata.target_, dos);
        int size = (bsMetadata.ranges_ == null) ? 0 : bsMetadata.ranges_.size();            
        dos.writeInt(size);
        
        for ( Range range : bsMetadata.ranges_ )
        {
            Range.serializer().serialize(range, dos);
        }            
    }

    public BootstrapMetadata deserialize(DataInputStream dis) throws IOException
    {            
        EndPoint target = CompactEndPointSerializationHelper.deserialize(dis);
        int size = dis.readInt();
        List<Range> ranges = (size == 0) ? null : new ArrayList<Range>();
        for( int i = 0; i < size; ++i )
        {
            ranges.add(Range.serializer().deserialize(dis));
        }            
        return new BootstrapMetadata( target, ranges );
    }
}

