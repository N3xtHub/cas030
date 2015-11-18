

/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "table",
    "key",
    "columnFamily"
})
@XmlRootElement(name = "Importer")
public class Importer {

    @XmlElement(name = "Table", required = true)
    protected String table;
    @XmlElement(name = "Key", required = true)
    protected KeyType key;
    @XmlElement(name = "ColumnFamily", required = true)
    protected ColumnFamilyType columnFamily;

    /**
     * Gets the value of the table property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTable() {
        return table;
    }

    /**
     * Sets the value of the table property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTable(String value) {
        this.table = value;
    }

    /**
     * Gets the value of the key property.
     * 
     * @return
     *     possible object is
     *     {@link KeyType }
     *     
     */
    public KeyType getKey() {
        return key;
    }

    /**
     * Sets the value of the key property.
     * 
     * @param value
     *     allowed object is
     *     {@link KeyType }
     *     
     */
    public void setKey(KeyType value) {
        this.key = value;
    }

    /**
     * Gets the value of the columnFamily property.
     * 
     * @return
     *     possible object is
     *     {@link ColumnFamilyType }
     *     
     */
    public ColumnFamilyType getColumnFamily() {
        return columnFamily;
    }

    /**
     * Sets the value of the columnFamily property.
     * 
     * @param value
     *     allowed object is
     *     {@link ColumnFamilyType }
     *     
     */
    public void setColumnFamily(ColumnFamilyType value) {
        this.columnFamily = value;
    }

}
