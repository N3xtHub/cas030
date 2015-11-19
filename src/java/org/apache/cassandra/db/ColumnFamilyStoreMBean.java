public interface ColumnFamilyStoreMBean
{
    /**
     * Returns the total amount of data stored in the memtable, including
     * column related overhead.
     * 
     * @return The size in bytes.
     */
    public int getMemtableDataSize();
    
    /**
     * Returns the total number of columns present in the memtable.
     * 
     * @return The number of columns.
     */
    public int getMemtableColumnsCount();
    
    /**
     * Returns the number of times that a flush has resulted in the
     * memtable being switched out.
     *
     * @return the number of memtable switches
     */
    public int getMemtableSwitchCount();

    /**
     * Triggers an immediate memtable flush.
     */
    public void forceFlush();

    /**
     * @return the number of read operations on this column family in the last minute
     */
    public int getReadCount();

    /**
     * @return the number of read operations on this column family that hit the disk in the last minute
     */
    public int getReadDiskHits();

    /**
     * @return average latency per read operation in the last minute
     */
    public double getReadLatency();
}
