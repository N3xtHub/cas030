
public class KeyChecker
{
    private static final int bufSize_ = 128*1024*1024;
    /*
     * This function checks if the local storage endpoint 
     * is reponsible for storing this key .
     */
    private static boolean checkIfProcessKey(String key)
    {
        EndPoint[] endPoints = StorageService.instance().getNStorageEndPoint(key);
        EndPoint localEndPoint = StorageService.getLocalStorageEndPoint();
        for(EndPoint endPoint : endPoints)
        {
            if(endPoint.equals(localEndPoint))
                return true;
        }
        return false;
    }
    
    public static void main(String[] args) throws Throwable
    {
        if ( args.length != 1 )
        {
            System.out.println("Usage : java com.facebook.infrastructure.tools.KeyChecker <file containing all keys>");
            System.exit(1);
        }
        
        LogUtil.init();
        StorageService s = StorageService.instance();
        s.start();
        
        /* Sleep for proper discovery */
        Thread.sleep(240000);
        /* Create the file for the missing keys */
        RandomAccessFile raf = new RandomAccessFile( "Missing-" + FBUtilities.getHostAddress() + ".dat", "rw");
        
        /* Start reading the file that contains the keys */
        BufferedReader bufReader = new BufferedReader( new InputStreamReader( new FileInputStream(args[0]) ), KeyChecker.bufSize_ );
        String key = null;
        boolean bStarted = false;
        
        while ( ( key = bufReader.readLine() ) != null )
        {            
            if ( !bStarted )
            {
                bStarted = true;
                System.out.println("Started the processing of the file ...");
            }
            
            key = key.trim();
            if ( StorageService.instance().isPrimary(key) )
            {
                System.out.println("Processing key " + key);
                Row row = Table.open("Mailbox").getRow(key, "MailboxMailList0");
                if ( row.isEmpty() )
                {
                    System.out.println("MISSING KEY : " + key);
                    raf.write(key.getBytes());
                    raf.write(System.getProperty("line.separator").getBytes());
                }
            }
        }
        System.out.println("DONE checking keys ...");
        raf.close();
    }
}
