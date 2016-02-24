
public class Row
{
    private String key_;

    private Map<String, ColumnFamily> columnFamilies_ = new Hashtable<String, ColumnFamily>();

    protected Row()
    {
    }

    public Row(String key)
    {
        key_ = key;
    }

    public String key()
    {
        return key_;
    }

    void key(String key)
    {
        key_ = key;
    }

    public Set<String> getColumnFamilyNames()
    {
        return columnFamilies_.keySet();
    }

    public Collection<ColumnFamily> getColumnFamilies()
    {
        return columnFamilies_.values();
    }

    @Deprecated
    // (use getColumnFamilies or getColumnFamilyNames)
    public Map<String, ColumnFamily> getColumnFamilyMap()
    {
        return columnFamilies_;
    }

    public ColumnFamily getColumnFamily(String cfName)
    {
        return columnFamilies_.get(cfName);
    }

    void addColumnFamily(ColumnFamily columnFamily)
    {
        columnFamilies_.put(columnFamily.name(), columnFamily);
    }

    void removeColumnFamily(ColumnFamily columnFamily)
    {
        columnFamilies_.remove(columnFamily.name());
        int delta = (-1) * columnFamily.size();
    }

    public boolean isEmpty()
    {
        return (columnFamilies_.size() == 0);
    }

    /*
     * This function will repair the current row with the input row
     * what that means is that if there are any differences between the 2 rows then
     * this fn will make the current row take the latest changes .
     */
    public void repair(Row rowOther)
    {
        for (ColumnFamily cfOld : rowOther.getColumnFamilies())
        {
            ColumnFamily cf = columnFamilies_.get(cfOld.name());
            if (cf == null)
            {
                addColumnFamily(cfOld);
            }
            else
            {
                columnFamilies_.remove(cf.name());
                addColumnFamily(ColumnFamily.resolve(Arrays.asList(cfOld, cf)));
            }
        }
    }

    /*
     * This function will calculate the difference between 2 rows
     * and return the resultant row. This assumes that the row that
     * is being submitted is a super set of the current row so
     * it only calculates additional
     * difference and does not take care of what needs to be removed from the current row to make
     * it same as the input row.
     */
    public Row diff(Row rowComposite)
    {
        Row rowDiff = new Row(key_);

        for (ColumnFamily cfComposite : rowComposite.getColumnFamilies())
        {
            ColumnFamily cf = columnFamilies_.get(cfComposite.name());
            if (cf == null)
                rowDiff.addColumnFamily(cfComposite);
            else
            {
                ColumnFamily cfDiff = cf.diff(cfComposite);
                if (cfDiff != null)
                    rowDiff.addColumnFamily(cfDiff);
            }
        }
        if (rowDiff.getColumnFamilies().isEmpty())
            return null;
        else
            return rowDiff;
    }

    public Row cloneMe()
    {
        Row row = new Row(key_);
        row.columnFamilies_ = new HashMap<String, ColumnFamily>(columnFamilies_);
        return row;
    }

    public byte[] digest()
    {
        Set<String> cfamilies = columnFamilies_.keySet();
        byte[] xorHash = ArrayUtils.EMPTY_BYTE_ARRAY;
        for (String cFamily : cfamilies)
        {
            if (xorHash.length == 0)
            {
                xorHash = columnFamilies_.get(cFamily).digest();
            }
            else
            {
                xorHash = FBUtilities.xor(xorHash, columnFamilies_.get(cFamily).digest());
            }
        }
        return xorHash;
    }

    void clear()
    {
        columnFamilies_.clear();
    }

    public String toString()
    {
        return "Row(" + key_ + " [" + StringUtils.join(columnFamilies_.values(), ", ") + ")]";
    }
}

