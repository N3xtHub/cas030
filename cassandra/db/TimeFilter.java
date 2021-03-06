
/**
 * This class provides a filter for fitering out columns
 * that are older than a specific time.
 * 
 */
class TimeFilter implements IFilter
{
	private long timeLimit_;
	private boolean isDone_;
	
	TimeFilter(long timeLimit)
	{
		timeLimit_ = timeLimit;		
		isDone_ = false;
	}

	public ColumnFamily filter(String cf, ColumnFamily columnFamily)
	{
    	if (columnFamily == null)
    		return columnFamily;

        String[] values = RowMutation.getColumnAndColumnFamily(cf);
        ColumnFamily filteredCf = new ColumnFamily(columnFamily.name(), columnFamily.type());
		if (values.length == 1 && !columnFamily.isSuper())
		{
    		Collection<IColumn> columns = columnFamily.getAllColumns();
    		int i =0; 
    		for(IColumn column : columns)
    		{
    			if ( column.timestamp() >=  timeLimit_ )
    			{
    				filteredCf.addColumn(column);
    				++i;
    			}
    			else
    			{
    				break;
    			}
    		}
    		if( i < columns.size() )
    		{
    			isDone_ = true;
    		}
		}    	
    	else if (values.length == 2 && columnFamily.isSuper())
    	{
    		/* 
    		 * TODO : For super columns we need to re-visit this issue.
    		 * For now this fn will set done to true if we are done with
    		 * atleast one super column
    		 */
    		Collection<IColumn> columns = columnFamily.getAllColumns();
    		for(IColumn column : columns)
    		{
    			SuperColumn superColumn = (SuperColumn)column;
       			SuperColumn filteredSuperColumn = new SuperColumn(superColumn.name());
				filteredCf.addColumn(filteredSuperColumn);
        		Collection<IColumn> subColumns = superColumn.getSubColumns();
        		int i = 0;
        		for(IColumn subColumn : subColumns)
        		{
	    			if (  subColumn.timestamp()  >=  timeLimit_ )
	    			{
			            filteredSuperColumn.addColumn(subColumn);
	    				++i;
	    			}
	    			else
	    			{
	    				break;
	    			}
        		}
        		if( i < filteredSuperColumn.getColumnCount() )
        		{
        			isDone_ = true;
        		}
    		}
    	}
    	else 
    	{
    		throw new UnsupportedOperationException();
    	}
		return filteredCf;
	}
    
    public IColumn filter(IColumn column, DataInputStream dis) throws IOException
    {
    	long timeStamp = 0;
    	/*
    	 * If its a column instance we need the timestamp to verify if 
    	 * it should be filtered , but at this instance the timestamp is not read
    	 * so we read the timestamp and set the buffer back so that the rest of desrialization
    	 * logic does not change.
    	 */
    	if(column instanceof Column)
    	{
	    	dis.mark(1000);
	        dis.readBoolean();
	        timeStamp = dis.readLong();
		    dis.reset();
	    	if( timeStamp < timeLimit_ )
	    	{
	    		isDone_ = true;
	    		return null;
	    	}
    	}
    	return column;
    }
    
	
	public boolean isDone()
	{
		return isDone_;
	}

	public DataInputBuffer next(String key, String cfName, SSTable ssTable) throws IOException
    {
    	return ssTable.next( key, cfName, null, new IndexHelper.TimeRange( timeLimit_, Long.MAX_VALUE ) );
    }
}
