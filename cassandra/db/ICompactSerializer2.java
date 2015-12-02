

/**
 * This interface is an extension of the ICompactSerializer which allows for partial deserialization
 * of a type.
 */

public interface ICompactSerializer2<T> extends ICompactSerializer<T>
{   
	/**
     * Returns an instance of an IColumn which contains only the 
     * columns that are required. This is specified in the <i>columnNames</i>
     * argument.
     * 
     * @param dis DataInput from which we need to deserialize.
     * @throws IOException
     * @return type which contains the specified items.
	*/
	public T deserialize(DataInputStream dis, IFilter filter) throws IOException;
    
    /**
     * This method is used to deserialize just the specified field from 
     * the serialized stream.
     * 
     * @param dis DataInput from which we need to deserialize.
     * @param name name of the desired field.
     * @throws IOException
     * @return the deserialized type.
    */
	public T deserialize(DataInputStream dis, String name, IFilter filter) throws IOException;
    
    /**
     * 
     * @param dis
     * @throws IOException
     */
    public void skip(DataInputStream dis) throws IOException;
}
