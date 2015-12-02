
/**
 * This is implemented by the Gossiper module to publish change events to interested parties.
 * Interested parties register/unregister interest by invoking the methods of this interface.
 */

public interface IEndPointStateChangePublisher
{
    /**
     * Register for interesting state changes.
     * @param subcriber module which implements the IEndPointStateChangeSubscriber
     */
    public void register(IEndPointStateChangeSubscriber subcriber);
    
    /**
     * Unregister interest for state changes.
     * @param subcriber module which implements the IEndPointStateChangeSubscriber
     */
    public void unregister(IEndPointStateChangeSubscriber subcriber);
}
