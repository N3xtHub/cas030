public class LogUtil
{

    public LogUtil()
    {
    }

    public static void init()
    {
        //BasicConfigurator.configure();
        String file = System.getProperty("storage-config");
        file += System.getProperty("file.separator") + "log4j.properties";
        PropertyConfigurator.configure(file);
    }

    public static Logger getLogger(String name)
    {
        return Logger.getLogger(name);
    }
    
    public static String stackTrace(Throwable e)
    {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        return sw.toString();
    }

    public static String getTimestamp()
    {
        Date date = new Date();
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        return df.format(date);
    }
    
    public static String throwableToString(Throwable e)
    {
        StringBuffer sbuf = new StringBuffer("");
        String trace = stackTrace(e);
        sbuf.append((new StringBuilder()).append("Exception was generated at : ").append(getTimestamp()).append(" on thread ").append(Thread.currentThread().getName()).toString());
        sbuf.append(System.getProperty("line.separator"));
        String message = e.getMessage();
        if(message != null)
            sbuf.append(message);
        sbuf.append(System.getProperty("line.separator"));
        sbuf.append(trace);
        return sbuf.toString();
    }

    public static String getLogMessage(String message)
    {
        StringBuffer sbuf = new StringBuffer((new StringBuilder()).append("Log started at : ").append(getTimestamp()).toString());
        sbuf.append(System.getProperty("line.separator"));
        sbuf.append(message);
        return sbuf.toString();
    }

    public static void setLogLevel(String logger, String level)
    {        
        Logger loggerObj = LogManager.getLogger(logger);
        if(null == loggerObj)
            return;
        level = level.toUpperCase();
        if(level.equals("DEBUG"))
            loggerObj.setLevel(Level.DEBUG);
        else
        if(level.equals("ERROR"))
            loggerObj.setLevel(Level.ERROR);
        else
        if(level.equals("FATAL"))
            loggerObj.setLevel(Level.FATAL);
        else
        if(level.equals("INFO"))
            loggerObj.setLevel(Level.INFO);
        else
        if(level.equals("OFF"))
            loggerObj.setLevel(Level.OFF);
        else
        if(level.equals("WARN"))
            loggerObj.setLevel(Level.WARN);
        else
            loggerObj.setLevel(Level.ALL);
    }
}
