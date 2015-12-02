
public class ProtocolState extends StartState
{
    private ByteBuffer buffer_;

    public ProtocolState(TcpReader stream)
    {
        super(stream);
        buffer_ = ByteBuffer.allocate(16);
    }

    public byte[] read() throws IOException, ReadNotCompleteException
    {        
        return doRead(buffer_);
    }

    public void morphState() throws IOException
    {
        byte[] protocol = buffer_.array();
        if ( MessagingService.isProtocolValid(protocol) )
        {            
            StartState nextState = stream_.getSocketState(TcpReader.TcpReaderState.PROTOCOL);
            if ( nextState == null )
            {
                nextState = new ProtocolHeaderState(stream_);
                stream_.putSocketState( TcpReader.TcpReaderState.PROTOCOL, nextState );
            }
            stream_.morphState( nextState ); 
            buffer_.clear();
        }
        else
        {
            throw new IOException("Invalid protocol header. The preamble seems to be messed up.");
        }
    }
    
    public void setContextData(Object data)
    {
        throw new UnsupportedOperationException("This method is not supported in the ProtocolState");
    }
}

