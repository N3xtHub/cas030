public class FastSerializer implements ISerializer
{ 
    public byte[] serialize(Message message) throws IOException
    {
        DataOutputBuffer buffer = new DataOutputBuffer();
        Message.serializer().serialize(message, buffer);
        return buffer.getData();
    }
    
    public Message deserialize(byte[] bytes) throws IOException
    {
        DataInputBuffer bufIn = new DataInputBuffer();
        bufIn.reset(bytes, bytes.length);
        return Message.serializer().deserialize(bufIn);
    }
}
