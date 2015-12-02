
public class ConnectionStatistics
{
    private String localHost;
    private int localPort;
    private String remoteHost;
    private int remotePort;
    private int totalConnections;
    private int connectionsInUse;

    ConnectionStatistics(EndPoint localEp, EndPoint remoteEp, int tc, int ciu)
    {
        localHost = localEp.getHost();
        localPort = localEp.getPort();
        remoteHost = remoteEp.getHost();
        remotePort = remoteEp.getPort();
        totalConnections = tc;
        connectionsInUse = ciu;
    }
    
    public String getLocalHost()
    {
        return localHost;
    }
    
    public int getLocalPort()
    {
        return localPort;
    }
    
    public String getRemoteHost()
    {
        return remoteHost;
    }
    
    public int getRemotePort()
    {
        return remotePort;
    }
    
    public int getTotalConnections()
    {
        return totalConnections;
    }
    
    public int getConnectionInUse()
    {
        return connectionsInUse;
    }

    public String toString()
    {
        return localHost + ":" + localPort + "->" + remoteHost + ":" + remotePort + " Total Connections open : " + totalConnections + " Connections in use : " + connectionsInUse;
    }
}