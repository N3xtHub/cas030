class ConsistencyManager implements Runnable
{
  	class DigestResponseHandler implements IAsyncCallback
  	{
  	    List<Message> responses_ = new ArrayList<Message>();
  		
  		  public void response(Message msg)
  		  {
  			    responses_.add(msg);
  			    if ( responses_.size() == ConsistencyManager.this.replicas_.size() )
  				  handleDigestResponses();
  		  }
  		
  		  private void handleDigestResponses()
  		  {
            DataInputBuffer bufIn = new DataInputBuffer();
  			
            for( Message response : responses_ )
  			    {
  				      byte[] body = response.getMessageBody();            
  	            bufIn.reset(body, body.length);
                ReadResponse result = ReadResponse.serializer().deserialize(bufIn);
                byte[] digest = result.digest();
                if( !Arrays.equals(row_.digest(), digest) )
  				      {
               	    doReadRepair();
               	    break;
  				      }
  			    }
  		  }
  		
  		  private void doReadRepair()
  		  {
  		      IResponseResolver<Row> readResponseResolver = new ReadResponseResolver();
            /* Add the local storage endpoint to the replicas_ list */
            replicas_.add(StorageService.getLocalStorageEndPoint());
  			
            IAsyncCallback responseHandler = new DataRepairHandler(
                  ConsistencyManager.this.replicas_.size(), readResponseResolver);	
            ReadCommand readCommand = constructReadMessage(false);
            Message message = readCommand.makeReadMessage();
        
            MessagingService.getMessagingInstance().sendRR(message, 
                replicas_.toArray(new EndPoint[replicas_.size()]), responseHandler);
  		  }
  	}
	
  	class DataRepairHandler implements IAsyncCallback, ICacheExpungeHook<String, String>
  	{
  		  private List<Message> responses_ = new ArrayList<Message>();
  		  private IResponseResolver<Row> readResponseResolver_;
  		  private int majority_;
  		
  		  DataRepairHandler(int responseCount, IResponseResolver<Row> readResponseResolver)
  		  {
  			    readResponseResolver_ = readResponseResolver;
  			    majority_ = (responseCount >> 1) + 1;  
  		  }
  		
  		  public void response(Message message)
  		  {
  			    responses_.add(message);
  			    if ( responses_.size() == majority_ )
  			    {
  				      String messageId = message.getMessageId();
  				      readRepairTable_.put(messageId, messageId, this);				
  			    }
  		  }
  		
  		  public void callMe(String key, String value)
  		  {
  			    handleResponses();
  		  }
  		
  		  private void handleResponses()
  		  {
  			    readResponseResolver_.resolve(new ArrayList<Message>(responses_));
  		  }
  	}

  	private static long scheduledTimeMillis_ = 600;
  	private static ICachetable<String, String> readRepairTable_ = new Cachetable<String, String>(scheduledTimeMillis_);
  	private final Row row_;
  	protected final List<EndPoint> replicas_;
  	private final ReadCommand readCommand_;

    public ConsistencyManager(Row row, List<EndPoint> replicas, ReadCommand readCommand)
    {
        row_ = row;
        replicas_ = replicas;
        readCommand_ = readCommand;
    }

  	public void run()
  	{
        ReadCommand readCommandDigestOnly = constructReadMessage(true);
  		  Message message = readCommandDigestOnly.makeReadMessage();
        MessagingService.getMessagingInstance()
          .sendRR(message, replicas_.toArray(new EndPoint[replicas_.size()]), new DigestResponseHandler());
  	}
      
    private ReadCommand constructReadMessage(boolean isDigestQuery)
    {
        ReadCommand readCommand = readCommand_.copy();
        readCommand.setDigestQuery(isDigestQuery);
        return readCommand;
    }
}
