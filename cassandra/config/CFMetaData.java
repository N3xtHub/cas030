
public class CFMetaData
{
    public String tableName;            // name of table which has this column family
    public String cfName;               // name of the column family
    public String columnType;           // type: super, standard, etc.
    public String indexProperty_;       // name sorted, time stamp sorted etc. 

    // The user chosen names (n_) for various parts of data in a column family.
    // CQL queries, for instance, will refer to/extract data within a column
    // family using these logical names.
    public String n_rowKey;               
    public String n_superColumnMap;     // only used if this is a super column family
    public String n_superColumnKey;     // only used if this is a super column family
    public String n_columnMap;
    public String n_columnKey;
    public String n_columnValue;
    public String n_columnTimestamp;
    public int    flushPeriodInMinutes = 0; // flush interval, if <=0, no periodic flusher is scheduled
    
    // a quick and dirty pretty printer for describing the column family...
    public String pretty()
    {
        String desc;
        desc = n_columnMap + "(" + n_columnKey + ", " + n_columnValue + ", " + n_columnTimestamp + ")";
        if ("Super".equals(columnType))
        {
            desc = n_superColumnMap + "(" + n_superColumnKey + ", " + desc + ")"; 
        }
        desc = tableName + "." + cfName + "(" + n_rowKey + ", " + desc + ")\n";
        
        desc += "Column Family Type: " + columnType + "\n" +
                "Columns Sorted By: " + indexProperty_ + "\n";
        desc += "flush period: " + flushPeriodInMinutes + " minutes\n";
        return desc;
    }
}
