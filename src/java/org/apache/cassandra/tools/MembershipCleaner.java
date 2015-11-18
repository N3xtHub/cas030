
public class MembershipCleaner
{
    private static final int port_ = 7000;
    private static final long waitTime_ = 10000;
    
    public static void main(String[] args) throws Throwable
    {
        if ( args.length != 3 )
        {
            System.out.println("Usage : java com.facebook.infrastructure.tools.MembershipCleaner " +
                    "<ip:port to send the message> " +
                    "<node which needs to be removed> " +
                    "<file containing all nodes in the cluster>");
            System.exit(1);
        }
        
        String ipPort = args[0];
        String node = args[1];
        String file = args[2];
        
        String[] ipPortPair = ipPort.split(":");
        EndPoint target = new EndPoint(ipPortPair[0], Integer.valueOf(ipPortPair[1]));
        MembershipCleanerMessage mcMessage = new MembershipCleanerMessage(node);
        
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);
        MembershipCleanerMessage.serializer().serialize(mcMessage, dos);
        /* Construct the token update message to be sent */
        Message mbrshipCleanerMessage = new Message( new EndPoint(FBUtilities.getHostAddress(), port_), "", StorageService.mbrshipCleanerVerbHandler_, bos.toByteArray() );
        
        BufferedReader bufReader = new BufferedReader( new InputStreamReader( new FileInputStream(file) ) );
        String line = null;
       
        while ( ( line = bufReader.readLine() ) != null )
        {            
            mbrshipCleanerMessage.addHeader(line, line.getBytes());
        }
        
        System.out.println("Sending a membership clean message to " + target);
        MessagingService.getMessagingInstance().sendOneWay(mbrshipCleanerMessage, target);
        Thread.sleep(MembershipCleaner.waitTime_);
        System.out.println("Done sending the update message");
    }
    
    public static class MembershipCleanerMessage implements Serializable
    {
        private static ICompactSerializer<MembershipCleanerMessage> serializer_;
        private static AtomicInteger idGen_ = new AtomicInteger(0);
        
        static
        {
            serializer_ = new MembershipCleanerMessageSerializer();            
        }
        
        static ICompactSerializer<MembershipCleanerMessage> serializer()
        {
            return serializer_;
        }

        private String target_;
        
        MembershipCleanerMessage(String target)
        {
            target_ = target;        
        }
        
        String getTarget()
        {
            return target_;
        }
    }
    
    public static class MembershipCleanerMessageSerializer implements ICompactSerializer<MembershipCleanerMessage>
    {
        public void serialize(MembershipCleanerMessage mcMessage, DataOutputStream dos) throws IOException
        {            
            dos.writeUTF(mcMessage.getTarget() );                      
        }
        
        public MembershipCleanerMessage deserialize(DataInputStream dis) throws IOException
        {            
            return new MembershipCleanerMessage(dis.readUTF());
        }
    }
}
