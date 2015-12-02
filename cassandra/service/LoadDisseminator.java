
class LoadDisseminator extends TimerTask
{
    private final static Logger logger_ = Logger.getLogger(LoadDisseminator.class);
    protected final static String loadInfo_= "LOAD-INFORMATION";
    
    public void run()
    {
        long diskSpace = FileUtils.getUsedDiskSpace();                
        String diskUtilization = FileUtils.stringifyFileSize(diskSpace);

        Gossiper.instance().addApplicationState(LoadDisseminator.loadInfo_, new ApplicationState(diskUtilization));
    }
}
