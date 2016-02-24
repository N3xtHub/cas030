public class MultiQuorumResponseHandler implements IAsyncCallback
{ 
    private Lock lock_ = new ReentrantLock();
    private Condition condition_;

    // maps keys to original data read messages
    private Map<String, ReadCommand> readMessages_ = new HashMap<String, ReadCommand>(); 
    /* This maps the key to its set of replicas */
    private Map<String, EndPoint[]> endpoints_ = new HashMap<String, EndPoint[]>();
    /* This maps the groupId to the individual callback for the set of messages */
    private Map<String, SingleQuorumResponseHandler> handlers_ = new HashMap<String, SingleQuorumResponseHandler>();
    /* This should hold all the responses for the keys */
    private List<Row> responses_ = new ArrayList<Row>();
    private AtomicBoolean done_ = new AtomicBoolean(false);
    
    /**
     * This is used to handle the responses from the individual messages
     * that are sent out to the replicas.
     *
    */
    private class SingleQuorumResponseHandler implements IAsyncCallback
    {
        private Lock lock_ = new ReentrantLock();
        private IResponseResolver<Row> responseResolver_;
        private List<Message> responses_ = new ArrayList<Message>();
        
        SingleQuorumResponseHandler(IResponseResolver<Row> responseResolver)
        {
            responseResolver_ = responseResolver;
        }
        
        public void response(Message response)
        {
            lock_.lock()
            {
                responses_.add(response);
                int majority = (DatabaseDescriptor.getReplicationFactor() >> 1) + 1;                            
                if ( responses_.size() >= majority && responseResolver_.isDataPresent(responses_))
                {
                    onCompletion();               
                }
            }
        }
        
        private void onCompletion()
        {
            Row row = responseResolver_.resolve(responses_);
            MultiQuorumResponseHandler.this.onCompleteResponse(row);
        }
        
        /**
         * This method is invoked on a digest match. We pass in the key
         * in order to retrieve the appropriate data message that needs
         * to be sent out to the replicas. 
         * 
         * @param key for which the mismatch occured.
        */
        private void onDigestMismatch(String key) 
        {
            if ( DatabaseDescriptor.getConsistencyCheck())
            {                                
                ReadCommand readCommand = readMessages_.get(key);
                readCommand.setDigestQuery(false);
                Message messageRepair = readCommand.makeReadMessage();
                EndPoint[] endpoints = MultiQuorumResponseHandler.this.endpoints_.get(readCommand.key);
                Message[][] messages = new Message[][]{ {messageRepair, messageRepair, messageRepair} };
                EndPoint[][] epList = new EndPoint[][]{ endpoints };
                MessagingService.getMessagingInstance().sendRR(messages, epList, MultiQuorumResponseHandler.this);                
            }
        }
    }
    
    public MultiQuorumResponseHandler(Map<String, ReadCommand> readMessages, Map<String, EndPoint[]> endpoints)
    {        
        condition_ = lock_.newCondition();
        readMessages_ = readMessages;
        endpoints_ = endpoints;
    }
    
    public Row[] get()
    {
        boolean bVal = true;            
        if ( !done_.get() )
        {                   
            bVal = condition_.await(DatabaseDescriptor.getRpcTimeout());
        }
        
        return responses_.toArray( new Row[0] );
    }
    
    /**
     * Invoked when a complete response has been obtained
     * for one of the sub-groups a.k.a keys for the query 
     * has been performed.
     * 
     * @param row obtained as a result of the response.
     */
    void onCompleteResponse(Row row)
    {        
        if ( !done_.get() )
        {
            responses_.add(row);
            if ( responses_.size() == readMessages_.size() )
            {
                done_.set(true);
                condition_.signal();                
            }
        }
    }
    
    /**
     * The handler of the response message that has been
     * sent by one of the replicas for one of the keys.
     * 
     * @param message the reponse message for one of the
     *        message that we sent out.
     */
    public void response(Message message)
    {
        lock_.lock() {
            SingleQuorumResponseHandler handler = handlers_.get(message.getMessageId());
            handler.response(message);
        }
    }
    
    /**
     * The context that is passed in for the query of
     * multiple keys in the system. For each message 
     * id in the context register a callback handler 
     * for the same. This is done so that all responses
     * for a given key use the same callback handler.
     * 
     * @param o the context which is an array of strings
     *        corresponding to the message id's for each
     *        key.
     */
    public void attachContext(Object o)
    {
        String[] gids = (String[])o;
        for ( String gid : gids )
        {
            IResponseResolver<Row> responseResolver = new ReadResponseResolver();
            SingleQuorumResponseHandler handler = new SingleQuorumResponseHandler(responseResolver);
            handlers_.put(gid, handler);
        }
    }
}
