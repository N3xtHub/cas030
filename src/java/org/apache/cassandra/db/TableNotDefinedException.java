public class TableNotDefinedException extends InvalidRequestException
{
    public TableNotDefinedException(String why)
    {
        super(why);
    }
}
