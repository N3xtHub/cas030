

/**
 * This class provides a filter for fitering out columns
 * greater than a certain count.
 * 
 *
 */
public class CountFilter implements IFilter
{
	private long countLimit_;
	private boolean isDone_;
	private int offset_;

	CountFilter(int countLimit)
	{
		countLimit_ = countLimit;
		isDone_ = false;
		offset_ = 0;
	}
	
	CountFilter(int countLimit, int offset)
    {
        this(countLimit);
        offset_ = offset;
    }

	public ColumnFamily filter(String cfNameParam, ColumnFamily columnFamily)
	{
    	String[] values = RowMutation.getColumnAndColumnFamily(cfNameParam);
        if ( columnFamily == null )
            return columnFamily;

        ColumnFamily filteredCf = new ColumnFamily(columnFamily.name(), columnFamily.type());
		if( countLimit_ <= 0 )
		{
			isDone_ = true;
			return filteredCf;
		}
		if( values.length == 1)
		{
    		Collection<IColumn> columns = columnFamily.getAllColumns();
    		for(IColumn column : columns)
    		{
    			if (offset_ <= 0) {
    				filteredCf.addColumn(column);
    				countLimit_--;
    			} else
    				offset_ --;
    			if( countLimit_ <= 0 )
    			{
    				isDone_ = true;
    				return filteredCf;
    			}
    		}
		}
		else if(values.length == 2 && columnFamily.isSuper())
		{
    		Collection<IColumn> columns = columnFamily.getAllColumns();
    		for(IColumn column : columns)
    		{
    			SuperColumn superColumn = (SuperColumn)column;
    			SuperColumn filteredSuperColumn = new SuperColumn(superColumn.name());
				filteredCf.addColumn(filteredSuperColumn);
        		Collection<IColumn> subColumns = superColumn.getSubColumns();
        		for(IColumn subColumn : subColumns)
        		{
        			if (offset_ <=0 ){
        				filteredSuperColumn.addColumn(subColumn);
        				countLimit_--;
        			} else
        				offset_--;
        			
	    			if( countLimit_ <= 0 )
	    			{
	    				isDone_ = true;
	    				return filteredCf;
	    			}
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
		countLimit_--;
		if( countLimit_ <= 0 )
		{
			isDone_ = true;
		}
		return column;
    }

	public boolean isDone()
	{
		return isDone_;
	}

	public void setDone()
	{
		isDone_ = true;
	}

    public DataInputBuffer next(String key, String cf, SSTable ssTable) throws IOException
    {
    	return ssTable.next(key, cf);
    }
}
