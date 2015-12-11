/**
 * This class is an implementation of the <i>IStage</i> interface. In particular
 * it is for a stage that has a thread pool with multiple threads. For details 
 * please refer to the <i>IStage</i> documentation.
 */

public class MultiThreadedStage implements IStage 
{    
    private String name_;
    private DebuggableThreadPoolExecutor executorService_;
            
    public MultiThreadedStage(String name, int numThreads)
    {        
        name_ = name;        
        executorService_ = new DebuggableThreadPoolExecutor( numThreads,
                numThreads,
                Integer.MAX_VALUE,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(),
                new ThreadFactoryImpl(name)
                );        
    }
    
    public String getName() 
    {        
        return name_;
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
    
    // UnsupportedOperationException();
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
        return executorService_.getPendingTasks();
    }
}
