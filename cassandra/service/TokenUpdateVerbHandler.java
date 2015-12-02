
public class TokenUpdateVerbHandler implements IVerbHandler
{
    public void doVerb(Message message)
    {
    	byte[] body = message.getMessageBody();
        Token token = StorageService.getPartitioner().getTokenFactory().fromByteArray(body);
        StorageService.instance().updateToken(token);
    }

}
