
public class ReadVerbHandler implements IVerbHandler
{
    protected static class ReadContext
    {
        protected DataInputBuffer bufIn_ = new DataInputBuffer();
        protected DataOutputBuffer bufOut_ = new DataOutputBuffer();
    }

    private static Logger logger_ = Logger.getLogger( ReadVerbHandler.class );
    /* We use this so that we can reuse the same row mutation context for the mutation. */
    private static ThreadLocal<ReadVerbHandler.ReadContext> tls_ = new InheritableThreadLocal<ReadVerbHandler.ReadContext>();
    
    protected static ReadVerbHandler.ReadContext getCurrentReadContext()
    {
        return tls_.get();
    }
    
    protected static void setCurrentReadContext(ReadVerbHandler.ReadContext readContext)
    {
        tls_.set(readContext);
    }

    public void doVerb(Message message)
    {
        byte[] body = message.getMessageBody();
        /* Obtain a Read Context from TLS */
        ReadContext readCtx = tls_.get();
        if ( readCtx == null )
        {
            readCtx = new ReadContext();
            tls_.set(readCtx);
        }
        readCtx.bufIn_.reset(body, body.length);

        try
        {
            ReadCommand readCommand = ReadCommand.serializer().deserialize(readCtx.bufIn_);
            Table table = Table.open(readCommand.table);
            Row row = null;
            row = readCommand.getRow(table);
            ReadResponse readResponse = null;
            if (readCommand.isDigestQuery())
            {
                readResponse = new ReadResponse(table.getTableName(), row.digest());
            }
            else
            {
                readResponse = new ReadResponse(table.getTableName(), row);
            }
            readResponse.setIsDigestQuery(readCommand.isDigestQuery());
            /* serialize the ReadResponseMessage. */
            readCtx.bufOut_.reset();

            ReadResponse.serializer().serialize(readResponse, readCtx.bufOut_);

            byte[] bytes = new byte[readCtx.bufOut_.getLength()];
            System.arraycopy(readCtx.bufOut_.getData(), 0, bytes, 0, bytes.length);

            Message response = message.getReply(StorageService.getLocalStorageEndPoint(), bytes);
            logger_.debug("Read key " + readCommand.key + "; sending response to " + message.getMessageId() + "@" + message.getFrom());
            MessagingService.getMessagingInstance().sendOneWay(response, message.getFrom());

            /* Do read repair if header of the message says so */
            if (message.getHeader(ReadCommand.DO_REPAIR) != null)
            {
                doReadRepair(row, readCommand);
            }
        }
    }
    
    private void doReadRepair(Row row, ReadCommand readCommand)
    {
        List<EndPoint> endpoints = StorageService.instance().getNLiveStorageEndPoint(readCommand.key);
        /* Remove the local storage endpoint from the list. */ 
        endpoints.remove( StorageService.getLocalStorageEndPoint() );
            
        if (endpoints.size() > 0 && DatabaseDescriptor.getConsistencyCheck())
            StorageService.instance().doConsistencyCheck(row, endpoints, readCommand);
    }     
}
