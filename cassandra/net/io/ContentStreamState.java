
class ContentStreamState extends StartState
{       
    private static Logger logger_ = Logger.getLogger(ContentStreamState.class);
    private static long count_ = 64*1024*1024;
    /* Return this byte array to exit event loop */
    private static byte[] bytes_ = new byte[1];
    private long bytesRead_ = 0L;
    private FileChannel fc_;
    private StreamContextManager.StreamContext streamContext_;
    private StreamContextManager.StreamStatus streamStatus_;
    
    ContentStreamState(TcpReader stream)
    {
        super(stream); 
        SocketChannel socketChannel = stream.getStream();
        InetSocketAddress remoteAddress = (InetSocketAddress)socketChannel.socket().getRemoteSocketAddress();
        String remoteHost = remoteAddress.getHostName();        
        streamContext_ = StreamContextManager.getStreamContext(remoteHost);   
        streamStatus_ = StreamContextManager.getStreamStatus(remoteHost);
    }
    
    private void createFileChannel() throws IOException
    {
        if ( fc_ == null )
        {
            logger_.debug("Creating file for " + streamContext_.getTargetFile());
            FileOutputStream fos = new FileOutputStream( streamContext_.getTargetFile(), true );
            fc_ = fos.getChannel();            
        }
    }

    public byte[] read() throws IOException, ReadNotCompleteException
    {        
        SocketChannel socketChannel = stream_.getStream();
        InetSocketAddress remoteAddress = (InetSocketAddress)socketChannel.socket().getRemoteSocketAddress();
        String remoteHost = remoteAddress.getHostName();  
        createFileChannel();
        if ( streamContext_ != null )
        {  
            try
            {
                bytesRead_ += fc_.transferFrom(socketChannel, bytesRead_, ContentStreamState.count_);
                if ( bytesRead_ != streamContext_.getExpectedBytes() )
                    throw new ReadNotCompleteException("Specified number of bytes have not been read from the Socket Channel");
            }
            catch ( IOException ex )
            {
                /* Ask the source node to re-stream this file. */
                streamStatus_.setAction(StreamContextManager.StreamCompletionAction.STREAM);                
                handleStreamCompletion(remoteHost);
                /* Delete the orphaned file. */
                File file = new File(streamContext_.getTargetFile());
                file.delete();
                throw ex;
            }
            if ( bytesRead_ == streamContext_.getExpectedBytes() )
            {       
                logger_.debug("Removing stream context " + streamContext_);                 
                handleStreamCompletion(remoteHost);                              
                bytesRead_ = 0L;
                fc_.close();
                morphState();
            }                            
        }
        
        return new byte[0];
    }
    
    private void handleStreamCompletion(String remoteHost) throws IOException
    {
        /* 
         * Streaming is complete. If all the data that has to be received inform the sender via 
         * the stream completion callback so that the source may perform the requisite cleanup. 
        */
        IStreamComplete streamComplete = StreamContextManager.getStreamCompletionHandler(remoteHost);
        if ( streamComplete != null )
        {                    
            streamComplete.onStreamCompletion(remoteHost, streamContext_, streamStatus_);                    
        }
    }

    public void morphState() throws IOException
    {        
        /* We instantiate an array of size 1 so that we can exit the event loop of the read. */                
        StartState nextState = stream_.getSocketState(TcpReader.TcpReaderState.DONE);
        if ( nextState == null )
        {
            nextState = new DoneState(stream_, ContentStreamState.bytes_);
            stream_.putSocketState( TcpReader.TcpReaderState.DONE, nextState );
        }
        else
        {
            nextState.setContextData(ContentStreamState.bytes_);
        }
        stream_.morphState( nextState );  
    }
    
    public void setContextData(Object data)
    {
        throw new UnsupportedOperationException("This method is not supported in the ContentStreamState");
    }
}