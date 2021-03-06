

public class CalloutDeployMessage
{
    private static ICompactSerializer<CalloutDeployMessage> serializer_;
    
    static
    {
        serializer_ = new CalloutDeployMessageSerializer();
    }
    
    public static ICompactSerializer<CalloutDeployMessage> serializer()
    {
        return serializer_;
    }
    
    public static Message getCalloutDeployMessage(CalloutDeployMessage cdMessage) throws IOException
    {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);
        serializer_.serialize(cdMessage, dos);
        Message message = new Message(StorageService.getLocalStorageEndPoint(), "", StorageService.calloutDeployVerbHandler_, bos.toByteArray());
        return message;
    }
    
    /* Name of the callout */
    private String callout_;
    /* The actual procedure */
    private String script_;
    
    public CalloutDeployMessage(String callout, String script)
    {
        callout_ = callout;
        script_ = script;
    }
    
    String getCallout()
    {
        return callout_;
    }
    
    String getScript()
    {
        return script_;
    }
}


