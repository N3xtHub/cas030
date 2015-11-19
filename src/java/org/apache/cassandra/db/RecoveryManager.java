
public class RecoveryManager
{
    private static RecoveryManager instance_;
    private static Logger logger_ = Logger.getLogger(RecoveryManager.class);
    
    synchronized static RecoveryManager instance() throws IOException
    {
        if ( instance_ == null )
            instance_ = new RecoveryManager();
        return instance_;
    }

    public static File[] getListofCommitLogs()
    {
        String directory = DatabaseDescriptor.getLogFileLocation();
        File file = new File(directory);
        File[] files = file.listFiles();
        return files;
    }
    
    public static Map<String, List<File>> getListOFCommitLogsPerTable()
    {
        File[] files = getListofCommitLogs();
        /* Maintains a mapping of table name to a list of commit log files */
        Map<String, List<File>> tableToCommitLogs = new HashMap<String, List<File>>();
        
        for (File f : files)
        {
            String table = CommitLog.getTableName(f.getName());
            List<File> clogs = tableToCommitLogs.get(table);
            if ( clogs == null )
            {
                clogs = new ArrayList<File>();
                tableToCommitLogs.put(table, clogs);
            }
            clogs.add(f);
        }
        return tableToCommitLogs;
    }
    
    public void doRecovery() throws IOException
    {
        File[] files = getListofCommitLogs();
        Map<String, List<File>> tableToCommitLogs = getListOFCommitLogsPerTable();
        recoverEachTable(tableToCommitLogs);
        FileUtils.delete(files);
    }
    
    private void recoverEachTable(Map<String, List<File>> tableToCommitLogs) throws IOException
    {
        Comparator<File> fCmp = new FileUtils.FileComparator();
        Set<String> tables = tableToCommitLogs.keySet();
        for ( String table : tables )
        {
            List<File> clogs = tableToCommitLogs.get(table);
            Collections.sort(clogs, fCmp);
            CommitLog clog = new CommitLog(table, true);
            clog.recover(clogs);
        }
    }
    
    public static void main(String[] args) throws Throwable
    {
        long start = System.currentTimeMillis();
        RecoveryManager rm = RecoveryManager.instance();
        rm.doRecovery();  
        logger_.debug( "Time taken : " + (System.currentTimeMillis() - start) + " ms.");
    }
}
