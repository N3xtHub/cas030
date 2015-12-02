
public class CqlResult
{
    public int                       errorCode; // 0 - success
    public String                    errorTxt;
    public List<Map<String, String>> resultSet;

    public CqlResult(List<Map<String, String>> rows)
    {
        resultSet = rows;
        errorTxt  = null;
        errorCode = 0; // success
    }
    
};
