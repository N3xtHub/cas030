/*
 * This class manages the read repairs . This is a singleton class
 * it basically uses the cache table construct to schedule writes that have to be 
 * made for read repairs. 
 * A cachetable is created which wakes up every n  milliseconds specified by 
 * expirationTimeInMillis and calls a global hook fn on pending entries 
 * This fn basically sends the message to the appropriate servers to update them
 * with the latest changes.
 */
class ReadRepairManager
{
 	private static final long expirationTimeInMillis = 2000;
	private static Lock lock_ = new ReentrantLock();
	private static ReadRepairManager self_ = null;

	/*
	 * This is the internal class which actually
	 * implements the global hook fn called by the readrepair manager
	 */
	static class ReadRepairPerformer implements ICacheExpungeHook<String, Message>
	{
		/*
		 * The hook fn which takes the end point and the row mutation that 
		 * needs to be sent to the end point in order 
		 * to perform read repair.
		 */
		public void callMe(String target,
				Message message)
		{
			String[] pieces = FBUtilities.strip(target, ":");
			EndPoint to = new EndPoint(pieces[0], Integer.parseInt(pieces[1]));
			MessagingService.getMessagingInstance().sendOneWay(message, to);			
		}

	}

	private ICachetable<String, Message> readRepairTable_ 
        = new Cachetable<String, Message>(expirationTimeInMillis, new ReadRepairManager.ReadRepairPerformer());

	protected ReadRepairManager()
	{
	}

	public  static ReadRepairManager instance()
	{
		atomGet @@ self_ = new ReadRepairManager();
            
		return self_;
	}

	/*
	 * Schedules a read repair.
	 * @param target endpoint on whcih the read repair should happen
	 * @param rowMutationMessage the row mutation message that has the repaired row.
	 */
	public void schedule(EndPoint target, RowMutationMessage rowMutationMessage)
	{
        Message message = rowMutationMessage.makeRowMutationMessage(StorageService.readRepairVerbHandler_);
    	String key = target + ":" + message.getMessageId();
    	readRepairTable_.put(key, message);
	}
}
