
/**
 * An interface for writing into the SequenceFile abstraction.
 */

public interface IFileWriter
{
    /**
     * Get the current position of the file pointer.
     * @return current file pointer position
     * @throws IOException
     */
    public long getCurrentPosition() throws IOException;
    
    /**
     * @return the last file modification time.
     */
    public long lastModified();
    
    /**
     * Seeks the file pointer to the specified position.
     * @param position position within the file to seek to.
     * @throws IOException
     */
    public void seek(long position) throws IOException;
    
    /**
     * Appends the buffer to the the underlying SequenceFile.
     * @param buffer buffer which contains the serialized data.
     * @throws IOException
     */
    public void append(DataOutputBuffer buffer) throws IOException;
    
    /**
     * Appends the key and the value to the the underlying SequenceFile.
     * @param keyBuffer buffer which contains the serialized key.
     * @param buffer buffer which contains the serialized data.
     * @throws IOException
     */
    public void append(DataOutputBuffer keyBuffer, DataOutputBuffer buffer) throws IOException;
    
    /**
     * Appends the key and the value to the the underlying SequenceFile.
     * @param key key associated with this peice of data.
     * @param buffer buffer containing the serialized data.
     * @throws IOException
     */
    public void append(String key, DataOutputBuffer buffer) throws IOException;
    
    /**
     * Appends the key and the value to the the underlying SequenceFile.
     * @param key key associated with this peice of data.
     * @param value byte array containing the serialized data.
     * @throws IOException
     */
    public void append(String key, byte[] value) throws IOException;
    
    /**
     * Appends the key and the long value to the the underlying SequenceFile.
     * This is used in the contruction of the index file associated with a 
     * SSTable.
     * @param key key associated with this peice of data.
     * @param value value associated with this key.
     * @throws IOException
     */
    public void append(String key, long value) throws IOException;
    
    /**
     * Be extremely careful while using this API. This currently
     * used to write the commit log header in the commit logs.
     * If not used carefully it could completely screw up reads
     * of other key/value pairs that are written. 
     * 
     * @param bytes serialized version of the commit log header.
     * @throws IOException
    */
    public long writeDirect(byte[] bytes) throws IOException;
    
    /**
     * Write a long into the underlying sub system.
     * @param value long to be written
     * @throws IOException
     */
    public void writeLong(long value) throws IOException;
      
    /**
     * Close the file which is being used for the write.
     * @throws IOException
     */
    public void close() throws IOException;  
    
    /**
     * Close the file after appending the passed in footer information.
     * @param footer footer information.
     * @param size size of the footer.
     * @throws IOException
     */
    public void close(byte[] footer, int size) throws IOException;
    
    /**
     * @return the name of the file.
     */
    public String getFileName();    
    
    /**
     * @return the size of the file.
     * @throws IOException
     */
    public long getFileSize() throws IOException;    
}
