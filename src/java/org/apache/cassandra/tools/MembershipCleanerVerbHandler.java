
public class MembershipCleanerVerbHandler implements IVerbHandler
{
    private static Logger logger_ = Logger.getLogger(MembershipCleanerVerbHandler.class);

    public void doVerb(Message message)
    {
        byte[] body = message.getMessageBody();
        
        try
        {
            DataInputBuffer bufIn = new DataInputBuffer();
            bufIn.reset(body, body.length);
            /* Deserialize to get the token for this endpoint. */
            MembershipCleaner.MembershipCleanerMessage mcMessage = MembershipCleaner.MembershipCleanerMessage.serializer().deserialize(bufIn);
            
            String target = mcMessage.getTarget();
            logger_.info("Removing the node [" + target + "] from membership");
            EndPoint targetEndPoint = new EndPoint(target, DatabaseDescriptor.getControlPort());
            /* Remove the token related information for this endpoint */
            StorageService.instance().removeTokenState(targetEndPoint);
            
            /* Get the headers for this message */
            Map<String, byte[]> headers = message.getHeaders();
            headers.remove( StorageService.getLocalStorageEndPoint().getHost() );
            logger_.debug("Number of nodes in the header " + headers.size());
            Set<String> nodes = headers.keySet();
            
            for ( String node : nodes )
            {            
                logger_.debug("Processing node " + node);
                byte[] bytes = headers.remove(node);
                /* Send a message to this node to alter its membership state. */
                EndPoint targetNode = new EndPoint(node, DatabaseDescriptor.getStoragePort());                
                
                logger_.debug("Sending a membership clean message to " + targetNode);
                MessagingService.getMessagingInstance().sendOneWay(message, targetNode);
                break;
            }                        
        }
        catch( IOException ex )
        {
            logger_.debug(LogUtil.throwableToString(ex));
        }
    }

}
