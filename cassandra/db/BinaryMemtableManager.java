
public class BinaryMemtableManager
{
    private static BinaryMemtableManager instance_;
    private static Lock lock_ = new ReentrantLock();

    static BinaryMemtableManager instance() 
    {
        if ( instance_ == null )
        {
            instance_ = new BinaryMemtableManager();
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
            memtable_.flush();
        }
    }
    
    private ExecutorService flusher_ = new DebuggableThreadPoolExecutor("BINARY-MEMTABLE-FLUSHER-POOL");
    
    /* Submit memtables to be flushed to disk */
    void submit(String cfName, BinaryMemtable memtbl)
    {
    	flusher_.submit( new BinaryMemtableFlusher(memtbl) );
    }
}
