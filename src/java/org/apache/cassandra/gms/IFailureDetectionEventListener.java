
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
