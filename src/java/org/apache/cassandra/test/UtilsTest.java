
public class UtilsTest 
{
	private static void doHashPerf() throws Throwable
	{
		List<BigInteger> list = new ArrayList<BigInteger>();
		for ( int i = 0; i < 100; ++i )
		{
			String guid = GuidGenerator.guid();
			list.add( FBUtilities.hash(guid) );
		}
		Collections.sort(list);
		
		int startValue = 1000000;
		
		while ( true )
		{
			long start = System.currentTimeMillis();
			for ( int i = 0; i < 1024; ++i )
			{
				String key = Integer.toString(startValue + i);
				BigInteger hash = FBUtilities.hash(key);
				Collections.binarySearch(list, hash);
			}
			System.out.println("TIME TAKEN: " + (System.currentTimeMillis() - start));
			Thread.sleep(100);
		}
	}
	
	public static void main(String[] args) throws Throwable
	{
	}
}
