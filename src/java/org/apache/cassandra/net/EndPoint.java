
public class EndPoint implements Serializable, Comparable<EndPoint>
{
    // logging and profiling.
    private static Logger logger_ = Logger.getLogger(EndPoint.class);
    private static final long serialVersionUID = -4962625949179835907L;
    private static Map<CharBuffer, String> hostNames_ = new HashMap<CharBuffer, String>();
    protected static final int randomPort_ = 5555;
    public static EndPoint randomLocalEndPoint_;
    
    static
    {
        try
        {
            randomLocalEndPoint_ = new EndPoint(FBUtilities.getHostAddress(), EndPoint.randomPort_);
        }        
        catch ( IOException ex )
        {
            logger_.warn(LogUtil.throwableToString(ex));
        }
    }

    private String host_;
    private int port_;

    private transient InetSocketAddress ia_;

    public EndPoint(String host, int port)
    {
        assert host.matches("\\d+\\.\\d+\\.\\d+\\.\\d+") : host;
        host_ = host;
        port_ = port;
    }

    // create a local endpoint id
    public EndPoint(int port)
    {
        try
        {
            host_ = FBUtilities.getHostAddress();
        }
        catch (UnknownHostException e)
        {
            throw new RuntimeException(e);
        }
        port_ = port;
    }

    public String getHost()
    {
        return host_;
    }

    public int getPort()
    {
        return port_;
    }

    public void setPort(int port)
    {
        port_ = port;
    }

    public InetSocketAddress getInetAddress()
    {
        if (ia_ == null || ia_.isUnresolved())
        {
            ia_ = new InetSocketAddress(host_, port_);
        }
        return ia_;
    }

    public boolean equals(Object o)
    {
        if (!(o instanceof EndPoint))
            return false;

        EndPoint rhs = (EndPoint) o;
        return (host_.equals(rhs.host_) && port_ == rhs.port_);
    }

    public int hashCode()
    {
        return (host_ + port_).hashCode();
    }

    public int compareTo(EndPoint rhs)
    {
        return host_.compareTo(rhs.host_);
    }

    public String toString()
    {
        return (host_ + ":" + port_);
    }

    public static EndPoint fromString(String str)
    {
        String[] values = str.split(":");
        return new EndPoint(values[0], Integer.parseInt(values[1]));
    }

    public static byte[] toBytes(EndPoint ep)
    {
        ByteBuffer buffer = ByteBuffer.allocate(6);
        byte[] iaBytes = ep.getInetAddress().getAddress().getAddress();
        buffer.put(iaBytes);
        buffer.put(MessagingService.toByteArray((short) ep.getPort()));
        buffer.flip();
        return buffer.array();
    }

    public static EndPoint fromBytes(byte[] bytes)
    {
        ByteBuffer buffer = ByteBuffer.allocate(4);
        System.arraycopy(bytes, 0, buffer.array(), 0, 4);
        byte[] portBytes = new byte[2];
        System.arraycopy(bytes, 4, portBytes, 0, portBytes.length);
        try
        {
            CharBuffer charBuffer = buffer.asCharBuffer();
            String host = hostNames_.get(charBuffer);
            if (host == null)
            {               
                host = InetAddress.getByAddress(buffer.array()).getHostAddress();              
                hostNames_.put(charBuffer, host);
            }
            int port = (int) MessagingService.byteArrayToShort(portBytes);
            return new EndPoint(host, port);
        }
        catch (UnknownHostException e)
        {
            throw new IllegalArgumentException(e);
        }
    }
}

