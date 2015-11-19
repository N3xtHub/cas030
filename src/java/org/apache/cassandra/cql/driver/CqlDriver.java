

// Server side driver class for CQL
public class CqlDriver 
{
    private final static Logger logger_ = Logger.getLogger(CqlDriver.class);

    // Execute a CQL Statement 
    public static CqlResult executeQuery(String query)
    {
        CqlCompiler compiler = new CqlCompiler();

        try
        {
            logger_.debug("Compiling CQL query ...");
            Plan plan = compiler.compileQuery(query);
            if (plan != null)
            {
                logger_.debug("Executing CQL query ...");            
                return plan.execute();
            }
        }
        catch (Exception e)
        {
            CqlResult result = new CqlResult(null);
            result.errorTxt = e.getMessage();           

            Class<? extends Exception> excpClass = e.getClass();
            if ((excpClass != SemanticException.class)
                && (excpClass != ParseException.class)
                && (excpClass != RuntimeException.class))
            {
                result.errorTxt = "CQL Internal Error: " + result.errorTxt;
                result.errorCode = 1; // failure
                logger_.error(LogUtil.throwableToString(e));
            }

            return result;
        }

        return null;
    }
}
