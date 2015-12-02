
public abstract class StartState
{
    protected TcpReader stream_;

    public StartState(TcpReader stream)
    {
        stream_ = stream;
    }

    public abstract byte[] read() throws IOException, ReadNotCompleteException;
    public abstract void morphState() throws IOException;
    public abstract void setContextData(Object data);

    protected byte[] doRead(ByteBuffer buffer) throws IOException, ReadNotCompleteException
    {        
        SocketChannel socketChannel = stream_.getStream();
        int bytesRead = socketChannel.read(buffer);     
        if ( bytesRead == -1 && buffer.remaining() > 0 )
        {            
            throw new IOException("Reached an EOL or something bizzare occured. Reading from: " + socketChannel.socket().getInetAddress() + " BufferSizeRemaining: " + buffer.remaining());
        }
        if ( buffer.remaining() == 0 )
        {
            morphState();
        }
        else
        {            
            throw new ReadNotCompleteException("Specified number of bytes have not been read from the Socket Channel");
        }
        return new byte[0];
    }
}
