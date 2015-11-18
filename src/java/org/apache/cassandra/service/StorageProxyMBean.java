public interface StorageProxyMBean
{
    public double getReadLatency();
    public int getReadOperations();
    public double getRangeLatency();
    public int getRangeOperations();
    public double getWriteLatency();
    public int getWriteOperations();
}
