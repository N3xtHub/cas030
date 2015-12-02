
public class TcpConnectionHandler extends SelectionKeyHandler
{
    private static Logger logger_ = Logger.getLogger(TcpConnectionHandler.class);
    EndPoint localEp_;    
    
    public TcpConnectionHandler(EndPoint localEp) 
    {
        localEp_ = localEp;
    }

    public void accept(SelectionKey key)
    {
        try 
        {            
            ServerSocketChannel serverChannel = (ServerSocketChannel)key.channel();
            SocketChannel client = serverChannel.accept();
            
            if ( client != null )
            {                        
                //new TcpConnection(client, localEp_, true);
                TcpConnection.acceptConnection(client, localEp_, true);                
            }            
        } 
        catch(IOException e) 
        {
            logger_.warn(LogUtil.throwableToString(e));
        }
    }
}
