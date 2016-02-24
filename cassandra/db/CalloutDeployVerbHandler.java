

public class CalloutDeployVerbHandler implements IVerbHandler
{
     public void doVerb(Message message)
    {
        byte[] bytes = message.getMessageBody();
        DataInputBuffer bufIn = new DataInputBuffer();
        bufIn.reset(bytes, bytes.length);

        CalloutDeployMessage cdMessage = CalloutDeployMessage.serializer().deserialize(bufIn);
        /* save the callout to callout cache and to disk. */
        CalloutManager.instance().addCallout( cdMessage.getCallout(), cdMessage.getScript() );     
    }
}
