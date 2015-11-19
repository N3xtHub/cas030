abstract class AbstractColumnFactory
{
    private static Map<String, AbstractColumnFactory> columnFactory_ = new HashMap<String, AbstractColumnFactory>();

	static
	{
		columnFactory_.put(ColumnFamily.getColumnType("Standard"),new ColumnFactory());
		columnFactory_.put(ColumnFamily.getColumnType("Super"),new SuperColumnFactory());
	}

	static AbstractColumnFactory getColumnFactory(String columnType)
	{
		/* Create based on the type required. */
		if ( columnType == null || columnType.equals("Standard") )
			return columnFactory_.get("Standard");
		else
			return columnFactory_.get("Super");
	}

	public abstract IColumn createColumn(String name);
	public abstract IColumn createColumn(String name, byte[] value);
    public abstract IColumn createColumn(String name, byte[] value, long timestamp);
    public abstract IColumn createColumn(String name, byte[] value, long timestamp, boolean deleted);
    public abstract ICompactSerializer2<IColumn> createColumnSerializer();
}

class ColumnFactory extends AbstractColumnFactory
{
	public IColumn createColumn(String name)
	{
		return new Column(name);
	}

	public IColumn createColumn(String name, byte[] value)
	{
		return new Column(name, value);
	}

	public IColumn createColumn(String name, byte[] value, long timestamp)
	{
		return new Column(name, value, timestamp);
	}

    public IColumn createColumn(String name, byte[] value, long timestamp, boolean deleted) {
        return new Column(name, value, timestamp, deleted);
    }

    public ICompactSerializer2<IColumn> createColumnSerializer()
    {
        return Column.serializer();
    }
}

class SuperColumnFactory extends AbstractColumnFactory
{
    static String[] getSuperColumnAndColumn(String cName)
    {
        StringTokenizer st = new StringTokenizer(cName, ":");
        String[] values = new String[st.countTokens()];
        int i = 0;
        while ( st.hasMoreElements() )
        {
            values[i++] = (String)st.nextElement();
        }
        return values;
    }

	public IColumn createColumn(String name)
	{
		String[] values = SuperColumnFactory.getSuperColumnAndColumn(name);
        if ( values.length == 0 ||  values.length > 2 )
            throw new IllegalArgumentException("Super Column " + name + " in invalid format. Must be in <super column name>:<column name> format.");
        IColumn superColumn = new SuperColumn(values[0]);
        if(values.length == 2)
        {
	        IColumn subColumn = new Column(values[1]);
	        superColumn.addColumn(subColumn);
        }
		return superColumn;
	}

	public IColumn createColumn(String name, byte[] value)
	{
        return createColumn(name, value, 0);
	}

    public IColumn createColumn(String name, byte[] value, long timestamp)
    {
        return createColumn(name, value, timestamp, false);
    }

    public IColumn createColumn(String name, byte[] value, long timestamp, boolean deleted)
	{
		String[] values = SuperColumnFactory.getSuperColumnAndColumn(name);
        if ( values.length != 2 )
            throw new IllegalArgumentException("Super Column " + name + " in invalid format. Must be in <super column name>:<column name> format.");
        IColumn superColumn = new SuperColumn(values[0]);
        IColumn subColumn = new Column(values[1], value, timestamp, deleted);
        superColumn.addColumn(subColumn);
		return superColumn;
	}

    public ICompactSerializer2<IColumn> createColumnSerializer()
    {
        return SuperColumn.serializer();
    }
}