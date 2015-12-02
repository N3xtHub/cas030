
class MinorCompactionManager implements IComponentShutdown
{
    private static MinorCompactionManager instance_;
    private static Lock lock_ = new ReentrantLock();
    private static Logger logger_ = Logger.getLogger(MinorCompactionManager.class);
    private static final long intervalInMins_ = 5;
    static final int COMPACTION_THRESHOLD = 4; // compact this many sstables at a time

    public static MinorCompactionManager instance()
    {
        if ( instance_ == null )
        {
            lock_.lock();
            try
            {
                if ( instance_ == null )
                    instance_ = new MinorCompactionManager();
            }
            finally
            {
                lock_.unlock();
            }
        }
        return instance_;
    }

    class FileCompactor2 implements Callable<Boolean>
    {
        private ColumnFamilyStore columnFamilyStore_;
        private List<Range> ranges_;
        private EndPoint target_;
        private List<String> fileList_;

        FileCompactor2(ColumnFamilyStore columnFamilyStore, List<Range> ranges, EndPoint target,List<String> fileList)
        {
            columnFamilyStore_ = columnFamilyStore;
            ranges_ = ranges;
            target_ = target;
            fileList_ = fileList;
        }

        public Boolean call()
        {
        	boolean result;
            logger_.debug("Started  compaction ..."+columnFamilyStore_.columnFamily_);
            try
            {
                result = columnFamilyStore_.doAntiCompaction(ranges_, target_,fileList_);
            }
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }
            logger_.debug("Finished compaction ..."+columnFamilyStore_.columnFamily_);
            return result;
        }
    }

    class OnDemandCompactor implements Runnable
    {
        private ColumnFamilyStore columnFamilyStore_;
        private long skip_ = 0L;

        OnDemandCompactor(ColumnFamilyStore columnFamilyStore, long skip)
        {
            columnFamilyStore_ = columnFamilyStore;
            skip_ = skip;
        }

        public void run()
        {
            logger_.debug("Started  Major compaction for " + columnFamilyStore_.columnFamily_);
            columnFamilyStore_.doMajorCompaction(skip_);
            logger_.debug("Finished Major compaction for " + columnFamilyStore_.columnFamily_);
        }
    }

    class CleanupCompactor implements Runnable
    {
        private ColumnFamilyStore columnFamilyStore_;

        CleanupCompactor(ColumnFamilyStore columnFamilyStore)
        {
        	columnFamilyStore_ = columnFamilyStore;
        }

        public void run()
        {
            logger_.debug("Started  compaction ..."+columnFamilyStore_.columnFamily_);
            try
            {
                columnFamilyStore_.doCleanupCompaction();
            }
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }
            logger_.debug("Finished compaction ..."+columnFamilyStore_.columnFamily_);
        }
    }
    
    
    private ScheduledExecutorService compactor_ = new DebuggableScheduledThreadPoolExecutor(1, new ThreadFactoryImpl("MINOR-COMPACTION-POOL"));

    public MinorCompactionManager()
    {
    	StorageService.instance().registerComponentForShutdown(this);
	}

    public void shutdown()
    {
    	compactor_.shutdownNow();
    }

    public void submitPeriodicCompaction(final ColumnFamilyStore columnFamilyStore)
    {
        Runnable runnable = new Runnable()
        {
            public void run()
            {
                try
                {
                    columnFamilyStore.doCompaction(COMPACTION_THRESHOLD);
                }
                catch (IOException e)
                {
                    throw new RuntimeException(e);
                }
            }
        };
    	compactor_.scheduleWithFixedDelay(runnable, MinorCompactionManager.intervalInMins_,
    			MinorCompactionManager.intervalInMins_, TimeUnit.MINUTES);       
    }

    public Future<Integer> submit(final ColumnFamilyStore columnFamilyStore)
    {
        return submit(columnFamilyStore, COMPACTION_THRESHOLD);
    }

    Future<Integer> submit(final ColumnFamilyStore columnFamilyStore, final int threshold)
    {
        Callable<Integer> callable = new Callable<Integer>()
        {
            public Integer call() throws IOException
            {
                return columnFamilyStore.doCompaction(threshold);
            }
        };
        return compactor_.submit(callable);
    }

    public void submitCleanup(ColumnFamilyStore columnFamilyStore)
    {
        compactor_.submit(new CleanupCompactor(columnFamilyStore));
    }

    public Future<Boolean> submit(ColumnFamilyStore columnFamilyStore, List<Range> ranges, EndPoint target, List<String> fileList)
    {
        return compactor_.submit( new FileCompactor2(columnFamilyStore, ranges, target, fileList) );
    }

    public void  submitMajor(ColumnFamilyStore columnFamilyStore, long skip)
    {
        compactor_.submit( new OnDemandCompactor(columnFamilyStore, skip) );
    }
}
