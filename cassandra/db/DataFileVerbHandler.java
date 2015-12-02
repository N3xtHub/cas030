

public class DataFileVerbHandler implements IVerbHandler
{
    private static Logger logger_ = Logger.getLogger( DataFileVerbHandler.class );
    
    public void doVerb(Message message)
    {        
        byte[] bytes = message.getMessageBody();
        String table = new String(bytes);
        logger_.info("**** Received a request from " + message.getFrom());
        
        List<String> allFiles = Table.open(table).getAllSSTablesOnDisk();        
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);
        try
        {
            dos.writeInt(allFiles.size());
            for ( String file : allFiles )
            {
                dos.writeUTF(file);
            }
            Message response = message.getReply( StorageService.getLocalStorageEndPoint(), bos.toByteArray());
            MessagingService.getMessagingInstance().sendOneWay(response, message.getFrom());
        }
        catch ( IOException ex )
        {
            logger_.warn(LogUtil.throwableToString(ex));
        }
    }
}
