
/**
 * BindOperand: 
 * Represents a bind variable in the CQL statement. Lives
 * in the shared execution plan.
 */
public class BindOperand implements OperandDef 
{
    int bindIndex_;  // bind position

    public BindOperand(int bindIndex)
    {
        bindIndex_ = bindIndex;
    }

    public Object get()
    {
        // TODO: Once bind variables are supported, the get() will extract
        // the value of the bind at position "bindIndex_" from the execution
        // context.
        throw new RuntimeException(RuntimeErrorMsg.IMPLEMENTATION_RESTRICTION
                                   .getMsg("bind params not yet supported"));
    }
    
    public String explain()
    {
        return "Bind #: " + bindIndex_;
    }

};