
/**
 * This is a wrapper class for the <i>ScheduledThreadPoolExecutor</i>. It provides an implementation
 * for the <i>afterExecute()</i> found in the <i>ThreadPoolExecutor</i> class to log any unexpected 
 * Runtime Exceptions.
 */

public class DebuggableThreadPoolExecutor extends ThreadPoolExecutor implements DebuggableThreadPoolExecutorMBean
{
    private static Logger logger_ = Logger.getLogger(DebuggableThreadPoolExecutor.class);

    private ObjectName objName;
    public DebuggableThreadPoolExecutor(String threadPoolName) 
    {
        this(1, 1, Integer.MAX_VALUE, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(), new ThreadFactoryImpl(threadPoolName));
    }

    public DebuggableThreadPoolExecutor(int corePoolSize,
            int maximumPoolSize,
            long keepAliveTime,
            TimeUnit unit,
            BlockingQueue<Runnable> workQueue,
            ThreadFactoryImpl threadFactory)
    {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
        super.prestartAllCoreThreads();
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        objName = new ObjectName("org.apache.cassandra.concurrent:type=" + threadFactory.id_);
        mbs.registerMBean(this, objName);
    }
    
    public void unregisterMBean()
    {
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        mbs.unregisterMBean(objName);
    }

    public long getPendingTasks()
    {
        return getTaskCount() - getCompletedTaskCount();
    }

    /*
     * 
     *  (non-Javadoc)
     * @see java.util.concurrent.ThreadPoolExecutor#afterExecute(java.lang.Runnable, java.lang.Throwable)
     * Helps us in figuring out why sometimes the threads are getting 
     * killed and replaced by new ones.
     */
    public void afterExecute(Runnable r, Throwable t)
    {
        super.afterExecute(r,t);

        if (r instanceof FutureTask) {
            assert t == null;
            ((FutureTask)r).get();
        }
      
        Context ctx = ThreadLocalContext.get();
        Object object = ctx.get(r.getClass().getName());   
    }
}
