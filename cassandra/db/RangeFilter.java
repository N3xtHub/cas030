
/**
 * Filters columns to satisfy colmin <= colname <= colmax
 *
 */
public class RangeFilter implements IFilter
{
    private final String colMin_;
    private final String colMax_;
    private boolean isDone_;
    int count_;

    RangeFilter(String colMin, String colMax)
    {
        colMin_ = colMin;
        colMax_ = colMax;
        isDone_ = false;
        count_ = -1;
    }
    
    RangeFilter(String colMin, String colMax, int count)
    {
        colMin_ = colMin;
        colMax_ = colMax;
        isDone_ = false;
        count_ = count;
    }

    public ColumnFamily filter(String cfName, ColumnFamily cf)
    {
        if (cf == null)
            return null;

        if (count_ == 0)
        {
            isDone_ = true;
            return null;
        }

        ColumnFamily filteredColumnFamily = new ColumnFamily(cfName, cf.type());

        Collection<IColumn> columns = cf.getAllColumns();
        for (IColumn c : columns)
        {
            if (c.name().compareTo(colMin_) >= 0
                    && c.name().compareTo(colMax_) <= 0)
            {
                filteredColumnFamily.addColumn(c);
                if (count_ > 0)
                    count_--;
                if (count_==0)
                {
                    isDone_ = true;
                    break;
                }
            }
        }
        return filteredColumnFamily;
    }

    public IColumn filter(IColumn column, DataInputStream dis)
            throws IOException
    {
        if (column == null || isDone_)
            return null;

        if (column.name().compareTo(colMin_) >= 0
                && column.name().compareTo(colMax_) <= 0)
        {
            if (count_ > 0)
                count_--;
            if (count_ == 0)
                isDone_ = true;
            return column;
        } else
        {
            return null;
        }
    }

    public boolean isDone()
    {
        return isDone_;
    }

    public DataInputBuffer next(String key, String cf, SSTable ssTable)
            throws IOException
    {
        return ssTable.next(key, cf);
    }

}
