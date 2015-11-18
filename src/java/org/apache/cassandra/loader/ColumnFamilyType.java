@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ColumnFamilyType", propOrder = {
    "column",
    "superColumn",
    "directory",
    "delimiter"
})
public class ColumnFamilyType {

    @XmlElement(name = "Column", required = true, nillable = true)
    protected List<ColumnType> column;
    @XmlElement(name = "SuperColumn", required = true, nillable = true)
    protected SuperColumnType superColumn;
    @XmlElement(name = "Directory", required = true)
    protected String directory;
    @XmlElement(name = "Delimiter", required = true)
    protected String delimiter;
    @XmlAttribute(name = "Name")
    protected String name;

    public List<ColumnType> getColumn() {
        if (column == null) {
            column = new ArrayList<ColumnType>();
        }
        return this.column;
    }

    public SuperColumnType getSuperColumn() {
        return superColumn;
    }

    public void setSuperColumn(SuperColumnType value) {
        this.superColumn = value;
    }

    public String getDirectory() {
        return directory;
    }

    public void setDirectory(String value) {
        this.directory = value;
    }
}
