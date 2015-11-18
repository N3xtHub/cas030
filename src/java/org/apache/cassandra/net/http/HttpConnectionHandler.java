
public class HttpConnectionHandler extends SelectionKeyHandler
{
    private static Logger logger_ = Logger.getLogger(HttpConnectionHandler.class);
    
    public void accept(SelectionKey key)
    {
        try
        {
            ServerSocketChannel serverChannel = (ServerSocketChannel)key.channel();
            SocketChannel client = serverChannel.accept();
            if ( client != null )
            {
                client.configureBlocking(false);            
                SelectionKeyHandler handler = new HttpConnection();
                SelectorManager.getSelectorManager().register(client, handler, SelectionKey.OP_READ);
            }
        } 
        catch(IOException e) 
        {
            logger_.warn(e);
        }
    }
}
