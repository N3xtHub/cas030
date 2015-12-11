
public class SingleThreadedContinuationStage implements IStage 
{
    protected ContinuationsExecutor executorService_;
    private String name_;

    public SingleThreadedContinuationStage(String name)
    {        
        executorService_ = new ContinuationsExecutor( 1,
                1,
                Integer.MAX_VALUE,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(),
                new ThreadFactoryImpl(name)
                );        
        name_ = name;        
    }
    
    public ExecutorService getInternalThreadPool()
    {
        return executorService_;
    }
    
    public void execute(Runnable runnable)
    {
        executorService_.execute(runnable);
    }
    
    public Future<Object> execute(Callable<Object> callable)
    {
        return executorService_.submit(callable);
    }
    
    // UnsupportedOperationException();
    public ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit)
    public ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit)
    public ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit)
    
    public void shutdown()
    {
        executorService_.shutdownNow();
    }
    
    public boolean isShutdown()
    {
        return executorService_.isShutdown();
    }    
    
    public long getPendingTasks(){
        return (executorService_.getTaskCount() - executorService_.getCompletedTaskCount());
    }
}

