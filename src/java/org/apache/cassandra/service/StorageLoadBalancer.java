
/*
 * The load balancing algorithm here is an implementation of
 * the algorithm as described in the paper "Scalable range query
 * processing for large-scale distributed database applications".
 * This class keeps track of load information across the system.
 * It registers itself with the Gossiper for ApplicationState namely
 * load information i.e number of requests processed w.r.t distinct
 * keys at an Endpoint. Monitor load infomation for a 5 minute
 * interval and then do load balancing operations if necessary.
 * 
 */
final class StorageLoadBalancer implements IEndPointStateChangeSubscriber, IComponentShutdown
{
    class LoadBalancer implements Runnable
    {
        LoadBalancer()
        {
            /* Copy the entries in loadInfo_ into loadInfo2_ and use it for all calculations */
            loadInfo2_.putAll(loadInfo_);
        }

        /**
         * Obtain a node which is a potential target. Start with
         * the neighbours i.e either successor or predecessor.
         * Send the target a MoveMessage. If the node cannot be
         * relocated on the ring then we pick another candidate for
         * relocation.
        */        
        public void run()
        {
        }
    }

    class MoveMessageVerbHandler implements IVerbHandler
    {
        public void doVerb(Message message)
        {
            Message reply = message.getReply(
                StorageService.getLocalStorageEndPoint(), new byte[] {(byte)(isMoveable_.get() ? 1 : 0)});
            MessagingService.getMessagingInstance().sendOneWay(reply, message.getFrom());
            if ( isMoveable_.get() )
            {
                // MoveMessage moveMessage = (MoveMessage)message.getMessageBody()[0];
                /* Start the leave operation and join the ring at the position specified */
                isMoveable_.set(false);
            }
        }
    }

    private static final String lbStage_ = "LOAD-BALANCER-STAGE";
    private static final String moveMessageVerbHandler_ = "MOVE-MESSAGE-VERB-HANDLER";
    /* time to delay in minutes the actual load balance procedure if heavily loaded */
    private static final int delay_ = 5;
    /* Ratio of highest loaded node and the average load. */
    private static final double ratio_ = 1.5;

    private StorageService storageService_;
    /* this indicates whether this node is already helping someone else */
    private AtomicBoolean isMoveable_ = new AtomicBoolean(false);
    private Map<EndPoint, LoadInfo> loadInfo_ = new HashMap<EndPoint, LoadInfo>();
    /* This map is a clone of the one above and is used for various calculations during LB operation */
    private Map<EndPoint, LoadInfo> loadInfo2_ = new HashMap<EndPoint, LoadInfo>();
    /* This thread pool is used for initiating load balancing operations */
    private ScheduledThreadPoolExecutor lb_ = new DebuggableScheduledThreadPoolExecutor(
            1,
            new ThreadFactoryImpl("LB-OPERATIONS")
            );
    /* This thread pool is used by target node to leave the ring. */
    private ExecutorService lbOperations_ = new DebuggableThreadPoolExecutor("LB-TARGET");

    StorageLoadBalancer(StorageService storageService)
    {
        storageService_ = storageService;
        /* register the load balancer stage */
        StageManager.registerStage(StorageLoadBalancer.lbStage_, new SingleThreadedStage(StorageLoadBalancer.lbStage_));
        /* register the load balancer verb handler */
        MessagingService.getMessagingInstance().registerVerbHandlers(StorageLoadBalancer.moveMessageVerbHandler_, new MoveMessageVerbHandler());
        /* register with the StorageService */
        storageService_.registerComponentForShutdown(this);
    }

    public void start()
    {
        /* Register with the Gossiper for EndPointState notifications */
        Gossiper.instance().register(this);
    }

    public void shutdown()
    {
        lbOperations_.shutdownNow();
        lb_.shutdownNow();
    }

    public void onChange(EndPoint endpoint, EndPointState epState)
    {
        // load information for this specified endpoint for load balancing 
        ApplicationState loadInfoState = epState.getApplicationState(LoadDisseminator.loadInfo_);
        if ( loadInfoState != null )
        {
            String lInfoState = loadInfoState.getState();
            LoadInfo lInfo = new LoadInfo(lInfoState);
            loadInfo_.put(endpoint, lInfo);
        }       
    }

    // Load information associated with a given endpoint.
    LoadInfo getLoad(EndPoint ep)
    {
        LoadInfo li = loadInfo_.get(ep);        
        return li;        
    }

    
    
    private boolean isANeighbour(EndPoint neighbour)
    {
        EndPoint predecessor = storageService_.getPredecessor(StorageService.getLocalStorageEndPoint());
        if ( predecessor.equals(neighbour) )
            return true;

        EndPoint successor = storageService_.getSuccessor(StorageService.getLocalStorageEndPoint());
        if ( successor.equals(neighbour) )
            return true;

        return false;
    }
}

class MoveMessage implements Serializable
{
    private Token targetToken_;
}
