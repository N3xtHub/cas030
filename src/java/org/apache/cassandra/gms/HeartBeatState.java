

/**
 * HeartBeat State associated with any given endpoint. 
 */

class HeartBeatState
{
    private static ICompactSerializer<HeartBeatState> serializer_;
    
    static
    {
        serializer_ = new HeartBeatStateSerializer();
    }
    
    int generation_;
    AtomicInteger heartbeat_;
    int version_;

    HeartBeatState()
    {
    }
    
    HeartBeatState(int generation, int heartbeat)
    {
        this(generation, heartbeat, 0);
    }
    
    HeartBeatState(int generation, int heartbeat, int version)
    {
        generation_ = generation;
        heartbeat_ = new AtomicInteger(heartbeat);
        version_ = version;
    }

    public static ICompactSerializer<HeartBeatState> serializer()
    {
        return serializer_;
    }
    
    int getGeneration()
    {
        return generation_;
    }
    
    void updateGeneration()
    {
        ++generation_;
        version_ = VersionGenerator.getNextVersion();
    }
    
    int getHeartBeat()
    {
        return heartbeat_.get();
    }
    
    void updateHeartBeat()
    {
        heartbeat_.incrementAndGet();      
        version_ = VersionGenerator.getNextVersion();
    }
    
    int getHeartBeatVersion()
    {
        return version_;
    }
};

class HeartBeatStateSerializer implements ICompactSerializer<HeartBeatState>
{
    public void serialize(HeartBeatState hbState, DataOutputStream dos) throws IOException
    {
        dos.writeInt(hbState.generation_);
        dos.writeInt(hbState.heartbeat_.get());
        dos.writeInt(hbState.version_);
    }
    
    public HeartBeatState deserialize(DataInputStream dis) throws IOException
    {
        return new HeartBeatState(dis.readInt(), dis.readInt(), dis.readInt());
    }
}
