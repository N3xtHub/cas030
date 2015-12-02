
/**
 * ConstantOperand:
 * Represents a literal/constant operand in the CQL statement.
 * Lives as part of the shared execution plan.
 */
public class ConstantOperand implements OperandDef 
{
    Object value_;
    public ConstantOperand(Object value)
    {
        value_ = value;
    }

    public Object get()
    {
        return value_;
    }

    public String explain()
    {
        return "Constant: '" + value_ + "'";
    }
};