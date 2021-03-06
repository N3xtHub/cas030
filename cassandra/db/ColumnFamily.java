public final class ColumnFamily
{
    public static final short utfPrefix_ = 2;   

    private static Map<String, String> columnTypes_ = new HashMap<String, String>();
    private static Map<String, String> indexTypes_ = new HashMap<String, String>();
    private String type_;

    static
    {
        /* TODO: These are the various column types. Hard coded for now. */
        columnTypes_.put("Standard", "Standard");
        columnTypes_.put("Super", "Super");

        indexTypes_.put("Name", "Name");
        indexTypes_.put("Time", "Time");
    }


    public static String getColumnType(String key)
    {
    	if ( key == null )
    		return columnTypes_.get("Standard");
    	return columnTypes_.get(key);
    }

    public static String getColumnSortProperty(String columnIndexProperty)
    {
    	if ( columnIndexProperty == null )
    		return indexTypes_.get("Time");
        return indexTypes_.get(columnIndexProperty);
    }

    private transient AbstractColumnFactory columnFactory_;

    private String name_;

    private transient ICompactSerializer2<IColumn> columnSerializer_;
    private long markedForDeleteAt = Long.MIN_VALUE;
    private int localDeletionTime = Integer.MIN_VALUE;
    private AtomicInteger size_ = new AtomicInteger(0);
    private EfficientBidiMap columns_;

    private Comparator<IColumn> columnComparator_;

	private Comparator<IColumn> getColumnComparator(String cfName, String columnType)
	{
		if(columnComparator_ == null)
		{
			/*
			 * if this columnfamily has supercolumns or there is an index on the column name,
			 * then sort by name
			*/
			if("Super".equals(columnType) || DatabaseDescriptor.isNameSortingEnabled(cfName))
			{
				columnComparator_ = ColumnComparatorFactory.getComparator(ColumnComparatorFactory.ComparatorType.NAME);
			}
			/* if this columnfamily has simple columns, and no index on name sort by timestamp */
			else
			{
				columnComparator_ = ColumnComparatorFactory.getComparator(ColumnComparatorFactory.ComparatorType.TIMESTAMP);
			}
		}

		return columnComparator_;
	}

    public ColumnFamily(String cfName, String columnType)
    {
        name_ = cfName;
        type_ = columnType;
        createColumnFactoryAndColumnSerializer(columnType);
    }

    void createColumnFactoryAndColumnSerializer(String columnType)
    {
        if ( columnFactory_ == null )
        {
            columnFactory_ = AbstractColumnFactory.getColumnFactory(columnType);
            columnSerializer_ = columnFactory_.createColumnSerializer();
            if(columns_ == null)
                columns_ = new EfficientBidiMap(getColumnComparator(name_, columnType));
        }
    }

    void createColumnFactoryAndColumnSerializer()
    {
    	String columnType = DatabaseDescriptor.getColumnFamilyType(name_);
        if ( columnType == null )
        {
        	List<String> tables = DatabaseDescriptor.getTables();
        	if ( tables.size() > 0 )
        	{
        		String table = tables.get(0);
        		columnType = Table.open(table).getColumnFamilyType(name_);
        	}
        }
        createColumnFactoryAndColumnSerializer(columnType);
    }

    ColumnFamily cloneMeShallow()
    {
        ColumnFamily cf = new ColumnFamily(name_, type_);
        cf.markedForDeleteAt = markedForDeleteAt;
        cf.localDeletionTime = localDeletionTime;
        return cf;
    }

    ColumnFamily cloneMe()
    {
        ColumnFamily cf = cloneMeShallow();
        cf.columns_ = columns_.cloneMe();
    	return cf;
    }

    public String name()
    {
        return name_;
    }

    /*
     *  We need to go through each column
     *  in the column family and resolve it before adding
    */
    void addColumns(ColumnFamily cf)
    {
        for (IColumn column : cf.getAllColumns())
        {
            addColumn(column);
        }
    }

    public ICompactSerializer2<IColumn> getColumnSerializer()
    {
        createColumnFactoryAndColumnSerializer();
    	return columnSerializer_;
    }

    public void addColumn(String name)
    {
    	addColumn(columnFactory_.createColumn(name));
    }

    int getColumnCount()
    {
    	int count = 0;
    	Map<String, IColumn> columns = columns_.getColumns();
    	if( columns != null )
    	{
    		if(!isSuper())
    		{
    			count = columns.size();
    		}
    		else
    		{
    			Collection<IColumn> values = columns.values();
		    	for(IColumn column: values)
		    	{
		    		count += column.getObjectCount();
		    	}
    		}
    	}
    	return count;
    }

    public boolean isSuper()
    {
        return type_.equals("Super");
    }

    public void addColumn(String name, byte[] value)
    {
    	addColumn(name, value, 0);
    }

    public void addColumn(String name, byte[] value, long timestamp)
    {
        addColumn(name, value, timestamp, false);
    }

    public void addColumn(String name, byte[] value, long timestamp, boolean deleted)
	{
		IColumn column = columnFactory_.createColumn(name, value, timestamp, deleted);
		addColumn(column);
    }

    void clear()
    {
    	columns_.clear();
    }

    /*
     * If we find an old column that has the same name
     * the ask it to resolve itself else add the new column .
    */
    void addColumn(IColumn column)
    {
        String name = column.name();
        IColumn oldColumn = columns_.get(name);
        if (oldColumn != null)
        {
            if (oldColumn instanceof SuperColumn)
            {
                int oldSize = oldColumn.size();
                ((SuperColumn) oldColumn).putColumn(column);
                size_.addAndGet(oldColumn.size() - oldSize);
            }
            else
            {
                if (((Column)oldColumn).comparePriority((Column)column) <= 0)
                {
                    columns_.put(name, column);
                    size_.addAndGet(column.size());
                }
            }
        }
        else
        {
            size_.addAndGet(column.size());
            columns_.put(name, column);
        }
    }

    public IColumn getColumn(String name)
    {
        return columns_.get( name );
    }

    public SortedSet<IColumn> getAllColumns()
    {
        return columns_.getSortedColumns();
    }

    public Map<String, IColumn> getColumns()
    {
        return columns_.getColumns();
    }

    public void remove(String columnName)
    {
    	columns_.remove(columnName);
    }

    void delete(int localtime, long timestamp)
    {
        localDeletionTime = localtime;
        markedForDeleteAt = timestamp;
    }

    public boolean isMarkedForDelete()
    {
        return markedForDeleteAt > Long.MIN_VALUE;
    }

    /*
     * This function will calculate the differnce between 2 column families
     * the external input is considered the superset of internal
     * so there are no deletes in the diff.
     */
    ColumnFamily diff(ColumnFamily cfComposite)
    {
    	ColumnFamily cfDiff = new ColumnFamily(cfComposite.name(), cfComposite.type_);
        if (cfComposite.getMarkedForDeleteAt() > getMarkedForDeleteAt())
        {
            cfDiff.delete(cfComposite.getLocalDeletionTime(), cfComposite.getMarkedForDeleteAt());
        }

        // (don't need to worry about cfNew containing IColumns that are shadowed by
        // the delete tombstone, since cfNew was generated by CF.resolve, which
        // takes care of those for us.)
        Map<String, IColumn> columns = cfComposite.getColumns();
        Set<String> cNames = columns.keySet();
        for ( String cName : cNames )
        {
        	IColumn columnInternal = columns_.get(cName);
        	IColumn columnExternal = columns.get(cName);
        	if( columnInternal == null )
        	{
        		cfDiff.addColumn(columnExternal);
        	}
        	else
        	{
            	IColumn columnDiff = columnInternal.diff(columnExternal);
        		if(columnDiff != null)
        		{
        			cfDiff.addColumn(columnDiff);
        		}
        	}
        }

        if (!cfDiff.getColumns().isEmpty() || cfDiff.isMarkedForDelete())
        	return cfDiff;
        else
        	return null;
    }

    int size()
    {
        if ( size_.get() == 0 )
        {
            Set<String> cNames = columns_.getColumns().keySet();
            for ( String cName : cNames )
            {
                size_.addAndGet(columns_.get(cName).size());
            }
        }
        return size_.get();
    }

    public int hashCode()
    {
        return name().hashCode();
    }

    public boolean equals(Object o)
    {
        if ( !(o instanceof ColumnFamily) )
            return false;
        ColumnFamily cf = (ColumnFamily)o;
        return name().equals(cf.name());
    }

    public String toString()
    {
    	StringBuilder sb = new StringBuilder();
        sb.append("ColumnFamily(");
    	sb.append(name_);

        if (isMarkedForDelete()) {
            sb.append(" -delete at " + getMarkedForDeleteAt() + "-");
        }

    	sb.append(" [");
        sb.append(StringUtils.join(getAllColumns(), ", "));
        sb.append("])");

    	return sb.toString();
    }

    public byte[] digest()
    {
    	Set<IColumn> columns = columns_.getSortedColumns();
    	byte[] xorHash = ArrayUtils.EMPTY_BYTE_ARRAY;
        for(IColumn column : columns)
    	{
    		if(xorHash.length == 0)
    		{
    			xorHash = column.digest();
    		}
    		else
    		{
                xorHash = FBUtilities.xor(xorHash, column.digest());
    		}
    	}
    	return xorHash;
    }

    public long getMarkedForDeleteAt()
    {
        return markedForDeleteAt;
    }

    public int getLocalDeletionTime()
    {
        return localDeletionTime;
    }

    public String type()
    {
        return type_;
    }

    /** merge all columnFamilies into a single instance, with only the newest versions of columns preserved. */
    static ColumnFamily resolve(List<ColumnFamily> columnFamilies)
    {
        int size = columnFamilies.size();
        if (size == 0)
            return null;

        // start from nothing so that we don't include potential deleted columns from the first instance
        ColumnFamily cf0 = columnFamilies.get(0);
        ColumnFamily cf = cf0.cloneMeShallow();

        // merge
        for (ColumnFamily cf2 : columnFamilies)
        {
            assert cf.name().equals(cf2.name());
            cf.addColumns(cf2);
            cf.delete(Math.max(cf.getLocalDeletionTime(), cf2.getLocalDeletionTime()),
                      Math.max(cf.getMarkedForDeleteAt(), cf2.getMarkedForDeleteAt()));
        }
        return cf;
    }
}

