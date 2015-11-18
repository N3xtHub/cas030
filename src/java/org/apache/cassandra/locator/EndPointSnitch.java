
public class EndPointSnitch implements IEndPointSnitch
{
    public boolean isOnSameRack(EndPoint host, EndPoint host2) throws UnknownHostException
    {
        /*
         * Look at the IP Address of the two hosts. Compare 
         * the 3rd octet. If they are the same then the hosts
         * are in the same rack else different racks. 
        */        
        byte[] ip = getIPAddress(host.getHost());
        byte[] ip2 = getIPAddress(host2.getHost());
        
        return ( ip[2] == ip2[2] );
    }
    
    public boolean isInSameDataCenter(EndPoint host, EndPoint host2) throws UnknownHostException
    {
        /*
         * Look at the IP Address of the two hosts. Compare 
         * the 2nd octet. If they are the same then the hosts
         * are in the same datacenter else different datacenter. 
        */
        byte[] ip = getIPAddress(host.getHost());
        byte[] ip2 = getIPAddress(host2.getHost());
        
        return ( ip[1] == ip2[1] );
    }
    
    private byte[] getIPAddress(String host) throws UnknownHostException
    {
        InetAddress ia = InetAddress.getByName(host);         
        return ia.getAddress();
    }
}
