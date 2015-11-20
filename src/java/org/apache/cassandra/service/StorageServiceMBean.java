
public interface StorageServiceMBean
{    
    public String getLiveNodes();
    public String getUnreachableNodes();
    public String getToken();
    
    /**
     * This method will cause the local node initiate
     * the bootstrap process for all the nodes specified
     * in the string parameter passed in. This local node
     * will calculate who gives what ranges to the nodes
     * and then instructs the nodes to do so.
     * 
     * @param nodes colon delimited list of endpoints that need
     *              to be bootstrapped
    */
    public void loadAll(String nodes);
    
    public void doGC();

    /**
     * Stream the files in the bootstrap directory over to the
     * node being bootstrapped. This is used in case of normal
     * bootstrap failure. Use a tool to re-calculate the cardinality
     * at a later point at the destination.
     * @param sources colon separated list of directories from where 
     *                files need to be picked up.
     * @param target endpoint receiving data.
    */
    public void forceHandoff(String directories, String target) throws IOException;
}
