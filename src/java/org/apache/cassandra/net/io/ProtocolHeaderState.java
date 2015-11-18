
public class ProtocolHeaderState extends StartState
{
    private ByteBuffer buffer_;

    public ProtocolHeaderState(TcpReader stream)
    {
        super(stream);
        buffer_ = ByteBuffer.allocate(4);
    }

    public byte[] read() throws IOException, ReadNotCompleteException
    {        
        return doRead(buffer_);
    }

    public void morphState() throws IOException
    {
        byte[] protocolHeader = buffer_.array();
        int pH = MessagingService.byteArrayToInt(protocolHeader);
        
        int type = MessagingService.getBits(pH, 1, 2);
        stream_.getProtocolHeader().serializerType_ = type;
        
        int stream = MessagingService.getBits(pH, 3, 1);
        stream_.getProtocolHeader().isStreamingMode_ = (stream == 1) ? true : false;
        
        if ( stream_.getProtocolHeader().isStreamingMode_ )
            MessagingService.setStreamingMode(true);
        
        int listening = MessagingService.getBits(pH, 4, 1);
        stream_.getProtocolHeader().isListening_ = (listening == 1) ? true : false;
        
        int version = MessagingService.getBits(pH, 15, 8);
        stream_.getProtocolHeader().version_ = version;
        
        if ( version <= MessagingService.getVersion() )
        {
            if ( stream_.getProtocolHeader().isStreamingMode_ )
            { 
                StartState nextState = stream_.getSocketState(TcpReader.TcpReaderState.CONTENT_STREAM);
                if ( nextState == null )
                {
                    nextState = new ContentStreamState(stream_);
                    stream_.putSocketState( TcpReader.TcpReaderState.CONTENT_STREAM, nextState );
                }
                stream_.morphState( nextState );
                buffer_.clear();
            }
            else
            {
                StartState nextState = stream_.getSocketState(TcpReader.TcpReaderState.CONTENT_LENGTH);
                if ( nextState == null )
                {
                    nextState = new ContentLengthState(stream_);
                    stream_.putSocketState( TcpReader.TcpReaderState.CONTENT_LENGTH, nextState );
                }                
                stream_.morphState( nextState );   
                buffer_.clear();
            }            
        }
        else
        {
            throw new IOException("Invalid version in message. Scram.");
        }
    }
    
    public void setContextData(Object data)
    {
        throw new UnsupportedOperationException("This method is not supported in the ProtocolHeaderState");
    }
}


