

/*
 * This interface has two implementations. One which
 * does not respect rack or datacenter awareness and
 * the other which does.
 */
public interface IReplicaPlacementStrategy
{
	public EndPoint[] getStorageEndPoints(Token token);
    public Map<String, EndPoint[]> getStorageEndPoints(String[] keys);
    public EndPoint[] getStorageEndPoints(Token token, Map<Token, EndPoint> tokenToEndPointMap);
    public Map<EndPoint, EndPoint> getHintedStorageEndPoints(Token token);
}
