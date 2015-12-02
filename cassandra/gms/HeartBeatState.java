

/**
 * HeartBeat State associated with any given endpoint. 
 */

class HeartBeatState
{
    
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
    
    void updateGeneration()
    {
        ++generation_;
        version_ = VersionGenerator.getNextVersion();
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

