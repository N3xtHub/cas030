
/**
 * DHT - Distributed Hash Table
 * A representation of the range that a node is responsible for on the DHT ring.
 */

public class Range implements Comparable<Range>
{
    private static ICompactSerializer<Range> serializer_ = new RangeSerializer();
    
    private final Token left_;
    private final Token right_;


    /**
     * Helps determine if a given point on the DHT ring is contained
     * in the range in question.
     * @param bi point in question
     * @return true if the point contains within the range else false.
     */
    public boolean contains(Token bi)
    {
        if ( left_.compareTo(right_) > 0 )
        {
            /* 
             * left is greater than right we are wrapping around.
             * So if the interval is [a,b) where a > b then we have
             * 3 cases one of which holds for any given token k.
             * (1) k > a -- return true
             * (2) k < b -- return true
             * (3) b < k < a -- return false
            */
            if ( bi.compareTo(left_) >= 0 )
                return true;
            else return right_.compareTo(bi) > 0;
        }
        else if ( left_.compareTo(right_) < 0 )
        {
            /*
             * This is the range [a, b) where a < b. 
            */
            return ( bi.compareTo(left_) >= 0 && right_.compareTo(bi) >=0 );
        }        
        else
    	{
    		return true;
    	}    	
    }

    /**
     * Tells if the given range is a wrap around.
     * @param range
     * @return
     */
    private static boolean isWrapAround(Range range)
    {
        return range.left_.compareTo(range.right_) > 0;
    }
    
    public int compareTo(Range rhs)
    {
        /* 
         * If the range represented by the "this" pointer
         * is a wrap around then it is the smaller one.
        */
        if ( isWrapAround(this) )
            return -1;
        
        if ( isWrapAround(rhs) )
            return 1;
        
        return right_.compareTo(rhs.right_);
    }
    

    public static boolean isTokenInRanges(Token token, List<Range> ranges)
    {
        for (Range range : ranges)
        {
            if(range.contains(token))
            {
                return true;
            }
        }
        return false;
    }

    public boolean equals(Object o)
    {
        if ( !(o instanceof Range) )
            return false;
        Range rhs = (Range)o;
        return left_.equals(rhs.left_) && right_.equals(rhs.right_);
    }
}
