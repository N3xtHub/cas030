
public interface IAsyncResult
{    
    /**
     * This is used to check if the task has been completed
     * 
     * @return true if the task has been completed and false otherwise.
     */
    public boolean isDone();
    
    /**
     * Returns the result for the task that was submitted.
     * @return the result wrapped in an Object[]
    */
    public byte[] get();
    
    /**
     * Same operation as the above get() but allows the calling
     * thread to specify a timeout.
     * @param timeout the maximum time to wait
     * @param tu the time unit of the timeout argument
     * @return the result wrapped in an Object[]
    */
    public byte[] get(long timeout, TimeUnit tu) throws TimeoutException;
    
    /**
     * Returns the result for all tasks that was submitted.
     * @return the list of results wrapped in an Object[]
    */
    public List<byte[]> multiget();
    
    /**
     * Same operation as the above get() but allows the calling
     * thread to specify a timeout.
     * @param timeout the maximum time to wait
     * @param tu the time unit of the timeout argument
     * @return the result wrapped in an Object[]
    */
    public List<byte[]> multiget(long timeout, TimeUnit tu) throws TimeoutException;
    
    /**
     * Store the result obtained for the submitted task.
     * @param result the response message
     */
    public void result(Message result);
}
