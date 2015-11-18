
class AsyncResult implements IAsyncResult
{
    private static Logger logger_ = Logger.getLogger( AsyncResult.class );
    private byte[] result_;
    private AtomicBoolean done_ = new AtomicBoolean(false);
    private Lock lock_ = new ReentrantLock();
    private Condition condition_;

    public AsyncResult()
    {        
        condition_ = lock_.newCondition();
    }    
    
    public byte[] get()
    {
        lock_.lock();
        try
        {
            if ( !done_.get() )
            {
                condition_.await();                    
            }
        }
        catch ( InterruptedException ex )
        {
            logger_.warn( LogUtil.throwableToString(ex) );
        }
        finally
        {
            lock_.unlock();            
        }        
        return result_;
    }
    
    public boolean isDone()
    {
        return done_.get();
    }
    
    public byte[] get(long timeout, TimeUnit tu) throws TimeoutException
    {
        lock_.lock();
        try
        {            
            boolean bVal = true;
            try
            {
                if ( !done_.get() )
                {                    
                    bVal = condition_.await(timeout, tu);
                }
            }
            catch ( InterruptedException ex )
            {
                logger_.warn( LogUtil.throwableToString(ex) );
            }
            
            if ( !bVal && !done_.get() )
            {                                           
                throw new TimeoutException("Operation timed out.");
            }
        }
        finally
        {
            lock_.unlock();      
        }
        return result_;
    }
    
    public List<byte[]> multiget()
    {
        throw new UnsupportedOperationException("This operation is not supported in the AsyncResult abstraction.");
    }
    
    public List<byte[]> multiget(long timeout, TimeUnit tu) throws TimeoutException
    {
        throw new UnsupportedOperationException("This operation is not supported in the AsyncResult abstraction.");
    }
    
    public void result(Message response)
    {        
        try
        {
            lock_.lock();
            if ( !done_.get() )
            {                
                result_ = response.getMessageBody();
                done_.set(true);
                condition_.signal();
            }
        }
        finally
        {
            lock_.unlock();
        }        
    }    
}
