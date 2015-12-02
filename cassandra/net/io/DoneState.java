
class DoneState extends StartState
{
    private byte[] bytes_ = new byte[0];

    DoneState(TcpReader stream, byte[] bytes)
    {
        super(stream);
        bytes_ = bytes;
    }

    public byte[] read() throws IOException, ReadNotCompleteException
    {        
        morphState();
        return bytes_;
    }

    public void morphState() throws IOException
    {                       
        stream_.morphState(null);
    }
    
    public void setContextData(Object data)
    {                
        bytes_ = (byte[])data;
    }
}
