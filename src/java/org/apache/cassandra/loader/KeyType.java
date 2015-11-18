
/**
 * <p>Java class for KeyType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "KeyType", propOrder = {
    "optimizeIt",
    "combiner",
    "fields"
})
public class KeyType {

    @XmlElement(name = "OptimizeIt", required = true, type = Boolean.class, nillable = true)
    protected Boolean optimizeIt;
    @XmlElement(name = "Combiner", required = true)
    protected String combiner;
    @XmlElement(name = "Fields", required = true)
    protected FieldCollection fields;

    /**
     * Gets the value of the optimizeIt property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isOptimizeIt() {
        return optimizeIt;
    }

    /**
     * Sets the value of the optimizeIt property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setOptimizeIt(Boolean value) {
        this.optimizeIt = value;
    }

    /**
     * Gets the value of the combiner property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCombiner() {
        return combiner;
    }

    /**
     * Sets the value of the combiner property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCombiner(String value) {
        this.combiner = value;
    }

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

}
