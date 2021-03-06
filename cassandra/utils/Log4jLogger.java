
/**
 * Log4j configurations may change while the application is running, 
 * potentially invalidating a logger's appender(s).  This is a convinience
 * class to wrap logger calls so that a logger is always explicitly 
 * invoked.
 */


public class Log4jLogger {
    
    private String name_ = null;
    
    public Log4jLogger(String name){
        name_ = name;
    }
    
    public void debug(Object arg){ 
        LogUtil.getLogger(name_).debug(LogUtil.getTimestamp() + " - " + arg);
    }    
    public void info(Object arg){
        LogUtil.getLogger(name_).info(LogUtil.getTimestamp() + " - " + arg);
    }
    public void warn(Object arg){
        LogUtil.getLogger(name_).warn(LogUtil.getTimestamp() + " - " + arg);
    }
    public void error(Object arg){
        LogUtil.getLogger(name_).error(LogUtil.getTimestamp() + " - " + arg);
    }
    public void fatal(Object arg){
        LogUtil.getLogger(name_).fatal(LogUtil.getTimestamp() + " - " + arg);
    } 
}
