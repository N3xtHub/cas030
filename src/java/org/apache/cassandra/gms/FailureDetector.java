
/**
 * This FailureDetector is an implementation of the paper titled
 * "The Phi Accrual Failure Detector" by Hayashibara. 
 * Check the paper and the <i>IFailureDetector</i> interface for details.
 * 
 */
public class FailureDetector implements IFailureDetector, FailureDetectorMBean
{
    private static final int sampleSize_ = 1000;
    private static final int phiSuspectThreshold_ = 5;
    private static final int phiConvictThreshold_ = 8;

    /* The Failure Detector has to have been up for atleast 1 min. */
    private static final long uptimeThreshold_ = 60000; // one minute

    private static IFailureDetector failureDetector_;
    /* Used to lock the factory for creation of FailureDetector instance */
    private static Lock createLock_ = new ReentrantLock();
    /* The time when the module was instantiated. */
    private static long creationTime_;
    
    public static IFailureDetector instance()
    {     
        atomGet @   failureDetector_ = new FailureDetector();

        return failureDetector_;
    }
    
    private Map<EndPoint, ArrivalWindow> arrivalSamples_ = new Hashtable<EndPoint, ArrivalWindow>();
    private List<IFailureDetectionEventListener> fdEvntListeners_ = new ArrayList<IFailureDetectionEventListener>();
    
    public FailureDetector()
    {
        creationTime_ = System.currentTimeMillis();
        // Register this instance with JMX
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        mbs.registerMBean(this, new ObjectName("cassandra.gms:type=FailureDetector"));
    }
    
    public void dumpInterArrivalTimes()
    {
        FileOutputStream fos = new FileOutputStream("/var/tmp/output-" + System.currentTimeMillis() + ".dat", true);
        fos.write(toString().getBytes());
        fos.close();
    }
    
    /**
     * We dump the arrival window for any endpoint only if the 
     * local Failure Detector module has been up for more than a 
     * minute.
     * 
     * @param ep for which the arrival window needs to be dumped.
     */
    private void dumpInterArrivalTimes(EndPoint ep)
    {
        long now = System.currentTimeMillis();
        if ( (now - FailureDetector.creationTime_) <= FailureDetector.uptimeThreshold_ )
            return;
    
        FileOutputStream fos = new FileOutputStream("/var/tmp/output-" + System.currentTimeMillis() + "-" + ep + ".dat", true);
        ArrivalWindow hWnd = arrivalSamples_.get(ep);
        fos.write(hWnd.toString().getBytes());
        fos.close();
    }

    public boolean isAlive(EndPoint ep)
    {
        /* If the endpoint in question is the local endpoint return true. */
        String localHost = FBUtilities.getHostAddress();
        if ( localHost.equals( ep.getHost() ) )
                return true;

    	/* Incoming port is assumed to be the Storage port. We need to change it to the control port */
    	EndPoint ep2 = new EndPoint(ep.getHost(), DatabaseDescriptor.getControlPort());        
        EndPointState epState = Gossiper.instance().getEndPointStateForEndPoint(ep2);
        return epState.isAlive();
    }
    
    public void report(EndPoint ep)
    {
        long now = System.currentTimeMillis();
        ArrivalWindow heartbeatWindow = arrivalSamples_.get(ep);
        if ( heartbeatWindow == null )
        {
            heartbeatWindow = new ArrivalWindow(sampleSize_);
            arrivalSamples_.put(ep, heartbeatWindow);
        }

        heartbeatWindow.add(now);
    }
    
    public void intepret(EndPoint ep)
    {
        ArrivalWindow hbWnd = arrivalSamples_.get(ep);

        long now = System.currentTimeMillis();
        /* We need this so that we do not suspect a convict. */
        boolean isConvicted = false;
        double phi = hbWnd.phi(now);
       
        if ( !isConvicted && phi > phiSuspectThreshold_ )
        {     
            for ( IFailureDetectionEventListener listener : fdEvntListeners_ )
            {
                listener.suspect(ep);
            }
        }        
    }
    
    public void registerFailureDetectionEventListener(IFailureDetectionEventListener listener)
    {
        fdEvntListeners_.add(listener);
    }
    
    public void unregisterFailureDetectionEventListener(IFailureDetectionEventListener listener)
    {
        fdEvntListeners_.remove(listener);
    }
}

class ArrivalWindow
{
    private double tLast_ = 0L;
    private BoundedStatsDeque arrivalIntervals_;
    private int size_;
    
    ArrivalWindow(int size)
    {
        size_ = size;
        arrivalIntervals_ = new BoundedStatsDeque(size);
    }
    
    synchronized void add(double value)
    {
        double interArrivalTime;
        if ( tLast_ > 0L )
        {                        
            interArrivalTime = (value - tLast_);            
        }
        else
        {
            interArrivalTime = Gossiper.intervalInMillis_ / 2;
        }
        tLast_ = value;            
        arrivalIntervals_.add(interArrivalTime);        
    }
    
    synchronized double sum()
    {
        return arrivalIntervals_.sum();
    }
    
    synchronized double sumOfDeviations()
    {
        return arrivalIntervals_.sumOfDeviations();
    }
    
    synchronized double mean()
    {
        return arrivalIntervals_.mean();
    }
    
    synchronized double variance()
    {
        return arrivalIntervals_.variance();
    }
    
    double stdev()
    {
        return arrivalIntervals_.stdev();
    }
    
    void clear()
    {
        arrivalIntervals_.clear();
    }
    
    double p(double t)
    {
        double mean = mean();
        double exponent = (-1)*(t)/mean;
        return 1 - ( 1 - Math.pow(Math.E, exponent) );
    }
    
    double phi(long tnow)
    {            
        int size = arrivalIntervals_.size();
        double log = 0d;
        if ( size > 0 )
        {
            double t = tnow - tLast_;                
            double probability = p(t);       
            log = (-1) * Math.log10( probability );                                 
        }
        return log;           
    } 
}

