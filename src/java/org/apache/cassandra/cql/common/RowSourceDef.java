/**
 * The abstract notion of a row source definition. A row source
 * is literally just anything that returns rows back.
 * 
 * The concrete implementations of row source might be things like a 
 * column family row source, a "super column family" row source, 
 * a table row source, etc.
 *
 * Note: Instances of sub-classes of this class are part of the "shared" 
 * execution plan of CQL. And hence they should not contain any mutable
 * (i.e. session specific) execution state. Mutable state, such a bind
 * variable values (corresponding to say a rowKey or a column Key) are
 * note part of the RowSourceDef tree.
 * 
 * [Eventually the notion of a "mutable" portion of the RowSource (RowSourceMut)
 * will be introduced to hold session-specific execution state of the RowSource.
 * For example, this would be needed when implementing iterator style rowsources
 * that yields rows back one at a time as opposed to returning them in one
 * shot.]
 */
public abstract class RowSourceDef
{
    public abstract List<Map<String,String>> getRows();
    public abstract String explainPlan();  
}