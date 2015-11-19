
//Note: This class is CQL related work in progress.
public class CType
{
    public static interface Type
    {
        String toString(); 
    };

    public static class IntegerType implements Type
    {
        public String toString() { return "Integer"; };
    }

    public static class StringType implements Type
    {
        public String toString() { return "String"; };
    }

    public static class RowType implements Type
    {
        ArrayList<Type> types_;
        public RowType(ArrayList<Type> types)
        {
            types_ = types;
        }

        public String toString()
        {
            StringBuffer sb = new StringBuffer("<");
            for (int idx = types_.size(); idx > 0; idx--)
            {
                sb.append(types_.toString());
                if (idx != 1)
                {
                    sb.append(", ");
                }
            }
            sb.append(">");
            return sb.toString();
        }
    }

    public static class ArrayType
    {
        Type elementType_;
        public ArrayType(Type elementType)
        {
            elementType_ = elementType;
        }

        public String toString()
        {
            return "Array(" + elementType_.toString() + ")";
        }
    }
}
