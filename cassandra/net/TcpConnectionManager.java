
class TcpConnectionManager
{
    private Lock lock_ = new ReentrantLock();
    private List<TcpConnection> allConnections_;
    private EndPoint localEp_;
    private EndPoint remoteEp_;
    private int initialSize_;
    private int growthFactor_;
    private int maxSize_;
    private long lastTimeUsed_;
    private boolean isShut_;
    
    private int inUse_;

    TcpConnectionManager(int initialSize, int growthFactor, int maxSize, EndPoint localEp, EndPoint remoteEp)
    {
        initialSize_ = initialSize;
        growthFactor_ = growthFactor;
        maxSize_ = maxSize;
        localEp_ = localEp;
        remoteEp_ = remoteEp;     
        isShut_ = false;                
        lastTimeUsed_ = System.currentTimeMillis();        
        allConnections_ = new Vector<TcpConnection>(); 
    }
    
    TcpConnection getConnection() throws IOException
    {
        lock_.lock();
        try
        {
            if (allConnections_.isEmpty()) 
            {                
                TcpConnection conn = new TcpConnection(this, localEp_, remoteEp_);
                addToPool(conn);
                conn.inUse_ = true;
                incUsed();
                return conn;
            }
            
            TcpConnection least = getLeastLoaded();
            
            if ( (least != null && least.pending() == 0) || allConnections_.size() == maxSize_) {
                least.inUse_ = true;
                incUsed();
                return least;
            }
                                    
            TcpConnection connection = new TcpConnection(this, localEp_, remoteEp_);
            if ( connection != null && !contains(connection) )
            {
                addToPool(connection);
                connection.inUse_ = true;
                incUsed();
                return connection;
            }
            else
            {
                if ( connection != null )
                {                
                    connection.closeSocket();
                }
                return getLeastLoaded();
            }
        }
        finally
        {
            lock_.unlock();
        }
    }
    
    protected TcpConnection getLeastLoaded() 
    {  
        TcpConnection connection = null;
        lock_.lock();
        try
        {
            Collections.sort(allConnections_);
            connection = (allConnections_.size() > 0 ) ? allConnections_.get(0) : null;
        }
        finally
        {
            lock_.unlock();
        }
        return connection;
    }
    
    void removeConnection(TcpConnection connection)
    {
        allConnections_.remove(connection);        
    }
    
    void incUsed()
    {
        inUse_++;
    }
    
    void decUsed()
    {        
        inUse_--;
    }
    
    int getConnectionsInUse()
    {
        return inUse_;
    }

    void addToPool(TcpConnection connection)
    { 
        
        if ( contains(connection) )
            return;
        
        lock_.lock();
        try
        {
            if ( allConnections_.size() < maxSize_ )
            {                 
                allConnections_.add(connection);                
            }
            else
            {                
                connection.closeSocket();
            }
        }
        finally
        {
            lock_.unlock();
        }
    }
    
    void shutdown()
    {    
        lock_.lock();
        try
        {
            while ( allConnections_.size() > 0 )
            {
                TcpConnection connection = allConnections_.remove(0);                        
                connection.closeSocket();
            }
        }
        finally
        {
            lock_.unlock();
        }
        isShut_ = true;
    }

    int getPoolSize()
    {
        return allConnections_.size();
    }

    EndPoint getLocalEndPoint()
    {
        return localEp_;
    }
    
    EndPoint getRemoteEndPoint()
    {
        return remoteEp_;
    }
    
    int getPendingWrites()
    {
        int total = 0;
        lock_.lock();
        try
        {
            for ( TcpConnection connection : allConnections_ )
            {
                total += connection.pending();
            }
        }
        finally
        {
            lock_.unlock();
        }
        return total;
    }
    
    boolean contains(TcpConnection connection)
    {
        return allConnections_.contains(connection);
    }
}
