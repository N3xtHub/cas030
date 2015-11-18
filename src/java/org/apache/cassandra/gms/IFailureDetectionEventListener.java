/**
 * Implemented by the Gossiper to either convict/suspect an endpoint
 * based on the PHI calculated by the Failure Detector on the inter-arrival
 * times of the heart beats.
 */

public interface IFailureDetectionEventListener
{  
    /**
     * Convict the specified endpoint.
     * @param ep endpoint to be convicted
     */
    public void convict(EndPoint ep);
    
    /**
     * Suspect the specified endpoint.
     * @param ep endpoint to be suspected.
     */
    public void suspect(EndPoint ep);    
}
