
/**
 * OperandDef:
 *
 * The abstract definition of an operand (i.e. data item) in 
 * CQL compiler/runtime. Examples, include a Constant operand
 * or a Bind operand. This is the part of an operand definition
 * that lives in the share-able execution plan.
 */
public abstract interface OperandDef
{
    public abstract Object get();
    public abstract String explain();
};