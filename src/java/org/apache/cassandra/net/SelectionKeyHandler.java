
public class SelectionKeyHandler 
{
    /**
     * Method which is called when the key becomes acceptable.
     *
     * @param key The key which is acceptable.
     */
    public void accept(SelectionKey key)
    {
         throw new UnsupportedOperationException("accept() cannot be called on " + getClass().getName() + "!");
    }
    
    /**
     * Method which is called when the key becomes connectable.
     *
     * @param key The key which is connectable.
     */
    public void connect(SelectionKey key)
    {
        throw new UnsupportedOperationException("connect() cannot be called on " + getClass().getName() + "!");
    }
    
    /**
     * Method which is called when the key becomes readable.
     *
     * @param key The key which is readable.
     */
    public void read(SelectionKey key)
    {
        throw new UnsupportedOperationException("read() cannot be called on " + getClass().getName() + "!");
    }
    
    /**
     * Method which is called when the key becomes writable.
     *
     * @param key The key which is writable.
     */
    public void write(SelectionKey key)
    {
        throw new UnsupportedOperationException("write() cannot be called on " + getClass().getName() + "!");
    }
    
    protected static void turnOnInterestOps(SelectionKey key, int ops)
    {
        synchronized(key)
        {
            key.interestOps(key.interestOps() | ops);
        }
    }
    
    protected static void turnOffInterestOps(SelectionKey key, int ops)
    {
        synchronized(key)
        {
            key.interestOps(key.interestOps() & (~ops) );
        }
    }
}
