
/**
 * A Row Source Defintion (RSD) for doing a super column range query on a Super Column Family.
 */
public class SuperColumnRangeQueryRSD extends RowSourceDef
{
    private final static Logger logger_ = Logger.getLogger(SuperColumnRangeQueryRSD.class);
    private CFMetaData cfMetaData_;
    private OperandDef rowKey_;
    private OperandDef superColumnKey_;
    private int        offset_;
    private int        limit_;

    /**
     * Set up a range query on super column map in a super column family.
     * The super column map is identified by the rowKey.
     * 
     * Note: "limit" of -1 is the equivalent of no limit.
     *       "offset" specifies the number of rows to skip.
     *        An offset of 0 implies from the first row.
     */
    public SuperColumnRangeQueryRSD(CFMetaData cfMetaData, OperandDef rowKey, int offset, int limit)
    {
        cfMetaData_     = cfMetaData;
        rowKey_         = rowKey;
        offset_         = offset;
        limit_          = limit;
    }

    public List<Map<String,String>> getRows()
    {
        Row row = null;
        try
        {
            String key = (String)(rowKey_.get());
            ReadCommand readCommand = new SliceReadCommand(cfMetaData_.tableName, key, cfMetaData_.cfName, offset_, limit_);
            row = StorageProxy.readProtocol(readCommand, StorageService.ConsistencyLevel.WEAK);
        }
        catch (Exception e)
        {
            logger_.error(LogUtil.throwableToString(e));
            throw new RuntimeException(RuntimeErrorMsg.GENERIC_ERROR.getMsg());
        }

        List<Map<String, String>> rows = new LinkedList<Map<String, String>>();
        if (row != null)
        {
            Map<String, ColumnFamily> cfMap = row.getColumnFamilyMap();
            if (cfMap != null && cfMap.size() > 0)
            {
                ColumnFamily cfamily = cfMap.get(cfMetaData_.cfName);
                if (cfamily != null)
                {
                    Collection<IColumn> columns = cfamily.getAllColumns();
                    if (columns != null && columns.size() > 0)
                    {
                        for (IColumn column : columns)
                        {
                            Collection<IColumn> subColumns = column.getSubColumns();
                            for( IColumn subColumn : subColumns )
                            {
                               Map<String, String> result = new HashMap<String, String>();
                               result.put(cfMetaData_.n_superColumnKey, column.name());                               
                               result.put(cfMetaData_.n_columnKey, subColumn.name());
                               result.put(cfMetaData_.n_columnValue, new String(subColumn.value()));
                               result.put(cfMetaData_.n_columnTimestamp, Long.toString(subColumn.timestamp()));
                               rows.add(result);
                            }
                        }
                    }
                }
            }
        }
        return rows;
    }

    public String explainPlan()
    {
        return String.format("%s Column Family: Super Column Range Query: \n" +
                "  Table Name:       %s\n" +
                "  Column Family:    %s\n" +
                "  RowKey:           %s\n" +
                "  Offset:           %d\n" +
                "  Limit:            %d\n" +
                "  Order By:         %s",
                cfMetaData_.columnType,
                cfMetaData_.tableName,
                cfMetaData_.cfName,
                rowKey_.explain(),
                offset_, limit_,
                cfMetaData_.indexProperty_);
    }
}