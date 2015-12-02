
class MessageDeserializationTask implements Runnable
{
    private static Logger logger_ = Logger.getLogger(MessageDeserializationTask.class); 
    private static ISerializer serializer_ = new FastSerializer();
    private int serializerType_;
    private byte[] bytes_ = new byte[0];    
    
    MessageDeserializationTask(int serializerType, byte[] bytes)
    {
        serializerType_ = serializerType;
        bytes_ = bytes;        
    }
    
    public void run()
    {
        Message message = null;
        try
        {
            message = serializer_.deserialize(bytes_);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }

        if ( message != null )
        {
            message = SinkManager.processServerMessageSink(message);
            MessagingService.receive(message);
        }
    }

}
