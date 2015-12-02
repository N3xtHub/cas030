class ContentState extends StartState
{
    private ByteBuffer buffer_;   
    private int length_;

    ContentState(TcpReader stream, int length)
    {
        super(stream);
        length_ = length; 
        buffer_ = ByteBuffer.allocate(length_);
    }

    public byte[] read() throws IOException, ReadNotCompleteException
    {          
        return doRead(buffer_);
    }

    public void morphState() throws IOException
    {        
        StartState nextState = stream_.getSocketState(TcpReader.TcpReaderState.DONE);
        if ( nextState == null )
        {
            nextState = new DoneState(stream_, toBytes());
            stream_.putSocketState( TcpReader.TcpReaderState.DONE, nextState );
        }
        else
        {            
            nextState.setContextData(toBytes());
        }
        stream_.morphState( nextState );               
    }
    
    private byte[] toBytes()
    {
        buffer_.position(0); 
        /*
        ByteBuffer slice = buffer_.slice();        
        return slice.array();
        */  
        byte[] bytes = new byte[length_];
        buffer_.get(bytes, 0, length_);
        return bytes;
    }
    
    public void setContextData(Object data)
    {
        Integer value = (Integer)data;
        length_ = value;               
        buffer_.clear();
        if ( buffer_.capacity() < length_ )
            buffer_ = ByteBuffer.allocate(length_);
        else
        {            
            buffer_.limit(length_);
        }        
    }
}
