
/**
 * This class is an implementation of the <i>IStage</i> interface. In particular
 * it is for a stage that has a thread pool with a single thread. For details 
 * please refer to the <i>IStage</i> documentation.
 */

public class SingleThreadedStage implements IStage 
{
    protected DebuggableThreadPoolExecutor executorService_;
    private String name_;

	public SingleThreadedStage(String name)
    {
        executorService_ = new DebuggableThreadPoolExecutor(name);
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
        return executorService_.getPendingTasks();
    }
}
