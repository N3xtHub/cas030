

/**
 * This abstraction represents the state associated with a particular node which an
 * application wants to make available to the rest of the nodes in the cluster. 
 * Whenever a peice of state needs to be disseminated to the rest of cluster wrap
 * the state in an instance of <i>ApplicationState</i> and add it to the Gossiper.
 *  
 * For eg. if we want to disseminate load information for node A do the following:
 * 
 *      ApplicationState loadState = new ApplicationState(<string reprensentation of load>);
 *      Gossiper.instance().addApplicationState("LOAD STATE", loadState);
 *  
 */

public class ApplicationState
{
    private static ICompactSerializer<ApplicationState> serializer_;
    static
    {
        serializer_ = new ApplicationStateSerializer();
    }
    
    int version_;
    String state_;

        
    ApplicationState(String state, int version)
    {
        state_ = state;
        version_ = version;
    }

    public static ICompactSerializer<ApplicationState> serializer()
    {
        return serializer_;
    }
    
    /**
     * Wraps the specified state into a ApplicationState instance.
     * @param state string representation of arbitrary state.
     */
    public ApplicationState(String state)
    {
        state_ = state;
        version_ = VersionGenerator.getNextVersion();
    }
        
    public String getState()
    {
        return state_;
    }
    
    int getStateVersion()
    {
        return version_;
    }
}

class ApplicationStateSerializer implements ICompactSerializer<ApplicationState>
{
    public void serialize(ApplicationState appState, DataOutputStream dos)
    {
        dos.writeUTF(appState.state_);
        dos.writeInt(appState.version_);
    }

    public ApplicationState deserialize(DataInputStream dis) 
    {
        String state = dis.readUTF();
        int version = dis.readInt();
        return new ApplicationState(state, version);
    }
}

