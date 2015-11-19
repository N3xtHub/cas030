

public class CalloutDeployVerbHandler implements IVerbHandler
{
    private static Logger logger_ = Logger.getLogger(CalloutDeployVerbHandler.class);
    
    public void doVerb(Message message)
    {
        byte[] bytes = message.getMessageBody();
        DataInputBuffer bufIn = new DataInputBuffer();
        bufIn.reset(bytes, bytes.length);
        try
        {
            CalloutDeployMessage cdMessage = CalloutDeployMessage.serializer().deserialize(bufIn);
            /* save the callout to callout cache and to disk. */
            CalloutManager.instance().addCallout( cdMessage.getCallout(), cdMessage.getScript() );
        }
        catch ( IOException ex )
        {
            logger_.warn(LogUtil.throwableToString(ex));
        }        
    }
}
