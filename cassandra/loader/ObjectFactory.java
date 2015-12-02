

/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.facebook.infrastructure.loader package. 
 * An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _SuperColumn_QNAME = new QName("", "SuperColumn");
    private final static QName _Table_QNAME = new QName("", "Table");
    private final static QName _Column_QNAME = new QName("", "Column");

    public ObjectFactory() {
    }

    public KeyType createKeyType() {
        return new KeyType();
    }

    public SuperColumnType createSuperColumnType() {
        return new SuperColumnType();
    }

    public ColumnType createColumnType() {
        return new ColumnType();
    }

    public Importer createImporter() {
        return new Importer();
    }

    public ColumnFamilyType createColumnFamilyType() {
        return new ColumnFamilyType();
    }

    public TimestampType createTimestampType() {
        return new TimestampType();
    }

    public FieldCollection createFieldCollection() {
        return new FieldCollection();
    }

    public ValueType createValueType() {
        return new ValueType();
    }

    public JAXBElement<SuperColumnType> createSuperColumn(SuperColumnType value) {
        return new JAXBElement<SuperColumnType>(_SuperColumn_QNAME, SuperColumnType.class, null, value);
    }

    public JAXBElement<String> createTable(String value) {
        return new JAXBElement<String>(_Table_QNAME, String.class, null, value);
    }

    public JAXBElement<ColumnType> createColumn(ColumnType value) {
        return new JAXBElement<ColumnType>(_Column_QNAME, ColumnType.class, null, value);
    }

}
