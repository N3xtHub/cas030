/**
 * An interface that provides an application with the ability
 * to query liveness information of a node in the cluster. It 
 * also exposes methods which help an application register callbacks
 * for notifications of liveness information of nodes.
 */

public interface IFailureDetector
{
    public boolean isAlive(EndPoint ep);
    
    /**
     * This method is invoked by any entity wanting to interrogate the status of an endpoint. 
     * In our case it would be the Gossiper. The Failure Detector will then calculate Phi and
     * deem an endpoint as suspicious or alive as explained in the Hayashibara paper. 
     * 
     * param ep endpoint for which we interpret the inter arrival times.
    */
    public void intepret(EndPoint ep);
    
    /**
     * This method is invoked by the receiver of the heartbeat. In our case it would be
     * the Gossiper. Gossiper inform the Failure Detector on receipt of a heartbeat. The
     * FailureDetector will then sample the arrival time as explained in the paper.
     * 
     * param ep endpoint being reported.
    */
    public void report(EndPoint ep);
    
    /**
     * Register interest for Failure Detector events. 
     * @param listener implementation of an application provided IFailureDetectionEventListener 
     */
    public void registerFailureDetectionEventListener(IFailureDetectionEventListener listener);
    
    /**
     * Un-register interest for Failure Detector events. 
     * @param listener implementation of an application provided IFailureDetectionEventListener 
     */
    public void unregisterFailureDetectionEventListener(IFailureDetectionEventListener listener);
}
