
/**
 * General-purpose, unchecked metrics exception.
 *
 */
public class AnalyticsException extends RuntimeException
{

	  private static final long serialVersionUID = -1643257498540498497L;

	  /**
	   * Creates a new instance of MetricsException
	   */
	  public AnalyticsException()
	  {
	  }

	  /** Creates a new instance of MetricsException
	   *
	   * @param message an error message
	   */
	  public AnalyticsException(String message)
	  {
	    super(message);
	  }

	}
