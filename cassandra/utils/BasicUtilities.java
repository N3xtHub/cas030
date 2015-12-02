

public class BasicUtilities
{        
	public static byte[] longToByteArray(long arg)
	{      
        byte[] retVal = new byte[8];
        ByteBuffer bb= ByteBuffer.wrap(retVal);
        bb.putLong(arg);
        return retVal; 
	 }
	
	public static long byteArrayToLong(byte[] arg)
	{
		ByteBuffer bb= ByteBuffer.wrap(arg);
		return bb.getLong();
	}
	
	public static byte[] intToByteArray(int arg)
	{      
        byte[] retVal = new byte[4];
        ByteBuffer bb= ByteBuffer.wrap(retVal);
        bb.putInt(arg);
        return retVal; 
	 }
	
	public static int byteArrayToInt(byte[] arg)
	{
		ByteBuffer bb= ByteBuffer.wrap(arg);
		return bb.getInt();
	}
	
	public static byte[] shortToByteArray(short arg)
	{      
        byte[] retVal = new byte[2];
        ByteBuffer bb= ByteBuffer.wrap(retVal);
        bb.putShort(arg);
        return retVal; 
	 }
	
	public static short byteArrayToShort(byte[] arg)
	{
		ByteBuffer bb= ByteBuffer.wrap(arg);
		return bb.getShort();
    }
}
