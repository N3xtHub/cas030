

/**
 * <p>Java class for SuperColumnType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SuperColumnType", propOrder = {
    "fields"
})
public class SuperColumnType {

    @XmlElement(name = "Fields", required = true)
    protected FieldCollection fields;
    @XmlAttribute(name = "Tokenize")
    protected Boolean tokenize;

    /**
     * Gets the value of the fields property.
     * 
     * @return
     *     possible object is
     *     {@link FieldCollection }
     *     
     */
    public FieldCollection getFields() {
        return fields;
    }

    /**
     * Sets the value of the fields property.
     * 
     * @param value
     *     allowed object is
     *     {@link FieldCollection }
     *     
     */
    public void setFields(FieldCollection value) {
        this.fields = value;
    }

    /**
     * Gets the value of the tokenize property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isTokenize() {
        return tokenize;
    }

    /**
     * Sets the value of the tokenize property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setTokenize(Boolean value) {
        this.tokenize = value;
    }

}
