
/**
 * This class keeps a back-pointer to the AnalyticsContext
 * and delegates back to it on <code>update</code> and <code>remove()</code>.
 */
public class MetricsRecord {

	private AnalyticsContext.TagMap tagTable = new AnalyticsContext.TagMap();
	private Map<String,MetricValue> metricTable = new LinkedHashMap<String,MetricValue>();

	private String recordName;
	private AnalyticsContext context;


	/**
	 * Creates a new instance of MetricsRecord
	 *
	 *  @param recordName name of this record
	 *  @param context the context which this record is a part of
	 */
	protected MetricsRecord(String recordName, AnalyticsContext context)
	{
		this.recordName = recordName;
		this.context = context;
	}

	/**
	 * Returns the record name.
	 *
	 * @return the record name
	 */
	public String getRecordName() {
		return recordName;
	}

	/**
	 * Sets the named tag to the specified value.
	 *
	 * @param tagName name of the tag
	 * @param tagValue new value of the tag
	 * @throws MetricsException if the tagName conflicts with the configuration
	 */
	public void setTag(String tagName, String tagValue) {
		if (tagValue == null) {
			tagValue = "";
		}
		tagTable.put(tagName, tagValue);
	}

	/**
	 * Sets the named tag to the specified value.
	 *
	 * @param tagName name of the tag
	 * @param tagValue new value of the tag
	 * @throws MetricsException if the tagName conflicts with the configuration
	 */
	public void setTag(String tagName, int tagValue) {
		tagTable.put(tagName, new Integer(tagValue));
	}

	/**
	 * Sets the named tag to the specified value.
	 *
	 * @param tagName name of the tag
	 * @param tagValue new value of the tag
	 * @throws MetricsException if the tagName conflicts with the configuration
	 */
	public void setTag(String tagName, short tagValue) {
		tagTable.put(tagName, new Short(tagValue));
	}

	/**
	 * Sets the named tag to the specified value.
	 *
	 * @param tagName name of the tag
	 * @param tagValue new value of the tag
	 * @throws MetricsException if the tagName conflicts with the configuration
	 */
	public void setTag(String tagName, byte tagValue)
	{
		tagTable.put(tagName, new Byte(tagValue));
	}

	/**
	 * Sets the named metric to the specified value.
	 *
	 * @param metricName name of the metric
	 * @param metricValue new value of the metric
	 * @throws MetricsException if the metricName or the type of the metricValue
	 * conflicts with the configuration
	 */
	public void setMetric(String metricName, int metricValue)
	{
		setAbsolute(metricName, new Integer(metricValue));
	}

	/**
	 * Sets the named metric to the specified value.
	 *
	 * @param metricName name of the metric
	 * @param metricValue new value of the metric
	 * @throws MetricsException if the metricName or the type of the metricValue
	 * conflicts with the configuration
	 */
	public void setMetric(String metricName, short metricValue)
	{
		setAbsolute(metricName, new Short(metricValue));
	}

	/**
	 * Sets the named metric to the specified value.
	 *
	 * @param metricName name of the metric
	 * @param metricValue new value of the metric
	 * @throws MetricsException if the metricName or the type of the metricValue
	 * conflicts with the configuration
	 */
	public void setMetric(String metricName, byte metricValue)
	{
		setAbsolute(metricName, new Byte(metricValue));
	}

	/**
	 * Sets the named metric to the specified value.
	 *
	 * @param metricName name of the metric
	 * @param metricValue new value of the metric
	 * @throws MetricsException if the metricName or the type of the metricValue
	 * conflicts with the configuration
	 */
	public void setMetric(String metricName, float metricValue)
	{
		setAbsolute(metricName, new Float(metricValue));
	}

	/**
	 * Increments the named metric by the specified value.
	 *
	 * @param metricName name of the metric
	 * @param metricValue incremental value
	 * @throws MetricsException if the metricName or the type of the metricValue
	 * conflicts with the configuration
	 */
	public void incrMetric(String metricName, int metricValue)
	{
		setIncrement(metricName, new Integer(metricValue));
	}

	/**
	 * Increments the named metric by the specified value.
	 *
	 * @param metricName name of the metric
	 * @param metricValue incremental value
	 * @throws MetricsException if the metricName or the type of the metricValue
	 * conflicts with the configuration
	 */
	public void incrMetric(String metricName, short metricValue)
	{
		setIncrement(metricName, new Short(metricValue));
	}

	/**
	 * Increments the named metric by the specified value.
	 *
	 * @param metricName name of the metric
	 * @param metricValue incremental value
	 * @throws MetricsException if the metricName or the type of the metricValue
	 * conflicts with the configuration
	 */
	public void incrMetric(String metricName, byte metricValue)
	{
		setIncrement(metricName, new Byte(metricValue));
	}

	/**
	 * Increments the named metric by the specified value.
	 *
	 * @param metricName name of the metric
	 * @param metricValue incremental value
	 * @throws MetricsException if the metricName or the type of the metricValue
	 * conflicts with the configuration
	 */
	public void incrMetric(String metricName, float metricValue)
	{
		setIncrement(metricName, new Float(metricValue));
	}

	/**
	 * Sets the value of the metric identified by metricName with the
	 * number metricValue.
	 *
	 * @param metricName name of the metric
	 * @param metricValue number value to which it should be updated
	 */
	private void setAbsolute(String metricName, Number metricValue)
	{
		metricTable.put(metricName, new MetricValue(metricValue, MetricValue.ABSOLUTE));
	}

	/**
	 * Increments the value of the metric identified by metricName with the
	 * number metricValue.
	 *
	 * @param metricName name of the metric
	 * @param metricValue number value by which it should be incremented
	 */
	private void setIncrement(String metricName, Number metricValue)
	{
		metricTable.put(metricName, new MetricValue(metricValue, MetricValue.INCREMENT));
	}

	/**
	 * Updates the table of buffered data which is to be sent periodically.
	 * If the tag values match an existing row, that row is updated;
	 * otherwise, a new row is added.
	 */
	public void update()
	{
		context.update(this);
	}

	/**
	 * Removes the row, if it exists, in the buffered data table having tags
	 * that equal the tags that have been set on this record.
	 */
	public void remove()
	{
		context.remove(this);
	}

	AnalyticsContext.TagMap getTagTable()
	{
		return tagTable;
	}

	Map<String, MetricValue> getMetricTable()
	{
		return metricTable;
	}
}
