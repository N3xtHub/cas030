
/**
 * A Number that is either an absolute or an incremental amount.
 */
public class MetricValue
{
	public static final boolean ABSOLUTE = false;
	public static final boolean INCREMENT = true;

	private boolean isIncrement;
	private Number number;

}
