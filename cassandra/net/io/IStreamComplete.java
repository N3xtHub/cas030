
public interface IStreamComplete
{
    /*
     * This callback if registered with the StreamContextManager is 
     * called when the stream from a host is completely handled. 
    */
    public void onStreamCompletion(String from, StreamContextManager.StreamContext streamContext, StreamContextManager.StreamStatus streamStatus) throws IOException;
}
