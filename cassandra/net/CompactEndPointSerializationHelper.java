
public class CompactEndPointSerializationHelper
{
    public static void serialize(EndPoint endPoint, DataOutputStream dos) throws IOException
    {        
        dos.write(EndPoint.toBytes(endPoint));
    }
    
    public static EndPoint deserialize(DataInputStream dis) throws IOException
    {     
        byte[] bytes = new byte[6];
        dis.readFully(bytes, 0, bytes.length);
        return EndPoint.fromBytes(bytes);       
    }
    
    public static void main(String[] args) throws Throwable
    {
        EndPoint ep = new EndPoint(7000);
        byte[] bytes = EndPoint.toBytes(ep);
        System.out.println(bytes.length);
        EndPoint ep2 = EndPoint.fromBytes(bytes);
        System.out.println(ep2);
    }
}