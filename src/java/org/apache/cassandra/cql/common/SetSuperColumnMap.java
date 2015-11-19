
/**
 * Execution plan for batch setting a set of super columns in a Super column family.
  *   SET table.super_cf[<rowKey>] = <superColumnMapExpr>;
 */
public class SetSuperColumnMap extends DMLPlan
{
    private final static Logger logger_ = Logger.getLogger(SetUniqueKey.class);    
    private CFMetaData         cfMetaData_;
    private OperandDef         rowKey_;
    private SuperColumnMapExpr superColumnMapExpr_;

    /**
     *  construct an execution plan node to batch set a bunch of super columns in a 
     *  super column family.
     *
     *    SET table.super_cf[<rowKey>] = <superColumnMapExpr>;
     */
    public SetSuperColumnMap(CFMetaData cfMetaData, OperandDef rowKey, SuperColumnMapExpr superColumnMapExpr)
    {
        cfMetaData_         = cfMetaData;
        rowKey_             = rowKey;
        superColumnMapExpr_ = superColumnMapExpr;
    }
    
    public CqlResult execute()
    {
        try
        {
            RowMutation rm = new RowMutation(cfMetaData_.tableName, (String)(rowKey_.get()));
            long time = System.currentTimeMillis();

            for (Pair<OperandDef, ColumnMapExpr> superColumn : superColumnMapExpr_)
            {
                OperandDef    superColumnKey = superColumn.getFirst();
                ColumnMapExpr columnMapExpr = superColumn.getSecond();
                
                String columnFamily_column = cfMetaData_.cfName + ":" + (String)(superColumnKey.get()) + ":";
                
                for (Pair<OperandDef, OperandDef> entry : columnMapExpr)
                {
                    OperandDef columnKey = entry.getFirst();
                    OperandDef value     = entry.getSecond();
                    rm.add(columnFamily_column + (String)(columnKey.get()), ((String)value.get()).getBytes(), time);
                }
            }
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
        StringBuffer sb = new StringBuffer();
        
        String prefix =
            String.format("%s Column Family: Batch SET a set of Super Columns: \n" +
            "   Table Name:     %s\n" +
            "   Column Famly:   %s\n" +
            "   RowKey:         %s\n",
            cfMetaData_.columnType,
            cfMetaData_.tableName,
            cfMetaData_.cfName,
            rowKey_.explain());

        for (Pair<OperandDef, ColumnMapExpr> superColumn : superColumnMapExpr_)
        {
            OperandDef    superColumnKey = superColumn.getFirst();
            ColumnMapExpr columnMapExpr = superColumn.getSecond();

            for (Pair<OperandDef, OperandDef> entry : columnMapExpr)
            {
                OperandDef columnKey = entry.getFirst();
                OperandDef value     = entry.getSecond();
                sb.append(String.format("     SuperColumnKey: %s\n" + 
                                        "     ColumnKey:      %s\n" +
                                        "     Value:          %s\n",
                                        superColumnKey.explain(),
                                        columnKey.explain(),
                                        value.explain()));
            }
        }
        
        return prefix + sb.toString();
    }
}