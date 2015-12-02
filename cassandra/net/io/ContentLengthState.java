
class ContentLengthState extends StartState
{
    private ByteBuffer buffer_;

    ContentLengthState(TcpReader stream)
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
        int size = FBUtilities.byteArrayToInt(buffer_.array());        
        StartState nextState = stream_.getSocketState(TcpReader.TcpReaderState.CONTENT);
        if ( nextState == null )
        {
            nextState = new ContentState(stream_, size);
            stream_.putSocketState( TcpReader.TcpReaderState.CONTENT, nextState );
        }
        else
        {               
            nextState.setContextData(size);
        }
        stream_.morphState( nextState );
        buffer_.clear();
    }
    
    public void setContextData(Object data)
    {
        throw new UnsupportedOperationException("This method is not supported in the ContentLengthState");
    }
}
