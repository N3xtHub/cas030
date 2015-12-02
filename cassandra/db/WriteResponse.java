

/*
 * This message is sent back the row mutation verb handler 
 * and basically specifes if the write succeeded or not for a particular 
 * key in a table
 */
public class WriteResponse 
{
    private static WriteResponseSerializer serializer_ = new WriteResponseSerializer();

    public static WriteResponseSerializer serializer()
    {
        return serializer_;
    }

    public static Message makeWriteResponseMessage(Message original, WriteResponse writeResponseMessage) throws IOException
    {
    	ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream( bos );
        WriteResponse.serializer().serialize(writeResponseMessage, dos);
        return original.getReply(StorageService.getLocalStorageEndPoint(), bos.toByteArray());
    }

	private final String table_;
	private final String key_;
	private final boolean status_;

	public WriteResponse(String table, String key, boolean bVal) {
		table_ = table;
		key_ = key;
		status_ = bVal;
	}

	public String table()
	{
		return table_;
	}

	public String key()
	{
		return key_;
	}

	public boolean isSuccess()
	{
		return status_;
	}

    public static class WriteResponseSerializer implements ICompactSerializer<WriteResponse>
    {
        public void serialize(WriteResponse wm, DataOutputStream dos) throws IOException
        {
            dos.writeUTF(wm.table());
            dos.writeUTF(wm.key());
            dos.writeBoolean(wm.isSuccess());
        }

        public WriteResponse deserialize(DataInputStream dis) throws IOException
        {
            String table = dis.readUTF();
            String key = dis.readUTF();
            boolean status = dis.readBoolean();
            return new WriteResponse(table, key, status);
        }
    }
}
