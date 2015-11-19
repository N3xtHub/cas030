

/**
 * List of error messages thrown by CQL's Execution Layer
 **/
public enum RuntimeErrorMsg
{
    // Error messages with String.format() style format specifiers
    GENERIC_ERROR("CQL Execution Error"),
    INTERNAL_ERROR("CQL Internal Error: %s"),
    IMPLEMENTATION_RESTRICTION("Implementation Restriction: %s"),
    NO_DATA_FOUND("No data found")
    ;

    private String mesg;
    RuntimeErrorMsg(String mesg) 
    {
        this.mesg = mesg;
    }

    // Returns the formatted error message. 
    public String getMsg(Object... args)
    {
        // note: mesg itself might contain other format specifiers...
        return String.format(mesg, args);
    }
}
