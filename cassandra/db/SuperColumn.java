
public final class SuperColumn implements IColumn, Serializable
{
	private static SuperColumnSerializer serializer_ = new SuperColumnSerializer();
	private final static String seperator_ = ":";

    static SuperColumnSerializer serializer()
    {
        return serializer_;
    }

	private String name_;
    private EfficientBidiMap columns_ 
        = new EfficientBidiMap(ColumnComparatorFactory.getComparator(TIMESTAMP));
    private int localDeletionTime = Integer.MIN_VALUE;
	private long markedForDeleteAt = Long.MIN_VALUE;
    private AtomicInteger size_ = new AtomicInteger(0);

    SuperColumn()
    {
    }

    SuperColumn(String name)
    {
    	name_ = name;
    }

	public boolean isMarkedForDelete()
	{
		return markedForDeleteAt > Long.MIN_VALUE;
	}

    public Collection<IColumn> getSubColumns()
    {
    	return columns_.getSortedColumns();
    }

    public IColumn getSubColumn(String columnName)
    {
        IColumn column = columns_.get(columnName);
        assert column == null || column instanceof Column;
        return column;
    }

    public int compareTo(IColumn superColumn)
    {
        return (name_.compareTo(superColumn.name()));
    }


    public int size()
    {
        /*
         * return the size of the individual columns
         * that make up the super column. This is an
         * APPROXIMATION of the size used only from the
         * Memtable.
        */
        return size_.get();
    }

    /**
     * This returns the size of the super-column when serialized.
     * @see org.apache.cassandra.db.IColumn#serializedSize()
    */
    public int serializedSize()
    {
        /*
         * Size of a super-column is =
         *   size of a name (UtfPrefix + length of the string)
         * + 1 byte to indicate if the super-column has been deleted
         * + 4 bytes for size of the sub-columns
         * + 4 bytes for the number of sub-columns
         * + size of all the sub-columns.
        */

    	/*
    	 * We store the string as UTF-8 encoded, so when we calculate the length, it
    	 * should be converted to UTF-8.
    	 */
    	/*
    	 * We need to keep the way we are calculating the column size in sync with the
    	 * way we are calculating the size for the column family serializer.
    	 */
    	return IColumn.UtfPrefix_ + FBUtilities.getUTF8Length(name_) + DBConstants.boolSize_ + DBConstants.intSize_ + DBConstants.intSize_ + getSizeOfAllColumns();
    }

    /**
     * This calculates the exact size of the sub columns on the fly
     */
    int getSizeOfAllColumns()
    {
        int size = 0;
        Collection<IColumn> subColumns = getSubColumns();
        for ( IColumn subColumn : subColumns )
        {
            size += subColumn.serializedSize();
        }
        return size;
    }

    public void remove(String columnName)
    {
    	columns_.remove(columnName);
    }

    public long timestamp()
    {
    	throw new UnsupportedOperationException("This operation is not supported for Super Columns.");
    }

    public long timestamp(String key)
    {
    	IColumn column = columns_.get(key);
    	if ( column instanceof SuperColumn )
    		throw new UnsupportedOperationException("A super column cannot hold other super columns.");
    	if ( column != null )
    		return column.timestamp();
    	throw new IllegalArgumentException("Timestamp was requested for a column that does not exist.");
    }

    public byte[] value()
    {
    	throw new UnsupportedOperationException("This operation is not supported for Super Columns.");
    }

    public byte[] value(String key)
    {
    	IColumn column = columns_.get(key);
    	if ( column != null )
    		return column.value();
    	throw new IllegalArgumentException("Value was requested for a column that does not exist.");
    }

    public void addColumn(IColumn column)
    {
    	if (!(column instanceof Column))
    		throw new UnsupportedOperationException("A super column can only contain simple columns.");
    	IColumn oldColumn = columns_.get(column.name());
    	if ( oldColumn == null )
        {
    		columns_.put(column.name(), column);
            size_.addAndGet(column.size());
        }
    	else
    	{
    		if (((Column)oldColumn).comparePriority((Column)column) <= 0)
            {
    			columns_.put(column.name(), column);
                int delta = (-1)*oldColumn.size();
                /* subtract the size of the oldColumn */
                size_.addAndGet(delta);
                /* add the size of the new column */
                size_.addAndGet(column.size());
            }
    	}
    }

    /*
     * Go through each sub column if it exists then as it to resolve itself
     * if the column does not exist then create it.
     */
    public void putColumn(IColumn column)
    {
    	if ( !(column instanceof SuperColumn))
    		throw new UnsupportedOperationException("Only Super column objects should be put here");
    	if( !name_.equals(column.name()))
    		throw new IllegalArgumentException("The name should match the name of the current column or super column");

        for (IColumn subColumn : column.getSubColumns())
        {
        	addColumn(subColumn);
        }
        if (column.getMarkedForDeleteAt() > markedForDeleteAt)
        {
            markForDeleteAt(column.getLocalDeletionTime(),  column.getMarkedForDeleteAt());
        }
    }

    public int getObjectCount()
    {
    	return 1 + columns_.size();
    }

    public long getMarkedForDeleteAt() {
        return markedForDeleteAt;
    }

    int getColumnCount()
    {
    	return columns_.size();
    }

    public IColumn diff(IColumn columnNew)
    {
    	IColumn columnDiff = new SuperColumn(columnNew.name());
        if (columnNew.getMarkedForDeleteAt() > getMarkedForDeleteAt())
        {
            ((SuperColumn)columnDiff).markForDeleteAt(columnNew.getLocalDeletionTime(), columnNew.getMarkedForDeleteAt());
        }

        // (don't need to worry about columnNew containing subColumns that are shadowed by
        // the delete tombstone, since columnNew was generated by CF.resolve, which
        // takes care of those for us.)
        for (IColumn subColumn : columnNew.getSubColumns())
        {
        	IColumn columnInternal = columns_.get(subColumn.name());
        	if(columnInternal == null )
        	{
        		columnDiff.addColumn(subColumn);
        	}
        	else
        	{
            	IColumn subColumnDiff = columnInternal.diff(subColumn);
        		if(subColumnDiff != null)
        		{
            		columnDiff.addColumn(subColumnDiff);
        		}
        	}
        }

        if (!columnDiff.getSubColumns().isEmpty() || columnNew.isMarkedForDelete())
        	return columnDiff;
        else
        	return null;
    }

    public byte[] digest()
    {
    	Set<IColumn> columns = columns_.getSortedColumns();
    	byte[] xorHash = ArrayUtils.EMPTY_BYTE_ARRAY;
    	if(name_ == null)
    		return xorHash;
    	xorHash = name_.getBytes();
    	for(IColumn column : columns)
    	{
			xorHash = FBUtilities.xor(xorHash, column.digest());
    	}
    	return xorHash;
    }

    public int getLocalDeletionTime()
    {
        return localDeletionTime;
    }

    public void markForDeleteAt(int localDeleteTime, long timestamp)
    {
        this.localDeletionTime = localDeleteTime;
        this.markedForDeleteAt = timestamp;
    }
}

