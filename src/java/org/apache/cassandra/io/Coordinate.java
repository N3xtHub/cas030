/**
 * Section of a file that needs to be scanned
 * is represented by this class.
*/
public class Coordinate
{
    public final long start_;
    public final long end_;
    
    Coordinate(long start, long end)
    {
        start_ = start;
        end_ = end;
    }

    public String toString()
    {
        return "Coordinate(" +
               "start_=" + start_ +
               ", end_=" + end_ +
               ')';
    }
}
