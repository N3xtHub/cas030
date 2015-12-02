
public class RowMutationMessage implements Serializable
{   
    public static final String hint_ = "HINT";
    private static RowMutationMessageSerializer serializer_ = new RowMutationMessageSerializer();
	
    static RowMutationMessageSerializer serializer()
    {
        return serializer_;
    }

    public Message makeRowMutationMessage() throws IOException
    {         
        return makeRowMutationMessage(StorageService.mutationVerbHandler_);
    }
    
    public Message makeRowMutationMessage(String verbHandlerName) throws IOException
    {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream( bos );
        RowMutationMessage.serializer().serialize(this, dos);
        EndPoint local = StorageService.getLocalStorageEndPoint();
        EndPoint from = ( local != null ) ? local : new EndPoint(FBUtilities.getHostAddress(), 7000);
        return new Message(from, StorageService.mutationStage_, verbHandlerName, bos.toByteArray());         
    }
    
    @XmlElement(name="RowMutation")
    private RowMutation rowMutation_;
    
    private RowMutationMessage()
    {}
    
    public RowMutationMessage(RowMutation rowMutation)
    {
        rowMutation_ = rowMutation;
    }
    
   public RowMutation getRowMutation()
   {
       return rowMutation_;
   }
}

class RowMutationMessageSerializer implements ICompactSerializer<RowMutationMessage>
{
	public void serialize(RowMutationMessage rm, DataOutputStream dos) throws IOException
	{
		RowMutation.serializer().serialize(rm.getRowMutation(), dos);
	}
	
    public RowMutationMessage deserialize(DataInputStream dis) throws IOException
    {
    	RowMutation rm = RowMutation.serializer().deserialize(dis);
    	return new RowMutationMessage(rm);
    }
}