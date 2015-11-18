
/**
 * Call-back interface.  See <code>AnalyticsContext.registerUpdater()</code>.
 * This callback is called at a regular (pre-registered time interval) in
 * order to update the metric values.
 *
 */
public interface IAnalyticsSource
{
  /**
   * Timer-based call-back from the metric library.
   */
  public abstract void doUpdates(AnalyticsContext context);

}
