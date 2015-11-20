
class LoadInfo
{
    protected static class DiskSpaceComparator implements Comparator<LoadInfo>
    {
        public int compare(LoadInfo li, LoadInfo li2)
        {           
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
}
