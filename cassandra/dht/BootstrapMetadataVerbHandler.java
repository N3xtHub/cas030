
/**
 * This verb handler handles the BootstrapMetadataMessage that is sent
 * by the leader to the nodes that are responsible for handing off data. 
*/
public class BootstrapMetadataVerbHandler implements IVerbHandler
{
    public void doVerb(Message message)
    {
        byte[] body = message.getMessageBody();
        DataInputBuffer bufIn = new DataInputBuffer();
        bufIn.reset(body, body.length);
       
        BootstrapMetadataMessage bsMetadataMessage = BootstrapMetadataMessage.serializer().deserialize(bufIn);
        BootstrapMetadata[] bsMetadata = bsMetadataMessage.bsMetadata_;
        
        for ( BootstrapMetadata bsmd : bsMetadata )
        {
            long startTime = System.currentTimeMillis();
            doTransfer(bsmd.target_, bsmd.ranges_);     
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
            return;
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
            table.flush(false);
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
        }
    }
}

