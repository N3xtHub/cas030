
public class PreLoad
{
	
	private static long siesta_ = 2*60*1000;
	private static Logger logger_ = Logger.getLogger( Loader.class );
    private StorageService storageService_;
	
	public PreLoad(StorageService storageService)
    {
        storageService_ = storageService;
    }
    /*
     * This method loads all the keys into a special column family 
     * called "RecycleBin". This column family is used for temporary
     * processing of data and then can be recycled. The idea is that 
     * after the load is complete we have all the keys in the system.
     * Now we force a compaction and examine the single Index file 
     * that is generated to determine how the nodes need to relocate
     * to be perfectly load balanced.
     * 
     *  param @ rootDirectory - rootDirectory at which the parsing begins.
     *  param @ table - table that will be populated.
     *  param @ cfName - name of the column that will be populated. This is 
     *  passed in so that we do not unncessary allocate temporary String objects.
    */
    private void preParse(String rootDirectory, String table, String cfName) throws Throwable
    {        
        BufferedReader bufReader = new BufferedReader(new InputStreamReader(
                new FileInputStream(rootDirectory)), 16 * 1024 * 1024);
        String line = null;
        while ((line = bufReader.readLine()) != null)
        {
                String userId = line;
                RowMutation rm = new RowMutation(table, userId);
                rm.add(cfName, userId.getBytes(), 0);
                rm.apply();
        }
    }
    
    void run(String userFile) 
    {
        String table = DatabaseDescriptor.getTables().get(0);
        String cfName = Table.recycleBin_ + ":" + "Keys";
        /* populate just the keys. */
        preParse(userFile, table, cfName);
        /* dump the memtables */
        Table.open(table).flush(false);
        /* force a compaction of the files. */
        Table.open(table).forceCompaction(null, null,null);
        
        /*
         * This is a hack to let everyone finish. Just sleep for
         * a couple of minutes. 
        */
        logger_.info("Taking a nap after forcing a compaction ...");
        Thread.sleep(PreLoad.siesta_);
        
        /* Figure out the keys in the index file to relocate the node */
        List<String> ssTables = Table.open(table).getAllSSTablesOnDisk();
        /* Load the indexes into memory */
        for ( String df : ssTables )
        {
        	SSTable ssTable = new SSTable(df, StorageService.getPartitioner());
        	ssTable.close();
        }
        /* We should have only one file since we just compacted. */        
        List<String> indexedKeys = SSTable.getIndexedKeys();        
        storageService_.relocate(indexedKeys.toArray( new String[0]) );
        
        /*
         * This is a hack to let everyone relocate and learn about
         * each other. Just sleep for a couple of minutes. 
        */
        logger_.info("Taking a nap after relocating ...");
        Thread.sleep(PreLoad.siesta_);  
        
        /* 
         * Do the cleanup necessary. Delete all commit logs and
         * the SSTables and reset the load state in the StorageService. 
        */
        SSTable.delete(ssTables.get(0));
        logger_.info("Finished all the requisite clean up ...");
    }

	public static void main(String[] args)
	{

		LogUtil.init();
        StorageService s = StorageService.instance();
        s.start();
        PreLoad preLoad = new PreLoad(s);
        preLoad.run(args[0]);
	}

}
