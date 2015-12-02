
public interface IAsyncCallback 
{
	/**
	 * @param msg responses to be returned
	 */
	public void response(Message msg);
    
    /**
     * Attach some application specific context to the
     * callback.
     * @param o application specific context
     */
    public void attachContext(Object o);
}
