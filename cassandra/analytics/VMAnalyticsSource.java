
/**
 * This class sets up the analytics package to report metrics into
 * Ganglia for VM heap utilization.
 */

public class VMAnalyticsSource implements IAnalyticsSource
{
	private static final String METRIC_MEMUSAGE = "VM Heap Utilization";
	private static final String RECORD_MEMUSAGE = "MemoryUsageRecord";
	private static final String TAG_MEMUSAGE = "MemoryUsageTag";
	private static final String TAG_MEMUSAGE_MEMUSED = "MemoryUsedTagValue";

	/**
	 * Setup the Ganglia record to display the VM heap utilization.
	 */
	public VMAnalyticsSource()
	{
		// set the units for the metric type
		AnalyticsContext.instance().setAttribute("units." + METRIC_MEMUSAGE, "MB");
		// create the record
        AnalyticsContext.instance().createRecord(RECORD_MEMUSAGE);
  	}

	/**
	 * Update the VM heap utilization record with the relevant data.
	 *
	 * @param context the reference to the context which has called this callback
	 */
	public void doUpdates(AnalyticsContext context)
	{
        // update the memory used record
		MetricsRecord memUsageRecord = context.getMetricsRecord(RECORD_MEMUSAGE);
		if(memUsageRecord != null)
		{
			updateUsedMemory(memUsageRecord);
		}
	}

	private void updateUsedMemory(MetricsRecord memUsageRecord)
	{
		memUsageRecord.setTag(TAG_MEMUSAGE, TAG_MEMUSAGE_MEMUSED);
		memUsageRecord.setMetric(METRIC_MEMUSAGE, getMemoryUsed());
		memUsageRecord.update();
	}

	private float getMemoryUsed()
	{
        MemoryMXBean memoryMxBean = ManagementFactory.getMemoryMXBean();
        MemoryUsage memUsage = memoryMxBean.getHeapMemoryUsage();
        return (float)memUsage.getUsed()/(1024 * 1024);
	}
}
