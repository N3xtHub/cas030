
public class LoadVerbHandler implements IVerbHandler
{
    private static Logger logger_ = Logger.getLogger(LoadVerbHandler.class);    
    
    public void doVerb(Message message)
    { 
        try
        {
	        byte[] body = message.getMessageBody();
            DataInputBuffer buffer = new DataInputBuffer();
            buffer.reset(body, body.length);
	        RowMutationMessage rmMsg = RowMutationMessage.serializer().deserialize(buffer);

            EndPoint[] endpoints = StorageService.instance().getNStorageEndPoint(rmMsg.getRowMutation().key());

			Message messageInternal = new Message(StorageService.getLocalStorageEndPoint(), 
	                StorageService.mutationStage_,
					StorageService.mutationVerbHandler_, 
	                body
	        );
            
            StringBuilder sb = new StringBuilder();
			for(EndPoint endPoint : endpoints)
			{                
                sb.append(endPoint);
				MessagingService.getMessagingInstance().sendOneWay(messageInternal, endPoint);
			}
            logger_.debug("Sent data to " + sb.toString());            
        }        
        catch ( Exception e )
        {
            logger_.debug(LogUtil.throwableToString(e));            
        }        
    }

}
