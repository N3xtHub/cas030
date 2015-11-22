

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
    private static ICompactSerializer<ApplicationState> serializer_  = new ApplicationStateSerializer();
    
    int version_;
    String state_;

    
    /**
     * Wraps the specified state into a ApplicationState instance.
     * @param state string representation of arbitrary state.
     */
    public ApplicationState(String state)
    {
        state_ = state;
        version_ = VersionGenerator.getNextVersion();
    }
}


