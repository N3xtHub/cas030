
public final class ProtocolHeader
{
    public static final String SERIALIZER = "SERIALIZER";
    public static final String COMPRESSION = "COMPRESSION";
    public static final String VERSION = "VERSION";
    
    public int serializerType_;
    public boolean isCompressed_;
    public boolean isStreamingMode_;
    public boolean isListening_;
    public int version_;
}
