
/**
 * Represents a record of metric data to be sent to a metrics system.
 *
 */
public class OutputRecord
{
	private AnalyticsContext.TagMap tagMap;
	private AnalyticsContext.MetricMap metricMap;

	/**
	 * Creates a new instance of OutputRecord
	 */
	OutputRecord(AnalyticsContext.TagMap tagMap, AnalyticsContext.MetricMap metricMap)
	{
		this.tagMap = tagMap;
		this.metricMap = metricMap;
	}

	/**
	 * Returns the set of tag names.
	 */
	public Set<String> getTagNames()
	{
		return Collections.unmodifiableSet(tagMap.keySet());
	}

	/**
	 * Returns a tag object which is can be a String, Integer, Short or Byte.
	 *
	 * @return the tag value, or null if there is no such tag
	 */
	public Object getTag(String name)
	{
		return tagMap.get(name);
	}

	/**
	 * Returns the set of metric names.
	 *
	 * @return the set of metric names
	 */
	public Set<String> getMetricNames()
	{
		return Collections.unmodifiableSet(metricMap.keySet());
	}

	/**
	 * Returns the metric object which can be a Float, Integer, Short or Byte.
	 *
	 * @param name name of the metric for which the value is being requested
	 * @return return the tag value, or null if there is no such tag
	 */
	public Number getMetric(String name)
	{
		return (Number) metricMap.get(name);
	}

}