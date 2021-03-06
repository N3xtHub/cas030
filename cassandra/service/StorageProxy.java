
public class StorageProxy implements StorageProxyMBean
{
    
    private static TimedStatsDeque readStats = new TimedStatsDeque(60000);
    private static TimedStatsDeque rangeStats = new TimedStatsDeque(60000);
    private static TimedStatsDeque writeStats = new TimedStatsDeque(60000);
    private StorageProxy() {}
    
    static
    {
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        mbs.registerMBean(new StorageProxy(), new ObjectName("service:type=StorageProxy"));
    }

    /**
     * This method is responsible for creating Message to be
     * sent over the wire to N replicas where some of the replicas
     * may be hints.
     */
    private static Map<EndPoint, Message> createWriteMessages(RowMutation rm, Map<EndPoint, EndPoint> endpointMap) 
    {
		Map<EndPoint, Message> messageMap = new HashMap<EndPoint, Message>();
		Message message = rm.makeRowMutationMessage();

		for (Map.Entry<EndPoint, EndPoint> entry : endpointMap.entrySet())
		{
            EndPoint target = entry.getKey();
            EndPoint hint = entry.getValue();
            if ( target != hint )
			{
				Message hintedMessage = rm.makeRowMutationMessage();
				hintedMessage.addHeader(RowMutation.HINT, EndPoint.toBytes(hint) );
				messageMap.put(target, hintedMessage);
			}
			else
			{
				messageMap.put(target, message);
			}
		}

		return messageMap;
    }
    
    /**
     * Use this method to have this RowMutation applied
     * across all replicas. This method will take care
     * of the possibility of a replica being down and hint
     * the data across to some other replica. 
     * @param rm the mutation to be applied across the replicas
    */
    public static void insert(RowMutation rm)
	{
        /*
         * Get the N nodes from storage service where the data needs to be
         * replicated
         * Construct a message for write
         * Send them asynchronously to the replicas.
        */

        long startTime = System.currentTimeMillis();
		
    	Map<EndPoint, EndPoint> endpointMap = StorageService.instance().getNStorageEndPointMap(rm.key());
		Map<EndPoint, Message> messageMap = createWriteMessages(rm, endpointMap);
		for (Map.Entry<EndPoint, Message> entry : messageMap.entrySet())
		{
            Message message = entry.getValue();
            EndPoint endpoint = entry.getKey();

            MessagingService.getMessagingInstance().sendOneWay(message, endpoint);
		}
	
        writeStats.add(System.currentTimeMillis() - startTime);
    }

    public static void insertBlocking(RowMutation rm) throws UnavailableException
    {
        long startTime = System.currentTimeMillis();
        Message message = rm.makeRowMutationMessage();
   
        QuorumResponseHandler<Boolean> quorumResponseHandler = new QuorumResponseHandler<Boolean>(
                DatabaseDescriptor.getReplicationFactor(),
                new WriteResponseResolver());
        EndPoint[] endpoints = StorageService.instance().getNStorageEndPoint(rm.key());
        
        MessagingService.getMessagingInstance().sendRR(message, endpoints, quorumResponseHandler);
        if (!quorumResponseHandler.get())
            throw new UnavailableException();
   
        writeStats.add(System.currentTimeMillis() - startTime);
    }
    
    private static Map<String, Message> constructMessages(Map<String, ReadCommand> readMessages) 
    {
        Map<String, Message> messages = new HashMap<String, Message>();
        Set<String> keys = readMessages.keySet();        
        for ( String key : keys )
        {
            Message message = readMessages.get(key).makeReadMessage();
            messages.put(key, message);
        }        
        return messages;
    }
    
    private static IAsyncResult dispatchMessages(Map<String, EndPoint> endPoints, Map<String, Message> messages)
    {
        Set<String> keys = endPoints.keySet();
        EndPoint[] eps = new EndPoint[keys.size()];
        Message[] msgs  = new Message[keys.size()];
        
        int i = 0;
        for ( String key : keys )
        {
            eps[i] = endPoints.get(key);
            msgs[i] = messages.get(key);
            ++i;
        }
        
        IAsyncResult iar = MessagingService.getMessagingInstance().sendRR(msgs, eps);
        return iar;
    }
    
    /**
     * This is an implementation for the multiget version. 
     * @param readMessages map of key --> ReadMessage to be sent
     * @return map of key --> Row
     * @throws IOException
     * @throws TimeoutException
     */
    public static Map<String, Row> doReadProtocol(Map<String, ReadCommand> readMessages) 
    {
        Map<String, Row> rows = new HashMap<String, Row>();
        Set<String> keys = readMessages.keySet();
        /* Find all the suitable endpoints for the keys */
        Map<String, EndPoint> endPoints = StorageService.instance().findSuitableEndPoints(keys.toArray( new String[0] ));
        /* Construct the messages to be sent out */
        Map<String, Message> messages = constructMessages(readMessages);
        /* Dispatch the messages to the respective endpoints */
        IAsyncResult iar = dispatchMessages(endPoints, messages);        
        List<byte[]> results = iar.multiget(2*DatabaseDescriptor.getRpcTimeout(), TimeUnit.MILLISECONDS);
        
        for ( byte[] body : results )
        {
            DataInputBuffer bufIn = new DataInputBuffer();
            bufIn.reset(body, body.length);
            ReadResponse response = ReadResponse.serializer().deserialize(bufIn);
            Row row = response.row();
            rows.put(row.key(), row);
        }        
        return rows;
    }

    /**
     * Read the data from one replica.  If there is no reply, read the data from another.  In the event we get
     * the data we perform consistency checks and figure out if any repairs need to be done to the replicas.
     * @param command the read to perform
     * @return the row associated with command.key
     * @throws Exception
     */
    private static Row weakReadRemote(ReadCommand command)
    {
        EndPoint endPoint = StorageService.instance().findSuitableEndPoint(command.key);
        assert endPoint != null;
        Message message = command.makeReadMessage();
        
        message.addHeader(ReadCommand.DO_REPAIR, ReadCommand.DO_REPAIR.getBytes());
        IAsyncResult iar = MessagingService.getMessagingInstance().sendRR(message, endPoint);
        byte[] body = iar.get(DatabaseDescriptor.getRpcTimeout(), TimeUnit.MILLISECONDS);
        DataInputBuffer bufIn = new DataInputBuffer();
        bufIn.reset(body, body.length);
        ReadResponse response = ReadResponse.serializer().deserialize(bufIn);
        return response.row();
    }

    /**
     * Performs the actual reading of a row out of the StorageService, fetching
     * a specific set of column names from a given column family.
     */
    public static Row readProtocol(ReadCommand command, StorageService.ConsistencyLevel consistencyLevel)
    {
        long startTime = System.currentTimeMillis();

        Row row;
        EndPoint[] endpoints = StorageService.instance().getNStorageEndPoint(command.key);

        if (consistencyLevel == StorageService.ConsistencyLevel.WEAK)
        {
            boolean foundLocal = Arrays.asList(endpoints).contains(StorageService.getLocalStorageEndPoint());
            if (foundLocal)
            {
                row = weakReadLocal(command);
            }
            else
            {
                row = weakReadRemote(command);
            }
        }
        else
        {
            assert consistencyLevel == StorageService.ConsistencyLevel.STRONG;
            row = strongRead(command);
        }

        readStats.add(System.currentTimeMillis() - startTime);

        return row;
    }

    public static Map<String, Row> readProtocol(String[] keys, ReadCommand readCommand, 
        StorageService.ConsistencyLevel consistencyLevel) 
    {
        Map<String, Row> rows = new HashMap<String, Row>();        
        switch ( consistencyLevel )
        {
            case WEAK:
                rows = weakReadProtocol(keys, readCommand);
                break;
                
            case STRONG:
                rows = strongReadProtocol(keys, readCommand);
                break;
                
            default:
                rows = weakReadProtocol(keys, readCommand);
                break;
        }
        return rows;
    }

    public static Map<String, Row> strongReadProtocol(String[] keys, ReadCommand readCommand) 
    {       
        Map<String, Row> rows;
        // TODO: throw a thrift exception if we do not have N nodes
        Map<String, ReadCommand[]> readMessages = new HashMap<String, ReadCommand[]>();
        for (String key : keys )
        {
            ReadCommand[] readParameters = new ReadCommand[2];
            readParameters[0] = readCommand.copy();
            readParameters[1] = readCommand.copy();
            readParameters[1].setDigestQuery(true);
            readMessages.put(key, readParameters);
        }        
        rows = doStrongReadProtocol(readMessages);         
        return rows;
    }

    /*
     * This function executes the read protocol.
        // 1. Get the N nodes from storage service where the data needs to be
        // replicated
        // 2. Construct a message for read\write
         * 3. Set one of teh messages to get teh data and teh rest to get teh digest
        // 4. SendRR ( to all the nodes above )
        // 5. Wait for a response from atleast X nodes where X <= N and teh data node
         * 6. If the digest matches return teh data.
         * 7. else carry out read repair by getting data from all the nodes.
        // 5. return success
     */
    private static Row strongRead(ReadCommand command) throws IOException, TimeoutException
    {
        // TODO: throw a thrift exception if we do not have N nodes
        assert !command.isDigestQuery();
        ReadCommand readMessageDigestOnly = command.copy();
        readMessageDigestOnly.setDigestQuery(true);

        Row row = null;
        Message message = command.makeReadMessage();
        Message messageDigestOnly = readMessageDigestOnly.makeReadMessage();

        IResponseResolver<Row> readResponseResolver = new ReadResponseResolver();
        QuorumResponseHandler<Row> quorumResponseHandler = new QuorumResponseHandler<Row>(
                DatabaseDescriptor.getReplicationFactor(),
                readResponseResolver);
        EndPoint dataPoint = StorageService.instance().findSuitableEndPoint(command.key);
        List<EndPoint> endpointList = new ArrayList<EndPoint>(Arrays.asList(StorageService.instance().getNStorageEndPoint(command.key)));
        /* Remove the local storage endpoint from the list. */
        endpointList.remove(dataPoint);
        EndPoint[] endPoints = new EndPoint[endpointList.size() + 1];
        Message messages[] = new Message[endpointList.size() + 1];

        /*
         * First message is sent to the node that will actually get
         * the data for us. The other two replicas are only sent a
         * digest query.
        */
        endPoints[0] = dataPoint;
        messages[0] = message;
      
        for (int i = 1; i < endPoints.length; i++)
        {
            EndPoint digestPoint = endpointList.get(i - 1);
            endPoints[i] = digestPoint;
            messages[i] = messageDigestOnly;
        }

        try
        {
            MessagingService.getMessagingInstance().sendRR(messages, endPoints, quorumResponseHandler);

            long startTime2 = System.currentTimeMillis();
            row = quorumResponseHandler.get();
        }
        catch (DigestMismatchException ex)
        {
            if ( DatabaseDescriptor.getConsistencyCheck())
            {
                IResponseResolver<Row> readResponseResolverRepair = new ReadResponseResolver();
                QuorumResponseHandler<Row> quorumResponseHandlerRepair = new QuorumResponseHandler<Row>(
                        DatabaseDescriptor.getReplicationFactor(),
                        readResponseResolverRepair);
                logger.info("DigestMismatchException: " + command.key);
                Message messageRepair = command.makeReadMessage();
                MessagingService.getMessagingInstance().sendRR(messageRepair, endPoints,
                                                               quorumResponseHandlerRepair);
                row = quorumResponseHandlerRepair.get();
            }
        }

        return row;
    }

    private static Map<String, Message[]> constructReplicaMessages(Map<String, ReadCommand[]> readMessages) 
    {
        Map<String, Message[]> messages = new HashMap<String, Message[]>();
        Set<String> keys = readMessages.keySet();
        
        for ( String key : keys )
        {
            Message[] msg = new Message[DatabaseDescriptor.getReplicationFactor()];
            ReadCommand[] readParameters = readMessages.get(key);
            msg[0] = readParameters[0].makeReadMessage();
            for ( int i = 1; i < msg.length; ++i )
            {
                msg[i] = readParameters[1].makeReadMessage();
            }
        }        
        return messages;
    }
    
    private static MultiQuorumResponseHandler dispatchMessages(Map<String, ReadCommand[]> readMessages, Map<String, Message[]> messages) 
    {
        Set<String> keys = messages.keySet();
        /* This maps the keys to the original data read messages */
        Map<String, ReadCommand> readMessage = new HashMap<String, ReadCommand>();
        /* This maps the keys to their respective endpoints/replicas */
        Map<String, EndPoint[]> endpoints = new HashMap<String, EndPoint[]>();
        /* Groups the messages that need to be sent to the individual keys */
        Message[][] msgList = new Message[messages.size()][DatabaseDescriptor.getReplicationFactor()];
        /* Respects the above grouping and provides the endpoints for the above messages */
        EndPoint[][] epList = new EndPoint[messages.size()][DatabaseDescriptor.getReplicationFactor()];
        
        int i = 0;
        for ( String key : keys )
        {
            /* This is the primary */
            EndPoint dataPoint = StorageService.instance().findSuitableEndPoint(key);
            List<EndPoint> replicas = new ArrayList<EndPoint>( StorageService.instance().getNLiveStorageEndPoint(key) );
            replicas.remove(dataPoint);
            /* Get the messages to be sent index 0 is the data messages and index 1 is the digest message */
            Message[] message = messages.get(key);           
            msgList[i][0] = message[0];
            int N = DatabaseDescriptor.getReplicationFactor();
            for ( int j = 1; j < N; ++j )
            {
                msgList[i][j] = message[1];
            }
            /* Get the endpoints to which the above messages need to be sent */
            epList[i][0] = dataPoint;
            for ( int j = 1; i < N; ++i )
            {                
                epList[i][j] = replicas.get(j - 1);
            } 
            /* Data ReadMessage associated with this key */
            readMessage.put( key, readMessages.get(key)[0] );
            /* EndPoints for this specific key */
            endpoints.put(key, epList[i]);
            ++i;
        }
                
        /* Handles the read semantics for this entire set of keys */
        MultiQuorumResponseHandler quorumResponseHandlers = new MultiQuorumResponseHandler(readMessage, endpoints);
        MessagingService.getMessagingInstance().sendRR(msgList, epList, quorumResponseHandlers);
        return quorumResponseHandlers;
    }
    
    /**
    *  This method performs the read from the replicas for a bunch of keys.
    *  @param readMessages map of key --> readMessage[] of two entries where 
    *         the first entry is the readMessage for the data and the second
    *         is the entry for the digest 
    *  @return map containing key ---> Row
    *  @throws IOException, TimeoutException
   */
    private static Map<String, Row> doStrongReadProtocol(Map<String, ReadCommand[]> readMessages)
    {        
        Map<String, Row> rows = new HashMap<String, Row>();
        /* Construct the messages to be sent to the replicas */
        Map<String, Message[]> replicaMessages = constructReplicaMessages(readMessages);
        /* Dispatch the messages to the different replicas */
        MultiQuorumResponseHandler cb = dispatchMessages(readMessages, replicaMessages);
        try
        {
            Row[] rows2 = cb.get();
            for ( Row row : rows2 )
            {
                rows.put(row.key(), row);
            }
        }

        return rows;
    }

    /**
     * This version is used when results for multiple keys needs to be
     * retrieved.
     * 
     * @param tablename name of the table that needs to be queried
     * @param keys keys whose values we are interested in 
     * @param columnFamily name of the "column" we are interested in
     * @param columns the columns we are interested in
     * @return a mapping of key --> Row
     * @throws Exception
     */
    public static Map<String, Row> weakReadProtocol(String[] keys, ReadCommand readCommand) 
    {
        Row row = null;
        Map<String, ReadCommand> readMessages = new HashMap<String, ReadCommand>();
        for ( String key : keys )
        {
            ReadCommand readCmd = readCommand.copy();
            readMessages.put(key, readCmd);
        }
        /* Performs the multiget in parallel */
        Map<String, Row> rows = doReadProtocol(readMessages);
        /*
         * Do the consistency checks for the keys that are being queried
         * in the background.
        */
        for ( String key : keys )
        {
            List<EndPoint> endpoints = StorageService.instance().getNLiveStorageEndPoint(key);
            /* Remove the local storage endpoint from the list. */
            endpoints.remove( StorageService.getLocalStorageEndPoint() );
            if ( endpoints.size() > 0 && DatabaseDescriptor.getConsistencyCheck())
                StorageService.instance().doConsistencyCheck(row, endpoints, readMessages.get(key));
        }
        return rows;
    }

    /*
    * This function executes the read protocol locally and should be used only if consistency is not a concern.
    * Read the data from the local disk and return if the row is NOT NULL. If the data is NULL do the read from
    * one of the other replicas (in the same data center if possible) till we get the data. In the event we get
    * the data we perform consistency checks and figure out if any repairs need to be done to the replicas.
    */
    private static Row weakReadLocal(ReadCommand command) 
    {
        logger.debug("weakreadlocal reading " + command);
        List<EndPoint> endpoints = StorageService.instance().getNLiveStorageEndPoint(command.key);
        /* Remove the local storage endpoint from the list. */
        endpoints.remove(StorageService.getLocalStorageEndPoint());
        // TODO: throw a thrift exception if we do not have N nodes

        Table table = Table.open(DatabaseDescriptor.getTables().get(0));
        Row row = command.getRow(table);

        /*
           * Do the consistency checks in the background and return the
           * non NULL row.
           */
        if (endpoints.size() > 0 && DatabaseDescriptor.getConsistencyCheck())
            StorageService.instance().doConsistencyCheck(row, endpoints, command);
        return row;
    }

    static List<String> getKeyRange(RangeCommand command)
    {
        long startTime = System.currentTimeMillis();

        EndPoint endPoint = StorageService.instance().findSuitableEndPoint(command.startWith);
        IAsyncResult iar = MessagingService.getMessagingInstance().sendRR(command.getMessage(), endPoint);

        // read response
        // TODO send more requests if we need to span multiple nodes
        byte[] responseBody = iar.get(DatabaseDescriptor.getRpcTimeout(), TimeUnit.MILLISECONDS);
        return RangeReply.read(responseBody).keys;

        rangeStats.add(System.currentTimeMillis() - startTime);
    }

    public double getReadLatency()
    {
        return readStats.mean();
    }

    public double getRangeLatency()
    {
        return rangeStats.mean();
    }

    public double getWriteLatency()
    {
        return writeStats.mean();
    }

    public int getReadOperations()
    {
        return readStats.size();
    }

    public int getRangeOperations()
    {
        return rangeStats.size();
    }

    public int getWriteOperations()
    {
        return writeStats.size();
    }
}
