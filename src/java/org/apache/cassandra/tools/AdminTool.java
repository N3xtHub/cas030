
public class AdminTool
{

	String server_ = null;
	String tableName_ = "Mailbox";
	String key_ = "Random";
	String cf1_ = "MailboxThreadList0";
	String cf2_ = "MailboxUserList0";
	String cf3_ = "MailboxMailList0";
	String cf4_ = "MailboxMailData0";
//	String cf5_ = "MailboxUserList";
	public static EndPoint from_ = new EndPoint("hadoop071.sf2p.facebook.com", 10001);
	private static final String[] servers_ =
	{
		"insearch001.sf2p.facebook.com",
		...
		"insearch040.ash1.facebook.com",
	};

	AdminTool()
	{
		server_ = null;
	}

	AdminTool(String server)
	{
		server_ = server;
	}

	public void run(int operation, String columnFamilyName, long skip) throws Throwable
	{
        byte[] bytes =  BasicUtilities.longToByteArray( skip );
        RowMutation rm = new RowMutation(tableName_, key_);
        if( columnFamilyName == null )
        {
			rm.add(Table.recycleBin_ + ":" + cf1_, bytes, operation);
			rm.add(Table.recycleBin_ + ":" + cf2_, bytes, operation);
			rm.add(Table.recycleBin_ + ":" + cf3_, bytes, operation);
			rm.add(Table.recycleBin_ + ":" + cf4_, bytes, operation);
        }
        else
        {
			rm.add(Table.recycleBin_ + ":" + columnFamilyName, bytes, operation);
        }
		RowMutationMessage rmMsg = new RowMutationMessage(rm);
        if( server_ != null)
        {
            Message message = rmMsg.makeRowMutationMessage(StorageService.binaryVerbHandler_);
	        EndPoint to = new EndPoint(server_, 7000);
			MessagingService.getMessagingInstance().sendOneWay(message, to);
        }
        else
        {
        	for( String server : servers_ )
        	{
                Message message = rmMsg.makeRowMutationMessage(StorageService.binaryVerbHandler_);
		        EndPoint to = new EndPoint(server, 7000);
				MessagingService.getMessagingInstance().sendOneWay(message, to);
        	}
        }
	}


	/**
	 * @param args
	 */
	public static void main(String[] args) throws Throwable
	{
		LogUtil.init();
		AdminTool postLoad = null;
		int operation = 1;
		String columnFamilyName = null;
		long skip = 0L;
		if(args.length < 1 )
		{
			System.out.println("Usage: PostLoad <serverName>  < operation 1- flushBinary 2 - compactions 3- flush> <ColumnFamilyName> <skip factor for compactions> or  PostLoad <-all> <operation> <ColumnFamilyName> <skip factor for compactions>");
		}
		if(args[0].equals("-all"))
		{
			 postLoad = new AdminTool();
		}
		else
		{
			 postLoad = new AdminTool(args[0]);
		}
		if(args.length > 1 )
			operation = Integer.parseInt(args[1]);
		if(args.length > 2 )
			columnFamilyName = args[2];
		if(args.length > 3 )
			skip = Long.parseLong(args[3]);
		postLoad.run(operation, columnFamilyName, skip);

		Thread.sleep(10000);
		System.out.println("Exiting app...");
		System.exit(0);
	}

}
