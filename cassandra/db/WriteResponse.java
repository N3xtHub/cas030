

/*
 * This message is sent back the row mutation verb handler 
 * and basically specifes if the write succeeded or not for a particular 
 * key in a table
 */
public class WriteResponse 
{
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

	public boolean isSuccess()
	{
		return status_;
	}

}
