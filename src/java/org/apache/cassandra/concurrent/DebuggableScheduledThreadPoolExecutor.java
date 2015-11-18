
/**
 * This is a wrapper class for the <i>ScheduledThreadPoolExecutor</i>. It provides an implementation
 * for the <i>afterExecute()</i> found in the <i>ThreadPoolExecutor</i> class to log any unexpected 
 * Runtime Exceptions.
 */
public final class DebuggableScheduledThreadPoolExecutor extends ScheduledThreadPoolExecutor
{
    private static Logger logger_ = Logger.getLogger(DebuggableScheduledThreadPoolExecutor.class);
    
    public DebuggableScheduledThreadPoolExecutor(int threads,
            ThreadFactory threadFactory)
    {
        super(threads, threadFactory);        
    }
    
    /**
     *  (non-Javadoc)
     * @see java.util.concurrent.ThreadPoolExecutor#afterExecute(java.lang.Runnable, java.lang.Throwable)
     */
    public void afterExecute(Runnable r, Throwable t)
    {
        super.afterExecute(r,t);
        if ( t != null )
        {  
            Context ctx = ThreadLocalContext.get();
            if ( ctx != null )
            {
                Object object = ctx.get(r.getClass().getName());
                
                if ( object != null )
                {
                    logger_.info("**** In afterExecute() " + t.getClass().getName() + " occured while working with " + object + " ****");
                }
                else
                {
                    logger_.info("**** In afterExecute() " + t.getClass().getName() + " occured ****");
                }
            }
            
            Throwable cause = t.getCause();
            if ( cause != null )
            {
                logger_.info( LogUtil.throwableToString(cause) );
            }
            logger_.info( LogUtil.throwableToString(t) );
        }
    }
}
