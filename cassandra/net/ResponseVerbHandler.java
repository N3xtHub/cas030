
class ResponseVerbHandler implements IVerbHandler
{
    private static final Logger logger_ = Logger.getLogger( ResponseVerbHandler.class );
    
    public void doVerb(Message message)
    {     
        String messageId = message.getMessageId();        
        IAsyncCallback cb = MessagingService.getRegisteredCallback(messageId);
        if ( cb != null )
        {
            logger_.debug("Processing response on a callback from " + message.getMessageId() + "@" + message.getFrom());
            cb.response(message);
        }
        else
        {            
            IAsyncResult ar = MessagingService.getAsyncResult(messageId);
            if ( ar != null )
            {
                logger_.debug("Processing response on an async result from " + message.getMessageId() + "@" + message.getFrom());
                ar.result(message);
            }
        }
    }
}
