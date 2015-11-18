
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
    
    /* Implementing the IStage interface methods */
    
    public String getName()
    {
        return name_;
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
    
    public ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit)
    {
        //return executorService_.schedule(command, delay, unit);
        throw new UnsupportedOperationException("This operation is not supported");
    }
    
    public ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit)
    {
        //return executorService_.scheduleAtFixedRate(command, initialDelay, period, unit);
        throw new UnsupportedOperationException("This operation is not supported");
    }
    
    public ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit)
    {
        //return executorService_.scheduleWithFixedDelay(command, initialDelay, delay, unit);
        throw new UnsupportedOperationException("This operation is not supported");
    }
    
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
    /* Finished implementing the IStage interface methods */
}

