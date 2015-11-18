

/**
 * <p>Java class for FieldCollection complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FieldCollection", propOrder = {
    "field"
})
public class FieldCollection {

    @XmlElement(name = "Field", type = Integer.class)
    protected List<Integer> field;

    /**
     * Gets the value of the field property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the field property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getField().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Integer }
     * 
     * 
     */
    public List<Integer> getField() {
        if (field == null) {
            field = new ArrayList<Integer>();
        }
        return this.field;
    }

}
