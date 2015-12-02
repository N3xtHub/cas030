
public class MultiAsyncResult implements IAsyncResult
{
    private static Logger logger_ = Logger.getLogger( AsyncResult.class );
    private int expectedResults_;
    private List<byte[]> result_ = new ArrayList<byte[]>();
    private AtomicBoolean done_ = new AtomicBoolean(false);
    private Lock lock_ = new ReentrantLock();
    private Condition condition_;
    
    MultiAsyncResult(int expectedResults)
    {
        expectedResults_ = expectedResults;
        condition_ = lock_.newCondition();
    }
    
    public byte[] get()
    {
        throw new UnsupportedOperationException("This operation is not supported in the AsyncResult abstraction.");
    }
    
    public byte[] get(long timeout, TimeUnit tu) throws TimeoutException
    {
        throw new UnsupportedOperationException("This operation is not supported in the AsyncResult abstraction.");
    }
    
    public List<byte[]> multiget()
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
    
    public List<byte[]> multiget(long timeout, TimeUnit tu) throws TimeoutException
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
    
    public void result(Message result)
    {        
        try
        {
            lock_.lock();
            if ( !done_.get() )
            {
                result_.add(result.getMessageBody());
                if ( result_.size() == expectedResults_ )
                {
                    done_.set(true);
                    condition_.signal();
                }
            }
        }
        finally
        {
            lock_.unlock();
        }        
    }    
}
