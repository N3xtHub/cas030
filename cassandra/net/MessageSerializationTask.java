
class MessageSerializationTask implements Runnable
{
    private static Logger logger_ = Logger.getLogger(MessageSerializationTask.class);
    private Message message_;
    private EndPoint to_;    
    
    public MessageSerializationTask(Message message, EndPoint to)
    {
        message_ = message;
        to_ = to;        
    }
    
    public Message getMessage()
    {
        return message_;
    }

    public void run()
    {        
        /* Adding the message to be serialized in the TLS. For accessing in the afterExecute() */
        Context ctx = new Context();
        ctx.put(this.getClass().getName(), message_);
        ThreadLocalContext.put(ctx);
        
        TcpConnection connection = null;
        try
        {
            Message message = SinkManager.processClientMessageSink(message_);
            if(null == message) 
                return;
            connection = MessagingService.getConnection(message_.getFrom(), to_);
            connection.write(message);            
        }            
        catch ( SocketException se )
        {            
            // Shutting down the entire pool. May be too conservative an approach.
            MessagingService.getConnectionPool(message_.getFrom(), to_).shutdown();
            logger_.warn(LogUtil.throwableToString(se));
        }
        catch ( IOException e )
        {
            logConnectAndIOException(e, connection);
        }
        catch (Throwable th)
        {
            logger_.warn(LogUtil.throwableToString(th));
        }
        finally
        {
            if ( connection != null )
            {
                connection.close();
            }            
        }
    }
    
    private void logConnectAndIOException(IOException ex, TcpConnection connection)
    {                    
        if ( connection != null )
        {
            connection.errorClose();
        }
        logger_.warn(LogUtil.throwableToString(ex));
    }
}

