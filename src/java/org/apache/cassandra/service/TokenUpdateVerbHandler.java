
public class TokenUpdateVerbHandler implements IVerbHandler
{
    private static Logger logger_ = Logger.getLogger(TokenUpdateVerbHandler.class);

    public void doVerb(Message message)
    {
    	byte[] body = message.getMessageBody();
        Token token = StorageService.getPartitioner().getTokenFactory().fromByteArray(body);
        StorageService.instance().updateToken(token);
    }

}
