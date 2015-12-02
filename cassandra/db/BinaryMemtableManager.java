
public class BinaryMemtableManager
{
    private static BinaryMemtableManager instance_;
    private static Lock lock_ = new ReentrantLock();
    private static Logger logger_ = Logger.getLogger(BinaryMemtableManager.class);    

    static BinaryMemtableManager instance() 
    {
        if ( instance_ == null )
        {
            lock_.lock();
            try
            {
                if ( instance_ == null )
                    instance_ = new BinaryMemtableManager();
            }
            finally
            {
                lock_.unlock();
            }
        }
        return instance_;
    }
    
    class BinaryMemtableFlusher implements Runnable
    {
        private BinaryMemtable memtable_;
        
        BinaryMemtableFlusher(BinaryMemtable memtable)
        {
            memtable_ = memtable;
        }
        
        public void run()
        {
            try
            {
            	memtable_.flush();
            }
            catch (IOException e)
            {
                logger_.debug( LogUtil.throwableToString(e) );
            }        	
        }
    }
    
    private ExecutorService flusher_ = new DebuggableThreadPoolExecutor("BINARY-MEMTABLE-FLUSHER-POOL");
    
    /* Submit memtables to be flushed to disk */
    void submit(String cfName, BinaryMemtable memtbl)
    {
    	flusher_.submit( new BinaryMemtableFlusher(memtbl) );
    }
}
