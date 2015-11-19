

/**
 * This class is used to loop through a retrieved column family
 * to get all columns in Iterator style. Usage is as follows:
 * Scanner scanner = new Scanner("table");
 * scanner.fetchColumnfamily(key, "column-family");
 * 
 * while ( scanner.hasNext() )
 * {
 *     Column column = scanner.next();
 *     // Do something with the column
 * }
 */

public class Scanner implements IScanner<IColumn>
{
    /* Table over which we are scanning. */
    private String table_; 
    /* Iterator when iterating over the columns of a given key in a column family */
    private Iterator<IColumn> columnIt_;
        
    public Scanner(String table)
    {
        table_ = table;
    }
    
    /**
     * Fetch the columns associated with this key for the specified column family.
     * This method basically sets up an iterator internally and then provides an 
     * iterator like interface to iterate over the columns.
     * @param key key we are interested in.
     * @param cf column family we are interested in.
     * @throws IOException
     */
    public void fetch(String key, String cf) throws IOException
    {        
        if ( cf != null )
        {
            Table table = Table.open(table_);
            ColumnFamily columnFamily = table.get(key, cf);
            if ( columnFamily != null )
            {
                Collection<IColumn> columns = columnFamily.getAllColumns();            
                columnIt_ = columns.iterator();
            }
        }
    }        
    
    public boolean hasNext() throws IOException
    {
        return columnIt_.hasNext();
    }
    
    public IColumn next()
    {
        return columnIt_.next();
    }
    
    public void close() throws IOException
    {
        throw new UnsupportedOperationException("This operation is not supported in the Scanner");
    }
}
