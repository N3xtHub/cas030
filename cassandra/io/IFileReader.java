/**
 * Interface to read from the SequenceFile abstraction.
 */

public interface IFileReader
{
    public String getFileName();
    public long getEOF() throws IOException;
    public long getCurrentPosition() throws IOException;
    public boolean isHealthyFileDescriptor() throws IOException;
    public void seek(long position) throws IOException;
    public boolean isEOF() throws IOException;

    /**
     * Be extremely careful while using this API. This currently
     * used to read the commit log header from the commit logs.
     * Treat this as an internal API.
     * 
     * @param bytes read into this byte array.
    */
    public void readDirect(byte[] bytes) throws IOException;
    
    /**
     * Read a long value from the underlying sub system.
     * @return value read
     * @throws IOException
     */
    public long readLong() throws IOException;
    
    /**
     * This method helps is retrieving the offset of the specified
     * key in the file using the block index.
     * 
     * @param key key whose position we need in the block index.
    */
    public long getPositionFromBlockIndex(String key) throws IOException;
    
    /**
     * This method dumps the next key/value into the DataOuputStream
     * passed in.
     *
     * @param bufOut DataOutputStream that needs to be filled.
     * @return number of bytes read.
     * @throws IOException 
    */
    public long next(DataOutputBuffer bufOut) throws IOException;

    /**
     * This method dumps the next key/value into the DataOuputStream
     * passed in.
     *
     * @param key key we are interested in.
     * @param bufOut DataOutputStream that needs to be filled.
     * @param section region of the file that needs to be read
     * @throws IOException
     * @return the number of bytes read.
    */
    public long next(String key, DataOutputBuffer bufOut, Coordinate section) throws IOException;

    /**
     * This method dumps the next key/value into the DataOuputStream
     * passed in. Always use this method to query for application
     * specific data as it will have indexes.
     *
     * @param key - key we are interested in.
     * @param bufOut - DataOutputStream that needs to be filled.
     * @param columnFamilyName The name of the column family only without the ":"
     * @param columnNames - The list of columns in the cfName column family
     * 					     that we want to return
     * OR
     * @param timeRange - time range we are interested in
     * @param section region of the file that needs to be read
     * @throws IOException
     * @return number of bytes read.
     *
    */
    public long next(String key, DataOutputBuffer bufOut, String columnFamilyName, List<String> columnNames, IndexHelper.TimeRange timeRange, Coordinate section) throws IOException;

    /**
     * Close the file after reading.
     * @throws IOException
     */
    public void close() throws IOException;
}
