
public class CassandraServer implements Cassandra.Iface
{
    private final static List<column_t> EMPTY_COLUMNS = Arrays.asList();
    private final static List<superColumn_t> EMPTY_SUPERCOLUMNS = Arrays.asList();

    /*
      * Handle to the storage service to interact with the other machines in the
      * cluster.
      */
	protected StorageService storageService;

    public CassandraServer()
	{
		storageService = StorageService.instance();
	}

	/*
	 * The start function initializes the server and start's listening on the
	 * specified port.
	 */
	public void start()
    {
		storageService.start();
	}
	
	private void validateCommand(String key, String tablename, String... columnFamilyNames) 
    {
        Table table = Table.open(tablename);
        for (String cfName : columnFamilyNames)
        {
            check@@ table.getColumnFamilies().contains(cfName));
        }
	}
    
	protected ColumnFamily readColumnFamily(ReadCommand command) 
    {
        String cfName = command.getColumnFamilyName();
        validateCommand(command.key, command.table, cfName);

        Row row = StorageProxy.readProtocol(command, StorageService.ConsistencyLevel.WEAK);

        return row.getColumnFamily(cfName);
	}

    public List<column_t> thriftifyColumns(Collection<IColumn> columns)
    {
        ArrayList<column_t> thriftColumns = new ArrayList<column_t>(columns.size());
        for (IColumn column : columns)
        {
            if (column.isMarkedForDelete())
            {
                continue;
            }
            column_t thrift_column = new column_t(column.name(), column.value(), column.timestamp());
            thriftColumns.add(thrift_column);
        }

        return thriftColumns;
    }

    public List<column_t> get_columns_since(String tablename, String key, String columnParent, long timeStamp) 
    {
        ColumnFamily cfamily = readColumnFamily(new ColumnsSinceReadCommand(tablename, key, columnParent, timeStamp));
        String[] values = RowMutation.getColumnAndColumnFamily(columnParent);
    
        Collection<IColumn> columns = null;
        if( values.length > 1 )
        {
            // this is the super column case
            IColumn column = cfamily.getColumn(values[1]);
            columns = column.getSubColumns();
        }
        else
        {
            columns = cfamily.getAllColumns();
        }
        return thriftifyColumns(columns);
	}
	

    public List<column_t> get_slice_by_names(String tablename, String key, String columnParent, List<String> columnNames)
    {
        ColumnFamily cfamily = readColumnFamily(new SliceByNamesReadCommand(tablename, key, columnParent, columnNames));
        return thriftifyColumns(cfamily.getAllColumns());
    }
    
    public List<column_t> get_slice(String tablename, String key, String columnParent, int start, int count) 
    {
        String[] values = RowMutation.getColumnAndColumnFamily(columnParent);
        ColumnFamily cfamily = readColumnFamily(new SliceReadCommand(tablename, key, columnParent, start, count));
  
        Collection<IColumn> columns = null;
        if( values.length > 1 )
        {
            // this is the super column case
            IColumn column = cfamily.getColumn(values[1]);
            columns = column.getSubColumns();
        }
        else
        {
            columns = cfamily.getAllColumns();
        }

        return thriftifyColumns(columns);
	}
    
    public column_t get_column(String tablename, String key, String columnPath) 
    {
        String[] values = RowMutation.getColumnAndColumnFamily(columnPath);
    
        ColumnReadCommand readCommand = new ColumnReadCommand(tablename, key, columnPath);
        ColumnFamily cfamily = readColumnFamily(readCommand);
        Collection<IColumn> columns = null;
        if( values.length > 2 )
        {
            // this is the super column case
            IColumn column = cfamily.getColumn(values[1]);
            columns = column.getSubColumns();
        }
        else
        {
            columns = cfamily.getAllColumns();
        }
    
        assert columns.size() == 1;
        IColumn column = columns.iterator().next();
    
        return new column_t(column.name(), column.value(), column.timestamp());
    }
    

    public int get_column_count(String tablename, String key, String columnParent) 
    {
        logger.debug("get_column_count");
        String[] values = RowMutation.getColumnAndColumnFamily(columnParent);
        ColumnFamily cfamily = readColumnFamily(new SliceReadCommand(tablename, key, columnParent, -1, Integer.MAX_VALUE));
        
        Collection<IColumn> columns = null;
        if( values.length > 1 )
        {
            // this is the super column case
            IColumn column = cfamily.getColumn(values[1]);
            if(column != null)
                columns = column.getSubColumns();
        }
        else
        {
            columns = cfamily.getAllColumns();
        }
        if (columns == null || columns.size() == 0)
        {
            return 0;
        }
        return columns.size();
	}

    public void insert(String tablename, String key, String columnPath, byte[] cellData, long timestamp, boolean block)
    {
        RowMutation rm = new RowMutation(tablename, key.trim());
        rm.add(columnPath, cellData, timestamp);
        Set<String> cfNames = rm.columnFamilyNames();
        validateCommand(rm.key(), rm.table(), cfNames.toArray(new String[cfNames.size()]));

        block? StorageProxy.insertBlocking(rm) : StorageProxy.insert(rm);
    }

    public void batch_insert(batch_mutation_t batchMutation, boolean block) 
    {
        RowMutation rm = RowMutation.getRowMutation(batchMutation);
        Set<String> cfNames = rm.columnFamilyNames();
        validateCommand(rm.key(), rm.table(), cfNames.toArray(new String[cfNames.size()]));

        block? StorageProxy.insertBlocking(rm) : StorageProxy.insert(rm);
    }

    public void remove(String tablename, String key, String columnPathOrParent, long timestamp, boolean block)
    throws InvalidRequestException, UnavailableException
    {
        RowMutation rm = new RowMutation(tablename, key.trim());
        rm.delete(columnPathOrParent, timestamp);
        Set<String> cfNames = rm.columnFamilyNames();
        validateCommand(rm.key(), rm.table(), cfNames.toArray(new String[cfNames.size()]));
        if (block)
        {
            StorageProxy.insertBlocking(rm);
        }
        else
        {
            StorageProxy.insert(rm);
        }
	}

    public List<superColumn_t> get_slice_super_by_names(
        String tablename, String key, String columnFamily, List<String> superColumnNames)
    {
        ColumnFamily cfamily = readColumnFamily(new SliceByNamesReadCommand(tablename, key, columnFamily, superColumnNames));
        return thriftifySuperColumns(cfamily.getAllColumns());
    }

    private List<superColumn_t> thriftifySuperColumns(Collection<IColumn> columns)
    {
        ArrayList<superColumn_t> thriftSuperColumns = new ArrayList<superColumn_t>(columns.size());
        for (IColumn column : columns)
        {
            List<column_t> subcolumns = thriftifyColumns(column.getSubColumns());
            if (subcolumns.isEmpty())
            {
                continue;
            }
            thriftSuperColumns.add(new superColumn_t(column.name(), subcolumns));
        }

        return thriftSuperColumns;
    }

    public List<superColumn_t> get_slice_super(String tablename, String key, String columnFamily, int start, int count) throws InvalidRequestException
    {
        ColumnFamily cfamily = readColumnFamily(new SliceReadCommand(tablename, key, columnFamily, start, count));

        Collection<IColumn> columns = cfamily.getAllColumns();
        return thriftifySuperColumns(columns);
    }
    
    public superColumn_t get_superColumn(String tablename, String key, String superColumnPath) throws InvalidRequestException, NotFoundException
    {
        logger.debug("get_superColumn");
        ColumnFamily cfamily = readColumnFamily(new ColumnReadCommand(tablename, key, superColumnPath));
        
        Collection<IColumn> columns = cfamily.getAllColumns();
        
        IColumn column = columns.iterator().next();
        
        return new superColumn_t(column.name(), thriftifyColumns(column.getSubColumns()));
    }

    public void batch_insert_superColumn(batch_mutation_super_t batchMutationSuper, boolean block)
    {
        RowMutation rm = RowMutation.getRowMutation(batchMutationSuper);
        Set<String> cfNames = rm.columnFamilyNames();
        validateCommand(rm.key(), rm.table(), cfNames.toArray(new String[cfNames.size()]));
        if (block)
        {
            StorageProxy.insertBlocking(rm);
        }
        else
        {
            StorageProxy.insert(rm);
        }
    }

    public String getStringProperty(String propertyName)
    {
        if (propertyName == "cluster name")
        {
            return DatabaseDescriptor.getClusterName();
        }
        else if (propertyName == "config file")
        {
            String filename = DatabaseDescriptor.getConfigFileName();
            StringBuffer fileData = new StringBuffer(8192);
            BufferedInputStream stream = new BufferedInputStream(new FileInputStream(filename));
            byte[] buf = new byte[1024];
            int numRead;
            while( (numRead = stream.read(buf)) != -1)
            {
                String str = new String(buf, 0, numRead);
                fileData.append(str);
            }
            stream.close();
            
            return fileData.toString();
        }
        else if (propertyName.equals("version"))
        {
            return "0.3.0";
        }
        else
        {
            return "?";
        }
    }

    public List<String> getStringListProperty(String propertyName)
    {
        if (propertyName.equals("tables"))
        {
            return DatabaseDescriptor.getTables();        
        }
        else
        {
            return new ArrayList<String>();
        }
    }

    public String describeTable(String tableName)
    {
        String desc = "";
        Map<String, CFMetaData> tableMetaData = DatabaseDescriptor.getTableMetaData(tableName);

        Iterator iter = tableMetaData.entrySet().iterator();
        while (iter.hasNext())
        {
            Map.Entry<String, CFMetaData> pairs = (Map.Entry<String, CFMetaData>)iter.next();
            desc = desc + pairs.getValue().pretty() + "-----\n";
        }
        return desc;
    }

    public CqlResult_t executeQuery(String query) throws TException
    {
        CqlResult_t result = new CqlResult_t();

        CqlResult cqlResult = CqlDriver.executeQuery(query);
        
        // convert CQL result type to Thrift specific return type
        if (cqlResult != null)
        {
            result.errorTxt = cqlResult.errorTxt;
            result.resultSet = cqlResult.resultSet;
            result.errorCode = cqlResult.errorCode;
        }
        return result;
    }

    public List<String> get_key_range(String tablename, String startWith, String stopAt, int maxResults) 
    {
        logger.debug("get_key_range");
        if (!(StorageService.getPartitioner() instanceof OrderPreservingPartitioner))
        {
            throw new InvalidRequestException("range queries may only be performed against an order-preserving partitioner");
        }

        return StorageProxy.getKeyRange(new RangeCommand(tablename, startWith, stopAt, maxResults));
    }

    // main method moved to CassandraDaemon
}
