
/**
 * List of error messages thrown by the CQL Compiler
 **/

public enum CompilerErrorMsg
{
    // Error messages with String.format() style format specifiers
    GENERIC_ERROR("CQL Compilation Error"),
    INTERNAL_ERROR("CQL Compilation Internal Error"),
    INVALID_TABLE("Table '%s' does not exist"),
    INVALID_COLUMN_FAMILY("Column Family '%s' not found in table '%s'"),
    TOO_MANY_DIMENSIONS("Too many dimensions specified for %s Column Family"),
    INVALID_TYPE("Expression is of invalid type")
    ;

    private String mesg;
    CompilerErrorMsg(String mesg)
    {
        this.mesg = mesg;
    }
    
    private static String getLineAndPosition(CommonTree tree) 
    {
        if (tree.getChildCount() == 0)
        {
            return tree.getToken().getLine() + ":" + tree.getToken().getCharPositionInLine();
        }
        return getLineAndPosition((CommonTree)tree.getChild(0));
    }

    // Returns the formatted error message. Derives line/position information
    // from the "tree" node passed in.
    public String getMsg(CommonTree tree, Object... args)
    {
        // We allocate another array since we want to add line and position as an 
        // implicit additional first argument to pass on to String.format.
        Object[] newArgs = new Object[args.length + 1];
        newArgs[0] = getLineAndPosition(tree);
        System.arraycopy(args, 0, newArgs, 1, args.length);

        // note: mesg itself might contain other format specifiers...
        return String.format("line %s " + mesg, newArgs);
    } 

    String getMsg()
    {
        return mesg;
    } 
}
