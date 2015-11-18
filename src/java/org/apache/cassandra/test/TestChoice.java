public class TestChoice
{
    private static final Logger logger_ = Logger.getLogger(TestChoice.class);
    private Set<EndPoint> allNodes_;
    private Map<EndPoint, List<EndPoint>> nodeToReplicaMap_ = new HashMap<EndPoint, List<EndPoint>>();
    
    public TestChoice(Set<EndPoint> allNodes)
    {
        allNodes_ = new HashSet<EndPoint>(allNodes);
    }
    
    public void assignReplicas()
    {
        IEndPointSnitch snitch = new EndPointSnitch();
        Set<EndPoint> allNodes = new HashSet<EndPoint>(allNodes_);
        Map<EndPoint, Integer> nOccurences = new HashMap<EndPoint, Integer>();
        
        for ( EndPoint node : allNodes_ )
        {
            nOccurences.put(node, 1);
        }
        
        for ( EndPoint node : allNodes_ )
        {
            allNodes.remove(node);
            for ( EndPoint choice : allNodes )
            {
                List<EndPoint> replicasChosen = nodeToReplicaMap_.get(node);
                if ( replicasChosen == null || replicasChosen.size() < DatabaseDescriptor.getReplicationFactor() - 1 )
                {
                    try
                    {
                        if ( !snitch.isInSameDataCenter(node, choice) )
                        {
                            if ( replicasChosen == null )
                            {
                                replicasChosen = new ArrayList<EndPoint>();
                                nodeToReplicaMap_.put(node, replicasChosen);
                            }
                            int nOccurence = nOccurences.get(choice);
                            if ( nOccurence < DatabaseDescriptor.getReplicationFactor() )
                            {
                                nOccurences.put(choice, ++nOccurence);
                                replicasChosen.add(choice);
                            }                            
                        }
                    }
                    catch ( UnknownHostException ex )
                    {
                        ex.printStackTrace();
                    }
                }
                else
                {
                    allNodes.add(node);
                    break;
                }
            }
        }
        
        
        Set<EndPoint> nodes = nodeToReplicaMap_.keySet();
        for ( EndPoint node : nodes )
        {
            List<EndPoint> replicas = nodeToReplicaMap_.get(node);
            StringBuilder sb = new StringBuilder("");
            for ( EndPoint replica : replicas )
            {
                sb.append(replica);
                sb.append(", ");
            }
            System.out.println(node + " ---> " + sb.toString() );
        }
    }
}
