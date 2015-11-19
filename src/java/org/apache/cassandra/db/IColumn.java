
public interface IColumn
{
    public static short UtfPrefix_ = 2;
    public boolean isMarkedForDelete();
    public long getMarkedForDeleteAt();
    public String name();
    public int size();
    public int serializedSize();
    public long timestamp();
    public long timestamp(String key);
    public byte[] value();
    public byte[] value(String key);
    public Collection<IColumn> getSubColumns();
    public IColumn getSubColumn(String columnName);
    public void addColumn(IColumn column);
    public IColumn diff(IColumn column);
    public int getObjectCount();
    public byte[] digest();
    public int getLocalDeletionTime(); // for tombstone GC, so int is sufficient granularity
}
