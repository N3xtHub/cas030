
public class TokenUpdateVerbHandler implements IVerbHandler
{
    private static Logger logger_ = Logger.getLogger(TokenUpdateVerbHandler.class);

    public void doVerb(Message message)
    {
    	byte[] body = message.getMessageBody();
        
        try
        {
            DataInputBuffer bufIn = new DataInputBuffer();
            bufIn.reset(body, body.length);
            /* Deserialize to get the token for this endpoint. */
            Token token = Token.serializer().deserialize(bufIn);

            logger_.info("Updating the token to [" + token + "]");
            StorageService.instance().updateToken(token);
            
            /* Get the headers for this message */
            Map<String, byte[]> headers = message.getHeaders();
            headers.remove( StorageService.getLocalStorageEndPoint().getHost() );
            logger_.debug("Number of nodes in the header " + headers.size());
            Set<String> nodes = headers.keySet();
            
            IPartitioner p = StorageService.getPartitioner();
            for ( String node : nodes )
            {            
                logger_.debug("Processing node " + node);
                byte[] bytes = headers.remove(node);
                /* Send a message to this node to update its token to the one retreived. */
                EndPoint target = new EndPoint(node, DatabaseDescriptor.getStoragePort());
                token = p.getTokenFactory().fromByteArray(bytes);
                
                /* Reset the new Message */
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                DataOutputStream dos = new DataOutputStream(bos);
                Token.serializer().serialize(token, dos);
                message.setMessageBody(bos.toByteArray());
                
                logger_.debug("Sending a token update message to " + target + " to update it to " + token);
                MessagingService.getMessagingInstance().sendOneWay(message, target);
                break;
            }                        
        }
    	catch( IOException ex )
    	{
    		logger_.debug(LogUtil.throwableToString(ex));
    	}
    }

}
