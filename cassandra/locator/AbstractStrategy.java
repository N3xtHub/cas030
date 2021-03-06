
/**
 * This class contains a helper method that will be used by
 * all abstraction that implement the IReplicaPlacementStrategy
 * interface.
*/
public abstract class AbstractStrategy implements IReplicaPlacementStrategy
{
    protected TokenMetadata tokenMetadata_;
    protected IPartitioner partitioner_;
    protected int replicas_;
    protected int storagePort_;

 
    /*
     * This method changes the ports of the endpoints from
     * the control port to the storage ports.
    */
    protected void retrofitPorts(List<EndPoint> eps)
    {
        for ( EndPoint ep : eps )
        {
            ep.setPort(storagePort_);
        }
    }

    protected EndPoint getNextAvailableEndPoint(EndPoint startPoint, List<EndPoint> topN, List<EndPoint> liveNodes)
    {
        EndPoint endPoint = null;
        Map<Token, EndPoint> tokenToEndPointMap = tokenMetadata_.cloneTokenEndPointMap();
        List tokens = new ArrayList(tokenToEndPointMap.keySet());
        Collections.sort(tokens);
        Token token = tokenMetadata_.getToken(startPoint);
        int index = Collections.binarySearch(tokens, token);
        if(index < 0)
        {
            index = (index + 1) * (-1);
            if (index >= tokens.size())
                index = 0;
        }
        int totalNodes = tokens.size();
        int startIndex = (index+1)%totalNodes;
        for (int i = startIndex, count = 1; count < totalNodes ; ++count, i = (i+1)%totalNodes)
        {
            EndPoint tmpEndPoint = tokenToEndPointMap.get(tokens.get(i));
            if(FailureDetector.instance().isAlive(tmpEndPoint) 
                && !topN.contains(tmpEndPoint) && !liveNodes.contains(tmpEndPoint))
            {
                endPoint = tmpEndPoint;
                break;
            }
        }
        return endPoint;
    }

    /*
     * This method returns the hint map. The key is the endpoint
     * on which the data is being placed and the value is the
     * endpoint which is in the top N.
     * Get the map of top N to the live nodes currently.
     */
    public Map<EndPoint, EndPoint> getHintedStorageEndPoints(Token token)
    {
        List<EndPoint> liveList = new ArrayList<EndPoint>();
        Map<EndPoint, EndPoint> map = new HashMap<EndPoint, EndPoint>();
        EndPoint[] topN = getStorageEndPoints( token );

        for( int i = 0 ; i < topN.length ; i++)
        {
            if( FailureDetector.instance().isAlive(topN[i]))
            {
                map.put(topN[i], topN[i]);
                liveList.add(topN[i]) ;
            }
            else
            {
                EndPoint endPoint = getNextAvailableEndPoint(topN[i], Arrays.asList(topN), liveList);
                if(endPoint != null)
                {
                    map.put(endPoint, topN[i]);
                    liveList.add(endPoint) ;
                }
            }
        }
        return map;
    }

    public abstract EndPoint[] getStorageEndPoints(Token token);
}
