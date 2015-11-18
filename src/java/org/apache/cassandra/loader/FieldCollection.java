

public class FieldCollection {

    protected List<Integer> field;

    /**
     * Gets the value of the field property.
     * 
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the field property.
     * 
     * For example, to add a new item, do as follows:
     *    getField().add(newItem);
     * 
     * Objects of the following type(s) are allowed in the list
     * {@link Integer }
     * 
     */
    public List<Integer> getField() {
        if (field == null) {
            field = new ArrayList<Integer>();
        }
        return this.field;
    }

}
