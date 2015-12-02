
/**
 * This class represents the execution plan for Query (data retrieval) statement. 
 */
public class QueryPlan extends Plan
{
    private final static Logger logger_ = Logger.getLogger(QueryPlan.class);    

    public RowSourceDef root;    // the root of the row source tree

    public QueryPlan(RowSourceDef rwsDef)
    {
        root = rwsDef;
    }
    
    public CqlResult execute()
    {
        if (root != null)
        {
            return new CqlResult(root.getRows());
        }
        else
            logger_.error("No rowsource to execute");
        return null;
    }
    
    public String explainPlan()
    {
        return root.explainPlan();
    }
    
}
