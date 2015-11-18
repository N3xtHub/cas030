
public class TokenUpdater
{
    private static final int port_ = 7000;
    private static final long waitTime_ = 10000;
    
    public static void main(String[] args) throws Throwable
    {
        if ( args.length != 3 )
        {
            System.out.println("Usage : java com.facebook.infrastructure.tools.TokenUpdater <ip:port> <token> <file containing node token info>");
            System.exit(1);
        }
        
        String ipPort = args[0];
        IPartitioner p = StorageService.getPartitioner();
        Token token = p.getTokenFactory().fromString(args[1]);
        String file = args[2];
        
        String[] ipPortPair = ipPort.split(":");
        EndPoint target = new EndPoint(ipPortPair[0], Integer.valueOf(ipPortPair[1]));

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);
        Token.serializer().serialize(token, dos);

        /* Construct the token update message to be sent */
        Message tokenUpdateMessage = new Message( new EndPoint(FBUtilities.getHostAddress(), port_), "", StorageService.tokenVerbHandler_, bos.toByteArray() );
        
        BufferedReader bufReader = new BufferedReader( new InputStreamReader( new FileInputStream(file) ) );
        String line = null;
       
        while ( ( line = bufReader.readLine() ) != null )
        {
            String[] nodeTokenPair = line.split(" ");
            /* Add the node and the token pair into the header of this message. */
            Token nodeToken = p.getTokenFactory().fromString(nodeTokenPair[1]);
            tokenUpdateMessage.addHeader(nodeTokenPair[0], p.getTokenFactory().toByteArray(nodeToken));
        }
        
        System.out.println("Sending a token update message to " + target);
        MessagingService.getMessagingInstance().sendOneWay(tokenUpdateMessage, target);
        Thread.sleep(TokenUpdater.waitTime_);
        System.out.println("Done sending the update message");
    }

}
