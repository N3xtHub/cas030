
public class ColumnComparatorFactory
{
    public static enum ComparatorType
    {
        NAME,
        TIMESTAMP
    }

    private static Comparator<IColumn> nameComparator_ = new ColumnNameComparator();
    private static Comparator<IColumn> timestampComparator_ = new ColumnTimestampComparator();

    public static Comparator<IColumn> getComparator(ComparatorType comparatorType)
    {
        Comparator<IColumn> columnComparator = timestampComparator_;

        switch (comparatorType)
        {
            case NAME:
                columnComparator = nameComparator_;
                break;

            case TIMESTAMP:

            default:
                columnComparator = timestampComparator_;
                break;
        }

        return columnComparator;
    }

    public static Comparator<IColumn> getComparator(int comparatorTypeInt)
    {
        ComparatorType comparatorType = ComparatorType.NAME;

        if (comparatorTypeInt == ComparatorType.NAME.ordinal())
        {
            comparatorType = ComparatorType.NAME;
        }
        else if (comparatorTypeInt == ComparatorType.TIMESTAMP.ordinal())
        {
            comparatorType = ComparatorType.TIMESTAMP;
        }
        return getComparator(comparatorType);
    }

}

abstract class AbstractColumnComparator implements Comparator<IColumn>, Serializable
{
    protected ColumnComparatorFactory.ComparatorType comparatorType_;

    public AbstractColumnComparator(ColumnComparatorFactory.ComparatorType comparatorType)
    {
        comparatorType_ = comparatorType;
    }

    ColumnComparatorFactory.ComparatorType getComparatorType()
    {
        return comparatorType_;
    }
}

class ColumnTimestampComparator extends AbstractColumnComparator
{
    ColumnTimestampComparator()
    {
        super(ColumnComparatorFactory.ComparatorType.TIMESTAMP);
    }

    /* if the time-stamps are the same then sort by names */
    public int compare(IColumn column1, IColumn column2)
    {
        assert column1.getClass() == column2.getClass();
        /* inverse sort by time to get hte latest first */
        long result = column2.timestamp() - column1.timestamp();
        int finalResult = 0;
        if (result == 0)
        {
            result = column1.name().compareTo(column2.name());
        }
        if (result > 0)
        {
            finalResult = 1;
        }
        if (result < 0)
        {
            finalResult = -1;
        }
        return finalResult;
    }
}

class ColumnNameComparator extends AbstractColumnComparator
{
    ColumnNameComparator()
    {
        super(ColumnComparatorFactory.ComparatorType.NAME);
    }

    /* if the names are the same then sort by time-stamps */
    public int compare(IColumn column1, IColumn column2)
    {
        assert column1.getClass() == column2.getClass();
        long result = column1.name().compareTo(column2.name());
        int finalResult = 0;
        if (result == 0 && (column1 instanceof Column))
        {
            /* inverse sort by time to get the latest first */
            result = column2.timestamp() - column1.timestamp();
        }
        if (result > 0)
        {
            finalResult = 1;
        }
        if (result < 0)
        {
            finalResult = -1;
        }
        return finalResult;
    }
}
