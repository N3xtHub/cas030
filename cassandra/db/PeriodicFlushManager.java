
/**
 *  Background flusher that force-flushes a column family periodically.
 */
class PeriodicFlushManager implements IComponentShutdown
{
    private static Logger logger_ = Logger.getLogger(PeriodicFlushManager.class);
    private static PeriodicFlushManager instance_;
    private static Lock lock_ = new ReentrantLock();
    private ScheduledExecutorService flusher_ = new DebuggableScheduledThreadPoolExecutor(1, new ThreadFactoryImpl("PERIODIC-FLUSHER-POOL"));

    public static PeriodicFlushManager instance()
    {
        if ( instance_ == null )
        {
            lock_.lock();
            try
            {
                if ( instance_ == null )
                    instance_ = new PeriodicFlushManager();
            }
            finally
            {
                lock_.unlock();
            }
        }
        return instance_;
    }

    public PeriodicFlushManager()
    {
        StorageService.instance().registerComponentForShutdown(this);
    }

    public void shutdown()
    {
        flusher_.shutdownNow();
    }

    public void submitPeriodicFlusher(final ColumnFamilyStore columnFamilyStore, int flushPeriodInMinutes)
    {        
        Runnable runnable= new Runnable()
        {
            public void run()
            {
                columnFamilyStore.forceFlush();
            }
        };
        flusher_.scheduleWithFixedDelay(runnable, flushPeriodInMinutes, flushPeriodInMinutes, TimeUnit.MINUTES);       
    }
}
