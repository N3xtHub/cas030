
public interface ISerializer
{
    public byte[] serialize(Message message) throws IOException;
    public Message deserialize(byte[] bytes) throws IOException;
}
