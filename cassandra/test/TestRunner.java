

public class TestRunner
{
    private static EndPoint to_ = new EndPoint("tdsearch001.sf2p.facebook.com", 7000);
    
    private static void doWrite() throws Throwable
    {
        Table table = Table.open("Mailbox");  
        Random random = new Random();
        int totalUsed = 0;
        int[] used = new int[16*1024];
        byte[] bytes = new byte[4*1024];
        for (int i = 0; i < 1; ++i)
        {
            String key = Integer.toString(i);
            RowMutation rm = new RowMutation("Mailbox", key);
            random.nextBytes(bytes);
            while ( totalUsed != 16*1024 )
            {
                int j = random.nextInt(16*1024);                
                if ( used[j] == 0 )
                {
                    used[j] = 1;
                    ++totalUsed;
                }             
                // rm.add("Test:Column-" + j, bytes, System.currentTimeMillis());
                
                for ( int k = 0; k < 1; ++k )
                {                             
                    rm.add("MailboxMailData0:SuperColumn-" + j + ":Column-" + k, bytes, k);                    
                }
            }
            rm.apply();            
        }
        System.out.println("Write done");
    }
    
    private static void doRead() throws Throwable
    {
        Table table = Table.open("Mailbox");
        String key = "511055962";
                
        /*
        List<String> list = new ArrayList<String>();
        list.add("SuperColumn-0");
        Row row = table.getRow(key, "MailboxMailList0", list);
        System.out.println(row);
        */
        
        List<String> list = new ArrayList<String>();
        list.add("SuperColumn-0");
        list.add("SuperColumn-189");
        list.add("SuperColumn-23");
        Row row = table.getRow("0", "MailboxMailData0", list);
        try
        {
            ColumnFamily cf = row.getColumnFamily("MailboxMailData0");
            Collection<IColumn> columns = cf.getAllColumns();            
            for ( IColumn column : columns )
            {                
                System.out.println(column.name());                
                Collection<IColumn> subColumns = column.getSubColumns();
                for ( IColumn subColumn : subColumns )
                {
                    System.out.println(subColumn);
                }                            
            }
        }
        catch ( Throwable th )
        {
            th.printStackTrace();
        }
    }
    
    private static void doCheck() throws Throwable
    {
        BufferedRandomAccessFile fis = new BufferedRandomAccessFile("C:\\Engagements\\buff.dat", "r", 4*1024*1024);
        IndexHelper.skipBloomFilterAndIndex(fis);
        byte[] bytes = new byte[(int)(fis.length() - fis.getFilePointer())];
        fis.readFully(bytes);
        DataInputBuffer bufIn = new DataInputBuffer();
        bufIn.reset(bytes, bytes.length);
        
        ColumnFamily cf = ColumnFamily.serializer().deserialize(bufIn);        
        Collection<IColumn> columns = cf.getAllColumns();       
        System.out.println(columns.size());
        for ( IColumn column : columns )
        {
            System.out.println(column.name());
        }
        fis.close();
    }
    
    public static void main(String[] args) throws Throwable
    {  
        // System.out.println( lastIndexOf("ababcbc", "abc") );
        /*
        String name = "/var/cassandra/test.dat";
        FileInputStream f = new FileInputStream(name);
        File file = new File("/var/cassandra");
        Path path = file.toPath();
        WatchService watcher = FileSystems.getDefault().newWatchService(); 
        Thread thread = new Thread( new WatchKeyMonitor(watcher) );
        thread.start();
        
        WatchKey wKey = path.register( watcher, StandardWatchEventKind.ENTRY_DELETE );          
        file = new File(name);
        file.delete();
        
        Thread.sleep(3000);
        System.out.println("Closing the stream ...");
        f.close();
        */
           
        //LogUtil.init();
        //StorageService s = StorageService.instance();
        //s.start();   
        // doRead();
        // doWrite();
        // doCheck();
        // doBuffered();
        
        /*
        FileOutputStream fos = new FileOutputStream("C:\\Engagements\\Test.dat", true);
        SequentialScanner scanner = new SequentialScanner("Mailbox");            
        int count = 0;
        while ( scanner.hasNext() )
        {
            Row row = scanner.next();  
            String value = row.key() + System.getProperty("line.separator");
            fos.write( value.getBytes() );
           
            Map<String, ColumnFamily> cfs = row.getColumnFamilyMap();
            Set<String> keys = cfs.keySet();
            
            for ( String key : keys )
            {
                System.out.println(row.getColumnFamily(key));
            }           
        }          
        fos.close();
        System.out.println("Done ...");
        */
        /*
        ExecutorService es = new DebuggableThreadPoolExecutor(1, 1, Integer.MAX_VALUE, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(), new ThreadFactoryImpl("TEST"));
        es.execute(new TestImpl());
        */
        /*
        LogUtil.init();
        StorageService s = StorageService.instance();
        s.start();
        */
        /*
        ReadMessage readMessage = new ReadMessage("Mailbox", args[1], "Test");
        Message message = ReadMessage.makeReadMessage(readMessage);
        Runnable task = new MessageDeliveryTask(message);
               
        ExecutorService es = new ContinuationsExecutor(1, 1, Integer.MAX_VALUE, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(), new ThreadFactoryImpl("TEST"));
        int end = Integer.parseInt(args[0]);
        for ( int i = 0; i < end; ++i )
        {
            es.execute(task);
        }
        */
        
        /*
        if ( args[0].equals("S") )
        {  
            ExecutorService es = new ContinuationsExecutor(1, 1, Integer.MAX_VALUE, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>() );               
            es.execute( new Scanner() );
        }
        */
        /*
        DataOutputBuffer bufOut = new DataOutputBuffer();
        String value = "Avinash Lakshman";
        for ( int i = 0; i < 100; ++i )
        {
            bufOut.writeUTF(Integer.toString(i));
            bufOut.writeInt(value.length());
            bufOut.write(value.getBytes());                        
        }
        
        DataInputBuffer bufIn = new DataInputBuffer();
        bufIn.reset(bufOut.getData(), bufOut.getLength());
        DataOutputBuffer buffer = new DataOutputBuffer();        
        IFileWriter writer = SequenceFile.aioWriter("C:\\Engagements\\test.dat", 64*1024);
        SortedMap<String, Integer> offsets = getOffsets(bufIn);
        Set<String> keys = offsets.keySet();                                                      
        for ( String key : keys )
        {
            bufIn.setPosition(offsets.get(key));
            buffer.reset();
            buffer.write(bufIn, bufIn.readInt());
            writer.append(key, buffer);            
        }
        writer.close();
        */
    }
}

class Scanner implements Runnable
{   
    private static final Logger logger_ = Logger.getLogger(Scanner.class);
    
    public void run()
    {        
/*        try
        {            
            SequentialScanner scanner = new SequentialScanner("Mailbox");            
            
            while ( scanner.hasNext() )
            {
                Row row = scanner.next();    
                logger_.debug(row.key());
            }            
        }
        catch ( IOException ex )
        {
            ex.printStackTrace();
        }        
        */
    }
}

class Test 
{
    public static void goo()
    {
        System.out.println("I am goo()");
    }
}
