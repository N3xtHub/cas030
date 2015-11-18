
public class ColumnFamilyType {

    protected List<ColumnType> column;
    protected SuperColumnType superColumn;
    protected String directory;
    protected String delimiter;
    protected String name;

    public List<ColumnType> getColumn() {
        if (column == null) {
            column = new ArrayList<ColumnType>();
        }
        return this.column;
    }
}
