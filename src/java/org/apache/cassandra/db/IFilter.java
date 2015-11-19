

public interface IFilter
{
	public boolean isDone();
	public ColumnFamily filter(String cfName, ColumnFamily cf);
    public IColumn filter(IColumn column, DataInputStream dis) throws IOException;
    public DataInputBuffer next(String key, String cf, SSTable ssTable) throws IOException;
}
