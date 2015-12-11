

public class ContinuationStage implements IStage
{
    private String name_;
    private ContinuationsExecutor executorService_;
            
    public ContinuationStage(String name, int numThreads)
    {        
        name_ = name;        
        executorService_ = new ContinuationsExecutor( numThreads,
                numThreads,
                Integer.MAX_VALUE,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(),
                new ThreadFactoryImpl(name)
                );        
    }
    
    public ExecutorService getInternalThreadPool()
    {
        return executorService_;
    }

    public Future<Object> execute(Callable<Object> callable) {
        return executorService_.submit(callable);
    }
    
    public void execute(Runnable runnable) {
        executorService_.execute(runnable);
    }
    
    //UnsupportedOperationException("");
    public ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit)
    public ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit)
    public ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit)
    
    public void shutdown() {  
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
