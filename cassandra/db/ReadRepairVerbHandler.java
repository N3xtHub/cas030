
public class ReadRepairVerbHandler implements IVerbHandler
{
    private static Logger logger_ = Logger.getLogger(ReadRepairVerbHandler.class);    
    
    public void doVerb(Message message)
    {          
        byte[] body = message.getMessageBody();
        DataInputBuffer buffer = new DataInputBuffer();
        buffer.reset(body, body.length);        
        
        try
        {
            RowMutationMessage rmMsg = RowMutationMessage.serializer().deserialize(buffer);
            RowMutation rm = rmMsg.getRowMutation();
            rm.apply();                                   
        }    
    }
}
