
public class QuorumResponseHandler<T> implements IAsyncCallback
{
    private static Logger logger_ = Logger.getLogger( QuorumResponseHandler.class );
    private Lock lock_ = new ReentrantLock();
    private Condition condition_;
    private int responseCount_;
    private List<Message> responses_ = new ArrayList<Message>();
    private IResponseResolver<T> responseResolver_;
    private AtomicBoolean done_ = new AtomicBoolean(false);
    
    public QuorumResponseHandler(int responseCount, IResponseResolver<T> responseResolver)
    {        
        condition_ = lock_.newCondition();
        responseCount_ = responseCount;
        responseResolver_ =  responseResolver;
    }
    
    public void  setResponseCount(int responseCount)
    {
        responseCount_ = responseCount;
    }
    
    public T get() throws TimeoutException, DigestMismatchException
    {
    	lock_.lock();
        try
        {            
            boolean bVal = true;            
            try
            {
            	if ( !done_.get() )
                {            		
            		bVal = condition_.await(DatabaseDescriptor.getRpcTimeout(), TimeUnit.MILLISECONDS);
                }
            }
            catch ( InterruptedException ex )
            {
                logger_.debug( LogUtil.throwableToString(ex) );
            }
            
            if ( !bVal && !done_.get() )
            {
                StringBuilder sb = new StringBuilder("");
                for ( Message message : responses_ )
                {
                    sb.append(message.getFrom());                    
                }                
                throw new TimeoutException("Operation timed out - received only " +  responses_.size() + " responses from " + sb.toString() + " .");
            }
        }
        finally
        {
            lock_.unlock();
            for(Message response : responses_)
            {
            	MessagingService.removeRegisteredCallback( response.getMessageId() );
            }
        }

    	return responseResolver_.resolve( responses_);
    }
    
    public void response(Message message)
    {
        lock_.lock();
    	int majority = (responseCount_ >> 1) + 1;            
        if ( !done_.get() )
        {
        	responses_.add( message );
        	if ( responses_.size() >= majority && responseResolver_.isDataPresent(responses_))
        	{
        		done_.set(true);
        		condition_.signal();            	
        	}
        }

        lock_.unlock();
    }
    
    public void attachContext(Object o)
    {
        throw new UnsupportedOperationException();
    }
}
