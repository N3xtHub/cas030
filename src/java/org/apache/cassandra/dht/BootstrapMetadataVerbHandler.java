
/**
 * This verb handler handles the BootstrapMetadataMessage that is sent
 * by the leader to the nodes that are responsible for handing off data. 
*/
public class BootstrapMetadataVerbHandler implements IVerbHandler
{
    public void doVerb(Message message)
    {
        logger_.debug("Received a BootstrapMetadataMessage from " + message.getFrom());
        byte[] body = message.getMessageBody();
        DataInputBuffer bufIn = new DataInputBuffer();
        bufIn.reset(body, body.length);
       
        BootstrapMetadataMessage bsMetadataMessage = BootstrapMetadataMessage.serializer().deserialize(bufIn);
        BootstrapMetadata[] bsMetadata = bsMetadataMessage.bsMetadata_;
        
        /*
         * This is for debugging purposes. Remove later.
        */
        for ( BootstrapMetadata bsmd : bsMetadata )
        {
            logger_.debug(bsmd.toString());                                      
        }
        
        for ( BootstrapMetadata bsmd : bsMetadata )
        {
            long startTime = System.currentTimeMillis();
            doTransfer(bsmd.target_, bsmd.ranges_);     
            logger_.debug("Time taken to boostrap " + 
                    bsmd.target_ + 
                    " is " + 
                    (System.currentTimeMillis() - startTime) +
                    " msecs.");
        }
    }
    
    /*
     * This method needs to figure out the files on disk
     * locally for each range and then stream them using
     * the Bootstrap protocol to the target endpoint.
    */
    private void doTransfer(EndPoint target, List<Range> ranges) throws IOException
    {
        if ( ranges.size() == 0 )
        {
            logger_.debug("No ranges to give scram ...");
            return;
        }
        
        /* Just for debugging process - remove later */            
        for ( Range range : ranges )
        {
            StringBuilder sb = new StringBuilder("");                
            sb.append(range.toString());
            sb.append(" ");            
            logger_.debug("Beginning transfer process to " + target + " for ranges " + sb.toString());                
        }
      
        /*
         * (1) First we dump all the memtables to disk.
         * (2) Run a version of compaction which will basically
         *     put the keys in the range specified into a directory
         *     named as per the endpoint it is destined for inside the
         *     bootstrap directory.
         * (3) Handoff the data.
        */
        List<String> tables = DatabaseDescriptor.getTables();
        for ( String tName : tables )
        {
            Table table = Table.open(tName);
            logger_.debug("Flushing memtables ...");
            table.flush(false);
            logger_.debug("Forcing compaction ...");
            /* Get the counting bloom filter for each endpoint and the list of files that need to be streamed */
            List<String> fileList = new ArrayList<String>();
            boolean bVal = table.forceCompaction(ranges, target, fileList);                
            doHandoff(target, fileList);
        }
    }

    /**
     * Stream the files in the bootstrap directory over to the
     * node being bootstrapped.
    */
    private void doHandoff(EndPoint target, List<String> fileList) throws IOException
    {
        List<File> filesList = new ArrayList<File>();
        for(String file : fileList)
        {
            filesList.add(new File(file));
        }
        File[] files = filesList.toArray(new File[0]);
        StreamContextManager.StreamContext[] streamContexts = new StreamContextManager.StreamContext[files.length];
        int i = 0;
        for ( File file : files )
        {
            streamContexts[i] = new StreamContextManager.StreamContext(file.getAbsolutePath(), file.length());
            logger_.debug("Stream context metadata " + streamContexts[i]);
            ++i;
        }
        
        if ( files.length > 0 )
        {
            /* Set up the stream manager with the files that need to streamed */
            StreamManager.instance(target).addFilesToStream(streamContexts);
            /* Send the bootstrap initiate message */
            BootstrapInitiateMessage biMessage = new BootstrapInitiateMessage(streamContexts);
            Message message = BootstrapInitiateMessage.makeBootstrapInitiateMessage(biMessage);
            
            MessagingService.getMessagingInstance().sendOneWay(message, target);                
            StreamManager.instance(target).waitForStreamCompletion();
            logger_.debug("Done with transfer to " + target);  
        }
    }
}

