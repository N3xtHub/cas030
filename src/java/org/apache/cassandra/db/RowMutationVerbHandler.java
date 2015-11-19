
public class RowMutationVerbHandler implements IVerbHandler
{
    protected static class RowMutationContext
    {
        protected Row row_ = new Row();
        protected DataInputBuffer buffer_ = new DataInputBuffer();
    }

    private static Logger logger_ = Logger.getLogger(RowMutationVerbHandler.class);
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
            RowMutation rm = RowMutation.serializer().deserialize(rowMutationCtx.buffer_);
            logger_.debug("Applying " + rm);

            /* Check if there were any hints in this message */
            byte[] hintedBytes = message.getHeader(RowMutation.HINT);
            if ( hintedBytes != null && hintedBytes.length > 0 )
            {
            	EndPoint hint = EndPoint.fromBytes(hintedBytes);
                logger_.debug("Adding hint for " + hint);
                /* add necessary hints to this mutation */
                RowMutation hintedMutation = new RowMutation(rm.table(), HintedHandOffManager.key_);
                hintedMutation.addHints(rm.key() + ":" + hint.getHost());
                hintedMutation.apply();
            }

            rowMutationCtx.row_.clear();
            rowMutationCtx.row_.key(rm.key());
            rm.apply(rowMutationCtx.row_);

            WriteResponse response = new WriteResponse(rm.table(), rm.key(), true);
            Message responseMessage = WriteResponse.makeWriteResponseMessage(message, response);
            logger_.debug(rm + " applied.  Sending response to " + message.getMessageId() + "@" + message.getFrom());
            MessagingService.getMessagingInstance().sendOneWay(responseMessage, message.getFrom());
        }
        catch (IOException e)
        {
            logger_.error("Error in row mutation", e);
        }
    }
}
