
/**
 * This class manages the persistence of checksums and keeps
 * them in memory. It maintains a mapping of data files on
 * disk to their corresponding checksum files. It is also
 * loads the checksums in memory on start up.
 */
public class ChecksumManager
{    
    private static Logger logger_ = Logger.getLogger(ChecksumManager.class);
    /* Keeps a mapping of checksum manager instances to data file */
    private static Map<String, ChecksumManager> chksumMgrs_ = new HashMap<String, ChecksumManager>();
    private static Lock lock_ = new ReentrantLock();
    private static final String checksumPrefix_ = "Checksum-";
    private static final int bufferSize_ = 8*1024*1024;
    private static final long chunkMask_ = 0x00000000FFFFFFFFL;
    private static final long fileIdMask_ = 0x7FFFFFFF00000000L;
    /* Map where checksums are cached. */
    private static Map<Long, Long> chksums_ = new HashMap<Long, Long>();

    public static ChecksumManager instance(String dataFile) throws IOException
    {
        ChecksumManager chksumMgr = chksumMgrs_.get(dataFile);
        atomic @
        {
            chksumMgr = new ChecksumManager(dataFile);
            chksumMgrs_.put(dataFile, chksumMgr);
        }

        return chksumMgr;
    }
    
    /* TODO: Debug only */
    public static ChecksumManager instance(String dataFile, String chkSumFile) throws IOException
    {
        ChecksumManager chksumMgr = chksumMgrs_.get(dataFile);
        atomic @ if ( chksumMgr == null )
        {
            chksumMgr = new ChecksumManager(dataFile, chkSumFile);
            chksumMgrs_.put(dataFile, chksumMgr);
        }

        return chksumMgr;
    }
    
    /**
     * This method returns true if the file specified is a 
     * checksum file and false otherwise.
     * 
     * @param file we are interested in.
     * @return true if checksum file false otherwise.
     */
    public static boolean isChecksumFile(String file)
    {
        return file.contains(ChecksumManager.checksumPrefix_);
    }
    
    /**
     * On start read all the check sum files on disk and
     * pull them into memory.
     * @throws IOException
     */
    public static void onStart() throws IOException
    {
        String[] directories = DatabaseDescriptor.getAllDataFileLocations(); 
        List<File> allFiles = new ArrayList<File>();
        for ( String directory : directories )
        {
            File file = new File(directory);
            File[] files = file.listFiles();
            for ( File f : files )
            {
                if ( f.getName().contains(ChecksumManager.checksumPrefix_) )
                {
                    allFiles.add(f);
                }
            }
        }
        
        for ( File file : allFiles )
        {                           
            int fId = ChecksumManager.getChecksumFileId(file.getName());
            RandomAccessFile chksumRdr = new RandomAccessFile(file, "r");            
            long size = chksumRdr.length();
            int chunk = 0;
            
            while ( chksumRdr.getFilePointer() != size )
            {
                long value = chksumRdr.readLong();
                long key = ChecksumManager.key(fId, ++chunk);
                chksums_.put(key, value);
            }
        }
    }
    
    /**
     * On delete of this dataFile remove the checksums associated with
     * this file from memory, remove the check sum manager instance.
     * 
     * @param dataFile data file that is being deleted.
     * @throws IOException
     */
    public static void onFileDelete(String dataFile) throws IOException
    {
        File f = new File(dataFile);
        long size = f.length();
        int fileId = ChecksumManager.getFileId(f.getName());
        int chunks = (int)(size >> 16L);
        
        for ( int i = 0; i < chunks; ++i )
        {
            long key = ChecksumManager.key(fileId, i);
            chksums_.remove(key);
        }
        
        /* remove the check sum manager instance */
        chksumMgrs_.remove(dataFile);
        String chksumFile = ChecksumManager.constructChksumFileNameFromDataFileName(f);
        FileUtils.delete(chksumFile);
    }
    
    private static long key(int fileId, int chunkId)
    {
        long key = 0;
        key |= fileId;
        key <<= 32;
        key |= chunkId;
        return key;
    }
    
    public static int getFileId(String file)
    {
        String filename = new File(file).getName();
        /*
         * File name is of the form <table>-<column family>-<index>-Data.db.
         * Always split and then use the value which is at index length - 2.
         */
        String[] peices = filename.split("-");
        return Integer.parseInt( peices[peices.length - 2] );
    }
    
    static void close(String dataFile) throws IOException
    {
        ChecksumManager.chksumMgrs_.get(dataFile).close();
    }
    
    private static int getChecksumFileId(String file)
    {
        String filename = new File(file).getName();
        /*
         * File name is of the form <table>-<column family>-Checksum-<index>.db.
         * This tokenizer will strip the .db portion.
         */
        String[] peices = filename.split("-");
        return Integer.parseInt( peices[3] );
    }
    
    private static String constructChksumFileNameFromDataFileName(File file)
    {
        String directory = file.getParent();
        String f = file.getName();
        /* we need the table and the column family name. */
        String[] peices = f.split("-");
        /* we need the index part of the file name */
        int fId = ChecksumManager.getFileId(f);
        String chkSumFile = directory + System.getProperty("file.separator") + peices[0] + "-" + peices[1] + "-" + checksumPrefix_ + fId + "-" + "Data" + ".db";
        return chkSumFile;
    }
    
    private RandomAccessFile raf_;
    private Adler32 adler_ = new Adler32();
    
    ChecksumManager(String dataFile) throws IOException
    {
        File file = new File(dataFile);
        String chkSumFile = ChecksumManager.constructChksumFileNameFromDataFileName(file);
        raf_ = new BufferedRandomAccessFile(chkSumFile, "rw");
    }
    
    /* TODO: Remove later. */
    ChecksumManager(String dataFile, String chkSumFile) 
    {
        File file = new File(dataFile);
        String directory = file.getParent();
        String f = file.getName();
        int fId = ChecksumManager.getFileId(f);        
        raf_ = new BufferedRandomAccessFile(chkSumFile, "rw");
        
        file = new File(chkSumFile);        
        ChecksumReader chksumRdr = new ChecksumReader(file.getAbsolutePath(), 0L, file.length());
                    
        int chunk = 0;
        while ( !chksumRdr.isEOF() )
        {
            long value = chksumRdr.readLong();
            long key = ChecksumManager.key(fId, ++chunk);
            chksums_.put(key, value);
        }
    }
    
    /**
     * Log the checksum for the the specified file and chunk
     * within the file.
     * @param fileId id associated with the file
     * @param chunkId chunk within the file.
     * @param buffer for which the checksum needs to be calculated.
     * @throws IOException
     */
    void logChecksum(int fileId, int chunkId, byte[] buffer)
    {
        logChecksum(fileId, chunkId, buffer, 0, buffer.length);
    }
    
    /**
     * Log the checksum for the the specified file and chunk
     * within the file.
     * @param fileId id associated with the file
     * @param chunkId chunk within the file.
     * @param buffer for which the checksum needs to be calculated.
     * @param startoffset offset to start within the buffer
     * @param length size of the checksum buffer.
     * @throws IOException
     */
    void logChecksum(int fileId, int chunkId, byte[] buffer, int startOffset, int length)
    {
        try
        {            
            adler_.update(buffer, startOffset, length);
            long chksum = adler_.getValue();
            adler_.reset();
            /* log checksums to disk */
            raf_.writeLong(chksum);
            /* add the chksum to memory */
            long key = ChecksumManager.key(fileId, chunkId);
            chksums_.put(key, chksum);
        }
    }
    
    /**
     * Validate checksums for the data in the buffer.
     * @file name of the file from which data is being
     *       read.
     * @chunkId chunkId
     * @param buffer with data for which checksum needs to be 
     *        verified.
     * @throws IOException
     */
    void validateChecksum(String file, int chunkId, byte[] buffer) throws IOException
    {                
        validateChecksum(file, chunkId, buffer, 0, buffer.length);
    }
    
    /**
     * Validate checksums for the data in the buffer for the region
     * that is encapsulated in the section object
     * @file name of the file from which data is being
     *       read.
     * @chunkId chunkId     
     * @param buffer with data for which checksum needs to be 
     *        verified.
     * @param startOffset within the buffer
     * @param length of the data whose checksum needs to be verified.
     * @throws IOException
     */
    void validateChecksum(String file, int chunkId, byte[] buffer, int startOffset, int length) throws IOException
    {            
        int fId = ChecksumManager.getFileId(file);
        long key = ChecksumManager.key(fId, chunkId);
        adler_.update(buffer, startOffset, length);
        long currentChksum = adler_.getValue();
        adler_.reset();
        long oldChksum = chksums_.get(key);
        if ( currentChksum != oldChksum )
        {                                   
            throw new IOException("Checksums do not match in file " + file + " for chunk " + chunkId + ".");
        }        
    }
    
    
    /**
     * Get the checksum for the specified file's chunk
     * @param fileId id associated with the file.
     * @param chunkId chunk within the file.
     * @return associated checksum for the chunk
     */
    long getChecksum(int fileId, int chunkId)
    {        
        long key = ChecksumManager.key(fileId, chunkId);
        return chksums_.get(key);
    }
    
    /**
     * Close the file handler.
     * 
     * @throws IOException
     */
    void close() throws IOException
    {
        raf_.close();
    }
    
    public static void main(String[] args)
    {
        ChecksumReader rdr = new ChecksumReader("C:\\Engagements\\Cassandra\\Checksum-1.db");
        while ( !rdr.isEOF() )
        {
            System.out.println(rdr.readLong());
        }
        rdr.close();
    }
}

/**
 * ChecksumReader is used to memory map the checksum files and
 * load the data into memory.
 * 
 * @author alakshman
 *
 */
class ChecksumReader 
{
    private static Logger logger_ = Logger.getLogger(ChecksumReader.class);
    private String filename_;
    private MappedByteBuffer buffer_;

    ChecksumReader(String filename) throws IOException
    {
        filename_ = filename;
        File f = new File(filename);
        map(0, f.length());
    }
    
    ChecksumReader(String filename, long start, long end) throws IOException
    {        
        filename_ = filename;
        map(start, end);
    }

    public void map() throws IOException
    {
        RandomAccessFile file = new RandomAccessFile(filename_, "rw");
        buffer_ = file.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, file.length() );
        buffer_.load();
        file.close();
    }

    public void map(long start, long end) throws IOException
    {
        RandomAccessFile file = new RandomAccessFile(filename_, "rw");
        if ( end == 0 )
            end = file.length();
        buffer_ = file.getChannel().map(FileChannel.MapMode.READ_ONLY, start, end);
        buffer_.load();
        file.close();
    }

    void unmap(final Object buffer)
    {
        AccessController.doPrivileged( new PrivilegedAction<MappedByteBuffer>()
                {
            public MappedByteBuffer run()
            {
                Method getCleanerMethod = buffer.getClass().getMethod("cleaner", new Class[0]);
                getCleanerMethod.setAccessible(true);
                sun.misc.Cleaner cleaner = (sun.misc.Cleaner)getCleanerMethod.invoke(buffer,new Object[0]);
                cleaner.clean();
                return null;
            }
                });
    }
    
    public long readLong() throws IOException
    {
        return buffer_.getLong();
    }
    
    public boolean isEOF()
    {
        return ( buffer_.remaining() == 0 );
    }

    
    public void close() throws IOException
    {
        unmap(buffer_);
    }
}
