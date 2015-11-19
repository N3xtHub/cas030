
public class BinaryVerbHandler implements IVerbHandler
{
    private static Logger logger_ = Logger.getLogger(BinaryVerbHandler.class);    
    /* We use this so that we can reuse the same row mutation context for the mutation. */
    private static ThreadLocal<RowMutationContext> tls_ = new InheritableThreadLocal<RowMutationContext>();
    
    public void doVerb(Message message)
    { 
        byte[] bytes = message.getMessageBody();
        /* Obtain a Row Mutation Context from TLS */
        RowMutationContext rowMutationCtx = tls_.get();
        if ( rowMutationCtx == null )
        {
            rowMutationCtx = new RowMutationContext();
            tls_.set(rowMutationCtx);
        }                
        rowMutationCtx.buffer_.reset(bytes, bytes.length);
        
	    try
	    {
            RowMutationMessage rmMsg = RowMutationMessage.serializer().deserialize(rowMutationCtx.buffer_);
            RowMutation rm = rmMsg.getRowMutation();            	                
            rowMutationCtx.row_.key(rm.key());
            rm.load(rowMutationCtx.row_);
	
	    }        
	    catch ( Exception e )
	    {
	        logger_.debug(LogUtil.throwableToString(e));            
	    }        
    }

}
