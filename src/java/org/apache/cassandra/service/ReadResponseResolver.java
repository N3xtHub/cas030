
/**
 * This class is used by all read functions and is called by the Quorum 
 * when atleast a few of the servers ( few is specified in Quorum)
 * have sent the response . The resolve fn then schedules read repair 
 * and resolution of read data from the various servers.
 */
public class ReadResponseResolver implements IResponseResolver<Row>
{
	/
	 * This method for resolving read data should look at the timestamps of each
	 * of the columns that are read and should pick up columns with the latest
	 * timestamp. For those columns where the timestamp is not the latest a
	 * repair request should be scheduled.
	 * 
	 */
	public Row resolve(List<Message> responses)
	{
        long startTime = System.currentTimeMillis();
		Row retRow = null;
		List<Row> rowList = new ArrayList<Row>();
		List<EndPoint> endPoints = new ArrayList<EndPoint>();
		String key = null;
		String table = null;
		byte[] digest = new byte[0];
		boolean isDigestQuery = false;
        
        /*
		 * Populate the list of rows from each of the messages
		 * Check to see if there is a digest query. If a digest 
         * query exists then we need to compare the digest with 
         * the digest of the data that is received.
        */
        DataInputBuffer bufIn = new DataInputBuffer();
		for (Message response : responses)
		{					            
            byte[] body = response.getMessageBody();
            bufIn.reset(body, body.length);
            try
            {
                long start = System.currentTimeMillis();
                ReadResponse result = ReadResponse.serializer().deserialize(bufIn);
                if(!result.isDigestQuery())
    			{
    				rowList.add(result.row());
    				endPoints.add(response.getFrom());
    				key = result.row().key();
    				table = result.table();
    			}
    			else
    			{
    				digest = result.digest();
    				isDigestQuery = true;
    			}
            }
		}
		// If there was a digest query compare it with all the data digests 
		// If there is a mismatch then throw an exception so that read repair can happen.
		if(isDigestQuery)
		{
			for(Row row: rowList)
			{
				if( !Arrays.equals(row.digest(), digest) )
				{
                    /* Wrap the key as the context in this exception */
					throw new DigestMismatchException(row.key());
				}
			}
		}
		
        /* If the rowList is empty then we had some exception above. */
        if ( rowList.size() == 0 )
        {
            return retRow;
        }
        
        /* Now calculate the resolved row */
		retRow = new Row(key);		
		for (int i = 0 ; i < rowList.size(); i++)
		{
			retRow.repair(rowList.get(i));			
		}

        // At  this point  we have the return row .
		// Now we need to calculate the differnce 
		// so that we can schedule read repairs 
		for (int i = 0 ; i < rowList.size(); i++)
		{
			// since retRow is the resolved row it can be used as the super set
			Row diffRow = rowList.get(i).diff(retRow);
			if(diffRow == null) // no repair needs to happen
				continue;
			// create the row mutation message based on the diff and schedule a read repair 
			RowMutation rowMutation = new RowMutation(table, key);            			
	        for (ColumnFamily cf : diffRow.getColumnFamilies())
	        {
	            rowMutation.add(cf);
	        }
            RowMutationMessage rowMutationMessage = new RowMutationMessage(rowMutation);
	        ReadRepairManager.instance().schedule(endPoints.get(i),rowMutationMessage);
		}
        
        return retRow;
	}

	public boolean isDataPresent(List<Message> responses)
	{
		for (Message response : responses)
		{
            byte[] body = response.getMessageBody();
			using (DataInputBuffer bufIn = new DataInputBuffer() ) 
            {
                bufIn.reset(body, body.length);
            	ReadResponse result = ReadResponse.serializer().deserialize(bufIn);
    			if(!result.isDigestQuery())
    			{
    				return true; // data is present
    			}
            }                     
		}

		return false;
	}
}
