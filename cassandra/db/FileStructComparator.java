
class FileStructComparator implements Comparator<FileStruct>
{
    public int compare(FileStruct f, FileStruct f2)
    {
        return f.getFileName().compareTo(f2.getFileName());
    }
}