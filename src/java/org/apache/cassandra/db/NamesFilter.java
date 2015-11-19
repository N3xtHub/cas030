
public class NamesFilter implements IFilter
{
    /* list of column names to filter against. */
    private List<String> names_;

    NamesFilter(List<String> names)
    {
        names_ = new ArrayList<String>(names);
    }

    public ColumnFamily filter(String cf, ColumnFamily columnFamily)
    {
        if ( columnFamily == null )
        {
            return columnFamily;
        }
    	String[] values = RowMutation.getColumnAndColumnFamily(cf);
        ColumnFamily filteredCf = new ColumnFamily(columnFamily.name(), columnFamily.type());
		if( values.length == 1 )
		{
			Collection<IColumn> columns = columnFamily.getAllColumns();
			for(IColumn column : columns)
			{
		        if ( names_.contains(column.name()) )
		        {
		            names_.remove(column.name());
					filteredCf.addColumn(column);
		        }
				if( isDone() )
				{
					return filteredCf;
				}
			}
		}
		else if (values.length == 2 && columnFamily.isSuper())
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
    		        if ( names_.contains(subColumn.name()) )
    		        {
    		            names_.remove(subColumn.name());
    		            filteredSuperColumn.addColumn(subColumn);
    		        }
    				if( isDone() )
    				{
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
        String columnName = column.name();
        if ( names_.contains(columnName) )
        {
            names_.remove(columnName);
        }
        else
        {
            column = null;
        }

        return column;
    }

    public boolean isDone()
    {
        return names_.isEmpty();
    }

    public DataInputBuffer next(String key, String cf, SSTable ssTable) throws IOException
    {
    	return ssTable.next(key, cf, names_, null);
    }

}
