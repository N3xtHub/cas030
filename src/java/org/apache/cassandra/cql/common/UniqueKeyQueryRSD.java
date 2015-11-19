
/**
 * A Row Source Defintion (RSD) for looking up a unique column within a column family.
 */
public class UniqueKeyQueryRSD extends RowSourceDef
{
    private final static Logger logger_ = Logger.getLogger(UniqueKeyQueryRSD.class);    
    private CFMetaData cfMetaData_;
    private OperandDef rowKey_;
    private OperandDef superColumnKey_;
    private OperandDef columnKey_;

    // super column family
    public UniqueKeyQueryRSD(CFMetaData cfMetaData, OperandDef rowKey, OperandDef superColumnKey, OperandDef columnKey)
    {
        cfMetaData_     = cfMetaData;
        rowKey_         = rowKey;
        superColumnKey_ = superColumnKey;
        columnKey_      = columnKey;
    }

    // simple column family
    public UniqueKeyQueryRSD(CFMetaData cfMetaData, OperandDef rowKey, OperandDef columnKey)
    {
        cfMetaData_ = cfMetaData;
        rowKey_     = rowKey;
        columnKey_  = columnKey;
        superColumnKey_ = null;
    }

    // specific column lookup
    public List<Map<String,String>> getRows() throws RuntimeException
    {
        String columnKey = (String)(columnKey_.get());
        String columnFamily_column;
        String superColumnKey = null;

        if (superColumnKey_ != null)
        {
            superColumnKey = (String)(superColumnKey_.get());
            columnFamily_column = cfMetaData_.cfName + ":" + superColumnKey + ":" + columnKey;
        }
        else
        {
            columnFamily_column = cfMetaData_.cfName + ":" + columnKey;
        }

        Row row = null;
        try
        {
            String key = (String)(rowKey_.get());
            ReadCommand readCommand = new ColumnReadCommand(cfMetaData_.tableName, key, columnFamily_column);
            row = StorageProxy.readProtocol(readCommand, StorageService.ConsistencyLevel.WEAK);
        }
        catch (Exception e)
        {
            logger_.error(LogUtil.throwableToString(e));
            throw new RuntimeException(RuntimeErrorMsg.GENERIC_ERROR.getMsg());
        }

        if (row != null)
        {
            Map<String, ColumnFamily> cfMap = row.getColumnFamilyMap();
            if (cfMap != null && cfMap.size() > 0)
            {
                ColumnFamily cfamily = cfMap.get(cfMetaData_.cfName);
                if (cfamily != null)
                {
                    Collection<IColumn> columns = null;
                    if (superColumnKey_ != null)
                    {
                        // this is the super column case 
                        IColumn column = cfamily.getColumn(superColumnKey);
                        if (column != null)
                            columns = column.getSubColumns();
                    }
                    else
                    {
                        columns = cfamily.getAllColumns();
                    }
                    
                    if (columns != null && columns.size() > 0)
                    {
                        if (columns.size() > 1)
                        {
                            // We are looking up by a rowKey & columnKey. There should
                            // be at most one column that matches. If we find more than
                            // one, then it is an internal error.
                            throw new RuntimeException(RuntimeErrorMsg.INTERNAL_ERROR.getMsg("Too many columns found for: " + columnKey));
                        }
                        for (IColumn column : columns)
                        {
                            List<Map<String, String>> rows = new LinkedList<Map<String, String>>();

                            Map<String, String> result = new HashMap<String, String>();
                            result.put(cfMetaData_.n_columnKey, column.name());
                            result.put(cfMetaData_.n_columnValue, new String(column.value()));
                            result.put(cfMetaData_.n_columnTimestamp, Long.toString(column.timestamp()));
                            
                            rows.add(result);
                                
                            // at this point, due to the prior checks, we are guaranteed that
                            // there is only one item in "columns".
                            return rows;
                        }
                        return null;
                    }
                }
            }
        }
        throw new RuntimeException(RuntimeErrorMsg.NO_DATA_FOUND.getMsg());
    }

    public String explainPlan()
    {
        return String.format("%s Column Family: Unique Key Query: \n" +
                "   Table Name:     %s\n" +
                "   Column Famly:   %s\n" +
                "   RowKey:         %s\n" +
                "%s" +
                "   ColumnKey:      %s",
                cfMetaData_.columnType,
                cfMetaData_.tableName,
                cfMetaData_.cfName,
                rowKey_.explain(),
                (superColumnKey_ == null) ? "" : "   SuperColumnKey: " + superColumnKey_.explain() + "\n",                
                columnKey_.explain());
    }
}