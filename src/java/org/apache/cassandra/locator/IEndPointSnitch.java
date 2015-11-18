

/**
 * This interface helps determine location of node in the data center relative to another node.
 * Give a node A and another node B it can tell if A and B are on the same rack or in the same
 * data center.
 */

public interface IEndPointSnitch
{
    /**
     * Helps determine if 2 nodes are in the same rack in the data center.
     * @param host a specified endpoint
     * @param host2 another specified endpoint
     * @return true if on the same rack false otherwise
     * @throws UnknownHostException
     */
    public boolean isOnSameRack(EndPoint host, EndPoint host2) throws UnknownHostException;
    
    /**
     * Helps determine if 2 nodes are in the same data center.
     * @param host a specified endpoint
     * @param host2 another specified endpoint
     * @return true if in the same data center false otherwise
     * @throws UnknownHostException
     */
    public boolean isInSameDataCenter(EndPoint host, EndPoint host2) throws UnknownHostException;
}
