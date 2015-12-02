
class EfficientBidiMap implements Serializable
{
    private NonBlockingHashMap<String, IColumn> map_;
    private ConcurrentSkipListSet<IColumn> sortedSet_;
    private Comparator<IColumn> columnComparator_;

    EfficientBidiMap(Comparator<IColumn> columnComparator)
    {
        this(new NonBlockingHashMap<String, IColumn>(), new ConcurrentSkipListSet<IColumn>(columnComparator), columnComparator);
    }

    private EfficientBidiMap(NonBlockingHashMap<String, IColumn> map, ConcurrentSkipListSet<IColumn> set, Comparator<IColumn> comparator)
    {
    	map_ = map;
    	sortedSet_ = set;
    	columnComparator_ = comparator;
    }

    public Comparator<IColumn> getComparator()
    {
    	return columnComparator_;
    }

    public void put(String key, IColumn column)
    {
        IColumn oldColumn = map_.put(key, column);
        if (oldColumn != null)
            sortedSet_.remove(oldColumn);
        sortedSet_.add(column);
    }

    public IColumn get(String key)
    {
        return map_.get(key);
    }

    public SortedSet<IColumn> getSortedColumns()
    {
    	return sortedSet_;
    }

    public Map<String, IColumn> getColumns()
    {
        return map_;
    }

    public int size()
    {
    	return map_.size();
    }

    public void remove (String columnName)
    {
    	sortedSet_.remove(map_.get(columnName));
    	map_.remove(columnName);
    }
    void clear()
    {
    	map_.clear();
    	sortedSet_.clear();
    }

    ColumnComparatorFactory.ComparatorType getComparatorType()
	{
		return ((AbstractColumnComparator)columnComparator_).getComparatorType();
	}

    EfficientBidiMap cloneMe()
    {
    	return new EfficientBidiMap((NonBlockingHashMap<String, IColumn>) map_.clone(), sortedSet_.clone(), columnComparator_);
    }
}


