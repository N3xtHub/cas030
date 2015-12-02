
public class BloomFilter extends Filter
{
    static ICompactSerializer<BloomFilter> serializer_ = new BloomFilterSerializer();

    public static ICompactSerializer<BloomFilter> serializer()
    {
        return serializer_;
    }

    private BitSet filter_;

    public BloomFilter(int numElements, int bucketsPerElement)
    {
        this(BloomCalculations.computeBestK(bucketsPerElement), new BitSet(numElements * bucketsPerElement + 20));
    }

    public BloomFilter(int numElements, double maxFalsePosProbability)
    {
        BloomCalculations.BloomSpecification spec = BloomCalculations
                .computeBucketsAndK(maxFalsePosProbability);
        filter_ = new BitSet(numElements * spec.bucketsPerElement + 20);
        hashCount = spec.K;
    }

    /*
     * This version is only used by the deserializer.
     */
    BloomFilter(int hashes, BitSet filter)
    {
        hashCount = hashes;
        filter_ = filter;
    }

    public void clear()
    {
        filter_.clear();
    }

    int buckets()
    {
        return filter_.size();
    }

    BitSet filter()
    {
        return filter_;
    }

    public boolean isPresent(String key)
    {
        for (int bucketIndex : getHashBuckets(key))
        {
            if (!filter_.get(bucketIndex))
            {
                return false;
            }
        }
        return true;
    }

    /*
     param@ key -- value whose hash is used to fill
     the filter_.
     This is a general purpose API.
     */
    public void add(String key)
    {
        for (int bucketIndex : getHashBuckets(key))
        {
            filter_.set(bucketIndex);
        }
    }

    public String toString()
    {
        return filter_.toString();
    }

    ICompactSerializer tserializer()
    {
        return serializer_;
    }

    int emptyBuckets()
    {
        int n = 0;
        for (int i = 0; i < buckets(); i++)
        {
            if (!filter_.get(i))
            {
                n++;
            }
        }
        return n;
    }
}

class BloomFilterSerializer implements ICompactSerializer<BloomFilter>
{
    public void serialize(BloomFilter bf, DataOutputStream dos)
            throws IOException
    {
        dos.writeInt(bf.getHashCount());
        BitSetSerializer.serialize(bf.filter(), dos);
    }

    public BloomFilter deserialize(DataInputStream dis) throws IOException
    {
        int hashes = dis.readInt();
        BitSet bs = BitSetSerializer.deserialize(dis);
        return new BloomFilter(hashes, bs);
    }
}
