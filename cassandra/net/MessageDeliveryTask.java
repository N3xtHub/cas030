
public class MessageDeliveryTask implements Runnable
{
    private Message message_;
    private static Logger logger_ = Logger.getLogger(MessageDeliveryTask.class);    
    
    public MessageDeliveryTask(Message message)
    {
        message_ = message;    
    }
    
    public void run()
    { 
        String verb = message_.getVerb();
        IVerbHandler verbHandler = MessagingService.getMessagingInstance().getVerbHandler(verb);
        if ( verbHandler != null )
        {
            verbHandler.doVerb(message_);
        }
    }
}
