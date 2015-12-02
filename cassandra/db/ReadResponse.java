

/*
 * The read response message is sent by the server when reading data 
 * this encapsulates the tablename and teh row that has been read.
 * The table name is needed so that we can use it to create repairs.
 */
public class ReadResponse implements Serializable 
{
private static ICompactSerializer<ReadResponse> serializer_;

    static
    {
        serializer_ = new ReadResponseSerializer();
    }

    public static ICompactSerializer<ReadResponse> serializer()
    {
        return serializer_;
    }
    
	public static Message makeReadResponseMessage(ReadResponse readResponse) throws IOException
    {
    	ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream( bos );
        ReadResponse.serializer().serialize(readResponse, dos);
        Message message = new Message(StorageService.getLocalStorageEndPoint(), MessagingService.responseStage_, MessagingService.responseVerbHandler_, bos.toByteArray());         
        return message;
    }
	
	private String table_;
	private Row row_;
	private byte[] digest_ = ArrayUtils.EMPTY_BYTE_ARRAY;
    private boolean isDigestQuery_ = false;

	public ReadResponse(String table, byte[] digest )
    {
        assert digest != null;
		table_ = table;
		digest_= digest;
	}

	public ReadResponse(String table, Row row)
    {
		table_ = table;
		row_ = row;
	}

	public String table() 
    {
		return table_;
	}

	public Row row() 
    {
		return row_;
    }
        
	public byte[] digest() 
    {
		return digest_;
	}

	public boolean isDigestQuery()
    {
    	return isDigestQuery_;
    }
    
    public void setIsDigestQuery(boolean isDigestQuery)
    {
    	isDigestQuery_ = isDigestQuery;
    }
}

class ReadResponseSerializer implements ICompactSerializer<ReadResponse>
{
	public void serialize(ReadResponse rm, DataOutputStream dos) throws IOException
	{
		dos.writeUTF(rm.table());
        dos.writeInt(rm.digest().length);
        dos.write(rm.digest());
        dos.writeBoolean(rm.isDigestQuery());
        
        if( !rm.isDigestQuery() && rm.row() != null )
        {            
            Row.serializer().serialize(rm.row(), dos);
        }				
	}
	
    public ReadResponse deserialize(DataInputStream dis) throws IOException
    {
    	String table = dis.readUTF();
        int digestSize = dis.readInt();
        byte[] digest = new byte[digestSize];
        dis.read(digest, 0 , digestSize);
        boolean isDigest = dis.readBoolean();
        
        Row row = null;
        if ( !isDigest )
        {
            row = Row.serializer().deserialize(dis);
        }
		
		ReadResponse rmsg = null;
    	if( isDigest  )
        {
    		rmsg =  new ReadResponse(table, digest);
        }
    	else
        {
    		rmsg =  new ReadResponse(table, row);
        }
        rmsg.setIsDigestQuery(isDigest);
    	return rmsg;
    } 
}