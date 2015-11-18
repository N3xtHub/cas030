
class LoadInfo
{
    protected static class DiskSpaceComparator implements Comparator<LoadInfo>
    {
        public int compare(LoadInfo li, LoadInfo li2)
        {
            if ( li == null || li2 == null )
                throw new IllegalArgumentException("Cannot pass in values that are NULL.");
            
            double space = FileUtils.stringToFileSize(li.diskSpace_);
            double space2 = FileUtils.stringToFileSize(li2.diskSpace_);
            return (int)(space - space2);
        }
    }
        
    private String diskSpace_;
    
    LoadInfo(long diskSpace)
    {       
        diskSpace_ = FileUtils.stringifyFileSize(diskSpace);
    }
    
    LoadInfo(String loadInfo)
    {
        diskSpace_ = loadInfo;
    }
    
    String diskSpace()
    {
        return diskSpace_;
    }
    
    public String toString()
    {
        StringBuilder sb = new StringBuilder("");       
        sb.append(diskSpace_);
        return sb.toString();
    }
}
