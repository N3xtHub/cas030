
/**
 * Abstract class representing the shared execution plan for a CQL
 * statement (query or DML operation).
 * 
 */
public abstract class Plan
{
    public abstract CqlResult execute();
    public abstract String explainPlan();
}
