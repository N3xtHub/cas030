
public class ContinuationContext
{
    private Continuation continuation_;
    private Object result_;
    
    public ContinuationContext(Continuation continuation)
    {
        continuation_ = continuation;        
    }
    
    public Continuation getContinuation()
    {
        return continuation_;
    }
    
    public void setContinuation(Continuation continuation)
    {
        continuation_ = continuation;
    }
    
    public Object result()
    {
        return result_;
    }
    
    public void result(Object result)
    {
        result_ = result;
    }
}
