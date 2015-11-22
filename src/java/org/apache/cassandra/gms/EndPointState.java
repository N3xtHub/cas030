
/**
 * This abstraction represents both the HeartBeatState and the ApplicationState in an EndPointState
 * instance. Any state for a given endpoint can be retrieved from this instance.
 * 
 */

public class EndPointState
{
    private static ICompactSerializer<EndPointState> serializer_ = new EndPointStateSerializer();
    
    HeartBeatState hbState_;
    Map<String, ApplicationState> applicationState_ = new Hashtable<String, ApplicationState>();
    
    /* fields below do not get serialized */
    long updateTimestamp_;
    boolean isAlive_;
    boolean isAGossiper_;
    
    EndPointState(HeartBeatState hbState) 
    { 
        hbState_ = hbState; 
        updateTimestamp_ = System.currentTimeMillis(); 
        isAlive_ = true; 
        isAGossiper_ = false;
    }
        
    HeartBeatState getHeartBeatState()
    {
        return hbState_;
    }
    
    synchronized void setHeartBeatState(HeartBeatState hbState)
    {
        updateTimestamp();
        hbState_ = hbState;
    }
    
    public ApplicationState getApplicationState(String key)
    {
        return applicationState_.get(key);
    }
    
    void addApplicationState(String key, ApplicationState appState)
    {        
        applicationState_.put(key, appState);        
    }

    synchronized void updateTimestamp()
    {
        updateTimestamp_ = System.currentTimeMillis();
    }
    
}

