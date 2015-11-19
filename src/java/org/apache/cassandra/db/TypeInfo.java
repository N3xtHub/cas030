
public enum TypeInfo
{
    BYTE,
    CHAR,
    SHORT,
    INT,
    LONG,
    DOUBLE,
    FLOAT,
    STRING,
    BLOB;
    
    public static byte toByte(TypeInfo ti)
    {
        byte value = 0;
        switch(ti)
        {
            case BYTE:
                value = 1;
                break;
            
            case CHAR:
                value = 2;
                break;
                
            case SHORT:
                value = 3;
                break;
                
            case INT:
                value = 4;
                break;
                
            case LONG:
                value = 5;
                break;
                
            case DOUBLE:
                value = 6;
                break;
                
            case FLOAT:
                value = 7;
                break;
                
            case STRING:
                value = 8;
                break;
                
            case BLOB:
                value = 9;
                break;
        }
        
        return value;
    }
    
    public static TypeInfo fromByte(byte b)
    {
        TypeInfo ti = null;
        switch(b)
        {
            case 1:
                ti = TypeInfo.BYTE;                
                break;
                
            case 2:
                ti = TypeInfo.CHAR;
                break;
                
            case 3:
                ti = TypeInfo.SHORT;
                break;
                
            case 4:
                ti = TypeInfo.INT;
                break;
                
            case 5:
                ti = TypeInfo.LONG;
                break;
                
            case 6:
                ti = TypeInfo.DOUBLE;
                break;
                
            case 7:
                ti = TypeInfo.FLOAT;
                break;
                
            case 8:
                ti = TypeInfo.STRING;
                break;
                
            case 9:
                ti = TypeInfo.BLOB;
                break;               
        }
        return ti;
    }
}
