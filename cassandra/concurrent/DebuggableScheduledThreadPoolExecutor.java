
/**
 * This is a wrapper class for the <i>ScheduledThreadPoolExecutor</i>. It provides an implementation
 * for the <i>afterExecute()</i> found in the <i>ThreadPoolExecutor</i> class to log any unexpected 
 * Runtime Exceptions.
 */
public final class DebuggableScheduledThreadPoolExecutor extends ScheduledThreadPoolExecutor
{
    public DebuggableScheduledThreadPoolExecutor(int threads,
            ThreadFactory threadFactory)
    {
        super(threads, threadFactory);        
    }
    
    public void afterExecute(Runnable r, Throwable t)
    {
        super.afterExecute(r,t);
        if ( t != null )
        {  
            Context ctx = ThreadLocalContext.get();
            Object object = ctx.get(r.getClass().getName());
            
            Throwable cause = t.getCause();
        }
    }
}
