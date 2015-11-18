
/*
 * This class manages the streaming of multiple files 
 * one after the other. 
*/
public final class StreamManager
{   
    private static Logger logger_ = Logger.getLogger( StreamManager.class );
    
    public static class BootstrapTerminateVerbHandler implements IVerbHandler
    {
        private static Logger logger_ = Logger.getLogger( BootstrapInitiateDoneVerbHandler.class );

        public void doVerb(Message message)
        {
            byte[] body = message.getMessageBody();
            DataInputBuffer bufIn = new DataInputBuffer();
            bufIn.reset(body, body.length);

            try
            {
                StreamContextManager.StreamStatusMessage streamStatusMessage = StreamContextManager.StreamStatusMessage.serializer().deserialize(bufIn);
                StreamContextManager.StreamStatus streamStatus = streamStatusMessage.getStreamStatus();
                                               
                switch( streamStatus.getAction() )
                {
                    case DELETE:                              
                        StreamManager.instance(message.getFrom()).finish(streamStatus.getFile());
                        break;

                    case STREAM:
                        logger_.debug("Need to re-stream file " + streamStatus.getFile());
                        StreamManager.instance(message.getFrom()).repeat();
                        break;

                    default:
                        break;
                }
            }
        }
    }
    
    private static Map<EndPoint, StreamManager> streamManagers_ = new HashMap<EndPoint, StreamManager>();
    
    public static StreamManager instance(EndPoint to)
    {
        StreamManager streamManager = streamManagers_.get(to);
        if ( streamManager == null )
        {
            streamManager = new StreamManager(to);
            streamManagers_.put(to, streamManager);
        }
        return streamManager;
    }
    
    private List<File> filesToStream_ = new ArrayList<File>();
    private EndPoint to_;
    private long totalBytesToStream_ = 0L;
    
    private StreamManager(EndPoint to)
    {
        to_ = to;
    }
    
    public void addFilesToStream(StreamContextManager.StreamContext[] streamContexts)
    {
        for ( StreamContextManager.StreamContext streamContext : streamContexts )
        {
            logger_.debug("Adding file " + streamContext.getTargetFile() + " to be streamed.");
            filesToStream_.add( new File( streamContext.getTargetFile() ) );
            totalBytesToStream_ += streamContext.getExpectedBytes();
        }
    }
    
    void start()
    {
        if ( filesToStream_.size() > 0 )
        {
            File file = filesToStream_.get(0);
            logger_.debug("Streaming file " + file + " ...");
            MessagingService.getMessagingInstance().stream(file.getAbsolutePath(), 0L, file.length(), StorageService.getLocalStorageEndPoint(), to_);
        }
    }
    
    void repeat()
    {
        if ( filesToStream_.size() > 0 )
            start();
    }
    
    void finish(String file) throws IOException
    {
        File f = new File(file);
        logger_.debug("Deleting file " + file + " after streaming " + f.length() + "/" + totalBytesToStream_ + " bytes.");
        FileUtils.delete(file);
        filesToStream_.remove(0);
        if ( filesToStream_.size() > 0 )
            start();
        else
        {
            synchronized(this)
            {
                logger_.debug("Signalling that streaming is done for " + to_);
                notifyAll();
            }
        }
    }
    
    public synchronized void waitForStreamCompletion()
    {
            wait();
    }
}
