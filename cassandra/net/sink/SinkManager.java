
public class SinkManager
{
    private static LinkedList<IMessageSink> messageSinks_ = new LinkedList<IMessageSink>();

    public static boolean isInitialized()
    {
        return ( messageSinks_.size() > 0 );
    }

    public static void addMessageSink(IMessageSink ms)
    {
        messageSinks_.addLast(ms);
    }
    
    public static void clearSinks(){
        messageSinks_.clear();
    }

    public static Message processClientMessageSink(Message message)
    {
        ListIterator<IMessageSink> li = messageSinks_.listIterator();
        while ( li.hasNext() )
        {
            IMessageSink ms = li.next();
            message = ms.handleMessage(message);
            if ( message == null )
            {
                return null;
            }
        }
        return message;
    }

    public static Message processServerMessageSink(Message message)
    {
        ListIterator<IMessageSink> li = messageSinks_.listIterator(messageSinks_.size());
        while ( li.hasPrevious() )
        {
            IMessageSink ms = li.previous();
            message = ms.handleMessage(message);
            if ( message == null )
            {
                return null;
            }
        }
        return message;
    }
}
