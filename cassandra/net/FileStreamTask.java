
class FileStreamTask implements Runnable
{
    private static Logger logger_ = Logger.getLogger( FileStreamTask.class );
    
    private String file_;
    private long startPosition_;
    private long total_;
    private EndPoint from_;
    private EndPoint to_;
    
    FileStreamTask(String file, long startPosition, long total, EndPoint from, EndPoint to)
    {
        file_ = file;
        startPosition_ = startPosition;
        total_ = total;
        from_ = from;
        to_ = to;
    }
    
    public void run()
    {
        TcpConnection connection = null;
        try
        {                        
            connection = new TcpConnection(from_, to_);
            File file = new File(file_);             
            connection.stream(file, startPosition_, total_);
            MessagingService.setStreamingMode(false);
            logger_.debug("Done streaming " + file);
        }            
        catch ( SocketException se )
        {                        
            logger_.info(LogUtil.throwableToString(se));
        }
        catch ( IOException e )
        {
            logConnectAndIOException(e, connection);
        }
        catch (Throwable th)
        {
            logger_.warn(LogUtil.throwableToString(th));
        }        
    }
    
    private void logConnectAndIOException(IOException ex, TcpConnection connection)
    {                    
        if ( connection != null )
        {
            connection.errorClose();
        }
        logger_.info(LogUtil.throwableToString(ex));
    }
}
