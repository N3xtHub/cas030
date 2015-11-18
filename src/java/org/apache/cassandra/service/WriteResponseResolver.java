
public class WriteResponseResolver implements IResponseResolver<Boolean> {

	private static Logger logger_ = Logger.getLogger(WriteResponseResolver.class);

	/*
	 * The resolve function for the Write looks at all the responses if all the
	 * respones returned are false then we have a problem since that means the
	 * key wa not written to any of the servers we want to notify the client of
	 * this so in that case we should return a false saying that the write
	 * failed.
	 * 
	 */
	public Boolean resolve(List<Message> responses) throws DigestMismatchException 
	{
		// TODO: We need to log error responses here for example
		// if a write fails for a key log that the key could not be replicated
		boolean returnValue = false;
		for (Message response : responses) {
            WriteResponse writeResponseMessage = WriteResponse.serializer().deserialize(new DataInputStream(new ByteArrayInputStream(response.getMessageBody())));
 
            boolean result = writeResponseMessage.isSuccess();
            
            returnValue |= result;
		}
        
		return returnValue;
	}

	public boolean isDataPresent(List<Message> responses)
	{
		return true;
	}
	
}
