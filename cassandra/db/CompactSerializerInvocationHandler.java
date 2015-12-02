

/*
 * This is the abstraction that pre-processes calls to implmentations
 * of the ICompactSerializer2 serialize() via dynamic proxies.
 */

public class CompactSerializerInvocationHandler<T> implements InvocationHandler
{
    private ICompactSerializer2<T> serializer_;

    public CompactSerializerInvocationHandler(ICompactSerializer2<T> serializer)
    {
        serializer_ = serializer;
    }

    /*
     * This dynamic runtime proxy adds the indexes before the actual coumns are serialized.
    */
    public Object invoke(Object proxy, Method m, Object[] args) throws Throwable
    {
        /* Do the preprocessing here. */
    	ColumnFamily cf = (ColumnFamily)args[0];
    	DataOutputBuffer bufOut = (DataOutputBuffer)args[1];
    	ColumnIndexer.serialize(cf, bufOut);
        return m.invoke(serializer_, args);
    }
}
