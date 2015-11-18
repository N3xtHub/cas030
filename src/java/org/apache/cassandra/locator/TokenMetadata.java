
public class TokenMetadata
{
    /* Maintains token to endpoint map of every node in the cluster. */
    private Map<Token, EndPoint> tokenToEndPointMap_ = new HashMap<Token, EndPoint>();
    /* Maintains a reverse index of endpoint to token in the cluster. */
    private Map<EndPoint, Token> endPointToTokenMap_ = new HashMap<EndPoint, Token>();
    
    /* Use this lock for manipulating the token map */
    private final ReadWriteLock lock_ = new ReentrantReadWriteLock(true);

    public TokenMetadata()
    {
    }

    private TokenMetadata(Map<Token, EndPoint> tokenToEndPointMap, Map<EndPoint, Token> endPointToTokenMap)
    {
        tokenToEndPointMap_ = tokenToEndPointMap;
        endPointToTokenMap_ = endPointToTokenMap;
    }
    
    public TokenMetadata cloneMe()
    {
        return new TokenMetadata(cloneTokenEndPointMap(), cloneEndPointTokenMap());
    }
    
    /**
     * Update the two maps in an safe mode. 
    */
    public void update(Token token, EndPoint endpoint)
    {
        lock_.writeLock().lock();
        {            
            Token oldToken = endPointToTokenMap_.get(endpoint);
            if ( oldToken != null )
                tokenToEndPointMap_.remove(oldToken);
            tokenToEndPointMap_.put(token, endpoint);
            endPointToTokenMap_.put(endpoint, token);
        }
    }
    
    /**
     * Remove the entries in the two maps.
     * @param endpoint
     */
    public void remove(EndPoint endpoint)
    {
        lock_.writeLock().lock()
        {            
            Token oldToken = endPointToTokenMap_.get(endpoint);
            if ( oldToken != null )
                tokenToEndPointMap_.remove(oldToken);            
            endPointToTokenMap_.remove(endpoint);
        }
    }
    
    public Token getToken(EndPoint endpoint)
    {
        lock_.readLock().lock();
        try
        {
            return endPointToTokenMap_.get(endpoint);
        }
        finally
        {
            lock_.readLock().unlock();
        }
    }
    
    public boolean isKnownEndPoint(EndPoint ep)
    {
        lock_.readLock().lock();
        try
        {
            return endPointToTokenMap_.containsKey(ep);
        }
        finally
        {
            lock_.readLock().unlock();
        }
    }
    
    /*
     * Returns a safe clone of tokenToEndPointMap_.
    */
    public Map<Token, EndPoint> cloneTokenEndPointMap()
    {
        lock_.readLock().lock();
        try
        {            
            return new HashMap<Token, EndPoint>( tokenToEndPointMap_ );
        }
        finally
        {
            lock_.readLock().unlock();
        }
    }
    
    /*
     * Returns a safe clone of endPointTokenMap_.
    */
    public Map<EndPoint, Token> cloneEndPointTokenMap()
    {
        return new HashMap<EndPoint, Token>( endPointToTokenMap_ );
    }
    
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        Set<EndPoint> eps = endPointToTokenMap_.keySet();
        
        for ( EndPoint ep : eps )
        {
            sb.append(ep);
            sb.append(":");
            sb.append(endPointToTokenMap_.get(ep));
            sb.append(System.getProperty("line.separator"));
        }
        
        return sb.toString();
    }
}
