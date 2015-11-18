
/**
 * A Number that is either an absolute or an incremental amount.
 */
public class MetricValue
{
	public static final boolean ABSOLUTE = false;
	public static final boolean INCREMENT = true;

	private boolean isIncrement;
	private Number number;

	/**
	 * Creates a new instance of MetricValue
	 *
	 *  @param number this initializes the initial value of this metric
	 *  @param isIncrement sets if the metric can be incremented or only set
	 */
	public MetricValue(Number number, boolean isIncrement)
	{
		this.number = number;
		this.isIncrement = isIncrement;
	}

	/**
	 * Checks if this metric can be incremented.
	 *
	 * @return true if the value of this metric can be incremented, false otherwise
	 */
	public boolean isIncrement()
	{
		return isIncrement;
	}

	/**
	 * Checks if the value of this metric is always an absolute value. This is the
	 * inverse of isIncrement.
	 *
	 * @return true if the
	 */
	public boolean isAbsolute()
	{
		return !isIncrement;
	}

	/**
	 * Returns the current number value of the metric.
	 *
	 * @return the Number value of this metric
	 */
	public Number getNumber()
	{
		return number;
	}
}
