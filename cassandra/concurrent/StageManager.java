
/**
 * This class manages all stages that exist within a process. The application registers
 * and de-registers stages with this abstraction. Any component that has the ID
 * associated with a stage can obtain a handle to actual stage.
 */

public class StageManager
{
    private static Map<String, IStage > stageQueues_ = new HashMap<String, IStage>();
    
    public static void registerStage(String stageName, IStage stage)
    {
        stageQueues_.put(stageName, stage);
    }
    
    /**
     * Returns the stage that we are currently executing on.
     * This relies on the fact that the thread names in the
     * stage have the name of the stage as the prefix.
     * @return Returns the stage that we are currently executing on.
     */
    public static IStage getCurrentStage()
    {
        String name = Thread.currentThread().getName();
        String[] peices = name.split(":");
        IStage stage = getStage(peices[0]);
        return stage;
    }

    public static IStage getStage(String stageName)
    {
        return stageQueues_.get(stageName);
    }
    
    /**
     * Retrieve the internal thread pool associated with the
     * specified stage name.
     * @param stageName name of the stage.
     */
    public static ExecutorService getStageInternalThreadPool(String stageName)
    {
        IStage stage = getStage(stageName);
        return stage.getInternalThreadPool();
    }

    /**
     * Deregister a stage from StageManager
     * @param stageName stage name.
     */
    public static void deregisterStage(String stageName)
    {
        stageQueues_.remove(stageName);
    }

    /**
     * This method gets the number of tasks on the
     * stage's internal queue.
     * @param stage name of the stage
     * @return stage task count.
     */
    public static long getStageTaskCount(String stage)
    {
        return stageQueues_.get(stage).getPendingTasks();
    }

    /**
     * This method shuts down all registered stages.
     */
    public static void shutdown()
    {
        Set<String> stages = stageQueues_.keySet();
        for ( String stage : stages )
        {
            IStage registeredStage = stageQueues_.get(stage);
            registeredStage.shutdown();
        }
    }
}
