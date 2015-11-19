
/**
 * Execution plan for setting a specific column in a Simple/Super column family.
 *   SET table.standard_cf[<rowKey>][<columnKey>] = <value>;
 *   SET table.super_cf[<rowKey>][<superColumnKey>][<columnKey>] = <value>; 
 */
public class SetUniqueKey extends DMLPlan
{
    private final static Logger logger_ = Logger.getLogger(SetUniqueKey.class);    
    private CFMetaData cfMetaData_;
    private OperandDef rowKey_;
    private OperandDef superColumnKey_;
    private OperandDef columnKey_;
    private OperandDef value_;

    /**
     *  Construct an execution plan for setting a column in a simple column family
     * 
     *   SET table.standard_cf[<rowKey>][<columnKey>] = <value>;
     */
    public SetUniqueKey(CFMetaData cfMetaData, OperandDef rowKey, OperandDef columnKey, OperandDef value)
    {
        cfMetaData_     = cfMetaData;
        rowKey_         = rowKey;
        columnKey_      = columnKey;
        superColumnKey_ = null;
        value_          = value;
    }
    
    /**
     * Construct execution plan for setting a column in a super column family.
     * 
     *  SET table.super_cf[<rowKey>][<superColumnKey>][<columnKey>] = <value>;
     */
    public SetUniqueKey(CFMetaData cfMetaData, OperandDef rowKey, OperandDef superColumnKey, OperandDef columnKey, OperandDef value)
    {
        cfMetaData_     = cfMetaData;
        rowKey_         = rowKey;
        superColumnKey_ = superColumnKey;
        columnKey_      = columnKey;
        value_          = value;
    }

    public CqlResult execute()
    {
        String columnKey = (String)(columnKey_.get());
        String columnFamily_column;

        if (superColumnKey_ != null)
        {
            String superColumnKey = (String)(superColumnKey_.get());
            columnFamily_column = cfMetaData_.cfName + ":" + superColumnKey + ":" + columnKey;
        }
        else
        {
            columnFamily_column = cfMetaData_.cfName + ":" + columnKey;
        }

        try
        {
            RowMutation rm = new RowMutation(cfMetaData_.tableName, (String)(rowKey_.get()));
            rm.add(columnFamily_column, ((String)value_.get()).getBytes(), System.currentTimeMillis());
            StorageProxy.insert(rm);
        }
        catch (Exception e)
        {
            logger_.error(LogUtil.throwableToString(e));
            throw new RuntimeException(RuntimeErrorMsg.GENERIC_ERROR.getMsg());            
        }
        return null;
    }

    public String explainPlan()
    {
        return
            String.format("%s Column Family: Unique Key SET: \n" +
                "   Table Name:     %s\n" +
                "   Column Famly:   %s\n" +
                "   RowKey:         %s\n" +
                "%s" +
                "   ColumnKey:      %s\n" +
                "   Value:          %s\n",
                cfMetaData_.columnType,
                cfMetaData_.tableName,
                cfMetaData_.cfName,
                rowKey_.explain(),
                (superColumnKey_ == null) ? "" : "   SuperColumnKey: " + superColumnKey_.explain() + "\n",                
                columnKey_.explain(),
                value_.explain());
    }
}