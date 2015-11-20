
public class Cassandra {

  public interface Iface {

    public List<column_t> 
    get_slice(String tablename, String key, String columnParent, int start, int count) ;

    public List<column_t> 
    get_slice_by_names(String tablename, String key, String columnParent, List<String> columnNames);

    public column_t 
    get_column(String tablename, String key, String columnPath);

    public int 
    get_column_count(String tablename, String key, String columnParent);

    public void 
    insert(String tablename, String key, String columnPath, byte[] cellData, long timestamp, boolean block);

    public void 
    batch_insert(batch_mutation_t batchMutation, boolean block) throws InvalidRequestException, UnavailableException, TException;

    public void 
    remove(String tablename, String key, String columnPathOrParent, long timestamp, boolean block) throws InvalidRequestException, UnavailableException, TException;

    public List<column_t> 
    get_columns_since(String tablename, String key, String columnParent, long timeStamp) throws InvalidRequestException, NotFoundException, TException;

    public List<superColumn_t> 
    get_slice_super(String tablename, String key, String columnFamily, int start, int count) throws InvalidRequestException, TException;

    public List<superColumn_t> 
    get_slice_super_by_names(String tablename, String key, String columnFamily, List<String> superColumnNames) throws InvalidRequestException, TException;

    public superColumn_t 
    get_superColumn(String tablename, String key, String superColumnPath) throws InvalidRequestException, NotFoundException, TException;

    public void 
    batch_insert_superColumn(batch_mutation_super_t batchMutationSuper, boolean block);
    
    public List<String> 
    get_key_range(String tablename, String startWith, String stopAt, int maxResults);

    public String 
    getStringProperty(String propertyName) throws TException;

    public List<String> 
    getStringListProperty(String propertyName) throws TException;

    public String 
    describeTable(String tableName) throws TException;

    public CqlResult_t 
    executeQuery(String query) throws TException;

  }

  public static class Client implements Iface {
    public Client(TProtocol prot)
    {
      this(prot, prot);
    }

    public Client(TProtocol iprot, TProtocol oprot)
    {
      iprot_ = iprot;
      oprot_ = oprot;
    }

    protected TProtocol iprot_;
    protected TProtocol oprot_;

    protected int seqid_;

    public TProtocol getInputProtocol()
    {
      return this.iprot_;
    }

    public TProtocol getOutputProtocol()
    {
      return this.oprot_;
    }

    public List<column_t> get_slice(String tablename, String key, String columnParent, int start, int count) throws InvalidRequestException, NotFoundException, TException
    {
      send_get_slice(tablename, key, columnParent, start, count);
      return recv_get_slice();
    }

    public void send_get_slice(String tablename, String key, String columnParent, int start, int count) throws TException
    {
      oprot_.writeMessageBegin(new TMessage("get_slice", TMessageType.CALL, seqid_));
      get_slice_args args = new get_slice_args();
      args.tablename = tablename;
      args.key = key;
      args.columnParent = columnParent;
      args.start = start;
      args.count = count;
      args.write(oprot_);
      oprot_.writeMessageEnd();
      oprot_.getTransport().flush();
    }

    public List<column_t> recv_get_slice() throws InvalidRequestException, NotFoundException, TException
    {
      TMessage msg = iprot_.readMessageBegin();
      if (msg.type == TMessageType.EXCEPTION) {
        TApplicationException x = TApplicationException.read(iprot_);
        iprot_.readMessageEnd();
        throw x;
      }
      get_slice_result result = new get_slice_result();
      result.read(iprot_);
      iprot_.readMessageEnd();
      if (result.isSetSuccess()) {
        return result.success;
      }
      if (result.ire != null) {
        throw result.ire;
      }
      if (result.nfe != null) {
        throw result.nfe;
      }
      throw new TApplicationException(TApplicationException.MISSING_RESULT, "get_slice failed: unknown result");
    }

    public List<column_t> get_slice_by_names(String tablename, String key, String columnParent, List<String> columnNames) throws InvalidRequestException, NotFoundException, TException
    {
      send_get_slice_by_names(tablename, key, columnParent, columnNames);
      return recv_get_slice_by_names();
    }

    public void send_get_slice_by_names(String tablename, String key, String columnParent, List<String> columnNames) throws TException
    {
      oprot_.writeMessageBegin(new TMessage("get_slice_by_names", TMessageType.CALL, seqid_));
      get_slice_by_names_args args = new get_slice_by_names_args();
      args.tablename = tablename;
      args.key = key;
      args.columnParent = columnParent;
      args.columnNames = columnNames;
      args.write(oprot_);
      oprot_.writeMessageEnd();
      oprot_.getTransport().flush();
    }

    public List<column_t> recv_get_slice_by_names() throws InvalidRequestException, NotFoundException, TException
    {
      TMessage msg = iprot_.readMessageBegin();
      if (msg.type == TMessageType.EXCEPTION) {
        TApplicationException x = TApplicationException.read(iprot_);
        iprot_.readMessageEnd();
        throw x;
      }
      get_slice_by_names_result result = new get_slice_by_names_result();
      result.read(iprot_);
      iprot_.readMessageEnd();
      if (result.isSetSuccess()) {
        return result.success;
      }
      if (result.ire != null) {
        throw result.ire;
      }
      if (result.nfe != null) {
        throw result.nfe;
      }
      throw new TApplicationException(TApplicationException.MISSING_RESULT, "get_slice_by_names failed: unknown result");
    }

    public column_t get_column(String tablename, String key, String columnPath) throws InvalidRequestException, NotFoundException, TException
    {
      send_get_column(tablename, key, columnPath);
      return recv_get_column();
    }

    public void send_get_column(String tablename, String key, String columnPath) throws TException
    {
      oprot_.writeMessageBegin(new TMessage("get_column", TMessageType.CALL, seqid_));
      get_column_args args = new get_column_args();
      args.tablename = tablename;
      args.key = key;
      args.columnPath = columnPath;
      args.write(oprot_);
      oprot_.writeMessageEnd();
      oprot_.getTransport().flush();
    }

    public column_t recv_get_column() throws InvalidRequestException, NotFoundException, TException
    {
      TMessage msg = iprot_.readMessageBegin();
      if (msg.type == TMessageType.EXCEPTION) {
        TApplicationException x = TApplicationException.read(iprot_);
        iprot_.readMessageEnd();
        throw x;
      }
      get_column_result result = new get_column_result();
      result.read(iprot_);
      iprot_.readMessageEnd();
      if (result.isSetSuccess()) {
        return result.success;
      }
      if (result.ire != null) {
        throw result.ire;
      }
      if (result.nfe != null) {
        throw result.nfe;
      }
      throw new TApplicationException(TApplicationException.MISSING_RESULT, "get_column failed: unknown result");
    }

    public int get_column_count(String tablename, String key, String columnParent) throws InvalidRequestException, TException
    {
      send_get_column_count(tablename, key, columnParent);
      return recv_get_column_count();
    }

    public void send_get_column_count(String tablename, String key, String columnParent) throws TException
    {
      oprot_.writeMessageBegin(new TMessage("get_column_count", TMessageType.CALL, seqid_));
      get_column_count_args args = new get_column_count_args();
      args.tablename = tablename;
      args.key = key;
      args.columnParent = columnParent;
      args.write(oprot_);
      oprot_.writeMessageEnd();
      oprot_.getTransport().flush();
    }

    public int recv_get_column_count() throws InvalidRequestException, TException
    {
      TMessage msg = iprot_.readMessageBegin();
      if (msg.type == TMessageType.EXCEPTION) {
        TApplicationException x = TApplicationException.read(iprot_);
        iprot_.readMessageEnd();
        throw x;
      }
      get_column_count_result result = new get_column_count_result();
      result.read(iprot_);
      iprot_.readMessageEnd();
      if (result.isSetSuccess()) {
        return result.success;
      }
      if (result.ire != null) {
        throw result.ire;
      }
      throw new TApplicationException(TApplicationException.MISSING_RESULT, "get_column_count failed: unknown result");
    }

    public void insert(String tablename, String key, String columnPath, byte[] cellData, long timestamp, boolean block) throws InvalidRequestException, UnavailableException, TException
    {
      send_insert(tablename, key, columnPath, cellData, timestamp, block);
      recv_insert();
    }

    public void send_insert(String tablename, String key, String columnPath, byte[] cellData, long timestamp, boolean block) throws TException
    {
      oprot_.writeMessageBegin(new TMessage("insert", TMessageType.CALL, seqid_));
      insert_args args = new insert_args();
      args.tablename = tablename;
      args.key = key;
      args.columnPath = columnPath;
      args.cellData = cellData;
      args.timestamp = timestamp;
      args.block = block;
      args.write(oprot_);
      oprot_.writeMessageEnd();
      oprot_.getTransport().flush();
    }

    public void recv_insert() throws InvalidRequestException, UnavailableException, TException
    {
      TMessage msg = iprot_.readMessageBegin();
      if (msg.type == TMessageType.EXCEPTION) {
        TApplicationException x = TApplicationException.read(iprot_);
        iprot_.readMessageEnd();
        throw x;
      }
      insert_result result = new insert_result();
      result.read(iprot_);
      iprot_.readMessageEnd();
      if (result.ire != null) {
        throw result.ire;
      }
      if (result.ue != null) {
        throw result.ue;
      }
      return;
    }

    public void batch_insert(batch_mutation_t batchMutation, boolean block) throws InvalidRequestException, UnavailableException, TException
    {
      send_batch_insert(batchMutation, block);
      recv_batch_insert();
    }

    public void send_batch_insert(batch_mutation_t batchMutation, boolean block) throws TException
    {
      oprot_.writeMessageBegin(new TMessage("batch_insert", TMessageType.CALL, seqid_));
      batch_insert_args args = new batch_insert_args();
      args.batchMutation = batchMutation;
      args.block = block;
      args.write(oprot_);
      oprot_.writeMessageEnd();
      oprot_.getTransport().flush();
    }

    public void recv_batch_insert() throws InvalidRequestException, UnavailableException, TException
    {
      TMessage msg = iprot_.readMessageBegin();
      if (msg.type == TMessageType.EXCEPTION) {
        TApplicationException x = TApplicationException.read(iprot_);
        iprot_.readMessageEnd();
        throw x;
      }
      batch_insert_result result = new batch_insert_result();
      result.read(iprot_);
      iprot_.readMessageEnd();
      if (result.ire != null) {
        throw result.ire;
      }
      if (result.ue != null) {
        throw result.ue;
      }
      return;
    }

    public void remove(String tablename, String key, String columnPathOrParent, long timestamp, boolean block) throws InvalidRequestException, UnavailableException, TException
    {
      send_remove(tablename, key, columnPathOrParent, timestamp, block);
      recv_remove();
    }

    public void send_remove(String tablename, String key, String columnPathOrParent, long timestamp, boolean block) throws TException
    {
      oprot_.writeMessageBegin(new TMessage("remove", TMessageType.CALL, seqid_));
      remove_args args = new remove_args();
      args.tablename = tablename;
      args.key = key;
      args.columnPathOrParent = columnPathOrParent;
      args.timestamp = timestamp;
      args.block = block;
      args.write(oprot_);
      oprot_.writeMessageEnd();
      oprot_.getTransport().flush();
    }

    public void recv_remove() throws InvalidRequestException, UnavailableException, TException
    {
      TMessage msg = iprot_.readMessageBegin();
      if (msg.type == TMessageType.EXCEPTION) {
        TApplicationException x = TApplicationException.read(iprot_);
        iprot_.readMessageEnd();
        throw x;
      }
      remove_result result = new remove_result();
      result.read(iprot_);
      iprot_.readMessageEnd();
      if (result.ire != null) {
        throw result.ire;
      }
      if (result.ue != null) {
        throw result.ue;
      }
      return;
    }

    public List<column_t> get_columns_since(String tablename, String key, String columnParent, long timeStamp) throws InvalidRequestException, NotFoundException, TException
    {
      send_get_columns_since(tablename, key, columnParent, timeStamp);
      return recv_get_columns_since();
    }

    public void send_get_columns_since(String tablename, String key, String columnParent, long timeStamp) throws TException
    {
      oprot_.writeMessageBegin(new TMessage("get_columns_since", TMessageType.CALL, seqid_));
      get_columns_since_args args = new get_columns_since_args();
      args.tablename = tablename;
      args.key = key;
      args.columnParent = columnParent;
      args.timeStamp = timeStamp;
      args.write(oprot_);
      oprot_.writeMessageEnd();
      oprot_.getTransport().flush();
    }

    public List<column_t> recv_get_columns_since() throws InvalidRequestException, NotFoundException, TException
    {
      TMessage msg = iprot_.readMessageBegin();
      if (msg.type == TMessageType.EXCEPTION) {
        TApplicationException x = TApplicationException.read(iprot_);
        iprot_.readMessageEnd();
        throw x;
      }
      get_columns_since_result result = new get_columns_since_result();
      result.read(iprot_);
      iprot_.readMessageEnd();
      if (result.isSetSuccess()) {
        return result.success;
      }
      if (result.ire != null) {
        throw result.ire;
      }
      if (result.nfe != null) {
        throw result.nfe;
      }
      throw new TApplicationException(TApplicationException.MISSING_RESULT, "get_columns_since failed: unknown result");
    }

    public List<superColumn_t> get_slice_super(String tablename, String key, String columnFamily, int start, int count) throws InvalidRequestException, TException
    {
      send_get_slice_super(tablename, key, columnFamily, start, count);
      return recv_get_slice_super();
    }

    public void send_get_slice_super(String tablename, String key, String columnFamily, int start, int count) throws TException
    {
      oprot_.writeMessageBegin(new TMessage("get_slice_super", TMessageType.CALL, seqid_));
      get_slice_super_args args = new get_slice_super_args();
      args.tablename = tablename;
      args.key = key;
      args.columnFamily = columnFamily;
      args.start = start;
      args.count = count;
      args.write(oprot_);
      oprot_.writeMessageEnd();
      oprot_.getTransport().flush();
    }

    public List<superColumn_t> recv_get_slice_super() throws InvalidRequestException, TException
    {
      TMessage msg = iprot_.readMessageBegin();
      if (msg.type == TMessageType.EXCEPTION) {
        TApplicationException x = TApplicationException.read(iprot_);
        iprot_.readMessageEnd();
        throw x;
      }
      get_slice_super_result result = new get_slice_super_result();
      result.read(iprot_);
      iprot_.readMessageEnd();
      if (result.isSetSuccess()) {
        return result.success;
      }
      if (result.ire != null) {
        throw result.ire;
      }
      throw new TApplicationException(TApplicationException.MISSING_RESULT, "get_slice_super failed: unknown result");
    }

    public List<superColumn_t> get_slice_super_by_names(String tablename, String key, String columnFamily, List<String> superColumnNames) throws InvalidRequestException, TException
    {
      send_get_slice_super_by_names(tablename, key, columnFamily, superColumnNames);
      return recv_get_slice_super_by_names();
    }

    public void send_get_slice_super_by_names(String tablename, String key, String columnFamily, List<String> superColumnNames) throws TException
    {
      oprot_.writeMessageBegin(new TMessage("get_slice_super_by_names", TMessageType.CALL, seqid_));
      get_slice_super_by_names_args args = new get_slice_super_by_names_args();
      args.tablename = tablename;
      args.key = key;
      args.columnFamily = columnFamily;
      args.superColumnNames = superColumnNames;
      args.write(oprot_);
      oprot_.writeMessageEnd();
      oprot_.getTransport().flush();
    }

    public List<superColumn_t> recv_get_slice_super_by_names() throws InvalidRequestException, TException
    {
      TMessage msg = iprot_.readMessageBegin();
      if (msg.type == TMessageType.EXCEPTION) {
        TApplicationException x = TApplicationException.read(iprot_);
        iprot_.readMessageEnd();
        throw x;
      }
      get_slice_super_by_names_result result = new get_slice_super_by_names_result();
      result.read(iprot_);
      iprot_.readMessageEnd();
      if (result.isSetSuccess()) {
        return result.success;
      }
      if (result.ire != null) {
        throw result.ire;
      }
      throw new TApplicationException(TApplicationException.MISSING_RESULT, "get_slice_super_by_names failed: unknown result");
    }

    public superColumn_t get_superColumn(String tablename, String key, String superColumnPath) throws InvalidRequestException, NotFoundException, TException
    {
      send_get_superColumn(tablename, key, superColumnPath);
      return recv_get_superColumn();
    }

    public void send_get_superColumn(String tablename, String key, String superColumnPath) throws TException
    {
      oprot_.writeMessageBegin(new TMessage("get_superColumn", TMessageType.CALL, seqid_));
      get_superColumn_args args = new get_superColumn_args();
      args.tablename = tablename;
      args.key = key;
      args.superColumnPath = superColumnPath;
      args.write(oprot_);
      oprot_.writeMessageEnd();
      oprot_.getTransport().flush();
    }

    public superColumn_t recv_get_superColumn() throws InvalidRequestException, NotFoundException, TException
    {
      TMessage msg = iprot_.readMessageBegin();
      if (msg.type == TMessageType.EXCEPTION) {
        TApplicationException x = TApplicationException.read(iprot_);
        iprot_.readMessageEnd();
        throw x;
      }
      get_superColumn_result result = new get_superColumn_result();
      result.read(iprot_);
      iprot_.readMessageEnd();
      if (result.isSetSuccess()) {
        return result.success;
      }
      if (result.ire != null) {
        throw result.ire;
      }
      if (result.nfe != null) {
        throw result.nfe;
      }
      throw new TApplicationException(TApplicationException.MISSING_RESULT, "get_superColumn failed: unknown result");
    }

    public void batch_insert_superColumn(batch_mutation_super_t batchMutationSuper, boolean block) throws InvalidRequestException, UnavailableException, TException
    {
      send_batch_insert_superColumn(batchMutationSuper, block);
      recv_batch_insert_superColumn();
    }

    public void send_batch_insert_superColumn(batch_mutation_super_t batchMutationSuper, boolean block) throws TException
    {
      oprot_.writeMessageBegin(new TMessage("batch_insert_superColumn", TMessageType.CALL, seqid_));
      batch_insert_superColumn_args args = new batch_insert_superColumn_args();
      args.batchMutationSuper = batchMutationSuper;
      args.block = block;
      args.write(oprot_);
      oprot_.writeMessageEnd();
      oprot_.getTransport().flush();
    }

    public void recv_batch_insert_superColumn() throws InvalidRequestException, UnavailableException, TException
    {
      TMessage msg = iprot_.readMessageBegin();
      if (msg.type == TMessageType.EXCEPTION) {
        TApplicationException x = TApplicationException.read(iprot_);
        iprot_.readMessageEnd();
        throw x;
      }
      batch_insert_superColumn_result result = new batch_insert_superColumn_result();
      result.read(iprot_);
      iprot_.readMessageEnd();
      if (result.ire != null) {
        throw result.ire;
      }
      if (result.ue != null) {
        throw result.ue;
      }
      return;
    }

    public List<String> get_key_range(String tablename, String startWith, String stopAt, int maxResults) throws InvalidRequestException, TException
    {
      send_get_key_range(tablename, startWith, stopAt, maxResults);
      return recv_get_key_range();
    }

    public void send_get_key_range(String tablename, String startWith, String stopAt, int maxResults) throws TException
    {
      oprot_.writeMessageBegin(new TMessage("get_key_range", TMessageType.CALL, seqid_));
      get_key_range_args args = new get_key_range_args();
      args.tablename = tablename;
      args.startWith = startWith;
      args.stopAt = stopAt;
      args.maxResults = maxResults;
      args.write(oprot_);
      oprot_.writeMessageEnd();
      oprot_.getTransport().flush();
    }

    public List<String> recv_get_key_range() throws InvalidRequestException, TException
    {
      TMessage msg = iprot_.readMessageBegin();
      if (msg.type == TMessageType.EXCEPTION) {
        TApplicationException x = TApplicationException.read(iprot_);
        iprot_.readMessageEnd();
        throw x;
      }
      get_key_range_result result = new get_key_range_result();
      result.read(iprot_);
      iprot_.readMessageEnd();
      if (result.isSetSuccess()) {
        return result.success;
      }
      if (result.ire != null) {
        throw result.ire;
      }
      throw new TApplicationException(TApplicationException.MISSING_RESULT, "get_key_range failed: unknown result");
    }

    public String getStringProperty(String propertyName) throws TException
    {
      send_getStringProperty(propertyName);
      return recv_getStringProperty();
    }

    public void send_getStringProperty(String propertyName) throws TException
    {
      oprot_.writeMessageBegin(new TMessage("getStringProperty", TMessageType.CALL, seqid_));
      getStringProperty_args args = new getStringProperty_args();
      args.propertyName = propertyName;
      args.write(oprot_);
      oprot_.writeMessageEnd();
      oprot_.getTransport().flush();
    }

    public String recv_getStringProperty() throws TException
    {
      TMessage msg = iprot_.readMessageBegin();
      if (msg.type == TMessageType.EXCEPTION) {
        TApplicationException x = TApplicationException.read(iprot_);
        iprot_.readMessageEnd();
        throw x;
      }
      getStringProperty_result result = new getStringProperty_result();
      result.read(iprot_);
      iprot_.readMessageEnd();
      if (result.isSetSuccess()) {
        return result.success;
      }
      throw new TApplicationException(TApplicationException.MISSING_RESULT, "getStringProperty failed: unknown result");
    }

    public List<String> getStringListProperty(String propertyName) throws TException
    {
      send_getStringListProperty(propertyName);
      return recv_getStringListProperty();
    }

    public void send_getStringListProperty(String propertyName) throws TException
    {
      oprot_.writeMessageBegin(new TMessage("getStringListProperty", TMessageType.CALL, seqid_));
      getStringListProperty_args args = new getStringListProperty_args();
      args.propertyName = propertyName;
      args.write(oprot_);
      oprot_.writeMessageEnd();
      oprot_.getTransport().flush();
    }

    public List<String> recv_getStringListProperty() throws TException
    {
      TMessage msg = iprot_.readMessageBegin();
      if (msg.type == TMessageType.EXCEPTION) {
        TApplicationException x = TApplicationException.read(iprot_);
        iprot_.readMessageEnd();
        throw x;
      }
      getStringListProperty_result result = new getStringListProperty_result();
      result.read(iprot_);
      iprot_.readMessageEnd();
      if (result.isSetSuccess()) {
        return result.success;
      }
      throw new TApplicationException(TApplicationException.MISSING_RESULT, "getStringListProperty failed: unknown result");
    }

    public String describeTable(String tableName) throws TException
    {
      send_describeTable(tableName);
      return recv_describeTable();
    }

    public void send_describeTable(String tableName) throws TException
    {
      oprot_.writeMessageBegin(new TMessage("describeTable", TMessageType.CALL, seqid_));
      describeTable_args args = new describeTable_args();
      args.tableName = tableName;
      args.write(oprot_);
      oprot_.writeMessageEnd();
      oprot_.getTransport().flush();
    }

    public String recv_describeTable() throws TException
    {
      TMessage msg = iprot_.readMessageBegin();
      if (msg.type == TMessageType.EXCEPTION) {
        TApplicationException x = TApplicationException.read(iprot_);
        iprot_.readMessageEnd();
        throw x;
      }
      describeTable_result result = new describeTable_result();
      result.read(iprot_);
      iprot_.readMessageEnd();
      if (result.isSetSuccess()) {
        return result.success;
      }
      throw new TApplicationException(TApplicationException.MISSING_RESULT, "describeTable failed: unknown result");
    }

    public CqlResult_t executeQuery(String query) throws TException
    {
      send_executeQuery(query);
      return recv_executeQuery();
    }

    public void send_executeQuery(String query) throws TException
    {
      oprot_.writeMessageBegin(new TMessage("executeQuery", TMessageType.CALL, seqid_));
      executeQuery_args args = new executeQuery_args();
      args.query = query;
      args.write(oprot_);
      oprot_.writeMessageEnd();
      oprot_.getTransport().flush();
    }

    public CqlResult_t recv_executeQuery() throws TException
    {
      TMessage msg = iprot_.readMessageBegin();
      if (msg.type == TMessageType.EXCEPTION) {
        TApplicationException x = TApplicationException.read(iprot_);
        iprot_.readMessageEnd();
        throw x;
      }
      executeQuery_result result = new executeQuery_result();
      result.read(iprot_);
      iprot_.readMessageEnd();
      if (result.isSetSuccess()) {
        return result.success;
      }
      throw new TApplicationException(TApplicationException.MISSING_RESULT, "executeQuery failed: unknown result");
    }

  }
  public static class Processor implements TProcessor {
    private static final Logger LOGGER = Logger.getLogger(Processor.class.getName());
    public Processor(Iface iface)
    {
      iface_ = iface;
      processMap_.put("get_slice", new get_slice());
      processMap_.put("get_slice_by_names", new get_slice_by_names());
      processMap_.put("get_column", new get_column());
      processMap_.put("get_column_count", new get_column_count());
      processMap_.put("insert", new insert());
      processMap_.put("batch_insert", new batch_insert());
      processMap_.put("remove", new remove());
      processMap_.put("get_columns_since", new get_columns_since());
      processMap_.put("get_slice_super", new get_slice_super());
      processMap_.put("get_slice_super_by_names", new get_slice_super_by_names());
      processMap_.put("get_superColumn", new get_superColumn());
      processMap_.put("batch_insert_superColumn", new batch_insert_superColumn());
      processMap_.put("get_key_range", new get_key_range());
      processMap_.put("getStringProperty", new getStringProperty());
      processMap_.put("getStringListProperty", new getStringListProperty());
      processMap_.put("describeTable", new describeTable());
      processMap_.put("executeQuery", new executeQuery());
    }

    protected static interface ProcessFunction {
      public void process(int seqid, TProtocol iprot, TProtocol oprot) throws TException;
    }

    private Iface iface_;
    protected final HashMap<String,ProcessFunction> processMap_ = new HashMap<String,ProcessFunction>();

    public boolean process(TProtocol iprot, TProtocol oprot) throws TException
    {
      TMessage msg = iprot.readMessageBegin();
      ProcessFunction fn = processMap_.get(msg.name);
      if (fn == null) {
        TProtocolUtil.skip(iprot, TType.STRUCT);
        iprot.readMessageEnd();
        TApplicationException x = new TApplicationException(TApplicationException.UNKNOWN_METHOD, "Invalid method name: '"+msg.name+"'");
        oprot.writeMessageBegin(new TMessage(msg.name, TMessageType.EXCEPTION, msg.seqid));
        x.write(oprot);
        oprot.writeMessageEnd();
        oprot.getTransport().flush();
        return true;
      }
      fn.process(msg.seqid, iprot, oprot);
      return true;
    }

    private class get_slice implements ProcessFunction {
      public void process(int seqid, TProtocol iprot, TProtocol oprot) throws TException
      {
        get_slice_args args = new get_slice_args();
        args.read(iprot);
        iprot.readMessageEnd();
        get_slice_result result = new get_slice_result();
        try {
          result.success = iface_.get_slice(args.tablename, args.key, args.columnParent, args.start, args.count);
        } catch (InvalidRequestException ire) {
          result.ire = ire;
        } catch (NotFoundException nfe) {
          result.nfe = nfe;
        } catch (Throwable th) {
          LOGGER.error("Internal error processing get_slice", th);
          TApplicationException x = new TApplicationException(TApplicationException.INTERNAL_ERROR, "Internal error processing get_slice");
          oprot.writeMessageBegin(new TMessage("get_slice", TMessageType.EXCEPTION, seqid));
          x.write(oprot);
          oprot.writeMessageEnd();
          oprot.getTransport().flush();
          return;
        }
        oprot.writeMessageBegin(new TMessage("get_slice", TMessageType.REPLY, seqid));
        result.write(oprot);
        oprot.writeMessageEnd();
        oprot.getTransport().flush();
      }

    }

    private class get_slice_by_names implements ProcessFunction {
      public void process(int seqid, TProtocol iprot, TProtocol oprot) throws TException
      {
        get_slice_by_names_args args = new get_slice_by_names_args();
        args.read(iprot);
        iprot.readMessageEnd();
        get_slice_by_names_result result = new get_slice_by_names_result();
        try {
          result.success = iface_.get_slice_by_names(args.tablename, args.key, args.columnParent, args.columnNames);
        } catch (InvalidRequestException ire) {
          result.ire = ire;
        } catch (NotFoundException nfe) {
          result.nfe = nfe;
        } catch (Throwable th) {
          LOGGER.error("Internal error processing get_slice_by_names", th);
          TApplicationException x = new TApplicationException(TApplicationException.INTERNAL_ERROR, "Internal error processing get_slice_by_names");
          oprot.writeMessageBegin(new TMessage("get_slice_by_names", TMessageType.EXCEPTION, seqid));
          x.write(oprot);
          oprot.writeMessageEnd();
          oprot.getTransport().flush();
          return;
        }
        oprot.writeMessageBegin(new TMessage("get_slice_by_names", TMessageType.REPLY, seqid));
        result.write(oprot);
        oprot.writeMessageEnd();
        oprot.getTransport().flush();
      }

    }

    private class get_column implements ProcessFunction {
      public void process(int seqid, TProtocol iprot, TProtocol oprot) throws TException
      {
        get_column_args args = new get_column_args();
        args.read(iprot);
        iprot.readMessageEnd();
        get_column_result result = new get_column_result();
        try {
          result.success = iface_.get_column(args.tablename, args.key, args.columnPath);
        } catch (InvalidRequestException ire) {
          result.ire = ire;
        } catch (NotFoundException nfe) {
          result.nfe = nfe;
        } catch (Throwable th) {
          LOGGER.error("Internal error processing get_column", th);
          TApplicationException x = new TApplicationException(TApplicationException.INTERNAL_ERROR, "Internal error processing get_column");
          oprot.writeMessageBegin(new TMessage("get_column", TMessageType.EXCEPTION, seqid));
          x.write(oprot);
          oprot.writeMessageEnd();
          oprot.getTransport().flush();
          return;
        }
        oprot.writeMessageBegin(new TMessage("get_column", TMessageType.REPLY, seqid));
        result.write(oprot);
        oprot.writeMessageEnd();
        oprot.getTransport().flush();
      }

    }

    private class get_column_count implements ProcessFunction {
      public void process(int seqid, TProtocol iprot, TProtocol oprot) throws TException
      {
        get_column_count_args args = new get_column_count_args();
        args.read(iprot);
        iprot.readMessageEnd();
        get_column_count_result result = new get_column_count_result();
        try {
          result.success = iface_.get_column_count(args.tablename, args.key, args.columnParent);
          result.__isset.success = true;
        } catch (InvalidRequestException ire) {
          result.ire = ire;
        } catch (Throwable th) {
          LOGGER.error("Internal error processing get_column_count", th);
          TApplicationException x = new TApplicationException(TApplicationException.INTERNAL_ERROR, "Internal error processing get_column_count");
          oprot.writeMessageBegin(new TMessage("get_column_count", TMessageType.EXCEPTION, seqid));
          x.write(oprot);
          oprot.writeMessageEnd();
          oprot.getTransport().flush();
          return;
        }
        oprot.writeMessageBegin(new TMessage("get_column_count", TMessageType.REPLY, seqid));
        result.write(oprot);
        oprot.writeMessageEnd();
        oprot.getTransport().flush();
      }

    }

    private class insert implements ProcessFunction {
      public void process(int seqid, TProtocol iprot, TProtocol oprot) throws TException
      {
        insert_args args = new insert_args();
        args.read(iprot);
        iprot.readMessageEnd();
        insert_result result = new insert_result();
        try {
          iface_.insert(args.tablename, args.key, args.columnPath, args.cellData, args.timestamp, args.block);
        } catch (InvalidRequestException ire) {
          result.ire = ire;
        } catch (UnavailableException ue) {
          result.ue = ue;
        } catch (Throwable th) {
          LOGGER.error("Internal error processing insert", th);
          TApplicationException x = new TApplicationException(TApplicationException.INTERNAL_ERROR, "Internal error processing insert");
          oprot.writeMessageBegin(new TMessage("insert", TMessageType.EXCEPTION, seqid));
          x.write(oprot);
          oprot.writeMessageEnd();
          oprot.getTransport().flush();
          return;
        }
        oprot.writeMessageBegin(new TMessage("insert", TMessageType.REPLY, seqid));
        result.write(oprot);
        oprot.writeMessageEnd();
        oprot.getTransport().flush();
      }

    }

    private class batch_insert implements ProcessFunction {
      public void process(int seqid, TProtocol iprot, TProtocol oprot) throws TException
      {
        batch_insert_args args = new batch_insert_args();
        args.read(iprot);
        iprot.readMessageEnd();
        batch_insert_result result = new batch_insert_result();
        try {
          iface_.batch_insert(args.batchMutation, args.block);
        } catch (InvalidRequestException ire) {
          result.ire = ire;
        } catch (UnavailableException ue) {
          result.ue = ue;
        } catch (Throwable th) {
          LOGGER.error("Internal error processing batch_insert", th);
          TApplicationException x = new TApplicationException(TApplicationException.INTERNAL_ERROR, "Internal error processing batch_insert");
          oprot.writeMessageBegin(new TMessage("batch_insert", TMessageType.EXCEPTION, seqid));
          x.write(oprot);
          oprot.writeMessageEnd();
          oprot.getTransport().flush();
          return;
        }
        oprot.writeMessageBegin(new TMessage("batch_insert", TMessageType.REPLY, seqid));
        result.write(oprot);
        oprot.writeMessageEnd();
        oprot.getTransport().flush();
      }

    }

    private class remove implements ProcessFunction {
      public void process(int seqid, TProtocol iprot, TProtocol oprot) throws TException
      {
        remove_args args = new remove_args();
        args.read(iprot);
        iprot.readMessageEnd();
        remove_result result = new remove_result();
        try {
          iface_.remove(args.tablename, args.key, args.columnPathOrParent, args.timestamp, args.block);
        } catch (InvalidRequestException ire) {
          result.ire = ire;
        } catch (UnavailableException ue) {
          result.ue = ue;
        } catch (Throwable th) {
          LOGGER.error("Internal error processing remove", th);
          TApplicationException x = new TApplicationException(TApplicationException.INTERNAL_ERROR, "Internal error processing remove");
          oprot.writeMessageBegin(new TMessage("remove", TMessageType.EXCEPTION, seqid));
          x.write(oprot);
          oprot.writeMessageEnd();
          oprot.getTransport().flush();
          return;
        }
        oprot.writeMessageBegin(new TMessage("remove", TMessageType.REPLY, seqid));
        result.write(oprot);
        oprot.writeMessageEnd();
        oprot.getTransport().flush();
      }

    }

    private class get_columns_since implements ProcessFunction {
      public void process(int seqid, TProtocol iprot, TProtocol oprot) throws TException
      {
        get_columns_since_args args = new get_columns_since_args();
        args.read(iprot);
        iprot.readMessageEnd();
        get_columns_since_result result = new get_columns_since_result();
        try {
          result.success = iface_.get_columns_since(args.tablename, args.key, args.columnParent, args.timeStamp);
        } catch (InvalidRequestException ire) {
          result.ire = ire;
        } catch (NotFoundException nfe) {
          result.nfe = nfe;
        } catch (Throwable th) {
          LOGGER.error("Internal error processing get_columns_since", th);
          TApplicationException x = new TApplicationException(TApplicationException.INTERNAL_ERROR, "Internal error processing get_columns_since");
          oprot.writeMessageBegin(new TMessage("get_columns_since", TMessageType.EXCEPTION, seqid));
          x.write(oprot);
          oprot.writeMessageEnd();
          oprot.getTransport().flush();
          return;
        }
        oprot.writeMessageBegin(new TMessage("get_columns_since", TMessageType.REPLY, seqid));
        result.write(oprot);
        oprot.writeMessageEnd();
        oprot.getTransport().flush();
      }

    }

    private class get_slice_super implements ProcessFunction {
      public void process(int seqid, TProtocol iprot, TProtocol oprot) throws TException
      {
        get_slice_super_args args = new get_slice_super_args();
        args.read(iprot);
        iprot.readMessageEnd();
        get_slice_super_result result = new get_slice_super_result();
        try {
          result.success = iface_.get_slice_super(args.tablename, args.key, args.columnFamily, args.start, args.count);
        } catch (InvalidRequestException ire) {
          result.ire = ire;
        } catch (Throwable th) {
          LOGGER.error("Internal error processing get_slice_super", th);
          TApplicationException x = new TApplicationException(TApplicationException.INTERNAL_ERROR, "Internal error processing get_slice_super");
          oprot.writeMessageBegin(new TMessage("get_slice_super", TMessageType.EXCEPTION, seqid));
          x.write(oprot);
          oprot.writeMessageEnd();
          oprot.getTransport().flush();
          return;
        }
        oprot.writeMessageBegin(new TMessage("get_slice_super", TMessageType.REPLY, seqid));
        result.write(oprot);
        oprot.writeMessageEnd();
        oprot.getTransport().flush();
      }

    }

    private class get_slice_super_by_names implements ProcessFunction {
      public void process(int seqid, TProtocol iprot, TProtocol oprot) throws TException
      {
        get_slice_super_by_names_args args = new get_slice_super_by_names_args();
        args.read(iprot);
        iprot.readMessageEnd();
        get_slice_super_by_names_result result = new get_slice_super_by_names_result();
        try {
          result.success = iface_.get_slice_super_by_names(args.tablename, args.key, args.columnFamily, args.superColumnNames);
        } catch (InvalidRequestException ire) {
          result.ire = ire;
        } catch (Throwable th) {
          LOGGER.error("Internal error processing get_slice_super_by_names", th);
          TApplicationException x = new TApplicationException(TApplicationException.INTERNAL_ERROR, "Internal error processing get_slice_super_by_names");
          oprot.writeMessageBegin(new TMessage("get_slice_super_by_names", TMessageType.EXCEPTION, seqid));
          x.write(oprot);
          oprot.writeMessageEnd();
          oprot.getTransport().flush();
          return;
        }
        oprot.writeMessageBegin(new TMessage("get_slice_super_by_names", TMessageType.REPLY, seqid));
        result.write(oprot);
        oprot.writeMessageEnd();
        oprot.getTransport().flush();
      }

    }

    private class get_superColumn implements ProcessFunction {
      public void process(int seqid, TProtocol iprot, TProtocol oprot) throws TException
      {
        get_superColumn_args args = new get_superColumn_args();
        args.read(iprot);
        iprot.readMessageEnd();
        get_superColumn_result result = new get_superColumn_result();
        try {
          result.success = iface_.get_superColumn(args.tablename, args.key, args.superColumnPath);
        } catch (InvalidRequestException ire) {
          result.ire = ire;
        } catch (NotFoundException nfe) {
          result.nfe = nfe;
        } catch (Throwable th) {
          LOGGER.error("Internal error processing get_superColumn", th);
          TApplicationException x = new TApplicationException(TApplicationException.INTERNAL_ERROR, "Internal error processing get_superColumn");
          oprot.writeMessageBegin(new TMessage("get_superColumn", TMessageType.EXCEPTION, seqid));
          x.write(oprot);
          oprot.writeMessageEnd();
          oprot.getTransport().flush();
          return;
        }
        oprot.writeMessageBegin(new TMessage("get_superColumn", TMessageType.REPLY, seqid));
        result.write(oprot);
        oprot.writeMessageEnd();
        oprot.getTransport().flush();
      }

    }

    private class batch_insert_superColumn implements ProcessFunction {
      public void process(int seqid, TProtocol iprot, TProtocol oprot) throws TException
      {
        batch_insert_superColumn_args args = new batch_insert_superColumn_args();
        args.read(iprot);
        iprot.readMessageEnd();
        batch_insert_superColumn_result result = new batch_insert_superColumn_result();
        try {
          iface_.batch_insert_superColumn(args.batchMutationSuper, args.block);
        } catch (InvalidRequestException ire) {
          result.ire = ire;
        } catch (UnavailableException ue) {
          result.ue = ue;
        } catch (Throwable th) {
          LOGGER.error("Internal error processing batch_insert_superColumn", th);
          TApplicationException x = new TApplicationException(TApplicationException.INTERNAL_ERROR, "Internal error processing batch_insert_superColumn");
          oprot.writeMessageBegin(new TMessage("batch_insert_superColumn", TMessageType.EXCEPTION, seqid));
          x.write(oprot);
          oprot.writeMessageEnd();
          oprot.getTransport().flush();
          return;
        }
        oprot.writeMessageBegin(new TMessage("batch_insert_superColumn", TMessageType.REPLY, seqid));
        result.write(oprot);
        oprot.writeMessageEnd();
        oprot.getTransport().flush();
      }

    }

    private class get_key_range implements ProcessFunction {
      public void process(int seqid, TProtocol iprot, TProtocol oprot) throws TException
      {
        get_key_range_args args = new get_key_range_args();
        args.read(iprot);
        iprot.readMessageEnd();
        get_key_range_result result = new get_key_range_result();
        try {
          result.success = iface_.get_key_range(args.tablename, args.startWith, args.stopAt, args.maxResults);
        } catch (InvalidRequestException ire) {
          result.ire = ire;
        } catch (Throwable th) {
          LOGGER.error("Internal error processing get_key_range", th);
          TApplicationException x = new TApplicationException(TApplicationException.INTERNAL_ERROR, "Internal error processing get_key_range");
          oprot.writeMessageBegin(new TMessage("get_key_range", TMessageType.EXCEPTION, seqid));
          x.write(oprot);
          oprot.writeMessageEnd();
          oprot.getTransport().flush();
          return;
        }
        oprot.writeMessageBegin(new TMessage("get_key_range", TMessageType.REPLY, seqid));
        result.write(oprot);
        oprot.writeMessageEnd();
        oprot.getTransport().flush();
      }

    }

    private class getStringProperty implements ProcessFunction {
      public void process(int seqid, TProtocol iprot, TProtocol oprot) throws TException
      {
        getStringProperty_args args = new getStringProperty_args();
        args.read(iprot);
        iprot.readMessageEnd();
        getStringProperty_result result = new getStringProperty_result();
        result.success = iface_.getStringProperty(args.propertyName);
        oprot.writeMessageBegin(new TMessage("getStringProperty", TMessageType.REPLY, seqid));
        result.write(oprot);
        oprot.writeMessageEnd();
        oprot.getTransport().flush();
      }

    }

    private class getStringListProperty implements ProcessFunction {
      public void process(int seqid, TProtocol iprot, TProtocol oprot) throws TException
      {
        getStringListProperty_args args = new getStringListProperty_args();
        args.read(iprot);
        iprot.readMessageEnd();
        getStringListProperty_result result = new getStringListProperty_result();
        result.success = iface_.getStringListProperty(args.propertyName);
        oprot.writeMessageBegin(new TMessage("getStringListProperty", TMessageType.REPLY, seqid));
        result.write(oprot);
        oprot.writeMessageEnd();
        oprot.getTransport().flush();
      }

    }

    private class describeTable implements ProcessFunction {
      public void process(int seqid, TProtocol iprot, TProtocol oprot) throws TException
      {
        describeTable_args args = new describeTable_args();
        args.read(iprot);
        iprot.readMessageEnd();
        describeTable_result result = new describeTable_result();
        result.success = iface_.describeTable(args.tableName);
        oprot.writeMessageBegin(new TMessage("describeTable", TMessageType.REPLY, seqid));
        result.write(oprot);
        oprot.writeMessageEnd();
        oprot.getTransport().flush();
      }

    }

    private class executeQuery implements ProcessFunction {
      public void process(int seqid, TProtocol iprot, TProtocol oprot) throws TException
      {
        executeQuery_args args = new executeQuery_args();
        args.read(iprot);
        iprot.readMessageEnd();
        executeQuery_result result = new executeQuery_result();
        result.success = iface_.executeQuery(args.query);
        oprot.writeMessageBegin(new TMessage("executeQuery", TMessageType.REPLY, seqid));
        result.write(oprot);
        oprot.writeMessageEnd();
        oprot.getTransport().flush();
      }

    }

  }

  public static class get_slice_args implements TBase, java.io.Serializable, Cloneable   {
    private static final TStruct STRUCT_DESC = new TStruct("get_slice_args");
    private static final TField TABLENAME_FIELD_DESC = new TField("tablename", TType.STRING, (short)1);
    private static final TField KEY_FIELD_DESC = new TField("key", TType.STRING, (short)2);
    private static final TField COLUMN_PARENT_FIELD_DESC = new TField("columnParent", TType.STRING, (short)3);
    private static final TField START_FIELD_DESC = new TField("start", TType.I32, (short)4);
    private static final TField COUNT_FIELD_DESC = new TField("count", TType.I32, (short)5);

    public String tablename;
    public static final int TABLENAME = 1;
    public String key;
    public static final int KEY = 2;
    public String columnParent;
    public static final int COLUMNPARENT = 3;
    public int start;
    public static final int START = 4;
    public int count;
    public static final int COUNT = 5;

    private final Isset __isset = new Isset();
    private static final class Isset implements java.io.Serializable {
      public boolean start = false;
      public boolean count = false;
    }

    public static final Map<Integer, FieldMetaData> metaDataMap = Collections.unmodifiableMap(new HashMap<Integer, FieldMetaData>() {{
      put(TABLENAME, new FieldMetaData("tablename", TFieldRequirementType.DEFAULT, 
          new FieldValueMetaData(TType.STRING)));
      put(KEY, new FieldMetaData("key", TFieldRequirementType.DEFAULT, 
          new FieldValueMetaData(TType.STRING)));
      put(COLUMNPARENT, new FieldMetaData("columnParent", TFieldRequirementType.DEFAULT, 
          new FieldValueMetaData(TType.STRING)));
      put(START, new FieldMetaData("start", TFieldRequirementType.DEFAULT, 
          new FieldValueMetaData(TType.I32)));
      put(COUNT, new FieldMetaData("count", TFieldRequirementType.DEFAULT, 
          new FieldValueMetaData(TType.I32)));
    }});

    static {
      FieldMetaData.addStructMetaDataMap(get_slice_args.class, metaDataMap);
    }

    public get_slice_args() {
      this.start = -1;

      this.count = -1;

    }

    public get_slice_args(
      String tablename,
      String key,
      String columnParent,
      int start,
      int count)
    {
      this();
      this.tablename = tablename;
      this.key = key;
      this.columnParent = columnParent;
      this.start = start;
      this.__isset.start = true;
      this.count = count;
      this.__isset.count = true;
    }

    /**
     * Performs a deep copy on <i>other</i>.
     */
    public get_slice_args(get_slice_args other) {
      if (other.isSetTablename()) {
        this.tablename = other.tablename;
      }
      if (other.isSetKey()) {
        this.key = other.key;
      }
      if (other.isSetColumnParent()) {
        this.columnParent = other.columnParent;
      }
      __isset.start = other.__isset.start;
      this.start = other.start;
      __isset.count = other.__isset.count;
      this.count = other.count;
    }

    @Override
    public get_slice_args clone() {
      return new get_slice_args(this);
    }

    public String getTablename() {
      return this.tablename;
    }

    public void setTablename(String tablename) {
      this.tablename = tablename;
    }

    public void unsetTablename() {
      this.tablename = null;
    }

    // Returns true if field tablename is set (has been asigned a value) and false otherwise
    public boolean isSetTablename() {
      return this.tablename != null;
    }

    public void setTablenameIsSet(boolean value) {
      if (!value) {
        this.tablename = null;
      }
    }

    public String getKey() {
      return this.key;
    }

    public void setKey(String key) {
      this.key = key;
    }

    public void unsetKey() {
      this.key = null;
    }

    // Returns true if field key is set (has been asigned a value) and false otherwise
    public boolean isSetKey() {
      return this.key != null;
    }

    public void setKeyIsSet(boolean value) {
      if (!value) {
        this.key = null;
      }
    }

    public String getColumnParent() {
      return this.columnParent;
    }

    public void setColumnParent(String columnParent) {
      this.columnParent = columnParent;
    }

    public void unsetColumnParent() {
      this.columnParent = null;
    }

    // Returns true if field columnParent is set (has been asigned a value) and false otherwise
    public boolean isSetColumnParent() {
      return this.columnParent != null;
    }

    public void setColumnParentIsSet(boolean value) {
      if (!value) {
        this.columnParent = null;
      }
    }

    public int getStart() {
      return this.start;
    }

    public void setStart(int start) {
      this.start = start;
      this.__isset.start = true;
    }

    public void unsetStart() {
      this.__isset.start = false;
    }

    // Returns true if field start is set (has been asigned a value) and false otherwise
    public boolean isSetStart() {
      return this.__isset.start;
    }

    public void setStartIsSet(boolean value) {
      this.__isset.start = value;
    }

    public int getCount() {
      return this.count;
    }

    public void setCount(int count) {
      this.count = count;
      this.__isset.count = true;
    }

    public void unsetCount() {
      this.__isset.count = false;
    }

    // Returns true if field count is set (has been asigned a value) and false otherwise
    public boolean isSetCount() {
      return this.__isset.count;
    }

    public void setCountIsSet(boolean value) {
      this.__isset.count = value;
    }

    public void setFieldValue(int fieldID, Object value) {
      switch (fieldID) {
      case TABLENAME:
        if (value == null) {
          unsetTablename();
        } else {
          setTablename((String)value);
        }
        break;

      case KEY:
        if (value == null) {
          unsetKey();
        } else {
          setKey((String)value);
        }
        break;

      case COLUMNPARENT:
        if (value == null) {
          unsetColumnParent();
        } else {
          setColumnParent((String)value);
        }
        break;

      case START:
        if (value == null) {
          unsetStart();
        } else {
          setStart((Integer)value);
        }
        break;

      case COUNT:
        if (value == null) {
          unsetCount();
        } else {
          setCount((Integer)value);
        }
        break;

      default:
        throw new IllegalArgumentException("Field " + fieldID + " doesn't exist!");
      }
    }

    public Object getFieldValue(int fieldID) {
      switch (fieldID) {
      case TABLENAME:
        return getTablename();

      case KEY:
        return getKey();

      case COLUMNPARENT:
        return getColumnParent();

      case START:
        return new Integer(getStart());

      case COUNT:
        return new Integer(getCount());

      default:
        throw new IllegalArgumentException("Field " + fieldID + " doesn't exist!");
      }
    }

    // Returns true if field corresponding to fieldID is set (has been asigned a value) and false otherwise
    public boolean isSet(int fieldID) {
      switch (fieldID) {
      case TABLENAME:
        return isSetTablename();
      case KEY:
        return isSetKey();
      case COLUMNPARENT:
        return isSetColumnParent();
      case START:
        return isSetStart();
      case COUNT:
        return isSetCount();
      default:
        throw new IllegalArgumentException("Field " + fieldID + " doesn't exist!");
      }
    }

    @Override
    public boolean equals(Object that) {
      if (that == null)
        return false;
      if (that instanceof get_slice_args)
        return this.equals((get_slice_args)that);
      return false;
    }

    public boolean equals(get_slice_args that) {
      if (that == null)
        return false;

      boolean this_present_tablename = true && this.isSetTablename();
      boolean that_present_tablename = true && that.isSetTablename();
      if (this_present_tablename || that_present_tablename) {
        if (!(this_present_tablename && that_present_tablename))
          return false;
        if (!this.tablename.equals(that.tablename))
          return false;
      }

      boolean this_present_key = true && this.isSetKey();
      boolean that_present_key = true && that.isSetKey();
      if (this_present_key || that_present_key) {
        if (!(this_present_key && that_present_key))
          return false;
        if (!this.key.equals(that.key))
          return false;
      }

      boolean this_present_columnParent = true && this.isSetColumnParent();
      boolean that_present_columnParent = true && that.isSetColumnParent();
      if (this_present_columnParent || that_present_columnParent) {
        if (!(this_present_columnParent && that_present_columnParent))
          return false;
        if (!this.columnParent.equals(that.columnParent))
          return false;
      }

      boolean this_present_start = true;
      boolean that_present_start = true;
      if (this_present_start || that_present_start) {
        if (!(this_present_start && that_present_start))
          return false;
        if (this.start != that.start)
          return false;
      }

      boolean this_present_count = true;
      boolean that_present_count = true;
      if (this_present_count || that_present_count) {
        if (!(this_present_count && that_present_count))
          return false;
        if (this.count != that.count)
          return false;
      }

      return true;
    }

    @Override
    public int hashCode() {
      return 0;
    }

    public void read(TProtocol iprot) throws TException {
      TField field;
      iprot.readStructBegin();
      while (true)
      {
        field = iprot.readFieldBegin();
        if (field.type == TType.STOP) { 
          break;
        }
        switch (field.id)
        {
          case TABLENAME:
            if (field.type == TType.STRING) {
              this.tablename = iprot.readString();
            } else { 
              TProtocolUtil.skip(iprot, field.type);
            }
            break;
          case KEY:
            if (field.type == TType.STRING) {
              this.key = iprot.readString();
            } else { 
              TProtocolUtil.skip(iprot, field.type);
            }
            break;
          case COLUMNPARENT:
            if (field.type == TType.STRING) {
              this.columnParent = iprot.readString();
            } else { 
              TProtocolUtil.skip(iprot, field.type);
            }
            break;
          case START:
            if (field.type == TType.I32) {
              this.start = iprot.readI32();
              this.__isset.start = true;
            } else { 
              TProtocolUtil.skip(iprot, field.type);
            }
            break;
          case COUNT:
            if (field.type == TType.I32) {
              this.count = iprot.readI32();
              this.__isset.count = true;
            } else { 
              TProtocolUtil.skip(iprot, field.type);
            }
            break;
          default:
            TProtocolUtil.skip(iprot, field.type);
            break;
        }
        iprot.readFieldEnd();
      }
      iprot.readStructEnd();


      // check for required fields of primitive type, which can't be checked in the validate method
      validate();
    }

    public void write(TProtocol oprot) throws TException {
      validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (this.tablename != null) {
        oprot.writeFieldBegin(TABLENAME_FIELD_DESC);
        oprot.writeString(this.tablename);
        oprot.writeFieldEnd();
      }
      if (this.key != null) {
        oprot.writeFieldBegin(KEY_FIELD_DESC);
        oprot.writeString(this.key);
        oprot.writeFieldEnd();
      }
      if (this.columnParent != null) {
        oprot.writeFieldBegin(COLUMN_PARENT_FIELD_DESC);
        oprot.writeString(this.columnParent);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldBegin(START_FIELD_DESC);
      oprot.writeI32(this.start);
      oprot.writeFieldEnd();
      oprot.writeFieldBegin(COUNT_FIELD_DESC);
      oprot.writeI32(this.count);
      oprot.writeFieldEnd();
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

    @Override
    public String toString() {
      StringBuilder sb = new StringBuilder("get_slice_args(");
      boolean first = true;

      sb.append("tablename:");
      if (this.tablename == null) {
        sb.append("null");
      } else {
        sb.append(this.tablename);
      }
      first = false;
      if (!first) sb.append(", ");
      sb.append("key:");
      if (this.key == null) {
        sb.append("null");
      } else {
        sb.append(this.key);
      }
      first = false;
      if (!first) sb.append(", ");
      sb.append("columnParent:");
      if (this.columnParent == null) {
        sb.append("null");
      } else {
        sb.append(this.columnParent);
      }
      first = false;
      if (!first) sb.append(", ");
      sb.append("start:");
      sb.append(this.start);
      first = false;
      if (!first) sb.append(", ");
      sb.append("count:");
      sb.append(this.count);
      first = false;
      sb.append(")");
      return sb.toString();
    }

    public void validate() throws TException {
      // check for required fields
      // check that fields of type enum have valid values
    }

  }

  public static class get_slice_result implements TBase, java.io.Serializable, Cloneable   {
    private static final TStruct STRUCT_DESC = new TStruct("get_slice_result");
    private static final TField SUCCESS_FIELD_DESC = new TField("success", TType.LIST, (short)0);
    private static final TField IRE_FIELD_DESC = new TField("ire", TType.STRUCT, (short)1);
    private static final TField NFE_FIELD_DESC = new TField("nfe", TType.STRUCT, (short)2);

    public List<column_t> success;
    public static final int SUCCESS = 0;
    public InvalidRequestException ire;
    public static final int IRE = 1;
    public NotFoundException nfe;
    public static final int NFE = 2;

    private final Isset __isset = new Isset();
    private static final class Isset implements java.io.Serializable {
    }

    public static final Map<Integer, FieldMetaData> metaDataMap = Collections.unmodifiableMap(new HashMap<Integer, FieldMetaData>() {{
      put(SUCCESS, new FieldMetaData("success", TFieldRequirementType.DEFAULT, 
          new ListMetaData(TType.LIST, 
              new StructMetaData(TType.STRUCT, column_t.class))));
      put(IRE, new FieldMetaData("ire", TFieldRequirementType.DEFAULT, 
          new FieldValueMetaData(TType.STRUCT)));
      put(NFE, new FieldMetaData("nfe", TFieldRequirementType.DEFAULT, 
          new FieldValueMetaData(TType.STRUCT)));
    }});

    static {
      FieldMetaData.addStructMetaDataMap(get_slice_result.class, metaDataMap);
    }

    public get_slice_result() {
    }

    public get_slice_result(
      List<column_t> success,
      InvalidRequestException ire,
      NotFoundException nfe)
    {
      this();
      this.success = success;
      this.ire = ire;
      this.nfe = nfe;
    }

    /**
     * Performs a deep copy on <i>other</i>.
     */
    public get_slice_result(get_slice_result other) {
      if (other.isSetSuccess()) {
        List<column_t> __this__success = new ArrayList<column_t>();
        for (column_t other_element : other.success) {
          __this__success.add(new column_t(other_element));
        }
        this.success = __this__success;
      }
      if (other.isSetIre()) {
        this.ire = new InvalidRequestException(other.ire);
      }
      if (other.isSetNfe()) {
        this.nfe = new NotFoundException(other.nfe);
      }
    }

    @Override
    public get_slice_result clone() {
      return new get_slice_result(this);
    }

    public int getSuccessSize() {
      return (this.success == null) ? 0 : this.success.size();
    }

    public java.util.Iterator<column_t> getSuccessIterator() {
      return (this.success == null) ? null : this.success.iterator();
    }

    public void addToSuccess(column_t elem) {
      if (this.success == null) {
        this.success = new ArrayList<column_t>();
      }
      this.success.add(elem);
    }

    public List<column_t> getSuccess() {
      return this.success;
    }

    public void setSuccess(List<column_t> success) {
      this.success = success;
    }

    public void unsetSuccess() {
      this.success = null;
    }

    // Returns true if field success is set (has been asigned a value) and false otherwise
    public boolean isSetSuccess() {
      return this.success != null;
    }

    public void setSuccessIsSet(boolean value) {
      if (!value) {
        this.success = null;
      }
    }

    public InvalidRequestException getIre() {
      return this.ire;
    }

    public void setIre(InvalidRequestException ire) {
      this.ire = ire;
    }

    public void unsetIre() {
      this.ire = null;
    }

    // Returns true if field ire is set (has been asigned a value) and false otherwise
    public boolean isSetIre() {
      return this.ire != null;
    }

    public void setIreIsSet(boolean value) {
      if (!value) {
        this.ire = null;
      }
    }

    public NotFoundException getNfe() {
      return this.nfe;
    }

    public void setNfe(NotFoundException nfe) {
      this.nfe = nfe;
    }

    public void unsetNfe() {
      this.nfe = null;
    }

    // Returns true if field nfe is set (has been asigned a value) and false otherwise
    public boolean isSetNfe() {
      return this.nfe != null;
    }

    public void setNfeIsSet(boolean value) {
      if (!value) {
        this.nfe = null;
      }
    }

    public void setFieldValue(int fieldID, Object value) {
      switch (fieldID) {
      case SUCCESS:
        if (value == null) {
          unsetSuccess();
        } else {
          setSuccess((List<column_t>)value);
        }
        break;

      case IRE:
        if (value == null) {
          unsetIre();
        } else {
          setIre((InvalidRequestException)value);
        }
        break;

      case NFE:
        if (value == null) {
          unsetNfe();
        } else {
          setNfe((NotFoundException)value);
        }
        break;

      default:
        throw new IllegalArgumentException("Field " + fieldID + " doesn't exist!");
      }
    }

    public Object getFieldValue(int fieldID) {
      switch (fieldID) {
      case SUCCESS:
        return getSuccess();

      case IRE:
        return getIre();

      case NFE:
        return getNfe();

      default:
        throw new IllegalArgumentException("Field " + fieldID + " doesn't exist!");
      }
    }

    // Returns true if field corresponding to fieldID is set (has been asigned a value) and false otherwise
    public boolean isSet(int fieldID) {
      switch (fieldID) {
      case SUCCESS:
        return isSetSuccess();
      case IRE:
        return isSetIre();
      case NFE:
        return isSetNfe();
      default:
        throw new IllegalArgumentException("Field " + fieldID + " doesn't exist!");
      }
    }

    @Override
    public boolean equals(Object that) {
      if (that == null)
        return false;
      if (that instanceof get_slice_result)
        return this.equals((get_slice_result)that);
      return false;
    }

    public boolean equals(get_slice_result that) {
      if (that == null)
        return false;

      boolean this_present_success = true && this.isSetSuccess();
      boolean that_present_success = true && that.isSetSuccess();
      if (this_present_success || that_present_success) {
        if (!(this_present_success && that_present_success))
          return false;
        if (!this.success.equals(that.success))
          return false;
      }

      boolean this_present_ire = true && this.isSetIre();
      boolean that_present_ire = true && that.isSetIre();
      if (this_present_ire || that_present_ire) {
        if (!(this_present_ire && that_present_ire))
          return false;
        if (!this.ire.equals(that.ire))
          return false;
      }

      boolean this_present_nfe = true && this.isSetNfe();
      boolean that_present_nfe = true && that.isSetNfe();
      if (this_present_nfe || that_present_nfe) {
        if (!(this_present_nfe && that_present_nfe))
          return false;
        if (!this.nfe.equals(that.nfe))
          return false;
      }

      return true;
    }

    @Override
    public int hashCode() {
      return 0;
    }

    public void read(TProtocol iprot) throws TException {
      TField field;
      iprot.readStructBegin();
      while (true)
      {
        field = iprot.readFieldBegin();
        if (field.type == TType.STOP) { 
          break;
        }
        switch (field.id)
        {
          case SUCCESS:
            if (field.type == TType.LIST) {
              {
                TList _list31 = iprot.readListBegin();
                this.success = new ArrayList<column_t>(_list31.size);
                for (int _i32 = 0; _i32 < _list31.size; ++_i32)
                {
                  column_t _elem33;
                  _elem33 = new column_t();
                  _elem33.read(iprot);
                  this.success.add(_elem33);
                }
                iprot.readListEnd();
              }
            } else { 
              TProtocolUtil.skip(iprot, field.type);
            }
            break;
          case IRE:
            if (field.type == TType.STRUCT) {
              this.ire = new InvalidRequestException();
              this.ire.read(iprot);
            } else { 
              TProtocolUtil.skip(iprot, field.type);
            }
            break;
          case NFE:
            if (field.type == TType.STRUCT) {
              this.nfe = new NotFoundException();
              this.nfe.read(iprot);
            } else { 
              TProtocolUtil.skip(iprot, field.type);
            }
            break;
          default:
            TProtocolUtil.skip(iprot, field.type);
            break;
        }
        iprot.readFieldEnd();
      }
      iprot.readStructEnd();


      // check for required fields of primitive type, which can't be checked in the validate method
      validate();
    }

    public void write(TProtocol oprot) throws TException {
      oprot.writeStructBegin(STRUCT_DESC);

      if (this.isSetSuccess()) {
        oprot.writeFieldBegin(SUCCESS_FIELD_DESC);
        {
          oprot.writeListBegin(new TList(TType.STRUCT, this.success.size()));
          for (column_t _iter34 : this.success)          {
            _iter34.write(oprot);
          }
          oprot.writeListEnd();
        }
        oprot.writeFieldEnd();
      } else if (this.isSetIre()) {
        oprot.writeFieldBegin(IRE_FIELD_DESC);
        this.ire.write(oprot);
        oprot.writeFieldEnd();
      } else if (this.isSetNfe()) {
        oprot.writeFieldBegin(NFE_FIELD_DESC);
        this.nfe.write(oprot);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

    @Override
    public String toString() {
      StringBuilder sb = new StringBuilder("get_slice_result(");
      boolean first = true;

      sb.append("success:");
      if (this.success == null) {
        sb.append("null");
      } else {
        sb.append(this.success);
      }
      first = false;
      if (!first) sb.append(", ");
      sb.append("ire:");
      if (this.ire == null) {
        sb.append("null");
      } else {
        sb.append(this.ire);
      }
      first = false;
      if (!first) sb.append(", ");
      sb.append("nfe:");
      if (this.nfe == null) {
        sb.append("null");
      } else {
        sb.append(this.nfe);
      }
      first = false;
      sb.append(")");
      return sb.toString();
    }

    public void validate() throws TException {
      // check for required fields
      // check that fields of type enum have valid values
    }

  }

  public static class get_slice_by_names_args implements TBase, java.io.Serializable, Cloneable   {
    private static final TStruct STRUCT_DESC = new TStruct("get_slice_by_names_args");
    private static final TField TABLENAME_FIELD_DESC = new TField("tablename", TType.STRING, (short)1);
    private static final TField KEY_FIELD_DESC = new TField("key", TType.STRING, (short)2);
    private static final TField COLUMN_PARENT_FIELD_DESC = new TField("columnParent", TType.STRING, (short)3);
    private static final TField COLUMN_NAMES_FIELD_DESC = new TField("columnNames", TType.LIST, (short)4);

    public String tablename;
    public static final int TABLENAME = 1;
    public String key;
    public static final int KEY = 2;
    public String columnParent;
    public static final int COLUMNPARENT = 3;
    public List<String> columnNames;
    public static final int COLUMNNAMES = 4;

    private final Isset __isset = new Isset();
    private static final class Isset implements java.io.Serializable {
    }

    public static final Map<Integer, FieldMetaData> metaDataMap = Collections.unmodifiableMap(new HashMap<Integer, FieldMetaData>() {{
      put(TABLENAME, new FieldMetaData("tablename", TFieldRequirementType.DEFAULT, 
          new FieldValueMetaData(TType.STRING)));
      put(KEY, new FieldMetaData("key", TFieldRequirementType.DEFAULT, 
          new FieldValueMetaData(TType.STRING)));
      put(COLUMNPARENT, new FieldMetaData("columnParent", TFieldRequirementType.DEFAULT, 
          new FieldValueMetaData(TType.STRING)));
      put(COLUMNNAMES, new FieldMetaData("columnNames", TFieldRequirementType.DEFAULT, 
          new ListMetaData(TType.LIST, 
              new FieldValueMetaData(TType.STRING))));
    }});

    static {
      FieldMetaData.addStructMetaDataMap(get_slice_by_names_args.class, metaDataMap);
    }

    public get_slice_by_names_args() {
    }

    public get_slice_by_names_args(
      String tablename,
      String key,
      String columnParent,
      List<String> columnNames)
    {
      this();
      this.tablename = tablename;
      this.key = key;
      this.columnParent = columnParent;
      this.columnNames = columnNames;
    }

    /**
     * Performs a deep copy on <i>other</i>.
     */
    public get_slice_by_names_args(get_slice_by_names_args other) {
      if (other.isSetTablename()) {
        this.tablename = other.tablename;
      }
      if (other.isSetKey()) {
        this.key = other.key;
      }
      if (other.isSetColumnParent()) {
        this.columnParent = other.columnParent;
      }
      if (other.isSetColumnNames()) {
        List<String> __this__columnNames = new ArrayList<String>();
        for (String other_element : other.columnNames) {
          __this__columnNames.add(other_element);
        }
        this.columnNames = __this__columnNames;
      }
    }

    @Override
    public get_slice_by_names_args clone() {
      return new get_slice_by_names_args(this);
    }

    public String getTablename() {
      return this.tablename;
    }

    public void setTablename(String tablename) {
      this.tablename = tablename;
    }

    public void unsetTablename() {
      this.tablename = null;
    }

    // Returns true if field tablename is set (has been asigned a value) and false otherwise
    public boolean isSetTablename() {
      return this.tablename != null;
    }

    public void setTablenameIsSet(boolean value) {
      if (!value) {
        this.tablename = null;
      }
    }

    public String getKey() {
      return this.key;
    }

    public void setKey(String key) {
      this.key = key;
    }

    public void unsetKey() {
      this.key = null;
    }

    // Returns true if field key is set (has been asigned a value) and false otherwise
    public boolean isSetKey() {
      return this.key != null;
    }

    public void setKeyIsSet(boolean value) {
      if (!value) {
        this.key = null;
      }
    }

    public String getColumnParent() {
      return this.columnParent;
    }

    public void setColumnParent(String columnParent) {
      this.columnParent = columnParent;
    }

    public void unsetColumnParent() {
      this.columnParent = null;
    }

    // Returns true if field columnParent is set (has been asigned a value) and false otherwise
    public boolean isSetColumnParent() {
      return this.columnParent != null;
    }

    public void setColumnParentIsSet(boolean value) {
      if (!value) {
        this.columnParent = null;
      }
    }

    public int getColumnNamesSize() {
      return (this.columnNames == null) ? 0 : this.columnNames.size();
    }

    public java.util.Iterator<String> getColumnNamesIterator() {
      return (this.columnNames == null) ? null : this.columnNames.iterator();
    }

    public void addToColumnNames(String elem) {
      if (this.columnNames == null) {
        this.columnNames = new ArrayList<String>();
      }
      this.columnNames.add(elem);
    }

    public List<String> getColumnNames() {
      return this.columnNames;
    }

    public void setColumnNames(List<String> columnNames) {
      this.columnNames = columnNames;
    }

    public void unsetColumnNames() {
      this.columnNames = null;
    }

    // Returns true if field columnNames is set (has been asigned a value) and false otherwise
    public boolean isSetColumnNames() {
      return this.columnNames != null;
    }

    public void setColumnNamesIsSet(boolean value) {
      if (!value) {
        this.columnNames = null;
      }
    }

    public void setFieldValue(int fieldID, Object value) {
      switch (fieldID) {
      case TABLENAME:
        if (value == null) {
          unsetTablename();
        } else {
          setTablename((String)value);
        }
        break;

      case KEY:
        if (value == null) {
          unsetKey();
        } else {
          setKey((String)value);
        }
        break;

      case COLUMNPARENT:
        if (value == null) {
          unsetColumnParent();
        } else {
          setColumnParent((String)value);
        }
        break;

      case COLUMNNAMES:
        if (value == null) {
          unsetColumnNames();
        } else {
          setColumnNames((List<String>)value);
        }
        break;

      default:
        throw new IllegalArgumentException("Field " + fieldID + " doesn't exist!");
      }
    }

    public Object getFieldValue(int fieldID) {
      switch (fieldID) {
      case TABLENAME:
        return getTablename();

      case KEY:
        return getKey();

      case COLUMNPARENT:
        return getColumnParent();

      case COLUMNNAMES:
        return getColumnNames();

      default:
        throw new IllegalArgumentException("Field " + fieldID + " doesn't exist!");
      }
    }

    // Returns true if field corresponding to fieldID is set (has been asigned a value) and false otherwise
    public boolean isSet(int fieldID) {
      switch (fieldID) {
      case TABLENAME:
        return isSetTablename();
      case KEY:
        return isSetKey();
      case COLUMNPARENT:
        return isSetColumnParent();
      case COLUMNNAMES:
        return isSetColumnNames();
      default:
        throw new IllegalArgumentException("Field " + fieldID + " doesn't exist!");
      }
    }

    @Override
    public boolean equals(Object that) {
      if (that == null)
        return false;
      if (that instanceof get_slice_by_names_args)
        return this.equals((get_slice_by_names_args)that);
      return false;
    }

    public boolean equals(get_slice_by_names_args that) {
      if (that == null)
        return false;

      boolean this_present_tablename = true && this.isSetTablename();
      boolean that_present_tablename = true && that.isSetTablename();
      if (this_present_tablename || that_present_tablename) {
        if (!(this_present_tablename && that_present_tablename))
          return false;
        if (!this.tablename.equals(that.tablename))
          return false;
      }

      boolean this_present_key = true && this.isSetKey();
      boolean that_present_key = true && that.isSetKey();
      if (this_present_key || that_present_key) {
        if (!(this_present_key && that_present_key))
          return false;
        if (!this.key.equals(that.key))
          return false;
      }

      boolean this_present_columnParent = true && this.isSetColumnParent();
      boolean that_present_columnParent = true && that.isSetColumnParent();
      if (this_present_columnParent || that_present_columnParent) {
        if (!(this_present_columnParent && that_present_columnParent))
          return false;
        if (!this.columnParent.equals(that.columnParent))
          return false;
      }

      boolean this_present_columnNames = true && this.isSetColumnNames();
      boolean that_present_columnNames = true && that.isSetColumnNames();
      if (this_present_columnNames || that_present_columnNames) {
        if (!(this_present_columnNames && that_present_columnNames))
          return false;
        if (!this.columnNames.equals(that.columnNames))
          return false;
      }

      return true;
    }

    @Override
    public int hashCode() {
      return 0;
    }

    public void read(TProtocol iprot) throws TException {
      TField field;
      iprot.readStructBegin();
      while (true)
      {
        field = iprot.readFieldBegin();
        if (field.type == TType.STOP) { 
          break;
        }
        switch (field.id)
        {
          case TABLENAME:
            if (field.type == TType.STRING) {
              this.tablename = iprot.readString();
            } else { 
              TProtocolUtil.skip(iprot, field.type);
            }
            break;
          case KEY:
            if (field.type == TType.STRING) {
              this.key = iprot.readString();
            } else { 
              TProtocolUtil.skip(iprot, field.type);
            }
            break;
          case COLUMNPARENT:
            if (field.type == TType.STRING) {
              this.columnParent = iprot.readString();
            } else { 
              TProtocolUtil.skip(iprot, field.type);
            }
            break;
          case COLUMNNAMES:
            if (field.type == TType.LIST) {
              {
                TList _list35 = iprot.readListBegin();
                this.columnNames = new ArrayList<String>(_list35.size);
                for (int _i36 = 0; _i36 < _list35.size; ++_i36)
                {
                  String _elem37;
                  _elem37 = iprot.readString();
                  this.columnNames.add(_elem37);
                }
                iprot.readListEnd();
              }
            } else { 
              TProtocolUtil.skip(iprot, field.type);
            }
            break;
          default:
            TProtocolUtil.skip(iprot, field.type);
            break;
        }
        iprot.readFieldEnd();
      }
      iprot.readStructEnd();


      // check for required fields of primitive type, which can't be checked in the validate method
      validate();
    }

    public void write(TProtocol oprot) throws TException {
      validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (this.tablename != null) {
        oprot.writeFieldBegin(TABLENAME_FIELD_DESC);
        oprot.writeString(this.tablename);
        oprot.writeFieldEnd();
      }
      if (this.key != null) {
        oprot.writeFieldBegin(KEY_FIELD_DESC);
        oprot.writeString(this.key);
        oprot.writeFieldEnd();
      }
      if (this.columnParent != null) {
        oprot.writeFieldBegin(COLUMN_PARENT_FIELD_DESC);
        oprot.writeString(this.columnParent);
        oprot.writeFieldEnd();
      }
      if (this.columnNames != null) {
        oprot.writeFieldBegin(COLUMN_NAMES_FIELD_DESC);
        {
          oprot.writeListBegin(new TList(TType.STRING, this.columnNames.size()));
          for (String _iter38 : this.columnNames)          {
            oprot.writeString(_iter38);
          }
          oprot.writeListEnd();
        }
        oprot.writeFieldEnd();
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

    @Override
    public String toString() {
      StringBuilder sb = new StringBuilder("get_slice_by_names_args(");
      boolean first = true;

      sb.append("tablename:");
      if (this.tablename == null) {
        sb.append("null");
      } else {
        sb.append(this.tablename);
      }
      first = false;
      if (!first) sb.append(", ");
      sb.append("key:");
      if (this.key == null) {
        sb.append("null");
      } else {
        sb.append(this.key);
      }
      first = false;
      if (!first) sb.append(", ");
      sb.append("columnParent:");
      if (this.columnParent == null) {
        sb.append("null");
      } else {
        sb.append(this.columnParent);
      }
      first = false;
      if (!first) sb.append(", ");
      sb.append("columnNames:");
      if (this.columnNames == null) {
        sb.append("null");
      } else {
        sb.append(this.columnNames);
      }
      first = false;
      sb.append(")");
      return sb.toString();
    }

    public void validate() throws TException {
      // check for required fields
      // check that fields of type enum have valid values
    }

  }

  public static class get_slice_by_names_result implements TBase, java.io.Serializable, Cloneable   {
    private static final TStruct STRUCT_DESC = new TStruct("get_slice_by_names_result");
    private static final TField SUCCESS_FIELD_DESC = new TField("success", TType.LIST, (short)0);
    private static final TField IRE_FIELD_DESC = new TField("ire", TType.STRUCT, (short)1);
    private static final TField NFE_FIELD_DESC = new TField("nfe", TType.STRUCT, (short)2);

    public List<column_t> success;
    public static final int SUCCESS = 0;
    public InvalidRequestException ire;
    public static final int IRE = 1;
    public NotFoundException nfe;
    public static final int NFE = 2;

    private final Isset __isset = new Isset();
    private static final class Isset implements java.io.Serializable {
    }

    public static final Map<Integer, FieldMetaData> metaDataMap = Collections.unmodifiableMap(new HashMap<Integer, FieldMetaData>() {{
      put(SUCCESS, new FieldMetaData("success", TFieldRequirementType.DEFAULT, 
          new ListMetaData(TType.LIST, 
              new StructMetaData(TType.STRUCT, column_t.class))));
      put(IRE, new FieldMetaData("ire", TFieldRequirementType.DEFAULT, 
          new FieldValueMetaData(TType.STRUCT)));
      put(NFE, new FieldMetaData("nfe", TFieldRequirementType.DEFAULT, 
          new FieldValueMetaData(TType.STRUCT)));
    }});

    static {
      FieldMetaData.addStructMetaDataMap(get_slice_by_names_result.class, metaDataMap);
    }

    public get_slice_by_names_result() {
    }

    public get_slice_by_names_result(
      List<column_t> success,
      InvalidRequestException ire,
      NotFoundException nfe)
    {
      this();
      this.success = success;
      this.ire = ire;
      this.nfe = nfe;
    }

    /**
     * Performs a deep copy on <i>other</i>.
     */
    public get_slice_by_names_result(get_slice_by_names_result other) {
      if (other.isSetSuccess()) {
        List<column_t> __this__success = new ArrayList<column_t>();
        for (column_t other_element : other.success) {
          __this__success.add(new column_t(other_element));
        }
        this.success = __this__success;
      }
      if (other.isSetIre()) {
        this.ire = new InvalidRequestException(other.ire);
      }
      if (other.isSetNfe()) {
        this.nfe = new NotFoundException(other.nfe);
      }
    }

    @Override
    public get_slice_by_names_result clone() {
      return new get_slice_by_names_result(this);
    }

    public int getSuccessSize() {
      return (this.success == null) ? 0 : this.success.size();
    }

    public java.util.Iterator<column_t> getSuccessIterator() {
      return (this.success == null) ? null : this.success.iterator();
    }

    public void addToSuccess(column_t elem) {
      if (this.success == null) {
        this.success = new ArrayList<column_t>();
      }
      this.success.add(elem);
    }

    public List<column_t> getSuccess() {
      return this.success;
    }

    public void setSuccess(List<column_t> success) {
      this.success = success;
    }

    public void unsetSuccess() {
      this.success = null;
    }

    // Returns true if field success is set (has been asigned a value) and false otherwise
    public boolean isSetSuccess() {
      return this.success != null;
    }

    public void setSuccessIsSet(boolean value) {
      if (!value) {
        this.success = null;
      }
    }

    public InvalidRequestException getIre() {
      return this.ire;
    }

    public void setIre(InvalidRequestException ire) {
      this.ire = ire;
    }

    public void unsetIre() {
      this.ire = null;
    }

    // Returns true if field ire is set (has been asigned a value) and false otherwise
    public boolean isSetIre() {
      return this.ire != null;
    }

    public void setIreIsSet(boolean value) {
      if (!value) {
        this.ire = null;
      }
    }

    public NotFoundException getNfe() {
      return this.nfe;
    }

    public void setNfe(NotFoundException nfe) {
      this.nfe = nfe;
    }

    public void unsetNfe() {
      this.nfe = null;
    }

    // Returns true if field nfe is set (has been asigned a value) and false otherwise
    public boolean isSetNfe() {
      return this.nfe != null;
    }

    public void setNfeIsSet(boolean value) {
      if (!value) {
        this.nfe = null;
      }
    }

    public void setFieldValue(int fieldID, Object value) {
      switch (fieldID) {
      case SUCCESS:
        if (value == null) {
          unsetSuccess();
        } else {
          setSuccess((List<column_t>)value);
        }
        break;

      case IRE:
        if (value == null) {
          unsetIre();
        } else {
          setIre((InvalidRequestException)value);
        }
        break;

      case NFE:
        if (value == null) {
          unsetNfe();
        } else {
          setNfe((NotFoundException)value);
        }
        break;

      default:
        throw new IllegalArgumentException("Field " + fieldID + " doesn't exist!");
      }
    }

    public Object getFieldValue(int fieldID) {
      switch (fieldID) {
      case SUCCESS:
        return getSuccess();

      case IRE:
        return getIre();

      case NFE:
        return getNfe();

      default:
        throw new IllegalArgumentException("Field " + fieldID + " doesn't exist!");
      }
    }

    // Returns true if field corresponding to fieldID is set (has been asigned a value) and false otherwise
    public boolean isSet(int fieldID) {
      switch (fieldID) {
      case SUCCESS:
        return isSetSuccess();
      case IRE:
        return isSetIre();
      case NFE:
        return isSetNfe();
      default:
        throw new IllegalArgumentException("Field " + fieldID + " doesn't exist!");
      }
    }

    @Override
    public boolean equals(Object that) {
      if (that == null)
        return false;
      if (that instanceof get_slice_by_names_result)
        return this.equals((get_slice_by_names_result)that);
      return false;
    }

    public boolean equals(get_slice_by_names_result that) {
      if (that == null)
        return false;

      boolean this_present_success = true && this.isSetSuccess();
      boolean that_present_success = true && that.isSetSuccess();
      if (this_present_success || that_present_success) {
        if (!(this_present_success && that_present_success))
          return false;
        if (!this.success.equals(that.success))
          return false;
      }

      boolean this_present_ire = true && this.isSetIre();
      boolean that_present_ire = true && that.isSetIre();
      if (this_present_ire || that_present_ire) {
        if (!(this_present_ire && that_present_ire))
          return false;
        if (!this.ire.equals(that.ire))
          return false;
      }

      boolean this_present_nfe = true && this.isSetNfe();
      boolean that_present_nfe = true && that.isSetNfe();
      if (this_present_nfe || that_present_nfe) {
        if (!(this_present_nfe && that_present_nfe))
          return false;
        if (!this.nfe.equals(that.nfe))
          return false;
      }

      return true;
    }

    @Override
    public int hashCode() {
      return 0;
    }

    public void read(TProtocol iprot) throws TException {
      TField field;
      iprot.readStructBegin();
      while (true)
      {
        field = iprot.readFieldBegin();
        if (field.type == TType.STOP) { 
          break;
        }
        switch (field.id)
        {
          case SUCCESS:
            if (field.type == TType.LIST) {
              {
                TList _list39 = iprot.readListBegin();
                this.success = new ArrayList<column_t>(_list39.size);
                for (int _i40 = 0; _i40 < _list39.size; ++_i40)
                {
                  column_t _elem41;
                  _elem41 = new column_t();
                  _elem41.read(iprot);
                  this.success.add(_elem41);
                }
                iprot.readListEnd();
              }
            } else { 
              TProtocolUtil.skip(iprot, field.type);
            }
            break;
          case IRE:
            if (field.type == TType.STRUCT) {
              this.ire = new InvalidRequestException();
              this.ire.read(iprot);
            } else { 
              TProtocolUtil.skip(iprot, field.type);
            }
            break;
          case NFE:
            if (field.type == TType.STRUCT) {
              this.nfe = new NotFoundException();
              this.nfe.read(iprot);
            } else { 
              TProtocolUtil.skip(iprot, field.type);
            }
            break;
          default:
            TProtocolUtil.skip(iprot, field.type);
            break;
        }
        iprot.readFieldEnd();
      }
      iprot.readStructEnd();


      // check for required fields of primitive type, which can't be checked in the validate method
      validate();
    }

    public void write(TProtocol oprot) throws TException {
      oprot.writeStructBegin(STRUCT_DESC);

      if (this.isSetSuccess()) {
        oprot.writeFieldBegin(SUCCESS_FIELD_DESC);
        {
          oprot.writeListBegin(new TList(TType.STRUCT, this.success.size()));
          for (column_t _iter42 : this.success)          {
            _iter42.write(oprot);
          }
          oprot.writeListEnd();
        }
        oprot.writeFieldEnd();
      } else if (this.isSetIre()) {
        oprot.writeFieldBegin(IRE_FIELD_DESC);
        this.ire.write(oprot);
        oprot.writeFieldEnd();
      } else if (this.isSetNfe()) {
        oprot.writeFieldBegin(NFE_FIELD_DESC);
        this.nfe.write(oprot);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

    @Override
    public String toString() {
      StringBuilder sb = new StringBuilder("get_slice_by_names_result(");
      boolean first = true;

      sb.append("success:");
      if (this.success == null) {
        sb.append("null");
      } else {
        sb.append(this.success);
      }
      first = false;
      if (!first) sb.append(", ");
      sb.append("ire:");
      if (this.ire == null) {
        sb.append("null");
      } else {
        sb.append(this.ire);
      }
      first = false;
      if (!first) sb.append(", ");
      sb.append("nfe:");
      if (this.nfe == null) {
        sb.append("null");
      } else {
        sb.append(this.nfe);
      }
      first = false;
      sb.append(")");
      return sb.toString();
    }

    public void validate() throws TException {
      // check for required fields
      // check that fields of type enum have valid values
    }

  }

  public static class get_column_args implements TBase, java.io.Serializable, Cloneable   {
    private static final TStruct STRUCT_DESC = new TStruct("get_column_args");
    private static final TField TABLENAME_FIELD_DESC = new TField("tablename", TType.STRING, (short)1);
    private static final TField KEY_FIELD_DESC = new TField("key", TType.STRING, (short)2);
    private static final TField COLUMN_PATH_FIELD_DESC = new TField("columnPath", TType.STRING, (short)3);

    public String tablename;
    public static final int TABLENAME = 1;
    public String key;
    public static final int KEY = 2;
    public String columnPath;
    public static final int COLUMNPATH = 3;

    private final Isset __isset = new Isset();
    private static final class Isset implements java.io.Serializable {
    }

    public static final Map<Integer, FieldMetaData> metaDataMap = Collections.unmodifiableMap(new HashMap<Integer, FieldMetaData>() {{
      put(TABLENAME, new FieldMetaData("tablename", TFieldRequirementType.DEFAULT, 
          new FieldValueMetaData(TType.STRING)));
      put(KEY, new FieldMetaData("key", TFieldRequirementType.DEFAULT, 
          new FieldValueMetaData(TType.STRING)));
      put(COLUMNPATH, new FieldMetaData("columnPath", TFieldRequirementType.DEFAULT, 
          new FieldValueMetaData(TType.STRING)));
    }});

    static {
      FieldMetaData.addStructMetaDataMap(get_column_args.class, metaDataMap);
    }

    public get_column_args() {
    }

    public get_column_args(
      String tablename,
      String key,
      String columnPath)
    {
      this();
      this.tablename = tablename;
      this.key = key;
      this.columnPath = columnPath;
    }

    /**
     * Performs a deep copy on <i>other</i>.
     */
    public get_column_args(get_column_args other) {
      if (other.isSetTablename()) {
        this.tablename = other.tablename;
      }
      if (other.isSetKey()) {
        this.key = other.key;
      }
      if (other.isSetColumnPath()) {
        this.columnPath = other.columnPath;
      }
    }

    @Override
    public get_column_args clone() {
      return new get_column_args(this);
    }

    public String getTablename() {
      return this.tablename;
    }

    public void setTablename(String tablename) {
      this.tablename = tablename;
    }

    public void unsetTablename() {
      this.tablename = null;
    }

    // Returns true if field tablename is set (has been asigned a value) and false otherwise
    public boolean isSetTablename() {
      return this.tablename != null;
    }

    public void setTablenameIsSet(boolean value) {
      if (!value) {
        this.tablename = null;
      }
    }

    public String getKey() {
      return this.key;
    }

    public void setKey(String key) {
      this.key = key;
    }

    public void unsetKey() {
      this.key = null;
    }

    // Returns true if field key is set (has been asigned a value) and false otherwise
    public boolean isSetKey() {
      return this.key != null;
    }

    public void setKeyIsSet(boolean value) {
      if (!value) {
        this.key = null;
      }
    }

    public String getColumnPath() {
      return this.columnPath;
    }

    public void setColumnPath(String columnPath) {
      this.columnPath = columnPath;
    }

    public void unsetColumnPath() {
      this.columnPath = null;
    }

    // Returns true if field columnPath is set (has been asigned a value) and false otherwise
    public boolean isSetColumnPath() {
      return this.columnPath != null;
    }

    public void setColumnPathIsSet(boolean value) {
      if (!value) {
        this.columnPath = null;
      }
    }

    public void setFieldValue(int fieldID, Object value) {
      switch (fieldID) {
      case TABLENAME:
        if (value == null) {
          unsetTablename();
        } else {
          setTablename((String)value);
        }
        break;

      case KEY:
        if (value == null) {
          unsetKey();
        } else {
          setKey((String)value);
        }
        break;

      case COLUMNPATH:
        if (value == null) {
          unsetColumnPath();
        } else {
          setColumnPath((String)value);
        }
        break;

      default:
        throw new IllegalArgumentException("Field " + fieldID + " doesn't exist!");
      }
    }

    public Object getFieldValue(int fieldID) {
      switch (fieldID) {
      case TABLENAME:
        return getTablename();

      case KEY:
        return getKey();

      case COLUMNPATH:
        return getColumnPath();

      default:
        throw new IllegalArgumentException("Field " + fieldID + " doesn't exist!");
      }
    }

    // Returns true if field corresponding to fieldID is set (has been asigned a value) and false otherwise
    public boolean isSet(int fieldID) {
      switch (fieldID) {
      case TABLENAME:
        return isSetTablename();
      case KEY:
        return isSetKey();
      case COLUMNPATH:
        return isSetColumnPath();
      default:
        throw new IllegalArgumentException("Field " + fieldID + " doesn't exist!");
      }
    }

    @Override
    public boolean equals(Object that) {
      if (that == null)
        return false;
      if (that instanceof get_column_args)
        return this.equals((get_column_args)that);
      return false;
    }

    public boolean equals(get_column_args that) {
      if (that == null)
        return false;

      boolean this_present_tablename = true && this.isSetTablename();
      boolean that_present_tablename = true && that.isSetTablename();
      if (this_present_tablename || that_present_tablename) {
        if (!(this_present_tablename && that_present_tablename))
          return false;
        if (!this.tablename.equals(that.tablename))
          return false;
      }

      boolean this_present_key = true && this.isSetKey();
      boolean that_present_key = true && that.isSetKey();
      if (this_present_key || that_present_key) {
        if (!(this_present_key && that_present_key))
          return false;
        if (!this.key.equals(that.key))
          return false;
      }

      boolean this_present_columnPath = true && this.isSetColumnPath();
      boolean that_present_columnPath = true && that.isSetColumnPath();
      if (this_present_columnPath || that_present_columnPath) {
        if (!(this_present_columnPath && that_present_columnPath))
          return false;
        if (!this.columnPath.equals(that.columnPath))
          return false;
      }

      return true;
    }

    @Override
    public int hashCode() {
      return 0;
    }

    public void read(TProtocol iprot) throws TException {
      TField field;
      iprot.readStructBegin();
      while (true)
      {
        field = iprot.readFieldBegin();
        if (field.type == TType.STOP) { 
          break;
        }
        switch (field.id)
        {
          case TABLENAME:
            if (field.type == TType.STRING) {
              this.tablename = iprot.readString();
            } else { 
              TProtocolUtil.skip(iprot, field.type);
            }
            break;
          case KEY:
            if (field.type == TType.STRING) {
              this.key = iprot.readString();
            } else { 
              TProtocolUtil.skip(iprot, field.type);
            }
            break;
          case COLUMNPATH:
            if (field.type == TType.STRING) {
              this.columnPath = iprot.readString();
            } else { 
              TProtocolUtil.skip(iprot, field.type);
            }
            break;
          default:
            TProtocolUtil.skip(iprot, field.type);
            break;
        }
        iprot.readFieldEnd();
      }
      iprot.readStructEnd();


      // check for required fields of primitive type, which can't be checked in the validate method
      validate();
    }

    public void write(TProtocol oprot) throws TException {
      validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (this.tablename != null) {
        oprot.writeFieldBegin(TABLENAME_FIELD_DESC);
        oprot.writeString(this.tablename);
        oprot.writeFieldEnd();
      }
      if (this.key != null) {
        oprot.writeFieldBegin(KEY_FIELD_DESC);
        oprot.writeString(this.key);
        oprot.writeFieldEnd();
      }
      if (this.columnPath != null) {
        oprot.writeFieldBegin(COLUMN_PATH_FIELD_DESC);
        oprot.writeString(this.columnPath);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

    @Override
    public String toString() {
      StringBuilder sb = new StringBuilder("get_column_args(");
      boolean first = true;

      sb.append("tablename:");
      if (this.tablename == null) {
        sb.append("null");
      } else {
        sb.append(this.tablename);
      }
      first = false;
      if (!first) sb.append(", ");
      sb.append("key:");
      if (this.key == null) {
        sb.append("null");
      } else {
        sb.append(this.key);
      }
      first = false;
      if (!first) sb.append(", ");
      sb.append("columnPath:");
      if (this.columnPath == null) {
        sb.append("null");
      } else {
        sb.append(this.columnPath);
      }
      first = false;
      sb.append(")");
      return sb.toString();
    }

    public void validate() throws TException {
      // check for required fields
      // check that fields of type enum have valid values
    }

  }

  public static class get_column_result implements TBase, java.io.Serializable, Cloneable   {
    private static final TStruct STRUCT_DESC = new TStruct("get_column_result");
    private static final TField SUCCESS_FIELD_DESC = new TField("success", TType.STRUCT, (short)0);
    private static final TField IRE_FIELD_DESC = new TField("ire", TType.STRUCT, (short)1);
    private static final TField NFE_FIELD_DESC = new TField("nfe", TType.STRUCT, (short)2);

    public column_t success;
    public static final int SUCCESS = 0;
    public InvalidRequestException ire;
    public static final int IRE = 1;
    public NotFoundException nfe;
    public static final int NFE = 2;

    private final Isset __isset = new Isset();
    private static final class Isset implements java.io.Serializable {
    }

    public static final Map<Integer, FieldMetaData> metaDataMap = Collections.unmodifiableMap(new HashMap<Integer, FieldMetaData>() {{
      put(SUCCESS, new FieldMetaData("success", TFieldRequirementType.DEFAULT, 
          new StructMetaData(TType.STRUCT, column_t.class)));
      put(IRE, new FieldMetaData("ire", TFieldRequirementType.DEFAULT, 
          new FieldValueMetaData(TType.STRUCT)));
      put(NFE, new FieldMetaData("nfe", TFieldRequirementType.DEFAULT, 
          new FieldValueMetaData(TType.STRUCT)));
    }});

    static {
      FieldMetaData.addStructMetaDataMap(get_column_result.class, metaDataMap);
    }

    public get_column_result() {
    }

    public get_column_result(
      column_t success,
      InvalidRequestException ire,
      NotFoundException nfe)
    {
      this();
      this.success = success;
      this.ire = ire;
      this.nfe = nfe;
    }

    /**
     * Performs a deep copy on <i>other</i>.
     */
    public get_column_result(get_column_result other) {
      if (other.isSetSuccess()) {
        this.success = new column_t(other.success);
      }
      if (other.isSetIre()) {
        this.ire = new InvalidRequestException(other.ire);
      }
      if (other.isSetNfe()) {
        this.nfe = new NotFoundException(other.nfe);
      }
    }

    @Override
    public get_column_result clone() {
      return new get_column_result(this);
    }

    public column_t getSuccess() {
      return this.success;
    }

    public void setSuccess(column_t success) {
      this.success = success;
    }

    public void unsetSuccess() {
      this.success = null;
    }

    // Returns true if field success is set (has been asigned a value) and false otherwise
    public boolean isSetSuccess() {
      return this.success != null;
    }

    public void setSuccessIsSet(boolean value) {
      if (!value) {
        this.success = null;
      }
    }

    public InvalidRequestException getIre() {
      return this.ire;
    }

    public void setIre(InvalidRequestException ire) {
      this.ire = ire;
    }

    public void unsetIre() {
      this.ire = null;
    }

    // Returns true if field ire is set (has been asigned a value) and false otherwise
    public boolean isSetIre() {
      return this.ire != null;
    }

    public void setIreIsSet(boolean value) {
      if (!value) {
        this.ire = null;
      }
    }

    public NotFoundException getNfe() {
      return this.nfe;
    }

    public void setNfe(NotFoundException nfe) {
      this.nfe = nfe;
    }

    public void unsetNfe() {
      this.nfe = null;
    }

    // Returns true if field nfe is set (has been asigned a value) and false otherwise
    public boolean isSetNfe() {
      return this.nfe != null;
    }

    public void setNfeIsSet(boolean value) {
      if (!value) {
        this.nfe = null;
      }
    }

    public void setFieldValue(int fieldID, Object value) {
      switch (fieldID) {
      case SUCCESS:
        if (value == null) {
          unsetSuccess();
        } else {
          setSuccess((column_t)value);
        }
        break;

      case IRE:
        if (value == null) {
          unsetIre();
        } else {
          setIre((InvalidRequestException)value);
        }
        break;

      case NFE:
        if (value == null) {
          unsetNfe();
        } else {
          setNfe((NotFoundException)value);
        }
        break;

      default:
        throw new IllegalArgumentException("Field " + fieldID + " doesn't exist!");
      }
    }

    public Object getFieldValue(int fieldID) {
      switch (fieldID) {
      case SUCCESS:
        return getSuccess();

      case IRE:
        return getIre();

      case NFE:
        return getNfe();

      default:
        throw new IllegalArgumentException("Field " + fieldID + " doesn't exist!");
      }
    }

    // Returns true if field corresponding to fieldID is set (has been asigned a value) and false otherwise
    public boolean isSet(int fieldID) {
      switch (fieldID) {
      case SUCCESS:
        return isSetSuccess();
      case IRE:
        return isSetIre();
      case NFE:
        return isSetNfe();
      default:
        throw new IllegalArgumentException("Field " + fieldID + " doesn't exist!");
      }
    }

    @Override
    public boolean equals(Object that) {
      if (that == null)
        return false;
      if (that instanceof get_column_result)
        return this.equals((get_column_result)that);
      return false;
    }

    public boolean equals(get_column_result that) {
      if (that == null)
        return false;

      boolean this_present_success = true && this.isSetSuccess();
      boolean that_present_success = true && that.isSetSuccess();
      if (this_present_success || that_present_success) {
        if (!(this_present_success && that_present_success))
          return false;
        if (!this.success.equals(that.success))
          return false;
      }

      boolean this_present_ire = true && this.isSetIre();
      boolean that_present_ire = true && that.isSetIre();
      if (this_present_ire || that_present_ire) {
        if (!(this_present_ire && that_present_ire))
          return false;
        if (!this.ire.equals(that.ire))
          return false;
      }

      boolean this_present_nfe = true && this.isSetNfe();
      boolean that_present_nfe = true && that.isSetNfe();
      if (this_present_nfe || that_present_nfe) {
        if (!(this_present_nfe && that_present_nfe))
          return false;
        if (!this.nfe.equals(that.nfe))
          return false;
      }

      return true;
    }

    @Override
    public int hashCode() {
      return 0;
    }

    public void read(TProtocol iprot) throws TException {
      TField field;
      iprot.readStructBegin();
      while (true)
      {
        field = iprot.readFieldBegin();
        if (field.type == TType.STOP) { 
          break;
        }
        switch (field.id)
        {
          case SUCCESS:
            if (field.type == TType.STRUCT) {
              this.success = new column_t();
              this.success.read(iprot);
            } else { 
              TProtocolUtil.skip(iprot, field.type);
            }
            break;
          case IRE:
            if (field.type == TType.STRUCT) {
              this.ire = new InvalidRequestException();
              this.ire.read(iprot);
            } else { 
              TProtocolUtil.skip(iprot, field.type);
            }
            break;
          case NFE:
            if (field.type == TType.STRUCT) {
              this.nfe = new NotFoundException();
              this.nfe.read(iprot);
            } else { 
              TProtocolUtil.skip(iprot, field.type);
            }
            break;
          default:
            TProtocolUtil.skip(iprot, field.type);
            break;
        }
        iprot.readFieldEnd();
      }
      iprot.readStructEnd();


      // check for required fields of primitive type, which can't be checked in the validate method
      validate();
    }

    public void write(TProtocol oprot) throws TException {
      oprot.writeStructBegin(STRUCT_DESC);

      if (this.isSetSuccess()) {
        oprot.writeFieldBegin(SUCCESS_FIELD_DESC);
        this.success.write(oprot);
        oprot.writeFieldEnd();
      } else if (this.isSetIre()) {
        oprot.writeFieldBegin(IRE_FIELD_DESC);
        this.ire.write(oprot);
        oprot.writeFieldEnd();
      } else if (this.isSetNfe()) {
        oprot.writeFieldBegin(NFE_FIELD_DESC);
        this.nfe.write(oprot);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

    @Override
    public String toString() {
      StringBuilder sb = new StringBuilder("get_column_result(");
      boolean first = true;

      sb.append("success:");
      if (this.success == null) {
        sb.append("null");
      } else {
        sb.append(this.success);
      }
      first = false;
      if (!first) sb.append(", ");
      sb.append("ire:");
      if (this.ire == null) {
        sb.append("null");
      } else {
        sb.append(this.ire);
      }
      first = false;
      if (!first) sb.append(", ");
      sb.append("nfe:");
      if (this.nfe == null) {
        sb.append("null");
      } else {
        sb.append(this.nfe);
      }
      first = false;
      sb.append(")");
      return sb.toString();
    }

    public void validate() throws TException {
      // check for required fields
      // check that fields of type enum have valid values
    }

  }

  public static class get_column_count_args implements TBase, java.io.Serializable, Cloneable   {
    private static final TStruct STRUCT_DESC = new TStruct("get_column_count_args");
    private static final TField TABLENAME_FIELD_DESC = new TField("tablename", TType.STRING, (short)1);
    private static final TField KEY_FIELD_DESC = new TField("key", TType.STRING, (short)2);
    private static final TField COLUMN_PARENT_FIELD_DESC = new TField("columnParent", TType.STRING, (short)3);

    public String tablename;
    public static final int TABLENAME = 1;
    public String key;
    public static final int KEY = 2;
    public String columnParent;
    public static final int COLUMNPARENT = 3;

    private final Isset __isset = new Isset();
    private static final class Isset implements java.io.Serializable {
    }

    public static final Map<Integer, FieldMetaData> metaDataMap = Collections.unmodifiableMap(new HashMap<Integer, FieldMetaData>() {{
      put(TABLENAME, new FieldMetaData("tablename", TFieldRequirementType.DEFAULT, 
          new FieldValueMetaData(TType.STRING)));
      put(KEY, new FieldMetaData("key", TFieldRequirementType.DEFAULT, 
          new FieldValueMetaData(TType.STRING)));
      put(COLUMNPARENT, new FieldMetaData("columnParent", TFieldRequirementType.DEFAULT, 
          new FieldValueMetaData(TType.STRING)));
    }});

    static {
      FieldMetaData.addStructMetaDataMap(get_column_count_args.class, metaDataMap);
    }

    public get_column_count_args() {
    }

    public get_column_count_args(
      String tablename,
      String key,
      String columnParent)
    {
      this();
      this.tablename = tablename;
      this.key = key;
      this.columnParent = columnParent;
    }

    /**
     * Performs a deep copy on <i>other</i>.
     */
    public get_column_count_args(get_column_count_args other) {
      if (other.isSetTablename()) {
        this.tablename = other.tablename;
      }
      if (other.isSetKey()) {
        this.key = other.key;
      }
      if (other.isSetColumnParent()) {
        this.columnParent = other.columnParent;
      }
    }

    @Override
    public get_column_count_args clone() {
      return new get_column_count_args(this);
    }

    public String getTablename() {
      return this.tablename;
    }

    public void setTablename(String tablename) {
      this.tablename = tablename;
    }

    public void unsetTablename() {
      this.tablename = null;
    }

    // Returns true if field tablename is set (has been asigned a value) and false otherwise
    public boolean isSetTablename() {
      return this.tablename != null;
    }

    public void setTablenameIsSet(boolean value) {
      if (!value) {
        this.tablename = null;
      }
    }

    public String getKey() {
      return this.key;
    }

    public void setKey(String key) {
      this.key = key;
    }

    public void unsetKey() {
      this.key = null;
    }

    // Returns true if field key is set (has been asigned a value) and false otherwise
    public boolean isSetKey() {
      return this.key != null;
    }

    public void setKeyIsSet(boolean value) {
      if (!value) {
        this.key = null;
      }
    }

    public String getColumnParent() {
      return this.columnParent;
    }

    public void setColumnParent(String columnParent) {
      this.columnParent = columnParent;
    }

    public void unsetColumnParent() {
      this.columnParent = null;
    }

    // Returns true if field columnParent is set (has been asigned a value) and false otherwise
    public boolean isSetColumnParent() {
      return this.columnParent != null;
    }

    public void setColumnParentIsSet(boolean value) {
      if (!value) {
        this.columnParent = null;
      }
    }

    public void setFieldValue(int fieldID, Object value) {
      switch (fieldID) {
      case TABLENAME:
        if (value == null) {
          unsetTablename();
        } else {
          setTablename((String)value);
        }
        break;

      case KEY:
        if (value == null) {
          unsetKey();
        } else {
          setKey((String)value);
        }
        break;

      case COLUMNPARENT:
        if (value == null) {
          unsetColumnParent();
        } else {
          setColumnParent((String)value);
        }
        break;

      default:
        throw new IllegalArgumentException("Field " + fieldID + " doesn't exist!");
      }
    }

    public Object getFieldValue(int fieldID) {
      switch (fieldID) {
      case TABLENAME:
        return getTablename();

      case KEY:
        return getKey();

      case COLUMNPARENT:
        return getColumnParent();

      default:
        throw new IllegalArgumentException("Field " + fieldID + " doesn't exist!");
      }
    }

    // Returns true if field corresponding to fieldID is set (has been asigned a value) and false otherwise
    public boolean isSet(int fieldID) {
      switch (fieldID) {
      case TABLENAME:
        return isSetTablename();
      case KEY:
        return isSetKey();
      case COLUMNPARENT:
        return isSetColumnParent();
      default:
        throw new IllegalArgumentException("Field " + fieldID + " doesn't exist!");
      }
    }

    @Override
    public boolean equals(Object that) {
      if (that == null)
        return false;
      if (that instanceof get_column_count_args)
        return this.equals((get_column_count_args)that);
      return false;
    }

    public boolean equals(get_column_count_args that) {
      if (that == null)
        return false;

      boolean this_present_tablename = true && this.isSetTablename();
      boolean that_present_tablename = true && that.isSetTablename();
      if (this_present_tablename || that_present_tablename) {
        if (!(this_present_tablename && that_present_tablename))
          return false;
        if (!this.tablename.equals(that.tablename))
          return false;
      }

      boolean this_present_key = true && this.isSetKey();
      boolean that_present_key = true && that.isSetKey();
      if (this_present_key || that_present_key) {
        if (!(this_present_key && that_present_key))
          return false;
        if (!this.key.equals(that.key))
          return false;
      }

      boolean this_present_columnParent = true && this.isSetColumnParent();
      boolean that_present_columnParent = true && that.isSetColumnParent();
      if (this_present_columnParent || that_present_columnParent) {
        if (!(this_present_columnParent && that_present_columnParent))
          return false;
        if (!this.columnParent.equals(that.columnParent))
          return false;
      }

      return true;
    }

    @Override
    public int hashCode() {
      return 0;
    }

    public void read(TProtocol iprot) throws TException {
      TField field;
      iprot.readStructBegin();
      while (true)
      {
        field = iprot.readFieldBegin();
        if (field.type == TType.STOP) { 
          break;
        }
        switch (field.id)
        {
          case TABLENAME:
            if (field.type == TType.STRING) {
              this.tablename = iprot.readString();
            } else { 
              TProtocolUtil.skip(iprot, field.type);
            }
            break;
          case KEY:
            if (field.type == TType.STRING) {
              this.key = iprot.readString();
            } else { 
              TProtocolUtil.skip(iprot, field.type);
            }
            break;
          case COLUMNPARENT:
            if (field.type == TType.STRING) {
              this.columnParent = iprot.readString();
            } else { 
              TProtocolUtil.skip(iprot, field.type);
            }
            break;
          default:
            TProtocolUtil.skip(iprot, field.type);
            break;
        }
        iprot.readFieldEnd();
      }
      iprot.readStructEnd();


      // check for required fields of primitive type, which can't be checked in the validate method
      validate();
    }

    public void write(TProtocol oprot) throws TException {
      validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (this.tablename != null) {
        oprot.writeFieldBegin(TABLENAME_FIELD_DESC);
        oprot.writeString(this.tablename);
        oprot.writeFieldEnd();
      }
      if (this.key != null) {
        oprot.writeFieldBegin(KEY_FIELD_DESC);
        oprot.writeString(this.key);
        oprot.writeFieldEnd();
      }
      if (this.columnParent != null) {
        oprot.writeFieldBegin(COLUMN_PARENT_FIELD_DESC);
        oprot.writeString(this.columnParent);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

    @Override
    public String toString() {
      StringBuilder sb = new StringBuilder("get_column_count_args(");
      boolean first = true;

      sb.append("tablename:");
      if (this.tablename == null) {
        sb.append("null");
      } else {
        sb.append(this.tablename);
      }
      first = false;
      if (!first) sb.append(", ");
      sb.append("key:");
      if (this.key == null) {
        sb.append("null");
      } else {
        sb.append(this.key);
      }
      first = false;
      if (!first) sb.append(", ");
      sb.append("columnParent:");
      if (this.columnParent == null) {
        sb.append("null");
      } else {
        sb.append(this.columnParent);
      }
      first = false;
      sb.append(")");
      return sb.toString();
    }

    public void validate() throws TException {
      // check for required fields
      // check that fields of type enum have valid values
    }

  }

  public static class get_column_count_result implements TBase, java.io.Serializable, Cloneable   {
    private static final TStruct STRUCT_DESC = new TStruct("get_column_count_result");
    private static final TField SUCCESS_FIELD_DESC = new TField("success", TType.I32, (short)0);
    private static final TField IRE_FIELD_DESC = new TField("ire", TType.STRUCT, (short)1);

    public int success;
    public static final int SUCCESS = 0;
    public InvalidRequestException ire;
    public static final int IRE = 1;

    private final Isset __isset = new Isset();
    private static final class Isset implements java.io.Serializable {
      public boolean success = false;
    }

    public static final Map<Integer, FieldMetaData> metaDataMap = Collections.unmodifiableMap(new HashMap<Integer, FieldMetaData>() {{
      put(SUCCESS, new FieldMetaData("success", TFieldRequirementType.DEFAULT, 
          new FieldValueMetaData(TType.I32)));
      put(IRE, new FieldMetaData("ire", TFieldRequirementType.DEFAULT, 
          new FieldValueMetaData(TType.STRUCT)));
    }});

    static {
      FieldMetaData.addStructMetaDataMap(get_column_count_result.class, metaDataMap);
    }

    public get_column_count_result() {
    }

    public get_column_count_result(
      int success,
      InvalidRequestException ire)
    {
      this();
      this.success = success;
      this.__isset.success = true;
      this.ire = ire;
    }

    /**
     * Performs a deep copy on <i>other</i>.
     */
    public get_column_count_result(get_column_count_result other) {
      __isset.success = other.__isset.success;
      this.success = other.success;
      if (other.isSetIre()) {
        this.ire = new InvalidRequestException(other.ire);
      }
    }

    @Override
    public get_column_count_result clone() {
      return new get_column_count_result(this);
    }

    public int getSuccess() {
      return this.success;
    }

    public void setSuccess(int success) {
      this.success = success;
      this.__isset.success = true;
    }

    public void unsetSuccess() {
      this.__isset.success = false;
    }

    // Returns true if field success is set (has been asigned a value) and false otherwise
    public boolean isSetSuccess() {
      return this.__isset.success;
    }

    public void setSuccessIsSet(boolean value) {
      this.__isset.success = value;
    }

    public InvalidRequestException getIre() {
      return this.ire;
    }

    public void setIre(InvalidRequestException ire) {
      this.ire = ire;
    }

    public void unsetIre() {
      this.ire = null;
    }

    // Returns true if field ire is set (has been asigned a value) and false otherwise
    public boolean isSetIre() {
      return this.ire != null;
    }

    public void setIreIsSet(boolean value) {
      if (!value) {
        this.ire = null;
      }
    }

    public void setFieldValue(int fieldID, Object value) {
      switch (fieldID) {
      case SUCCESS:
        if (value == null) {
          unsetSuccess();
        } else {
          setSuccess((Integer)value);
        }
        break;

      case IRE:
        if (value == null) {
          unsetIre();
        } else {
          setIre((InvalidRequestException)value);
        }
        break;

      default:
        throw new IllegalArgumentException("Field " + fieldID + " doesn't exist!");
      }
    }

    public Object getFieldValue(int fieldID) {
      switch (fieldID) {
      case SUCCESS:
        return new Integer(getSuccess());

      case IRE:
        return getIre();

      default:
        throw new IllegalArgumentException("Field " + fieldID + " doesn't exist!");
      }
    }

    // Returns true if field corresponding to fieldID is set (has been asigned a value) and false otherwise
    public boolean isSet(int fieldID) {
      switch (fieldID) {
      case SUCCESS:
        return isSetSuccess();
      case IRE:
        return isSetIre();
      default:
        throw new IllegalArgumentException("Field " + fieldID + " doesn't exist!");
      }
    }

    @Override
    public boolean equals(Object that) {
      if (that == null)
        return false;
      if (that instanceof get_column_count_result)
        return this.equals((get_column_count_result)that);
      return false;
    }

    public boolean equals(get_column_count_result that) {
      if (that == null)
        return false;

      boolean this_present_success = true;
      boolean that_present_success = true;
      if (this_present_success || that_present_success) {
        if (!(this_present_success && that_present_success))
          return false;
        if (this.success != that.success)
          return false;
      }

      boolean this_present_ire = true && this.isSetIre();
      boolean that_present_ire = true && that.isSetIre();
      if (this_present_ire || that_present_ire) {
        if (!(this_present_ire && that_present_ire))
          return false;
        if (!this.ire.equals(that.ire))
          return false;
      }

      return true;
    }

    @Override
    public int hashCode() {
      return 0;
    }

    public void read(TProtocol iprot) throws TException {
      TField field;
      iprot.readStructBegin();
      while (true)
      {
        field = iprot.readFieldBegin();
        if (field.type == TType.STOP) { 
          break;
        }
        switch (field.id)
        {
          case SUCCESS:
            if (field.type == TType.I32) {
              this.success = iprot.readI32();
              this.__isset.success = true;
            } else { 
              TProtocolUtil.skip(iprot, field.type);
            }
            break;
          case IRE:
            if (field.type == TType.STRUCT) {
              this.ire = new InvalidRequestException();
              this.ire.read(iprot);
            } else { 
              TProtocolUtil.skip(iprot, field.type);
            }
            break;
          default:
            TProtocolUtil.skip(iprot, field.type);
            break;
        }
        iprot.readFieldEnd();
      }
      iprot.readStructEnd();


      // check for required fields of primitive type, which can't be checked in the validate method
      validate();
    }

    public void write(TProtocol oprot) throws TException {
      oprot.writeStructBegin(STRUCT_DESC);

      if (this.isSetSuccess()) {
        oprot.writeFieldBegin(SUCCESS_FIELD_DESC);
        oprot.writeI32(this.success);
        oprot.writeFieldEnd();
      } else if (this.isSetIre()) {
        oprot.writeFieldBegin(IRE_FIELD_DESC);
        this.ire.write(oprot);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

    @Override
    public String toString() {
      StringBuilder sb = new StringBuilder("get_column_count_result(");
      boolean first = true;

      sb.append("success:");
      sb.append(this.success);
      first = false;
      if (!first) sb.append(", ");
      sb.append("ire:");
      if (this.ire == null) {
        sb.append("null");
      } else {
        sb.append(this.ire);
      }
      first = false;
      sb.append(")");
      return sb.toString();
    }

    public void validate() throws TException {
      // check for required fields
      // check that fields of type enum have valid values
    }

  }

  public static class insert_args implements TBase, java.io.Serializable, Cloneable   {
    private static final TStruct STRUCT_DESC = new TStruct("insert_args");
    private static final TField TABLENAME_FIELD_DESC = new TField("tablename", TType.STRING, (short)1);
    private static final TField KEY_FIELD_DESC = new TField("key", TType.STRING, (short)2);
    private static final TField COLUMN_PATH_FIELD_DESC = new TField("columnPath", TType.STRING, (short)3);
    private static final TField CELL_DATA_FIELD_DESC = new TField("cellData", TType.STRING, (short)4);
    private static final TField TIMESTAMP_FIELD_DESC = new TField("timestamp", TType.I64, (short)5);
    private static final TField BLOCK_FIELD_DESC = new TField("block", TType.BOOL, (short)6);

    public String tablename;
    public static final int TABLENAME = 1;
    public String key;
    public static final int KEY = 2;
    public String columnPath;
    public static final int COLUMNPATH = 3;
    public byte[] cellData;
    public static final int CELLDATA = 4;
    public long timestamp;
    public static final int TIMESTAMP = 5;
    public boolean block;
    public static final int BLOCK = 6;

    private final Isset __isset = new Isset();
    private static final class Isset implements java.io.Serializable {
      public boolean timestamp = false;
      public boolean block = false;
    }

    public static final Map<Integer, FieldMetaData> metaDataMap = Collections.unmodifiableMap(new HashMap<Integer, FieldMetaData>() {{
      put(TABLENAME, new FieldMetaData("tablename", TFieldRequirementType.DEFAULT, 
          new FieldValueMetaData(TType.STRING)));
      put(KEY, new FieldMetaData("key", TFieldRequirementType.DEFAULT, 
          new FieldValueMetaData(TType.STRING)));
      put(COLUMNPATH, new FieldMetaData("columnPath", TFieldRequirementType.DEFAULT, 
          new FieldValueMetaData(TType.STRING)));
      put(CELLDATA, new FieldMetaData("cellData", TFieldRequirementType.DEFAULT, 
          new FieldValueMetaData(TType.STRING)));
      put(TIMESTAMP, new FieldMetaData("timestamp", TFieldRequirementType.DEFAULT, 
          new FieldValueMetaData(TType.I64)));
      put(BLOCK, new FieldMetaData("block", TFieldRequirementType.DEFAULT, 
          new FieldValueMetaData(TType.BOOL)));
    }});

    static {
      FieldMetaData.addStructMetaDataMap(insert_args.class, metaDataMap);
    }

    public insert_args() {
      this.block = false;

    }

    public insert_args(
      String tablename,
      String key,
      String columnPath,
      byte[] cellData,
      long timestamp,
      boolean block)
    {
      this();
      this.tablename = tablename;
      this.key = key;
      this.columnPath = columnPath;
      this.cellData = cellData;
      this.timestamp = timestamp;
      this.__isset.timestamp = true;
      this.block = block;
      this.__isset.block = true;
    }

    /**
     * Performs a deep copy on <i>other</i>.
     */
    public insert_args(insert_args other) {
      if (other.isSetTablename()) {
        this.tablename = other.tablename;
      }
      if (other.isSetKey()) {
        this.key = other.key;
      }
      if (other.isSetColumnPath()) {
        this.columnPath = other.columnPath;
      }
      if (other.isSetCellData()) {
        this.cellData = new byte[other.cellData.length];
        System.arraycopy(other.cellData, 0, cellData, 0, other.cellData.length);
      }
      __isset.timestamp = other.__isset.timestamp;
      this.timestamp = other.timestamp;
      __isset.block = other.__isset.block;
      this.block = other.block;
    }

    @Override
    public insert_args clone() {
      return new insert_args(this);
    }

    public String getTablename() {
      return this.tablename;
    }

    public void setTablename(String tablename) {
      this.tablename = tablename;
    }

    public void unsetTablename() {
      this.tablename = null;
    }

    // Returns true if field tablename is set (has been asigned a value) and false otherwise
    public boolean isSetTablename() {
      return this.tablename != null;
    }

    public void setTablenameIsSet(boolean value) {
      if (!value) {
        this.tablename = null;
      }
    }

    public String getKey() {
      return this.key;
    }

    public void setKey(String key) {
      this.key = key;
    }

    public void unsetKey() {
      this.key = null;
    }

    // Returns true if field key is set (has been asigned a value) and false otherwise
    public boolean isSetKey() {
      return this.key != null;
    }

    public void setKeyIsSet(boolean value) {
      if (!value) {
        this.key = null;
      }
    }

    public String getColumnPath() {
      return this.columnPath;
    }

    public void setColumnPath(String columnPath) {
      this.columnPath = columnPath;
    }

    public void unsetColumnPath() {
      this.columnPath = null;
    }

    // Returns true if field columnPath is set (has been asigned a value) and false otherwise
    public boolean isSetColumnPath() {
      return this.columnPath != null;
    }

    public void setColumnPathIsSet(boolean value) {
      if (!value) {
        this.columnPath = null;
      }
    }

    public byte[] getCellData() {
      return this.cellData;
    }

    public void setCellData(byte[] cellData) {
      this.cellData = cellData;
    }

    public void unsetCellData() {
      this.cellData = null;
    }

    // Returns true if field cellData is set (has been asigned a value) and false otherwise
    public boolean isSetCellData() {
      return this.cellData != null;
    }

    public void setCellDataIsSet(boolean value) {
      if (!value) {
        this.cellData = null;
      }
    }

    public long getTimestamp() {
      return this.timestamp;
    }

    public void setTimestamp(long timestamp) {
      this.timestamp = timestamp;
      this.__isset.timestamp = true;
    }

    public void unsetTimestamp() {
      this.__isset.timestamp = false;
    }

    // Returns true if field timestamp is set (has been asigned a value) and false otherwise
    public boolean isSetTimestamp() {
      return this.__isset.timestamp;
    }

    public void setTimestampIsSet(boolean value) {
      this.__isset.timestamp = value;
    }

    public boolean isBlock() {
      return this.block;
    }

    public void setBlock(boolean block) {
      this.block = block;
      this.__isset.block = true;
    }

    public void unsetBlock() {
      this.__isset.block = false;
    }

    // Returns true if field block is set (has been asigned a value) and false otherwise
    public boolean isSetBlock() {
      return this.__isset.block;
    }

    public void setBlockIsSet(boolean value) {
      this.__isset.block = value;
    }

    public void setFieldValue(int fieldID, Object value) {
      switch (fieldID) {
      case TABLENAME:
        if (value == null) {
          unsetTablename();
        } else {
          setTablename((String)value);
        }
        break;

      case KEY:
        if (value == null) {
          unsetKey();
        } else {
          setKey((String)value);
        }
        break;

      case COLUMNPATH:
        if (value == null) {
          unsetColumnPath();
        } else {
          setColumnPath((String)value);
        }
        break;

      case CELLDATA:
        if (value == null) {
          unsetCellData();
        } else {
          setCellData((byte[])value);
        }
        break;

      case TIMESTAMP:
        if (value == null) {
          unsetTimestamp();
        } else {
          setTimestamp((Long)value);
        }
        break;

      case BLOCK:
        if (value == null) {
          unsetBlock();
        } else {
          setBlock((Boolean)value);
        }
        break;

      default:
        throw new IllegalArgumentException("Field " + fieldID + " doesn't exist!");
      }
    }

    public Object getFieldValue(int fieldID) {
      switch (fieldID) {
      case TABLENAME:
        return getTablename();

      case KEY:
        return getKey();

      case COLUMNPATH:
        return getColumnPath();

      case CELLDATA:
        return getCellData();

      case TIMESTAMP:
        return new Long(getTimestamp());

      case BLOCK:
        return new Boolean(isBlock());

      default:
        throw new IllegalArgumentException("Field " + fieldID + " doesn't exist!");
      }
    }

    // Returns true if field corresponding to fieldID is set (has been asigned a value) and false otherwise
    public boolean isSet(int fieldID) {
      switch (fieldID) {
      case TABLENAME:
        return isSetTablename();
      case KEY:
        return isSetKey();
      case COLUMNPATH:
        return isSetColumnPath();
      case CELLDATA:
        return isSetCellData();
      case TIMESTAMP:
        return isSetTimestamp();
      case BLOCK:
        return isSetBlock();
      default:
        throw new IllegalArgumentException("Field " + fieldID + " doesn't exist!");
      }
    }

    @Override
    public boolean equals(Object that) {
      if (that == null)
        return false;
      if (that instanceof insert_args)
        return this.equals((insert_args)that);
      return false;
    }

    public boolean equals(insert_args that) {
      if (that == null)
        return false;

      boolean this_present_tablename = true && this.isSetTablename();
      boolean that_present_tablename = true && that.isSetTablename();
      if (this_present_tablename || that_present_tablename) {
        if (!(this_present_tablename && that_present_tablename))
          return false;
        if (!this.tablename.equals(that.tablename))
          return false;
      }

      boolean this_present_key = true && this.isSetKey();
      boolean that_present_key = true && that.isSetKey();
      if (this_present_key || that_present_key) {
        if (!(this_present_key && that_present_key))
          return false;
        if (!this.key.equals(that.key))
          return false;
      }

      boolean this_present_columnPath = true && this.isSetColumnPath();
      boolean that_present_columnPath = true && that.isSetColumnPath();
      if (this_present_columnPath || that_present_columnPath) {
        if (!(this_present_columnPath && that_present_columnPath))
          return false;
        if (!this.columnPath.equals(that.columnPath))
          return false;
      }

      boolean this_present_cellData = true && this.isSetCellData();
      boolean that_present_cellData = true && that.isSetCellData();
      if (this_present_cellData || that_present_cellData) {
        if (!(this_present_cellData && that_present_cellData))
          return false;
        if (!java.util.Arrays.equals(this.cellData, that.cellData))
          return false;
      }

      boolean this_present_timestamp = true;
      boolean that_present_timestamp = true;
      if (this_present_timestamp || that_present_timestamp) {
        if (!(this_present_timestamp && that_present_timestamp))
          return false;
        if (this.timestamp != that.timestamp)
          return false;
      }

      boolean this_present_block = true;
      boolean that_present_block = true;
      if (this_present_block || that_present_block) {
        if (!(this_present_block && that_present_block))
          return false;
        if (this.block != that.block)
          return false;
      }

      return true;
    }

    @Override
    public int hashCode() {
      return 0;
    }

    public void read(TProtocol iprot) throws TException {
      TField field;
      iprot.readStructBegin();
      while (true)
      {
        field = iprot.readFieldBegin();
        if (field.type == TType.STOP) { 
          break;
        }
        switch (field.id)
        {
          case TABLENAME:
            if (field.type == TType.STRING) {
              this.tablename = iprot.readString();
            } else { 
              TProtocolUtil.skip(iprot, field.type);
            }
            break;
          case KEY:
            if (field.type == TType.STRING) {
              this.key = iprot.readString();
            } else { 
              TProtocolUtil.skip(iprot, field.type);
            }
            break;
          case COLUMNPATH:
            if (field.type == TType.STRING) {
              this.columnPath = iprot.readString();
            } else { 
              TProtocolUtil.skip(iprot, field.type);
            }
            break;
          case CELLDATA:
            if (field.type == TType.STRING) {
              this.cellData = iprot.readBinary();
            } else { 
              TProtocolUtil.skip(iprot, field.type);
            }
            break;
          case TIMESTAMP:
            if (field.type == TType.I64) {
              this.timestamp = iprot.readI64();
              this.__isset.timestamp = true;
            } else { 
              TProtocolUtil.skip(iprot, field.type);
            }
            break;
          case BLOCK:
            if (field.type == TType.BOOL) {
              this.block = iprot.readBool();
              this.__isset.block = true;
            } else { 
              TProtocolUtil.skip(iprot, field.type);
            }
            break;
          default:
            TProtocolUtil.skip(iprot, field.type);
            break;
        }
        iprot.readFieldEnd();
      }
      iprot.readStructEnd();


      // check for required fields of primitive type, which can't be checked in the validate method
      validate();
    }

    public void write(TProtocol oprot) throws TException {
      validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (this.tablename != null) {
        oprot.writeFieldBegin(TABLENAME_FIELD_DESC);
        oprot.writeString(this.tablename);
        oprot.writeFieldEnd();
      }
      if (this.key != null) {
        oprot.writeFieldBegin(KEY_FIELD_DESC);
        oprot.writeString(this.key);
        oprot.writeFieldEnd();
      }
      if (this.columnPath != null) {
        oprot.writeFieldBegin(COLUMN_PATH_FIELD_DESC);
        oprot.writeString(this.columnPath);
        oprot.writeFieldEnd();
      }
      if (this.cellData != null) {
        oprot.writeFieldBegin(CELL_DATA_FIELD_DESC);
        oprot.writeBinary(this.cellData);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldBegin(TIMESTAMP_FIELD_DESC);
      oprot.writeI64(this.timestamp);
      oprot.writeFieldEnd();
      oprot.writeFieldBegin(BLOCK_FIELD_DESC);
      oprot.writeBool(this.block);
      oprot.writeFieldEnd();
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

    @Override
    public String toString() {
      StringBuilder sb = new StringBuilder("insert_args(");
      boolean first = true;

      sb.append("tablename:");
      if (this.tablename == null) {
        sb.append("null");
      } else {
        sb.append(this.tablename);
      }
      first = false;
      if (!first) sb.append(", ");
      sb.append("key:");
      if (this.key == null) {
        sb.append("null");
      } else {
        sb.append(this.key);
      }
      first = false;
      if (!first) sb.append(", ");
      sb.append("columnPath:");
      if (this.columnPath == null) {
        sb.append("null");
      } else {
        sb.append(this.columnPath);
      }
      first = false;
      if (!first) sb.append(", ");
      sb.append("cellData:");
      if (this.cellData == null) {
        sb.append("null");
      } else {
          int __cellData_size = Math.min(this.cellData.length, 128);
          for (int i = 0; i < __cellData_size; i++) {
            if (i != 0) sb.append(" ");
            sb.append(Integer.toHexString(this.cellData[i]).length() > 1 ? Integer.toHexString(this.cellData[i]).substring(Integer.toHexString(this.cellData[i]).length() - 2).toUpperCase() : "0" + Integer.toHexString(this.cellData[i]).toUpperCase());
          }
          if (this.cellData.length > 128) sb.append(" ...");
      }
      first = false;
      if (!first) sb.append(", ");
      sb.append("timestamp:");
      sb.append(this.timestamp);
      first = false;
      if (!first) sb.append(", ");
      sb.append("block:");
      sb.append(this.block);
      first = false;
      sb.append(")");
      return sb.toString();
    }

    public void validate() throws TException {
      // check for required fields
      // check that fields of type enum have valid values
    }

  }

  public static class insert_result implements TBase, java.io.Serializable, Cloneable   {
    private static final TStruct STRUCT_DESC = new TStruct("insert_result");
    private static final TField IRE_FIELD_DESC = new TField("ire", TType.STRUCT, (short)1);
    private static final TField UE_FIELD_DESC = new TField("ue", TType.STRUCT, (short)2);

    public InvalidRequestException ire;
    public static final int IRE = 1;
    public UnavailableException ue;
    public static final int UE = 2;

    private final Isset __isset = new Isset();
    private static final class Isset implements java.io.Serializable {
    }

    public static final Map<Integer, FieldMetaData> metaDataMap = Collections.unmodifiableMap(new HashMap<Integer, FieldMetaData>() {{
      put(IRE, new FieldMetaData("ire", TFieldRequirementType.DEFAULT, 
          new FieldValueMetaData(TType.STRUCT)));
      put(UE, new FieldMetaData("ue", TFieldRequirementType.DEFAULT, 
          new FieldValueMetaData(TType.STRUCT)));
    }});

    static {
      FieldMetaData.addStructMetaDataMap(insert_result.class, metaDataMap);
    }

    public insert_result() {
    }

    public insert_result(
      InvalidRequestException ire,
      UnavailableException ue)
    {
      this();
      this.ire = ire;
      this.ue = ue;
    }

    /**
     * Performs a deep copy on <i>other</i>.
     */
    public insert_result(insert_result other) {
      if (other.isSetIre()) {
        this.ire = new InvalidRequestException(other.ire);
      }
      if (other.isSetUe()) {
        this.ue = new UnavailableException(other.ue);
      }
    }

    @Override
    public insert_result clone() {
      return new insert_result(this);
    }

    public InvalidRequestException getIre() {
      return this.ire;
    }

    public void setIre(InvalidRequestException ire) {
      this.ire = ire;
    }

    public void unsetIre() {
      this.ire = null;
    }

    // Returns true if field ire is set (has been asigned a value) and false otherwise
    public boolean isSetIre() {
      return this.ire != null;
    }

    public void setIreIsSet(boolean value) {
      if (!value) {
        this.ire = null;
      }
    }

    public UnavailableException getUe() {
      return this.ue;
    }

    public void setUe(UnavailableException ue) {
      this.ue = ue;
    }

    public void unsetUe() {
      this.ue = null;
    }

    // Returns true if field ue is set (has been asigned a value) and false otherwise
    public boolean isSetUe() {
      return this.ue != null;
    }

    public void setUeIsSet(boolean value) {
      if (!value) {
        this.ue = null;
      }
    }

    public void setFieldValue(int fieldID, Object value) {
      switch (fieldID) {
      case IRE:
        if (value == null) {
          unsetIre();
        } else {
          setIre((InvalidRequestException)value);
        }
        break;

      case UE:
        if (value == null) {
          unsetUe();
        } else {
          setUe((UnavailableException)value);
        }
        break;

      default:
        throw new IllegalArgumentException("Field " + fieldID + " doesn't exist!");
      }
    }

    public Object getFieldValue(int fieldID) {
      switch (fieldID) {
      case IRE:
        return getIre();

      case UE:
        return getUe();

      default:
        throw new IllegalArgumentException("Field " + fieldID + " doesn't exist!");
      }
    }

    // Returns true if field corresponding to fieldID is set (has been asigned a value) and false otherwise
    public boolean isSet(int fieldID) {
      switch (fieldID) {
      case IRE:
        return isSetIre();
      case UE:
        return isSetUe();
      default:
        throw new IllegalArgumentException("Field " + fieldID + " doesn't exist!");
      }
    }

    @Override
    public boolean equals(Object that) {
      if (that == null)
        return false;
      if (that instanceof insert_result)
        return this.equals((insert_result)that);
      return false;
    }

    public boolean equals(insert_result that) {
      if (that == null)
        return false;

      boolean this_present_ire = true && this.isSetIre();
      boolean that_present_ire = true && that.isSetIre();
      if (this_present_ire || that_present_ire) {
        if (!(this_present_ire && that_present_ire))
          return false;
        if (!this.ire.equals(that.ire))
          return false;
      }

      boolean this_present_ue = true && this.isSetUe();
      boolean that_present_ue = true && that.isSetUe();
      if (this_present_ue || that_present_ue) {
        if (!(this_present_ue && that_present_ue))
          return false;
        if (!this.ue.equals(that.ue))
          return false;
      }

      return true;
    }

    @Override
    public int hashCode() {
      return 0;
    }

    public void read(TProtocol iprot) throws TException {
      TField field;
      iprot.readStructBegin();
      while (true)
      {
        field = iprot.readFieldBegin();
        if (field.type == TType.STOP) { 
          break;
        }
        switch (field.id)
        {
          case IRE:
            if (field.type == TType.STRUCT) {
              this.ire = new InvalidRequestException();
              this.ire.read(iprot);
            } else { 
              TProtocolUtil.skip(iprot, field.type);
            }
            break;
          case UE:
            if (field.type == TType.STRUCT) {
              this.ue = new UnavailableException();
              this.ue.read(iprot);
            } else { 
              TProtocolUtil.skip(iprot, field.type);
            }
            break;
          default:
            TProtocolUtil.skip(iprot, field.type);
            break;
        }
        iprot.readFieldEnd();
      }
      iprot.readStructEnd();


      // check for required fields of primitive type, which can't be checked in the validate method
      validate();
    }

    public void write(TProtocol oprot) throws TException {
      oprot.writeStructBegin(STRUCT_DESC);

      if (this.isSetIre()) {
        oprot.writeFieldBegin(IRE_FIELD_DESC);
        this.ire.write(oprot);
        oprot.writeFieldEnd();
      } else if (this.isSetUe()) {
        oprot.writeFieldBegin(UE_FIELD_DESC);
        this.ue.write(oprot);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

    @Override
    public String toString() {
      StringBuilder sb = new StringBuilder("insert_result(");
      boolean first = true;

      sb.append("ire:");
      if (this.ire == null) {
        sb.append("null");
      } else {
        sb.append(this.ire);
      }
      first = false;
      if (!first) sb.append(", ");
      sb.append("ue:");
      if (this.ue == null) {
        sb.append("null");
      } else {
        sb.append(this.ue);
      }
      first = false;
      sb.append(")");
      return sb.toString();
    }

    public void validate() throws TException {
      // check for required fields
      // check that fields of type enum have valid values
    }

  }

  public static class batch_insert_args implements TBase, java.io.Serializable, Cloneable   {
    private static final TStruct STRUCT_DESC = new TStruct("batch_insert_args");
    private static final TField BATCH_MUTATION_FIELD_DESC = new TField("batchMutation", TType.STRUCT, (short)1);
    private static final TField BLOCK_FIELD_DESC = new TField("block", TType.BOOL, (short)2);

    public batch_mutation_t batchMutation;
    public static final int BATCHMUTATION = 1;
    public boolean block;
    public static final int BLOCK = 2;

    private final Isset __isset = new Isset();
    private static final class Isset implements java.io.Serializable {
      public boolean block = false;
    }

    public static final Map<Integer, FieldMetaData> metaDataMap = Collections.unmodifiableMap(new HashMap<Integer, FieldMetaData>() {{
      put(BATCHMUTATION, new FieldMetaData("batchMutation", TFieldRequirementType.DEFAULT, 
          new StructMetaData(TType.STRUCT, batch_mutation_t.class)));
      put(BLOCK, new FieldMetaData("block", TFieldRequirementType.DEFAULT, 
          new FieldValueMetaData(TType.BOOL)));
    }});

    static {
      FieldMetaData.addStructMetaDataMap(batch_insert_args.class, metaDataMap);
    }

    public batch_insert_args() {
      this.block = false;

    }

    public batch_insert_args(
      batch_mutation_t batchMutation,
      boolean block)
    {
      this();
      this.batchMutation = batchMutation;
      this.block = block;
      this.__isset.block = true;
    }

    /**
     * Performs a deep copy on <i>other</i>.
     */
    public batch_insert_args(batch_insert_args other) {
      if (other.isSetBatchMutation()) {
        this.batchMutation = new batch_mutation_t(other.batchMutation);
      }
      __isset.block = other.__isset.block;
      this.block = other.block;
    }

    @Override
    public batch_insert_args clone() {
      return new batch_insert_args(this);
    }

    public batch_mutation_t getBatchMutation() {
      return this.batchMutation;
    }

    public void setBatchMutation(batch_mutation_t batchMutation) {
      this.batchMutation = batchMutation;
    }

    public void unsetBatchMutation() {
      this.batchMutation = null;
    }

    // Returns true if field batchMutation is set (has been asigned a value) and false otherwise
    public boolean isSetBatchMutation() {
      return this.batchMutation != null;
    }

    public void setBatchMutationIsSet(boolean value) {
      if (!value) {
        this.batchMutation = null;
      }
    }

    public boolean isBlock() {
      return this.block;
    }

    public void setBlock(boolean block) {
      this.block = block;
      this.__isset.block = true;
    }

    public void unsetBlock() {
      this.__isset.block = false;
    }

    // Returns true if field block is set (has been asigned a value) and false otherwise
    public boolean isSetBlock() {
      return this.__isset.block;
    }

    public void setBlockIsSet(boolean value) {
      this.__isset.block = value;
    }

    public void setFieldValue(int fieldID, Object value) {
      switch (fieldID) {
      case BATCHMUTATION:
        if (value == null) {
          unsetBatchMutation();
        } else {
          setBatchMutation((batch_mutation_t)value);
        }
        break;

      case BLOCK:
        if (value == null) {
          unsetBlock();
        } else {
          setBlock((Boolean)value);
        }
        break;

      default:
        throw new IllegalArgumentException("Field " + fieldID + " doesn't exist!");
      }
    }

    public Object getFieldValue(int fieldID) {
      switch (fieldID) {
      case BATCHMUTATION:
        return getBatchMutation();

      case BLOCK:
        return new Boolean(isBlock());

      default:
        throw new IllegalArgumentException("Field " + fieldID + " doesn't exist!");
      }
    }

    // Returns true if field corresponding to fieldID is set (has been asigned a value) and false otherwise
    public boolean isSet(int fieldID) {
      switch (fieldID) {
      case BATCHMUTATION:
        return isSetBatchMutation();
      case BLOCK:
        return isSetBlock();
      default:
        throw new IllegalArgumentException("Field " + fieldID + " doesn't exist!");
      }
    }

    @Override
    public boolean equals(Object that) {
      if (that == null)
        return false;
      if (that instanceof batch_insert_args)
        return this.equals((batch_insert_args)that);
      return false;
    }

    public boolean equals(batch_insert_args that) {
      if (that == null)
        return false;

      boolean this_present_batchMutation = true && this.isSetBatchMutation();
      boolean that_present_batchMutation = true && that.isSetBatchMutation();
      if (this_present_batchMutation || that_present_batchMutation) {
        if (!(this_present_batchMutation && that_present_batchMutation))
          return false;
        if (!this.batchMutation.equals(that.batchMutation))
          return false;
      }

      boolean this_present_block = true;
      boolean that_present_block = true;
      if (this_present_block || that_present_block) {
        if (!(this_present_block && that_present_block))
          return false;
        if (this.block != that.block)
          return false;
      }

      return true;
    }

    @Override
    public int hashCode() {
      return 0;
    }

    public void read(TProtocol iprot) throws TException {
      TField field;
      iprot.readStructBegin();
      while (true)
      {
        field = iprot.readFieldBegin();
        if (field.type == TType.STOP) { 
          break;
        }
        switch (field.id)
        {
          case BATCHMUTATION:
            if (field.type == TType.STRUCT) {
              this.batchMutation = new batch_mutation_t();
              this.batchMutation.read(iprot);
            } else { 
              TProtocolUtil.skip(iprot, field.type);
            }
            break;
          case BLOCK:
            if (field.type == TType.BOOL) {
              this.block = iprot.readBool();
              this.__isset.block = true;
            } else { 
              TProtocolUtil.skip(iprot, field.type);
            }
            break;
          default:
            TProtocolUtil.skip(iprot, field.type);
            break;
        }
        iprot.readFieldEnd();
      }
      iprot.readStructEnd();


      // check for required fields of primitive type, which can't be checked in the validate method
      validate();
    }

    public void write(TProtocol oprot) throws TException {
      validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (this.batchMutation != null) {
        oprot.writeFieldBegin(BATCH_MUTATION_FIELD_DESC);
        this.batchMutation.write(oprot);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldBegin(BLOCK_FIELD_DESC);
      oprot.writeBool(this.block);
      oprot.writeFieldEnd();
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

    @Override
    public String toString() {
      StringBuilder sb = new StringBuilder("batch_insert_args(");
      boolean first = true;

      sb.append("batchMutation:");
      if (this.batchMutation == null) {
        sb.append("null");
      } else {
        sb.append(this.batchMutation);
      }
      first = false;
      if (!first) sb.append(", ");
      sb.append("block:");
      sb.append(this.block);
      first = false;
      sb.append(")");
      return sb.toString();
    }

    public void validate() throws TException {
      // check for required fields
      // check that fields of type enum have valid values
    }

  }

  public static class batch_insert_result implements TBase, java.io.Serializable, Cloneable   {
    private static final TStruct STRUCT_DESC = new TStruct("batch_insert_result");
    private static final TField IRE_FIELD_DESC = new TField("ire", TType.STRUCT, (short)1);
    private static final TField UE_FIELD_DESC = new TField("ue", TType.STRUCT, (short)2);

    public InvalidRequestException ire;
    public static final int IRE = 1;
    public UnavailableException ue;
    public static final int UE = 2;

    private final Isset __isset = new Isset();
    private static final class Isset implements java.io.Serializable {
    }

    public static final Map<Integer, FieldMetaData> metaDataMap = Collections.unmodifiableMap(new HashMap<Integer, FieldMetaData>() {{
      put(IRE, new FieldMetaData("ire", TFieldRequirementType.DEFAULT, 
          new FieldValueMetaData(TType.STRUCT)));
      put(UE, new FieldMetaData("ue", TFieldRequirementType.DEFAULT, 
          new FieldValueMetaData(TType.STRUCT)));
    }});

    static {
      FieldMetaData.addStructMetaDataMap(batch_insert_result.class, metaDataMap);
    }

    public batch_insert_result() {
    }

    public batch_insert_result(
      InvalidRequestException ire,
      UnavailableException ue)
    {
      this();
      this.ire = ire;
      this.ue = ue;
    }

    /**
     * Performs a deep copy on <i>other</i>.
     */
    public batch_insert_result(batch_insert_result other) {
      if (other.isSetIre()) {
        this.ire = new InvalidRequestException(other.ire);
      }
      if (other.isSetUe()) {
        this.ue = new UnavailableException(other.ue);
      }
    }

    @Override
    public batch_insert_result clone() {
      return new batch_insert_result(this);
    }

    public InvalidRequestException getIre() {
      return this.ire;
    }

    public void setIre(InvalidRequestException ire) {
      this.ire = ire;
    }

    public void unsetIre() {
      this.ire = null;
    }

    // Returns true if field ire is set (has been asigned a value) and false otherwise
    public boolean isSetIre() {
      return this.ire != null;
    }

    public void setIreIsSet(boolean value) {
      if (!value) {
        this.ire = null;
      }
    }

    public UnavailableException getUe() {
      return this.ue;
    }

    public void setUe(UnavailableException ue) {
      this.ue = ue;
    }

    public void unsetUe() {
      this.ue = null;
    }

    // Returns true if field ue is set (has been asigned a value) and false otherwise
    public boolean isSetUe() {
      return this.ue != null;
    }

    public void setUeIsSet(boolean value) {
      if (!value) {
        this.ue = null;
      }
    }

    public void setFieldValue(int fieldID, Object value) {
      switch (fieldID) {
      case IRE:
        if (value == null) {
          unsetIre();
        } else {
          setIre((InvalidRequestException)value);
        }
        break;

      case UE:
        if (value == null) {
          unsetUe();
        } else {
          setUe((UnavailableException)value);
        }
        break;

      default:
        throw new IllegalArgumentException("Field " + fieldID + " doesn't exist!");
      }
    }

    public Object getFieldValue(int fieldID) {
      switch (fieldID) {
      case IRE:
        return getIre();

      case UE:
        return getUe();

      default:
        throw new IllegalArgumentException("Field " + fieldID + " doesn't exist!");
      }
    }

    // Returns true if field corresponding to fieldID is set (has been asigned a value) and false otherwise
    public boolean isSet(int fieldID) {
      switch (fieldID) {
      case IRE:
        return isSetIre();
      case UE:
        return isSetUe();
      default:
        throw new IllegalArgumentException("Field " + fieldID + " doesn't exist!");
      }
    }

    @Override
    public boolean equals(Object that) {
      if (that == null)
        return false;
      if (that instanceof batch_insert_result)
        return this.equals((batch_insert_result)that);
      return false;
    }

    public boolean equals(batch_insert_result that) {
      if (that == null)
        return false;

      boolean this_present_ire = true && this.isSetIre();
      boolean that_present_ire = true && that.isSetIre();
      if (this_present_ire || that_present_ire) {
        if (!(this_present_ire && that_present_ire))
          return false;
        if (!this.ire.equals(that.ire))
          return false;
      }

      boolean this_present_ue = true && this.isSetUe();
      boolean that_present_ue = true && that.isSetUe();
      if (this_present_ue || that_present_ue) {
        if (!(this_present_ue && that_present_ue))
          return false;
        if (!this.ue.equals(that.ue))
          return false;
      }

      return true;
    }

    @Override
    public int hashCode() {
      return 0;
    }

    public void read(TProtocol iprot) throws TException {
      TField field;
      iprot.readStructBegin();
      while (true)
      {
        field = iprot.readFieldBegin();
        if (field.type == TType.STOP) { 
          break;
        }
        switch (field.id)
        {
          case IRE:
            if (field.type == TType.STRUCT) {
              this.ire = new InvalidRequestException();
              this.ire.read(iprot);
            } else { 
              TProtocolUtil.skip(iprot, field.type);
            }
            break;
          case UE:
            if (field.type == TType.STRUCT) {
              this.ue = new UnavailableException();
              this.ue.read(iprot);
            } else { 
              TProtocolUtil.skip(iprot, field.type);
            }
            break;
          default:
            TProtocolUtil.skip(iprot, field.type);
            break;
        }
        iprot.readFieldEnd();
      }
      iprot.readStructEnd();


      // check for required fields of primitive type, which can't be checked in the validate method
      validate();
    }

    public void write(TProtocol oprot) throws TException {
      oprot.writeStructBegin(STRUCT_DESC);

      if (this.isSetIre()) {
        oprot.writeFieldBegin(IRE_FIELD_DESC);
        this.ire.write(oprot);
        oprot.writeFieldEnd();
      } else if (this.isSetUe()) {
        oprot.writeFieldBegin(UE_FIELD_DESC);
        this.ue.write(oprot);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

    @Override
    public String toString() {
      StringBuilder sb = new StringBuilder("batch_insert_result(");
      boolean first = true;

      sb.append("ire:");
      if (this.ire == null) {
        sb.append("null");
      } else {
        sb.append(this.ire);
      }
      first = false;
      if (!first) sb.append(", ");
      sb.append("ue:");
      if (this.ue == null) {
        sb.append("null");
      } else {
        sb.append(this.ue);
      }
      first = false;
      sb.append(")");
      return sb.toString();
    }

    public void validate() throws TException {
      // check for required fields
      // check that fields of type enum have valid values
    }

  }

  public static class remove_args implements TBase, java.io.Serializable, Cloneable   {
    private static final TStruct STRUCT_DESC = new TStruct("remove_args");
    private static final TField TABLENAME_FIELD_DESC = new TField("tablename", TType.STRING, (short)1);
    private static final TField KEY_FIELD_DESC = new TField("key", TType.STRING, (short)2);
    private static final TField COLUMN_PATH_OR_PARENT_FIELD_DESC = new TField("columnPathOrParent", TType.STRING, (short)3);
    private static final TField TIMESTAMP_FIELD_DESC = new TField("timestamp", TType.I64, (short)4);
    private static final TField BLOCK_FIELD_DESC = new TField("block", TType.BOOL, (short)5);

    public String tablename;
    public static final int TABLENAME = 1;
    public String key;
    public static final int KEY = 2;
    public String columnPathOrParent;
    public static final int COLUMNPATHORPARENT = 3;
    public long timestamp;
    public static final int TIMESTAMP = 4;
    public boolean block;
    public static final int BLOCK = 5;

    private final Isset __isset = new Isset();
    private static final class Isset implements java.io.Serializable {
      public boolean timestamp = false;
      public boolean block = false;
    }

    public static final Map<Integer, FieldMetaData> metaDataMap = Collections.unmodifiableMap(new HashMap<Integer, FieldMetaData>() {{
      put(TABLENAME, new FieldMetaData("tablename", TFieldRequirementType.DEFAULT, 
          new FieldValueMetaData(TType.STRING)));
      put(KEY, new FieldMetaData("key", TFieldRequirementType.DEFAULT, 
          new FieldValueMetaData(TType.STRING)));
      put(COLUMNPATHORPARENT, new FieldMetaData("columnPathOrParent", TFieldRequirementType.DEFAULT, 
          new FieldValueMetaData(TType.STRING)));
      put(TIMESTAMP, new FieldMetaData("timestamp", TFieldRequirementType.DEFAULT, 
          new FieldValueMetaData(TType.I64)));
      put(BLOCK, new FieldMetaData("block", TFieldRequirementType.DEFAULT, 
          new FieldValueMetaData(TType.BOOL)));
    }});

    static {
      FieldMetaData.addStructMetaDataMap(remove_args.class, metaDataMap);
    }

    public remove_args() {
      this.block = false;

    }

    public remove_args(
      String tablename,
      String key,
      String columnPathOrParent,
      long timestamp,
      boolean block)
    {
      this();
      this.tablename = tablename;
      this.key = key;
      this.columnPathOrParent = columnPathOrParent;
      this.timestamp = timestamp;
      this.__isset.timestamp = true;
      this.block = block;
      this.__isset.block = true;
    }

    /**
     * Performs a deep copy on <i>other</i>.
     */
    public remove_args(remove_args other) {
      if (other.isSetTablename()) {
        this.tablename = other.tablename;
      }
      if (other.isSetKey()) {
        this.key = other.key;
      }
      if (other.isSetColumnPathOrParent()) {
        this.columnPathOrParent = other.columnPathOrParent;
      }
      __isset.timestamp = other.__isset.timestamp;
      this.timestamp = other.timestamp;
      __isset.block = other.__isset.block;
      this.block = other.block;
    }

    @Override
    public remove_args clone() {
      return new remove_args(this);
    }

    public String getTablename() {
      return this.tablename;
    }

    public void setTablename(String tablename) {
      this.tablename = tablename;
    }

    public void unsetTablename() {
      this.tablename = null;
    }

    // Returns true if field tablename is set (has been asigned a value) and false otherwise
    public boolean isSetTablename() {
      return this.tablename != null;
    }

    public void setTablenameIsSet(boolean value) {
      if (!value) {
        this.tablename = null;
      }
    }

    public String getKey() {
      return this.key;
    }

    public void setKey(String key) {
      this.key = key;
    }

    public void unsetKey() {
      this.key = null;
    }

    // Returns true if field key is set (has been asigned a value) and false otherwise
    public boolean isSetKey() {
      return this.key != null;
    }

    public void setKeyIsSet(boolean value) {
      if (!value) {
        this.key = null;
      }
    }

    public String getColumnPathOrParent() {
      return this.columnPathOrParent;
    }

    public void setColumnPathOrParent(String columnPathOrParent) {
      this.columnPathOrParent = columnPathOrParent;
    }

    public void unsetColumnPathOrParent() {
      this.columnPathOrParent = null;
    }

    // Returns true if field columnPathOrParent is set (has been asigned a value) and false otherwise
    public boolean isSetColumnPathOrParent() {
      return this.columnPathOrParent != null;
    }

    public void setColumnPathOrParentIsSet(boolean value) {
      if (!value) {
        this.columnPathOrParent = null;
      }
    }

    public long getTimestamp() {
      return this.timestamp;
    }

    public void setTimestamp(long timestamp) {
      this.timestamp = timestamp;
      this.__isset.timestamp = true;
    }

    public void unsetTimestamp() {
      this.__isset.timestamp = false;
    }

    // Returns true if field timestamp is set (has been asigned a value) and false otherwise
    public boolean isSetTimestamp() {
      return this.__isset.timestamp;
    }

    public void setTimestampIsSet(boolean value) {
      this.__isset.timestamp = value;
    }

    public boolean isBlock() {
      return this.block;
    }

    public void setBlock(boolean block) {
      this.block = block;
      this.__isset.block = true;
    }

    public void unsetBlock() {
      this.__isset.block = false;
    }

    // Returns true if field block is set (has been asigned a value) and false otherwise
    public boolean isSetBlock() {
      return this.__isset.block;
    }

    public void setBlockIsSet(boolean value) {
      this.__isset.block = value;
    }

    public void setFieldValue(int fieldID, Object value) {
      switch (fieldID) {
      case TABLENAME:
        if (value == null) {
          unsetTablename();
        } else {
          setTablename((String)value);
        }
        break;

      case KEY:
        if (value == null) {
          unsetKey();
        } else {
          setKey((String)value);
        }
        break;

      case COLUMNPATHORPARENT:
        if (value == null) {
          unsetColumnPathOrParent();
        } else {
          setColumnPathOrParent((String)value);
        }
        break;

      case TIMESTAMP:
        if (value == null) {
          unsetTimestamp();
        } else {
          setTimestamp((Long)value);
        }
        break;

      case BLOCK:
        if (value == null) {
          unsetBlock();
        } else {
          setBlock((Boolean)value);
        }
        break;

      default:
        throw new IllegalArgumentException("Field " + fieldID + " doesn't exist!");
      }
    }

    public Object getFieldValue(int fieldID) {
      switch (fieldID) {
      case TABLENAME:
        return getTablename();

      case KEY:
        return getKey();

      case COLUMNPATHORPARENT:
        return getColumnPathOrParent();

      case TIMESTAMP:
        return new Long(getTimestamp());

      case BLOCK:
        return new Boolean(isBlock());

      default:
        throw new IllegalArgumentException("Field " + fieldID + " doesn't exist!");
      }
    }

    // Returns true if field corresponding to fieldID is set (has been asigned a value) and false otherwise
    public boolean isSet(int fieldID) {
      switch (fieldID) {
      case TABLENAME:
        return isSetTablename();
      case KEY:
        return isSetKey();
      case COLUMNPATHORPARENT:
        return isSetColumnPathOrParent();
      case TIMESTAMP:
        return isSetTimestamp();
      case BLOCK:
        return isSetBlock();
      default:
        throw new IllegalArgumentException("Field " + fieldID + " doesn't exist!");
      }
    }

    @Override
    public boolean equals(Object that) {
      if (that == null)
        return false;
      if (that instanceof remove_args)
        return this.equals((remove_args)that);
      return false;
    }

    public boolean equals(remove_args that) {
      if (that == null)
        return false;

      boolean this_present_tablename = true && this.isSetTablename();
      boolean that_present_tablename = true && that.isSetTablename();
      if (this_present_tablename || that_present_tablename) {
        if (!(this_present_tablename && that_present_tablename))
          return false;
        if (!this.tablename.equals(that.tablename))
          return false;
      }

      boolean this_present_key = true && this.isSetKey();
      boolean that_present_key = true && that.isSetKey();
      if (this_present_key || that_present_key) {
        if (!(this_present_key && that_present_key))
          return false;
        if (!this.key.equals(that.key))
          return false;
      }

      boolean this_present_columnPathOrParent = true && this.isSetColumnPathOrParent();
      boolean that_present_columnPathOrParent = true && that.isSetColumnPathOrParent();
      if (this_present_columnPathOrParent || that_present_columnPathOrParent) {
        if (!(this_present_columnPathOrParent && that_present_columnPathOrParent))
          return false;
        if (!this.columnPathOrParent.equals(that.columnPathOrParent))
          return false;
      }

      boolean this_present_timestamp = true;
      boolean that_present_timestamp = true;
      if (this_present_timestamp || that_present_timestamp) {
        if (!(this_present_timestamp && that_present_timestamp))
          return false;
        if (this.timestamp != that.timestamp)
          return false;
      }

      boolean this_present_block = true;
      boolean that_present_block = true;
      if (this_present_block || that_present_block) {
        if (!(this_present_block && that_present_block))
          return false;
        if (this.block != that.block)
          return false;
      }

      return true;
    }

    @Override
    public int hashCode() {
      return 0;
    }

    public void read(TProtocol iprot) throws TException {
      TField field;
      iprot.readStructBegin();
      while (true)
      {
        field = iprot.readFieldBegin();
        if (field.type == TType.STOP) { 
          break;
        }
        switch (field.id)
        {
          case TABLENAME:
            if (field.type == TType.STRING) {
              this.tablename = iprot.readString();
            } else { 
              TProtocolUtil.skip(iprot, field.type);
            }
            break;
          case KEY:
            if (field.type == TType.STRING) {
              this.key = iprot.readString();
            } else { 
              TProtocolUtil.skip(iprot, field.type);
            }
            break;
          case COLUMNPATHORPARENT:
            if (field.type == TType.STRING) {
              this.columnPathOrParent = iprot.readString();
            } else { 
              TProtocolUtil.skip(iprot, field.type);
            }
            break;
          case TIMESTAMP:
            if (field.type == TType.I64) {
              this.timestamp = iprot.readI64();
              this.__isset.timestamp = true;
            } else { 
              TProtocolUtil.skip(iprot, field.type);
            }
            break;
          case BLOCK:
            if (field.type == TType.BOOL) {
              this.block = iprot.readBool();
              this.__isset.block = true;
            } else { 
              TProtocolUtil.skip(iprot, field.type);
            }
            break;
          default:
            TProtocolUtil.skip(iprot, field.type);
            break;
        }
        iprot.readFieldEnd();
      }
      iprot.readStructEnd();


      // check for required fields of primitive type, which can't be checked in the validate method
      validate();
    }

    public void write(TProtocol oprot) throws TException {
      validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (this.tablename != null) {
        oprot.writeFieldBegin(TABLENAME_FIELD_DESC);
        oprot.writeString(this.tablename);
        oprot.writeFieldEnd();
      }
      if (this.key != null) {
        oprot.writeFieldBegin(KEY_FIELD_DESC);
        oprot.writeString(this.key);
        oprot.writeFieldEnd();
      }
      if (this.columnPathOrParent != null) {
        oprot.writeFieldBegin(COLUMN_PATH_OR_PARENT_FIELD_DESC);
        oprot.writeString(this.columnPathOrParent);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldBegin(TIMESTAMP_FIELD_DESC);
      oprot.writeI64(this.timestamp);
      oprot.writeFieldEnd();
      oprot.writeFieldBegin(BLOCK_FIELD_DESC);
      oprot.writeBool(this.block);
      oprot.writeFieldEnd();
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

    @Override
    public String toString() {
      StringBuilder sb = new StringBuilder("remove_args(");
      boolean first = true;

      sb.append("tablename:");
      if (this.tablename == null) {
        sb.append("null");
      } else {
        sb.append(this.tablename);
      }
      first = false;
      if (!first) sb.append(", ");
      sb.append("key:");
      if (this.key == null) {
        sb.append("null");
      } else {
        sb.append(this.key);
      }
      first = false;
      if (!first) sb.append(", ");
      sb.append("columnPathOrParent:");
      if (this.columnPathOrParent == null) {
        sb.append("null");
      } else {
        sb.append(this.columnPathOrParent);
      }
      first = false;
      if (!first) sb.append(", ");
      sb.append("timestamp:");
      sb.append(this.timestamp);
      first = false;
      if (!first) sb.append(", ");
      sb.append("block:");
      sb.append(this.block);
      first = false;
      sb.append(")");
      return sb.toString();
    }

    public void validate() throws TException {
      // check for required fields
      // check that fields of type enum have valid values
    }

  }

  public static class remove_result implements TBase, java.io.Serializable, Cloneable   {
    private static final TStruct STRUCT_DESC = new TStruct("remove_result");
    private static final TField IRE_FIELD_DESC = new TField("ire", TType.STRUCT, (short)1);
    private static final TField UE_FIELD_DESC = new TField("ue", TType.STRUCT, (short)2);

    public InvalidRequestException ire;
    public static final int IRE = 1;
    public UnavailableException ue;
    public static final int UE = 2;

    private final Isset __isset = new Isset();
    private static final class Isset implements java.io.Serializable {
    }

    public static final Map<Integer, FieldMetaData> metaDataMap = Collections.unmodifiableMap(new HashMap<Integer, FieldMetaData>() {{
      put(IRE, new FieldMetaData("ire", TFieldRequirementType.DEFAULT, 
          new FieldValueMetaData(TType.STRUCT)));
      put(UE, new FieldMetaData("ue", TFieldRequirementType.DEFAULT, 
          new FieldValueMetaData(TType.STRUCT)));
    }});

    static {
      FieldMetaData.addStructMetaDataMap(remove_result.class, metaDataMap);
    }

    public remove_result() {
    }

    public remove_result(
      InvalidRequestException ire,
      UnavailableException ue)
    {
      this();
      this.ire = ire;
      this.ue = ue;
    }

    /**
     * Performs a deep copy on <i>other</i>.
     */
    public remove_result(remove_result other) {
      if (other.isSetIre()) {
        this.ire = new InvalidRequestException(other.ire);
      }
      if (other.isSetUe()) {
        this.ue = new UnavailableException(other.ue);
      }
    }

    @Override
    public remove_result clone() {
      return new remove_result(this);
    }

    public InvalidRequestException getIre() {
      return this.ire;
    }

    public void setIre(InvalidRequestException ire) {
      this.ire = ire;
    }

    public void unsetIre() {
      this.ire = null;
    }

    // Returns true if field ire is set (has been asigned a value) and false otherwise
    public boolean isSetIre() {
      return this.ire != null;
    }

    public void setIreIsSet(boolean value) {
      if (!value) {
        this.ire = null;
      }
    }

    public UnavailableException getUe() {
      return this.ue;
    }

    public void setUe(UnavailableException ue) {
      this.ue = ue;
    }

    public void unsetUe() {
      this.ue = null;
    }

    // Returns true if field ue is set (has been asigned a value) and false otherwise
    public boolean isSetUe() {
      return this.ue != null;
    }

    public void setUeIsSet(boolean value) {
      if (!value) {
        this.ue = null;
      }
    }

    public void setFieldValue(int fieldID, Object value) {
      switch (fieldID) {
      case IRE:
        if (value == null) {
          unsetIre();
        } else {
          setIre((InvalidRequestException)value);
        }
        break;

      case UE:
        if (value == null) {
          unsetUe();
        } else {
          setUe((UnavailableException)value);
        }
        break;

      default:
        throw new IllegalArgumentException("Field " + fieldID + " doesn't exist!");
      }
    }

    public Object getFieldValue(int fieldID) {
      switch (fieldID) {
      case IRE:
        return getIre();

      case UE:
        return getUe();

      default:
        throw new IllegalArgumentException("Field " + fieldID + " doesn't exist!");
      }
    }

    // Returns true if field corresponding to fieldID is set (has been asigned a value) and false otherwise
    public boolean isSet(int fieldID) {
      switch (fieldID) {
      case IRE:
        return isSetIre();
      case UE:
        return isSetUe();
      default:
        throw new IllegalArgumentException("Field " + fieldID + " doesn't exist!");
      }
    }

    @Override
    public boolean equals(Object that) {
      if (that == null)
        return false;
      if (that instanceof remove_result)
        return this.equals((remove_result)that);
      return false;
    }

    public boolean equals(remove_result that) {
      if (that == null)
        return false;

      boolean this_present_ire = true && this.isSetIre();
      boolean that_present_ire = true && that.isSetIre();
      if (this_present_ire || that_present_ire) {
        if (!(this_present_ire && that_present_ire))
          return false;
        if (!this.ire.equals(that.ire))
          return false;
      }

      boolean this_present_ue = true && this.isSetUe();
      boolean that_present_ue = true && that.isSetUe();
      if (this_present_ue || that_present_ue) {
        if (!(this_present_ue && that_present_ue))
          return false;
        if (!this.ue.equals(that.ue))
          return false;
      }

      return true;
    }

    @Override
    public int hashCode() {
      return 0;
    }

    public void read(TProtocol iprot) throws TException {
      TField field;
      iprot.readStructBegin();
      while (true)
      {
        field = iprot.readFieldBegin();
        if (field.type == TType.STOP) { 
          break;
        }
        switch (field.id)
        {
          case IRE:
            if (field.type == TType.STRUCT) {
              this.ire = new InvalidRequestException();
              this.ire.read(iprot);
            } else { 
              TProtocolUtil.skip(iprot, field.type);
            }
            break;
          case UE:
            if (field.type == TType.STRUCT) {
              this.ue = new UnavailableException();
              this.ue.read(iprot);
            } else { 
              TProtocolUtil.skip(iprot, field.type);
            }
            break;
          default:
            TProtocolUtil.skip(iprot, field.type);
            break;
        }
        iprot.readFieldEnd();
      }
      iprot.readStructEnd();


      // check for required fields of primitive type, which can't be checked in the validate method
      validate();
    }

    public void write(TProtocol oprot) throws TException {
      oprot.writeStructBegin(STRUCT_DESC);

      if (this.isSetIre()) {
        oprot.writeFieldBegin(IRE_FIELD_DESC);
        this.ire.write(oprot);
        oprot.writeFieldEnd();
      } else if (this.isSetUe()) {
        oprot.writeFieldBegin(UE_FIELD_DESC);
        this.ue.write(oprot);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

    @Override
    public String toString() {
      StringBuilder sb = new StringBuilder("remove_result(");
      boolean first = true;

      sb.append("ire:");
      if (this.ire == null) {
        sb.append("null");
      } else {
        sb.append(this.ire);
      }
      first = false;
      if (!first) sb.append(", ");
      sb.append("ue:");
      if (this.ue == null) {
        sb.append("null");
      } else {
        sb.append(this.ue);
      }
      first = false;
      sb.append(")");
      return sb.toString();
    }

    public void validate() throws TException {
      // check for required fields
      // check that fields of type enum have valid values
    }

  }

  public static class get_columns_since_args implements TBase, java.io.Serializable, Cloneable   {
    private static final TStruct STRUCT_DESC = new TStruct("get_columns_since_args");
    private static final TField TABLENAME_FIELD_DESC = new TField("tablename", TType.STRING, (short)1);
    private static final TField KEY_FIELD_DESC = new TField("key", TType.STRING, (short)2);
    private static final TField COLUMN_PARENT_FIELD_DESC = new TField("columnParent", TType.STRING, (short)3);
    private static final TField TIME_STAMP_FIELD_DESC = new TField("timeStamp", TType.I64, (short)4);

    public String tablename;
    public static final int TABLENAME = 1;
    public String key;
    public static final int KEY = 2;
    public String columnParent;
    public static final int COLUMNPARENT = 3;
    public long timeStamp;
    public static final int TIMESTAMP = 4;

    private final Isset __isset = new Isset();
    private static final class Isset implements java.io.Serializable {
      public boolean timeStamp = false;
    }

    public static final Map<Integer, FieldMetaData> metaDataMap = Collections.unmodifiableMap(new HashMap<Integer, FieldMetaData>() {{
      put(TABLENAME, new FieldMetaData("tablename", TFieldRequirementType.DEFAULT, 
          new FieldValueMetaData(TType.STRING)));
      put(KEY, new FieldMetaData("key", TFieldRequirementType.DEFAULT, 
          new FieldValueMetaData(TType.STRING)));
      put(COLUMNPARENT, new FieldMetaData("columnParent", TFieldRequirementType.DEFAULT, 
          new FieldValueMetaData(TType.STRING)));
      put(TIMESTAMP, new FieldMetaData("timeStamp", TFieldRequirementType.DEFAULT, 
          new FieldValueMetaData(TType.I64)));
    }});

    static {
      FieldMetaData.addStructMetaDataMap(get_columns_since_args.class, metaDataMap);
    }

    public get_columns_since_args() {
    }

    public get_columns_since_args(
      String tablename,
      String key,
      String columnParent,
      long timeStamp)
    {
      this();
      this.tablename = tablename;
      this.key = key;
      this.columnParent = columnParent;
      this.timeStamp = timeStamp;
      this.__isset.timeStamp = true;
    }

    /**
     * Performs a deep copy on <i>other</i>.
     */
    public get_columns_since_args(get_columns_since_args other) {
      if (other.isSetTablename()) {
        this.tablename = other.tablename;
      }
      if (other.isSetKey()) {
        this.key = other.key;
      }
      if (other.isSetColumnParent()) {
        this.columnParent = other.columnParent;
      }
      __isset.timeStamp = other.__isset.timeStamp;
      this.timeStamp = other.timeStamp;
    }

    @Override
    public get_columns_since_args clone() {
      return new get_columns_since_args(this);
    }

    public String getTablename() {
      return this.tablename;
    }

    public void setTablename(String tablename) {
      this.tablename = tablename;
    }

    public void unsetTablename() {
      this.tablename = null;
    }

    // Returns true if field tablename is set (has been asigned a value) and false otherwise
    public boolean isSetTablename() {
      return this.tablename != null;
    }

    public void setTablenameIsSet(boolean value) {
      if (!value) {
        this.tablename = null;
      }
    }

    public String getKey() {
      return this.key;
    }

    public void setKey(String key) {
      this.key = key;
    }

    public void unsetKey() {
      this.key = null;
    }

    // Returns true if field key is set (has been asigned a value) and false otherwise
    public boolean isSetKey() {
      return this.key != null;
    }

    public void setKeyIsSet(boolean value) {
      if (!value) {
        this.key = null;
      }
    }

    public String getColumnParent() {
      return this.columnParent;
    }

    public void setColumnParent(String columnParent) {
      this.columnParent = columnParent;
    }

    public void unsetColumnParent() {
      this.columnParent = null;
    }

    // Returns true if field columnParent is set (has been asigned a value) and false otherwise
    public boolean isSetColumnParent() {
      return this.columnParent != null;
    }

    public void setColumnParentIsSet(boolean value) {
      if (!value) {
        this.columnParent = null;
      }
    }

    public long getTimeStamp() {
      return this.timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
      this.timeStamp = timeStamp;
      this.__isset.timeStamp = true;
    }

    public void unsetTimeStamp() {
      this.__isset.timeStamp = false;
    }

    // Returns true if field timeStamp is set (has been asigned a value) and false otherwise
    public boolean isSetTimeStamp() {
      return this.__isset.timeStamp;
    }

    public void setTimeStampIsSet(boolean value) {
      this.__isset.timeStamp = value;
    }

    public void setFieldValue(int fieldID, Object value) {
      switch (fieldID) {
      case TABLENAME:
        if (value == null) {
          unsetTablename();
        } else {
          setTablename((String)value);
        }
        break;

      case KEY:
        if (value == null) {
          unsetKey();
        } else {
          setKey((String)value);
        }
        break;

      case COLUMNPARENT:
        if (value == null) {
          unsetColumnParent();
        } else {
          setColumnParent((String)value);
        }
        break;

      case TIMESTAMP:
        if (value == null) {
          unsetTimeStamp();
        } else {
          setTimeStamp((Long)value);
        }
        break;

      default:
        throw new IllegalArgumentException("Field " + fieldID + " doesn't exist!");
      }
    }

    public Object getFieldValue(int fieldID) {
      switch (fieldID) {
      case TABLENAME:
        return getTablename();

      case KEY:
        return getKey();

      case COLUMNPARENT:
        return getColumnParent();

      case TIMESTAMP:
        return new Long(getTimeStamp());

      default:
        throw new IllegalArgumentException("Field " + fieldID + " doesn't exist!");
      }
    }

    // Returns true if field corresponding to fieldID is set (has been asigned a value) and false otherwise
    public boolean isSet(int fieldID) {
      switch (fieldID) {
      case TABLENAME:
        return isSetTablename();
      case KEY:
        return isSetKey();
      case COLUMNPARENT:
        return isSetColumnParent();
      case TIMESTAMP:
        return isSetTimeStamp();
      default:
        throw new IllegalArgumentException("Field " + fieldID + " doesn't exist!");
      }
    }

    @Override
    public boolean equals(Object that) {
      if (that == null)
        return false;
      if (that instanceof get_columns_since_args)
        return this.equals((get_columns_since_args)that);
      return false;
    }

    public boolean equals(get_columns_since_args that) {
      if (that == null)
        return false;

      boolean this_present_tablename = true && this.isSetTablename();
      boolean that_present_tablename = true && that.isSetTablename();
      if (this_present_tablename || that_present_tablename) {
        if (!(this_present_tablename && that_present_tablename))
          return false;
        if (!this.tablename.equals(that.tablename))
          return false;
      }

      boolean this_present_key = true && this.isSetKey();
      boolean that_present_key = true && that.isSetKey();
      if (this_present_key || that_present_key) {
        if (!(this_present_key && that_present_key))
          return false;
        if (!this.key.equals(that.key))
          return false;
      }

      boolean this_present_columnParent = true && this.isSetColumnParent();
      boolean that_present_columnParent = true && that.isSetColumnParent();
      if (this_present_columnParent || that_present_columnParent) {
        if (!(this_present_columnParent && that_present_columnParent))
          return false;
        if (!this.columnParent.equals(that.columnParent))
          return false;
      }

      boolean this_present_timeStamp = true;
      boolean that_present_timeStamp = true;
      if (this_present_timeStamp || that_present_timeStamp) {
        if (!(this_present_timeStamp && that_present_timeStamp))
          return false;
        if (this.timeStamp != that.timeStamp)
          return false;
      }

      return true;
    }

    @Override
    public int hashCode() {
      return 0;
    }

    public void read(TProtocol iprot) throws TException {
      TField field;
      iprot.readStructBegin();
      while (true)
      {
        field = iprot.readFieldBegin();
        if (field.type == TType.STOP) { 
          break;
        }
        switch (field.id)
        {
          case TABLENAME:
            if (field.type == TType.STRING) {
              this.tablename = iprot.readString();
            } else { 
              TProtocolUtil.skip(iprot, field.type);
            }
            break;
          case KEY:
            if (field.type == TType.STRING) {
              this.key = iprot.readString();
            } else { 
              TProtocolUtil.skip(iprot, field.type);
            }
            break;
          case COLUMNPARENT:
            if (field.type == TType.STRING) {
              this.columnParent = iprot.readString();
            } else { 
              TProtocolUtil.skip(iprot, field.type);
            }
            break;
          case TIMESTAMP:
            if (field.type == TType.I64) {
              this.timeStamp = iprot.readI64();
              this.__isset.timeStamp = true;
            } else { 
              TProtocolUtil.skip(iprot, field.type);
            }
            break;
          default:
            TProtocolUtil.skip(iprot, field.type);
            break;
        }
        iprot.readFieldEnd();
      }
      iprot.readStructEnd();


      // check for required fields of primitive type, which can't be checked in the validate method
      validate();
    }

    public void write(TProtocol oprot) throws TException {
      validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (this.tablename != null) {
        oprot.writeFieldBegin(TABLENAME_FIELD_DESC);
        oprot.writeString(this.tablename);
        oprot.writeFieldEnd();
      }
      if (this.key != null) {
        oprot.writeFieldBegin(KEY_FIELD_DESC);
        oprot.writeString(this.key);
        oprot.writeFieldEnd();
      }
      if (this.columnParent != null) {
        oprot.writeFieldBegin(COLUMN_PARENT_FIELD_DESC);
        oprot.writeString(this.columnParent);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldBegin(TIME_STAMP_FIELD_DESC);
      oprot.writeI64(this.timeStamp);
      oprot.writeFieldEnd();
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

    @Override
    public String toString() {
      StringBuilder sb = new StringBuilder("get_columns_since_args(");
      boolean first = true;

      sb.append("tablename:");
      if (this.tablename == null) {
        sb.append("null");
      } else {
        sb.append(this.tablename);
      }
      first = false;
      if (!first) sb.append(", ");
      sb.append("key:");
      if (this.key == null) {
        sb.append("null");
      } else {
        sb.append(this.key);
      }
      first = false;
      if (!first) sb.append(", ");
      sb.append("columnParent:");
      if (this.columnParent == null) {
        sb.append("null");
      } else {
        sb.append(this.columnParent);
      }
      first = false;
      if (!first) sb.append(", ");
      sb.append("timeStamp:");
      sb.append(this.timeStamp);
      first = false;
      sb.append(")");
      return sb.toString();
    }

    public void validate() throws TException {
      // check for required fields
      // check that fields of type enum have valid values
    }

  }

  public static class get_columns_since_result implements TBase, java.io.Serializable, Cloneable   {
    private static final TStruct STRUCT_DESC = new TStruct("get_columns_since_result");
    private static final TField SUCCESS_FIELD_DESC = new TField("success", TType.LIST, (short)0);
    private static final TField IRE_FIELD_DESC = new TField("ire", TType.STRUCT, (short)1);
    private static final TField NFE_FIELD_DESC = new TField("nfe", TType.STRUCT, (short)2);

    public List<column_t> success;
    public static final int SUCCESS = 0;
    public InvalidRequestException ire;
    public static final int IRE = 1;
    public NotFoundException nfe;
    public static final int NFE = 2;

    private final Isset __isset = new Isset();
    private static final class Isset implements java.io.Serializable {
    }

    public static final Map<Integer, FieldMetaData> metaDataMap = Collections.unmodifiableMap(new HashMap<Integer, FieldMetaData>() {{
      put(SUCCESS, new FieldMetaData("success", TFieldRequirementType.DEFAULT, 
          new ListMetaData(TType.LIST, 
              new StructMetaData(TType.STRUCT, column_t.class))));
      put(IRE, new FieldMetaData("ire", TFieldRequirementType.DEFAULT, 
          new FieldValueMetaData(TType.STRUCT)));
      put(NFE, new FieldMetaData("nfe", TFieldRequirementType.DEFAULT, 
          new FieldValueMetaData(TType.STRUCT)));
    }});

    static {
      FieldMetaData.addStructMetaDataMap(get_columns_since_result.class, metaDataMap);
    }

    public get_columns_since_result() {
    }

    public get_columns_since_result(
      List<column_t> success,
      InvalidRequestException ire,
      NotFoundException nfe)
    {
      this();
      this.success = success;
      this.ire = ire;
      this.nfe = nfe;
    }

    /**
     * Performs a deep copy on <i>other</i>.
     */
    public get_columns_since_result(get_columns_since_result other) {
      if (other.isSetSuccess()) {
        List<column_t> __this__success = new ArrayList<column_t>();
        for (column_t other_element : other.success) {
          __this__success.add(new column_t(other_element));
        }
        this.success = __this__success;
      }
      if (other.isSetIre()) {
        this.ire = new InvalidRequestException(other.ire);
      }
      if (other.isSetNfe()) {
        this.nfe = new NotFoundException(other.nfe);
      }
    }

    @Override
    public get_columns_since_result clone() {
      return new get_columns_since_result(this);
    }

    public int getSuccessSize() {
      return (this.success == null) ? 0 : this.success.size();
    }

    public java.util.Iterator<column_t> getSuccessIterator() {
      return (this.success == null) ? null : this.success.iterator();
    }

    public void addToSuccess(column_t elem) {
      if (this.success == null) {
        this.success = new ArrayList<column_t>();
      }
      this.success.add(elem);
    }

    public List<column_t> getSuccess() {
      return this.success;
    }

    public void setSuccess(List<column_t> success) {
      this.success = success;
    }

    public void unsetSuccess() {
      this.success = null;
    }

    // Returns true if field success is set (has been asigned a value) and false otherwise
    public boolean isSetSuccess() {
      return this.success != null;
    }

    public void setSuccessIsSet(boolean value) {
      if (!value) {
        this.success = null;
      }
    }

    public InvalidRequestException getIre() {
      return this.ire;
    }

    public void setIre(InvalidRequestException ire) {
      this.ire = ire;
    }

    public void unsetIre() {
      this.ire = null;
    }

    // Returns true if field ire is set (has been asigned a value) and false otherwise
    public boolean isSetIre() {
      return this.ire != null;
    }

    public void setIreIsSet(boolean value) {
      if (!value) {
        this.ire = null;
      }
    }

    public NotFoundException getNfe() {
      return this.nfe;
    }

    public void setNfe(NotFoundException nfe) {
      this.nfe = nfe;
    }

    public void unsetNfe() {
      this.nfe = null;
    }

    // Returns true if field nfe is set (has been asigned a value) and false otherwise
    public boolean isSetNfe() {
      return this.nfe != null;
    }

    public void setNfeIsSet(boolean value) {
      if (!value) {
        this.nfe = null;
      }
    }

    public void setFieldValue(int fieldID, Object value) {
      switch (fieldID) {
      case SUCCESS:
        if (value == null) {
          unsetSuccess();
        } else {
          setSuccess((List<column_t>)value);
        }
        break;

      case IRE:
        if (value == null) {
          unsetIre();
        } else {
          setIre((InvalidRequestException)value);
        }
        break;

      case NFE:
        if (value == null) {
          unsetNfe();
        } else {
          setNfe((NotFoundException)value);
        }
        break;

      default:
        throw new IllegalArgumentException("Field " + fieldID + " doesn't exist!");
      }
    }

    public Object getFieldValue(int fieldID) {
      switch (fieldID) {
      case SUCCESS:
        return getSuccess();

      case IRE:
        return getIre();

      case NFE:
        return getNfe();

      default:
        throw new IllegalArgumentException("Field " + fieldID + " doesn't exist!");
      }
    }

    // Returns true if field corresponding to fieldID is set (has been asigned a value) and false otherwise
    public boolean isSet(int fieldID) {
      switch (fieldID) {
      case SUCCESS:
        return isSetSuccess();
      case IRE:
        return isSetIre();
      case NFE:
        return isSetNfe();
      default:
        throw new IllegalArgumentException("Field " + fieldID + " doesn't exist!");
      }
    }

    @Override
    public boolean equals(Object that) {
      if (that == null)
        return false;
      if (that instanceof get_columns_since_result)
        return this.equals((get_columns_since_result)that);
      return false;
    }

    public boolean equals(get_columns_since_result that) {
      if (that == null)
        return false;

      boolean this_present_success = true && this.isSetSuccess();
      boolean that_present_success = true && that.isSetSuccess();
      if (this_present_success || that_present_success) {
        if (!(this_present_success && that_present_success))
          return false;
        if (!this.success.equals(that.success))
          return false;
      }

      boolean this_present_ire = true && this.isSetIre();
      boolean that_present_ire = true && that.isSetIre();
      if (this_present_ire || that_present_ire) {
        if (!(this_present_ire && that_present_ire))
          return false;
        if (!this.ire.equals(that.ire))
          return false;
      }

      boolean this_present_nfe = true && this.isSetNfe();
      boolean that_present_nfe = true && that.isSetNfe();
      if (this_present_nfe || that_present_nfe) {
        if (!(this_present_nfe && that_present_nfe))
          return false;
        if (!this.nfe.equals(that.nfe))
          return false;
      }

      return true;
    }

    @Override
    public int hashCode() {
      return 0;
    }

    public void read(TProtocol iprot) throws TException {
      TField field;
      iprot.readStructBegin();
      while (true)
      {
        field = iprot.readFieldBegin();
        if (field.type == TType.STOP) { 
          break;
        }
        switch (field.id)
        {
          case SUCCESS:
            if (field.type == TType.LIST) {
              {
                TList _list43 = iprot.readListBegin();
                this.success = new ArrayList<column_t>(_list43.size);
                for (int _i44 = 0; _i44 < _list43.size; ++_i44)
                {
                  column_t _elem45;
                  _elem45 = new column_t();
                  _elem45.read(iprot);
                  this.success.add(_elem45);
                }
                iprot.readListEnd();
              }
            } else { 
              TProtocolUtil.skip(iprot, field.type);
            }
            break;
          case IRE:
            if (field.type == TType.STRUCT) {
              this.ire = new InvalidRequestException();
              this.ire.read(iprot);
            } else { 
              TProtocolUtil.skip(iprot, field.type);
            }
            break;
          case NFE:
            if (field.type == TType.STRUCT) {
              this.nfe = new NotFoundException();
              this.nfe.read(iprot);
            } else { 
              TProtocolUtil.skip(iprot, field.type);
            }
            break;
          default:
            TProtocolUtil.skip(iprot, field.type);
            break;
        }
        iprot.readFieldEnd();
      }
      iprot.readStructEnd();


      // check for required fields of primitive type, which can't be checked in the validate method
      validate();
    }

    public void write(TProtocol oprot) throws TException {
      oprot.writeStructBegin(STRUCT_DESC);

      if (this.isSetSuccess()) {
        oprot.writeFieldBegin(SUCCESS_FIELD_DESC);
        {
          oprot.writeListBegin(new TList(TType.STRUCT, this.success.size()));
          for (column_t _iter46 : this.success)          {
            _iter46.write(oprot);
          }
          oprot.writeListEnd();
        }
        oprot.writeFieldEnd();
      } else if (this.isSetIre()) {
        oprot.writeFieldBegin(IRE_FIELD_DESC);
        this.ire.write(oprot);
        oprot.writeFieldEnd();
      } else if (this.isSetNfe()) {
        oprot.writeFieldBegin(NFE_FIELD_DESC);
        this.nfe.write(oprot);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

    @Override
    public String toString() {
      StringBuilder sb = new StringBuilder("get_columns_since_result(");
      boolean first = true;

      sb.append("success:");
      if (this.success == null) {
        sb.append("null");
      } else {
        sb.append(this.success);
      }
      first = false;
      if (!first) sb.append(", ");
      sb.append("ire:");
      if (this.ire == null) {
        sb.append("null");
      } else {
        sb.append(this.ire);
      }
      first = false;
      if (!first) sb.append(", ");
      sb.append("nfe:");
      if (this.nfe == null) {
        sb.append("null");
      } else {
        sb.append(this.nfe);
      }
      first = false;
      sb.append(")");
      return sb.toString();
    }

    public void validate() throws TException {
      // check for required fields
      // check that fields of type enum have valid values
    }

  }

  public static class get_slice_super_args implements TBase, java.io.Serializable, Cloneable   {
    private static final TStruct STRUCT_DESC = new TStruct("get_slice_super_args");
    private static final TField TABLENAME_FIELD_DESC = new TField("tablename", TType.STRING, (short)1);
    private static final TField KEY_FIELD_DESC = new TField("key", TType.STRING, (short)2);
    private static final TField COLUMN_FAMILY_FIELD_DESC = new TField("columnFamily", TType.STRING, (short)3);
    private static final TField START_FIELD_DESC = new TField("start", TType.I32, (short)4);
    private static final TField COUNT_FIELD_DESC = new TField("count", TType.I32, (short)5);

    public String tablename;
    public static final int TABLENAME = 1;
    public String key;
    public static final int KEY = 2;
    public String columnFamily;
    public static final int COLUMNFAMILY = 3;
    public int start;
    public static final int START = 4;
    public int count;
    public static final int COUNT = 5;

    private final Isset __isset = new Isset();
    private static final class Isset implements java.io.Serializable {
      public boolean start = false;
      public boolean count = false;
    }

    public static final Map<Integer, FieldMetaData> metaDataMap = Collections.unmodifiableMap(new HashMap<Integer, FieldMetaData>() {{
      put(TABLENAME, new FieldMetaData("tablename", TFieldRequirementType.DEFAULT, 
          new FieldValueMetaData(TType.STRING)));
      put(KEY, new FieldMetaData("key", TFieldRequirementType.DEFAULT, 
          new FieldValueMetaData(TType.STRING)));
      put(COLUMNFAMILY, new FieldMetaData("columnFamily", TFieldRequirementType.DEFAULT, 
          new FieldValueMetaData(TType.STRING)));
      put(START, new FieldMetaData("start", TFieldRequirementType.DEFAULT, 
          new FieldValueMetaData(TType.I32)));
      put(COUNT, new FieldMetaData("count", TFieldRequirementType.DEFAULT, 
          new FieldValueMetaData(TType.I32)));
    }});

    static {
      FieldMetaData.addStructMetaDataMap(get_slice_super_args.class, metaDataMap);
    }

    public get_slice_super_args() {
      this.start = -1;

      this.count = -1;

    }

    public get_slice_super_args(
      String tablename,
      String key,
      String columnFamily,
      int start,
      int count)
    {
      this();
      this.tablename = tablename;
      this.key = key;
      this.columnFamily = columnFamily;
      this.start = start;
      this.__isset.start = true;
      this.count = count;
      this.__isset.count = true;
    }

    /**
     * Performs a deep copy on <i>other</i>.
     */
    public get_slice_super_args(get_slice_super_args other) {
      if (other.isSetTablename()) {
        this.tablename = other.tablename;
      }
      if (other.isSetKey()) {
        this.key = other.key;
      }
      if (other.isSetColumnFamily()) {
        this.columnFamily = other.columnFamily;
      }
      __isset.start = other.__isset.start;
      this.start = other.start;
      __isset.count = other.__isset.count;
      this.count = other.count;
    }

    @Override
    public get_slice_super_args clone() {
      return new get_slice_super_args(this);
    }

    public String getTablename() {
      return this.tablename;
    }

    public void setTablename(String tablename) {
      this.tablename = tablename;
    }

    public void unsetTablename() {
      this.tablename = null;
    }

    // Returns true if field tablename is set (has been asigned a value) and false otherwise
    public boolean isSetTablename() {
      return this.tablename != null;
    }

    public void setTablenameIsSet(boolean value) {
      if (!value) {
        this.tablename = null;
      }
    }

    public String getKey() {
      return this.key;
    }

    public void setKey(String key) {
      this.key = key;
    }

    public void unsetKey() {
      this.key = null;
    }

    // Returns true if field key is set (has been asigned a value) and false otherwise
    public boolean isSetKey() {
      return this.key != null;
    }

    public void setKeyIsSet(boolean value) {
      if (!value) {
        this.key = null;
      }
    }

    public String getColumnFamily() {
      return this.columnFamily;
    }

    public void setColumnFamily(String columnFamily) {
      this.columnFamily = columnFamily;
    }

    public void unsetColumnFamily() {
      this.columnFamily = null;
    }

    // Returns true if field columnFamily is set (has been asigned a value) and false otherwise
    public boolean isSetColumnFamily() {
      return this.columnFamily != null;
    }

    public void setColumnFamilyIsSet(boolean value) {
      if (!value) {
        this.columnFamily = null;
      }
    }

    public int getStart() {
      return this.start;
    }

    public void setStart(int start) {
      this.start = start;
      this.__isset.start = true;
    }

    public void unsetStart() {
      this.__isset.start = false;
    }

    // Returns true if field start is set (has been asigned a value) and false otherwise
    public boolean isSetStart() {
      return this.__isset.start;
    }

    public void setStartIsSet(boolean value) {
      this.__isset.start = value;
    }

    public int getCount() {
      return this.count;
    }

    public void setCount(int count) {
      this.count = count;
      this.__isset.count = true;
    }

    public void unsetCount() {
      this.__isset.count = false;
    }

    // Returns true if field count is set (has been asigned a value) and false otherwise
    public boolean isSetCount() {
      return this.__isset.count;
    }

    public void setCountIsSet(boolean value) {
      this.__isset.count = value;
    }

    public void setFieldValue(int fieldID, Object value) {
      switch (fieldID) {
      case TABLENAME:
        if (value == null) {
          unsetTablename();
        } else {
          setTablename((String)value);
        }
        break;

      case KEY:
        if (value == null) {
          unsetKey();
        } else {
          setKey((String)value);
        }
        break;

      case COLUMNFAMILY:
        if (value == null) {
          unsetColumnFamily();
        } else {
          setColumnFamily((String)value);
        }
        break;

      case START:
        if (value == null) {
          unsetStart();
        } else {
          setStart((Integer)value);
        }
        break;

      case COUNT:
        if (value == null) {
          unsetCount();
        } else {
          setCount((Integer)value);
        }
        break;

      default:
        throw new IllegalArgumentException("Field " + fieldID + " doesn't exist!");
      }
    }

    public Object getFieldValue(int fieldID) {
      switch (fieldID) {
      case TABLENAME:
        return getTablename();

      case KEY:
        return getKey();

      case COLUMNFAMILY:
        return getColumnFamily();

      case START:
        return new Integer(getStart());

      case COUNT:
        return new Integer(getCount());

      default:
        throw new IllegalArgumentException("Field " + fieldID + " doesn't exist!");
      }
    }

    // Returns true if field corresponding to fieldID is set (has been asigned a value) and false otherwise
    public boolean isSet(int fieldID) {
      switch (fieldID) {
      case TABLENAME:
        return isSetTablename();
      case KEY:
        return isSetKey();
      case COLUMNFAMILY:
        return isSetColumnFamily();
      case START:
        return isSetStart();
      case COUNT:
        return isSetCount();
      default:
        throw new IllegalArgumentException("Field " + fieldID + " doesn't exist!");
      }
    }

    @Override
    public boolean equals(Object that) {
      if (that == null)
        return false;
      if (that instanceof get_slice_super_args)
        return this.equals((get_slice_super_args)that);
      return false;
    }

    public boolean equals(get_slice_super_args that) {
      if (that == null)
        return false;

      boolean this_present_tablename = true && this.isSetTablename();
      boolean that_present_tablename = true && that.isSetTablename();
      if (this_present_tablename || that_present_tablename) {
        if (!(this_present_tablename && that_present_tablename))
          return false;
        if (!this.tablename.equals(that.tablename))
          return false;
      }

      boolean this_present_key = true && this.isSetKey();
      boolean that_present_key = true && that.isSetKey();
      if (this_present_key || that_present_key) {
        if (!(this_present_key && that_present_key))
          return false;
        if (!this.key.equals(that.key))
          return false;
      }

      boolean this_present_columnFamily = true && this.isSetColumnFamily();
      boolean that_present_columnFamily = true && that.isSetColumnFamily();
      if (this_present_columnFamily || that_present_columnFamily) {
        if (!(this_present_columnFamily && that_present_columnFamily))
          return false;
        if (!this.columnFamily.equals(that.columnFamily))
          return false;
      }

      boolean this_present_start = true;
      boolean that_present_start = true;
      if (this_present_start || that_present_start) {
        if (!(this_present_start && that_present_start))
          return false;
        if (this.start != that.start)
          return false;
      }

      boolean this_present_count = true;
      boolean that_present_count = true;
      if (this_present_count || that_present_count) {
        if (!(this_present_count && that_present_count))
          return false;
        if (this.count != that.count)
          return false;
      }

      return true;
    }

    @Override
    public int hashCode() {
      return 0;
    }

    public void read(TProtocol iprot) throws TException {
      TField field;
      iprot.readStructBegin();
      while (true)
      {
        field = iprot.readFieldBegin();
        if (field.type == TType.STOP) { 
          break;
        }
        switch (field.id)
        {
          case TABLENAME:
            if (field.type == TType.STRING) {
              this.tablename = iprot.readString();
            } else { 
              TProtocolUtil.skip(iprot, field.type);
            }
            break;
          case KEY:
            if (field.type == TType.STRING) {
              this.key = iprot.readString();
            } else { 
              TProtocolUtil.skip(iprot, field.type);
            }
            break;
          case COLUMNFAMILY:
            if (field.type == TType.STRING) {
              this.columnFamily = iprot.readString();
            } else { 
              TProtocolUtil.skip(iprot, field.type);
            }
            break;
          case START:
            if (field.type == TType.I32) {
              this.start = iprot.readI32();
              this.__isset.start = true;
            } else { 
              TProtocolUtil.skip(iprot, field.type);
            }
            break;
          case COUNT:
            if (field.type == TType.I32) {
              this.count = iprot.readI32();
              this.__isset.count = true;
            } else { 
              TProtocolUtil.skip(iprot, field.type);
            }
            break;
          default:
            TProtocolUtil.skip(iprot, field.type);
            break;
        }
        iprot.readFieldEnd();
      }
      iprot.readStructEnd();


      // check for required fields of primitive type, which can't be checked in the validate method
      validate();
    }

    public void write(TProtocol oprot) throws TException {
      validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (this.tablename != null) {
        oprot.writeFieldBegin(TABLENAME_FIELD_DESC);
        oprot.writeString(this.tablename);
        oprot.writeFieldEnd();
      }
      if (this.key != null) {
        oprot.writeFieldBegin(KEY_FIELD_DESC);
        oprot.writeString(this.key);
        oprot.writeFieldEnd();
      }
      if (this.columnFamily != null) {
        oprot.writeFieldBegin(COLUMN_FAMILY_FIELD_DESC);
        oprot.writeString(this.columnFamily);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldBegin(START_FIELD_DESC);
      oprot.writeI32(this.start);
      oprot.writeFieldEnd();
      oprot.writeFieldBegin(COUNT_FIELD_DESC);
      oprot.writeI32(this.count);
      oprot.writeFieldEnd();
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

    @Override
    public String toString() {
      StringBuilder sb = new StringBuilder("get_slice_super_args(");
      boolean first = true;

      sb.append("tablename:");
      if (this.tablename == null) {
        sb.append("null");
      } else {
        sb.append(this.tablename);
      }
      first = false;
      if (!first) sb.append(", ");
      sb.append("key:");
      if (this.key == null) {
        sb.append("null");
      } else {
        sb.append(this.key);
      }
      first = false;
      if (!first) sb.append(", ");
      sb.append("columnFamily:");
      if (this.columnFamily == null) {
        sb.append("null");
      } else {
        sb.append(this.columnFamily);
      }
      first = false;
      if (!first) sb.append(", ");
      sb.append("start:");
      sb.append(this.start);
      first = false;
      if (!first) sb.append(", ");
      sb.append("count:");
      sb.append(this.count);
      first = false;
      sb.append(")");
      return sb.toString();
    }

    public void validate() throws TException {
      // check for required fields
      // check that fields of type enum have valid values
    }

  }

  public static class get_slice_super_result implements TBase, java.io.Serializable, Cloneable   {
    private static final TStruct STRUCT_DESC = new TStruct("get_slice_super_result");
    private static final TField SUCCESS_FIELD_DESC = new TField("success", TType.LIST, (short)0);
    private static final TField IRE_FIELD_DESC = new TField("ire", TType.STRUCT, (short)1);

    public List<superColumn_t> success;
    public static final int SUCCESS = 0;
    public InvalidRequestException ire;
    public static final int IRE = 1;

    private final Isset __isset = new Isset();
    private static final class Isset implements java.io.Serializable {
    }

    public static final Map<Integer, FieldMetaData> metaDataMap = Collections.unmodifiableMap(new HashMap<Integer, FieldMetaData>() {{
      put(SUCCESS, new FieldMetaData("success", TFieldRequirementType.DEFAULT, 
          new ListMetaData(TType.LIST, 
              new StructMetaData(TType.STRUCT, superColumn_t.class))));
      put(IRE, new FieldMetaData("ire", TFieldRequirementType.DEFAULT, 
          new FieldValueMetaData(TType.STRUCT)));
    }});

    static {
      FieldMetaData.addStructMetaDataMap(get_slice_super_result.class, metaDataMap);
    }

    public get_slice_super_result() {
    }

    public get_slice_super_result(
      List<superColumn_t> success,
      InvalidRequestException ire)
    {
      this();
      this.success = success;
      this.ire = ire;
    }

    /**
     * Performs a deep copy on <i>other</i>.
     */
    public get_slice_super_result(get_slice_super_result other) {
      if (other.isSetSuccess()) {
        List<superColumn_t> __this__success = new ArrayList<superColumn_t>();
        for (superColumn_t other_element : other.success) {
          __this__success.add(new superColumn_t(other_element));
        }
        this.success = __this__success;
      }
      if (other.isSetIre()) {
        this.ire = new InvalidRequestException(other.ire);
      }
    }

    @Override
    public get_slice_super_result clone() {
      return new get_slice_super_result(this);
    }

    public int getSuccessSize() {
      return (this.success == null) ? 0 : this.success.size();
    }

    public java.util.Iterator<superColumn_t> getSuccessIterator() {
      return (this.success == null) ? null : this.success.iterator();
    }

    public void addToSuccess(superColumn_t elem) {
      if (this.success == null) {
        this.success = new ArrayList<superColumn_t>();
      }
      this.success.add(elem);
    }

    public List<superColumn_t> getSuccess() {
      return this.success;
    }

    public void setSuccess(List<superColumn_t> success) {
      this.success = success;
    }

    public void unsetSuccess() {
      this.success = null;
    }

    // Returns true if field success is set (has been asigned a value) and false otherwise
    public boolean isSetSuccess() {
      return this.success != null;
    }

    public void setSuccessIsSet(boolean value) {
      if (!value) {
        this.success = null;
      }
    }

    public InvalidRequestException getIre() {
      return this.ire;
    }

    public void setIre(InvalidRequestException ire) {
      this.ire = ire;
    }

    public void unsetIre() {
      this.ire = null;
    }

    // Returns true if field ire is set (has been asigned a value) and false otherwise
    public boolean isSetIre() {
      return this.ire != null;
    }

    public void setIreIsSet(boolean value) {
      if (!value) {
        this.ire = null;
      }
    }

    public void setFieldValue(int fieldID, Object value) {
      switch (fieldID) {
      case SUCCESS:
        if (value == null) {
          unsetSuccess();
        } else {
          setSuccess((List<superColumn_t>)value);
        }
        break;

      case IRE:
        if (value == null) {
          unsetIre();
        } else {
          setIre((InvalidRequestException)value);
        }
        break;

      default:
        throw new IllegalArgumentException("Field " + fieldID + " doesn't exist!");
      }
    }

    public Object getFieldValue(int fieldID) {
      switch (fieldID) {
      case SUCCESS:
        return getSuccess();

      case IRE:
        return getIre();

      default:
        throw new IllegalArgumentException("Field " + fieldID + " doesn't exist!");
      }
    }

    // Returns true if field corresponding to fieldID is set (has been asigned a value) and false otherwise
    public boolean isSet(int fieldID) {
      switch (fieldID) {
      case SUCCESS:
        return isSetSuccess();
      case IRE:
        return isSetIre();
      default:
        throw new IllegalArgumentException("Field " + fieldID + " doesn't exist!");
      }
    }

    @Override
    public boolean equals(Object that) {
      if (that == null)
        return false;
      if (that instanceof get_slice_super_result)
        return this.equals((get_slice_super_result)that);
      return false;
    }

    public boolean equals(get_slice_super_result that) {
      if (that == null)
        return false;

      boolean this_present_success = true && this.isSetSuccess();
      boolean that_present_success = true && that.isSetSuccess();
      if (this_present_success || that_present_success) {
        if (!(this_present_success && that_present_success))
          return false;
        if (!this.success.equals(that.success))
          return false;
      }

      boolean this_present_ire = true && this.isSetIre();
      boolean that_present_ire = true && that.isSetIre();
      if (this_present_ire || that_present_ire) {
        if (!(this_present_ire && that_present_ire))
          return false;
        if (!this.ire.equals(that.ire))
          return false;
      }

      return true;
    }

    @Override
    public int hashCode() {
      return 0;
    }

    public void read(TProtocol iprot) throws TException {
      TField field;
      iprot.readStructBegin();
      while (true)
      {
        field = iprot.readFieldBegin();
        if (field.type == TType.STOP) { 
          break;
        }
        switch (field.id)
        {
          case SUCCESS:
            if (field.type == TType.LIST) {
              {
                TList _list47 = iprot.readListBegin();
                this.success = new ArrayList<superColumn_t>(_list47.size);
                for (int _i48 = 0; _i48 < _list47.size; ++_i48)
                {
                  superColumn_t _elem49;
                  _elem49 = new superColumn_t();
                  _elem49.read(iprot);
                  this.success.add(_elem49);
                }
                iprot.readListEnd();
              }
            } else { 
              TProtocolUtil.skip(iprot, field.type);
            }
            break;
          case IRE:
            if (field.type == TType.STRUCT) {
              this.ire = new InvalidRequestException();
              this.ire.read(iprot);
            } else { 
              TProtocolUtil.skip(iprot, field.type);
            }
            break;
          default:
            TProtocolUtil.skip(iprot, field.type);
            break;
        }
        iprot.readFieldEnd();
      }
      iprot.readStructEnd();


      // check for required fields of primitive type, which can't be checked in the validate method
      validate();
    }

    public void write(TProtocol oprot) throws TException {
      oprot.writeStructBegin(STRUCT_DESC);

      if (this.isSetSuccess()) {
        oprot.writeFieldBegin(SUCCESS_FIELD_DESC);
        {
          oprot.writeListBegin(new TList(TType.STRUCT, this.success.size()));
          for (superColumn_t _iter50 : this.success)          {
            _iter50.write(oprot);
          }
          oprot.writeListEnd();
        }
        oprot.writeFieldEnd();
      } else if (this.isSetIre()) {
        oprot.writeFieldBegin(IRE_FIELD_DESC);
        this.ire.write(oprot);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

    @Override
    public String toString() {
      StringBuilder sb = new StringBuilder("get_slice_super_result(");
      boolean first = true;

      sb.append("success:");
      if (this.success == null) {
        sb.append("null");
      } else {
        sb.append(this.success);
      }
      first = false;
      if (!first) sb.append(", ");
      sb.append("ire:");
      if (this.ire == null) {
        sb.append("null");
      } else {
        sb.append(this.ire);
      }
      first = false;
      sb.append(")");
      return sb.toString();
    }

    public void validate() throws TException {
      // check for required fields
      // check that fields of type enum have valid values
    }

  }

  public static class get_slice_super_by_names_args implements TBase, java.io.Serializable, Cloneable   {
    private static final TStruct STRUCT_DESC = new TStruct("get_slice_super_by_names_args");
    private static final TField TABLENAME_FIELD_DESC = new TField("tablename", TType.STRING, (short)1);
    private static final TField KEY_FIELD_DESC = new TField("key", TType.STRING, (short)2);
    private static final TField COLUMN_FAMILY_FIELD_DESC = new TField("columnFamily", TType.STRING, (short)3);
    private static final TField SUPER_COLUMN_NAMES_FIELD_DESC = new TField("superColumnNames", TType.LIST, (short)4);

    public String tablename;
    public static final int TABLENAME = 1;
    public String key;
    public static final int KEY = 2;
    public String columnFamily;
    public static final int COLUMNFAMILY = 3;
    public List<String> superColumnNames;
    public static final int SUPERCOLUMNNAMES = 4;

    private final Isset __isset = new Isset();
    private static final class Isset implements java.io.Serializable {
    }

    public static final Map<Integer, FieldMetaData> metaDataMap = Collections.unmodifiableMap(new HashMap<Integer, FieldMetaData>() {{
      put(TABLENAME, new FieldMetaData("tablename", TFieldRequirementType.DEFAULT, 
          new FieldValueMetaData(TType.STRING)));
      put(KEY, new FieldMetaData("key", TFieldRequirementType.DEFAULT, 
          new FieldValueMetaData(TType.STRING)));
      put(COLUMNFAMILY, new FieldMetaData("columnFamily", TFieldRequirementType.DEFAULT, 
          new FieldValueMetaData(TType.STRING)));
      put(SUPERCOLUMNNAMES, new FieldMetaData("superColumnNames", TFieldRequirementType.DEFAULT, 
          new ListMetaData(TType.LIST, 
              new FieldValueMetaData(TType.STRING))));
    }});

    static {
      FieldMetaData.addStructMetaDataMap(get_slice_super_by_names_args.class, metaDataMap);
    }

    public get_slice_super_by_names_args() {
    }

    public get_slice_super_by_names_args(
      String tablename,
      String key,
      String columnFamily,
      List<String> superColumnNames)
    {
      this();
      this.tablename = tablename;
      this.key = key;
      this.columnFamily = columnFamily;
      this.superColumnNames = superColumnNames;
    }

    /**
     * Performs a deep copy on <i>other</i>.
     */
    public get_slice_super_by_names_args(get_slice_super_by_names_args other) {
      if (other.isSetTablename()) {
        this.tablename = other.tablename;
      }
      if (other.isSetKey()) {
        this.key = other.key;
      }
      if (other.isSetColumnFamily()) {
        this.columnFamily = other.columnFamily;
      }
      if (other.isSetSuperColumnNames()) {
        List<String> __this__superColumnNames = new ArrayList<String>();
        for (String other_element : other.superColumnNames) {
          __this__superColumnNames.add(other_element);
        }
        this.superColumnNames = __this__superColumnNames;
      }
    }

    @Override
    public get_slice_super_by_names_args clone() {
      return new get_slice_super_by_names_args(this);
    }

    public String getTablename() {
      return this.tablename;
    }

    public void setTablename(String tablename) {
      this.tablename = tablename;
    }

    public void unsetTablename() {
      this.tablename = null;
    }

    // Returns true if field tablename is set (has been asigned a value) and false otherwise
    public boolean isSetTablename() {
      return this.tablename != null;
    }

    public void setTablenameIsSet(boolean value) {
      if (!value) {
        this.tablename = null;
      }
    }

    public String getKey() {
      return this.key;
    }

    public void setKey(String key) {
      this.key = key;
    }

    public void unsetKey() {
      this.key = null;
    }

    // Returns true if field key is set (has been asigned a value) and false otherwise
    public boolean isSetKey() {
      return this.key != null;
    }

    public void setKeyIsSet(boolean value) {
      if (!value) {
        this.key = null;
      }
    }

    public String getColumnFamily() {
      return this.columnFamily;
    }

    public void setColumnFamily(String columnFamily) {
      this.columnFamily = columnFamily;
    }

    public void unsetColumnFamily() {
      this.columnFamily = null;
    }

    // Returns true if field columnFamily is set (has been asigned a value) and false otherwise
    public boolean isSetColumnFamily() {
      return this.columnFamily != null;
    }

    public void setColumnFamilyIsSet(boolean value) {
      if (!value) {
        this.columnFamily = null;
      }
    }

    public int getSuperColumnNamesSize() {
      return (this.superColumnNames == null) ? 0 : this.superColumnNames.size();
    }

    public java.util.Iterator<String> getSuperColumnNamesIterator() {
      return (this.superColumnNames == null) ? null : this.superColumnNames.iterator();
    }

    public void addToSuperColumnNames(String elem) {
      if (this.superColumnNames == null) {
        this.superColumnNames = new ArrayList<String>();
      }
      this.superColumnNames.add(elem);
    }

    public List<String> getSuperColumnNames() {
      return this.superColumnNames;
    }

    public void setSuperColumnNames(List<String> superColumnNames) {
      this.superColumnNames = superColumnNames;
    }

    public void unsetSuperColumnNames() {
      this.superColumnNames = null;
    }

    // Returns true if field superColumnNames is set (has been asigned a value) and false otherwise
    public boolean isSetSuperColumnNames() {
      return this.superColumnNames != null;
    }

    public void setSuperColumnNamesIsSet(boolean value) {
      if (!value) {
        this.superColumnNames = null;
      }
    }

    public void setFieldValue(int fieldID, Object value) {
      switch (fieldID) {
      case TABLENAME:
        if (value == null) {
          unsetTablename();
        } else {
          setTablename((String)value);
        }
        break;

      case KEY:
        if (value == null) {
          unsetKey();
        } else {
          setKey((String)value);
        }
        break;

      case COLUMNFAMILY:
        if (value == null) {
          unsetColumnFamily();
        } else {
          setColumnFamily((String)value);
        }
        break;

      case SUPERCOLUMNNAMES:
        if (value == null) {
          unsetSuperColumnNames();
        } else {
          setSuperColumnNames((List<String>)value);
        }
        break;

      default:
        throw new IllegalArgumentException("Field " + fieldID + " doesn't exist!");
      }
    }

    public Object getFieldValue(int fieldID) {
      switch (fieldID) {
      case TABLENAME:
        return getTablename();

      case KEY:
        return getKey();

      case COLUMNFAMILY:
        return getColumnFamily();

      case SUPERCOLUMNNAMES:
        return getSuperColumnNames();

      default:
        throw new IllegalArgumentException("Field " + fieldID + " doesn't exist!");
      }
    }

    // Returns true if field corresponding to fieldID is set (has been asigned a value) and false otherwise
    public boolean isSet(int fieldID) {
      switch (fieldID) {
      case TABLENAME:
        return isSetTablename();
      case KEY:
        return isSetKey();
      case COLUMNFAMILY:
        return isSetColumnFamily();
      case SUPERCOLUMNNAMES:
        return isSetSuperColumnNames();
      default:
        throw new IllegalArgumentException("Field " + fieldID + " doesn't exist!");
      }
    }

    @Override
    public boolean equals(Object that) {
      if (that == null)
        return false;
      if (that instanceof get_slice_super_by_names_args)
        return this.equals((get_slice_super_by_names_args)that);
      return false;
    }

    public boolean equals(get_slice_super_by_names_args that) {
      if (that == null)
        return false;

      boolean this_present_tablename = true && this.isSetTablename();
      boolean that_present_tablename = true && that.isSetTablename();
      if (this_present_tablename || that_present_tablename) {
        if (!(this_present_tablename && that_present_tablename))
          return false;
        if (!this.tablename.equals(that.tablename))
          return false;
      }

      boolean this_present_key = true && this.isSetKey();
      boolean that_present_key = true && that.isSetKey();
      if (this_present_key || that_present_key) {
        if (!(this_present_key && that_present_key))
          return false;
        if (!this.key.equals(that.key))
          return false;
      }

      boolean this_present_columnFamily = true && this.isSetColumnFamily();
      boolean that_present_columnFamily = true && that.isSetColumnFamily();
      if (this_present_columnFamily || that_present_columnFamily) {
        if (!(this_present_columnFamily && that_present_columnFamily))
          return false;
        if (!this.columnFamily.equals(that.columnFamily))
          return false;
      }

      boolean this_present_superColumnNames = true && this.isSetSuperColumnNames();
      boolean that_present_superColumnNames = true && that.isSetSuperColumnNames();
      if (this_present_superColumnNames || that_present_superColumnNames) {
        if (!(this_present_superColumnNames && that_present_superColumnNames))
          return false;
        if (!this.superColumnNames.equals(that.superColumnNames))
          return false;
      }

      return true;
    }

    @Override
    public int hashCode() {
      return 0;
    }

    public void read(TProtocol iprot) throws TException {
      TField field;
      iprot.readStructBegin();
      while (true)
      {
        field = iprot.readFieldBegin();
        if (field.type == TType.STOP) { 
          break;
        }
        switch (field.id)
        {
          case TABLENAME:
            if (field.type == TType.STRING) {
              this.tablename = iprot.readString();
            } else { 
              TProtocolUtil.skip(iprot, field.type);
            }
            break;
          case KEY:
            if (field.type == TType.STRING) {
              this.key = iprot.readString();
            } else { 
              TProtocolUtil.skip(iprot, field.type);
            }
            break;
          case COLUMNFAMILY:
            if (field.type == TType.STRING) {
              this.columnFamily = iprot.readString();
            } else { 
              TProtocolUtil.skip(iprot, field.type);
            }
            break;
          case SUPERCOLUMNNAMES:
            if (field.type == TType.LIST) {
              {
                TList _list51 = iprot.readListBegin();
                this.superColumnNames = new ArrayList<String>(_list51.size);
                for (int _i52 = 0; _i52 < _list51.size; ++_i52)
                {
                  String _elem53;
                  _elem53 = iprot.readString();
                  this.superColumnNames.add(_elem53);
                }
                iprot.readListEnd();
              }
            } else { 
              TProtocolUtil.skip(iprot, field.type);
            }
            break;
          default:
            TProtocolUtil.skip(iprot, field.type);
            break;
        }
        iprot.readFieldEnd();
      }
      iprot.readStructEnd();


      // check for required fields of primitive type, which can't be checked in the validate method
      validate();
    }

    public void write(TProtocol oprot) throws TException {
      validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (this.tablename != null) {
        oprot.writeFieldBegin(TABLENAME_FIELD_DESC);
        oprot.writeString(this.tablename);
        oprot.writeFieldEnd();
      }
      if (this.key != null) {
        oprot.writeFieldBegin(KEY_FIELD_DESC);
        oprot.writeString(this.key);
        oprot.writeFieldEnd();
      }
      if (this.columnFamily != null) {
        oprot.writeFieldBegin(COLUMN_FAMILY_FIELD_DESC);
        oprot.writeString(this.columnFamily);
        oprot.writeFieldEnd();
      }
      if (this.superColumnNames != null) {
        oprot.writeFieldBegin(SUPER_COLUMN_NAMES_FIELD_DESC);
        {
          oprot.writeListBegin(new TList(TType.STRING, this.superColumnNames.size()));
          for (String _iter54 : this.superColumnNames)          {
            oprot.writeString(_iter54);
          }
          oprot.writeListEnd();
        }
        oprot.writeFieldEnd();
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

    @Override
    public String toString() {
      StringBuilder sb = new StringBuilder("get_slice_super_by_names_args(");
      boolean first = true;

      sb.append("tablename:");
      if (this.tablename == null) {
        sb.append("null");
      } else {
        sb.append(this.tablename);
      }
      first = false;
      if (!first) sb.append(", ");
      sb.append("key:");
      if (this.key == null) {
        sb.append("null");
      } else {
        sb.append(this.key);
      }
      first = false;
      if (!first) sb.append(", ");
      sb.append("columnFamily:");
      if (this.columnFamily == null) {
        sb.append("null");
      } else {
        sb.append(this.columnFamily);
      }
      first = false;
      if (!first) sb.append(", ");
      sb.append("superColumnNames:");
      if (this.superColumnNames == null) {
        sb.append("null");
      } else {
        sb.append(this.superColumnNames);
      }
      first = false;
      sb.append(")");
      return sb.toString();
    }

    public void validate() throws TException {
      // check for required fields
      // check that fields of type enum have valid values
    }

  }

  public static class get_slice_super_by_names_result implements TBase, java.io.Serializable, Cloneable   {
    private static final TStruct STRUCT_DESC = new TStruct("get_slice_super_by_names_result");
    private static final TField SUCCESS_FIELD_DESC = new TField("success", TType.LIST, (short)0);
    private static final TField IRE_FIELD_DESC = new TField("ire", TType.STRUCT, (short)1);

    public List<superColumn_t> success;
    public static final int SUCCESS = 0;
    public InvalidRequestException ire;
    public static final int IRE = 1;

    private final Isset __isset = new Isset();
    private static final class Isset implements java.io.Serializable {
    }

    public static final Map<Integer, FieldMetaData> metaDataMap = Collections.unmodifiableMap(new HashMap<Integer, FieldMetaData>() {{
      put(SUCCESS, new FieldMetaData("success", TFieldRequirementType.DEFAULT, 
          new ListMetaData(TType.LIST, 
              new StructMetaData(TType.STRUCT, superColumn_t.class))));
      put(IRE, new FieldMetaData("ire", TFieldRequirementType.DEFAULT, 
          new FieldValueMetaData(TType.STRUCT)));
    }});

    static {
      FieldMetaData.addStructMetaDataMap(get_slice_super_by_names_result.class, metaDataMap);
    }

    public get_slice_super_by_names_result() {
    }

    public get_slice_super_by_names_result(
      List<superColumn_t> success,
      InvalidRequestException ire)
    {
      this();
      this.success = success;
      this.ire = ire;
    }

    /**
     * Performs a deep copy on <i>other</i>.
     */
    public get_slice_super_by_names_result(get_slice_super_by_names_result other) {
      if (other.isSetSuccess()) {
        List<superColumn_t> __this__success = new ArrayList<superColumn_t>();
        for (superColumn_t other_element : other.success) {
          __this__success.add(new superColumn_t(other_element));
        }
        this.success = __this__success;
      }
      if (other.isSetIre()) {
        this.ire = new InvalidRequestException(other.ire);
      }
    }

    @Override
    public get_slice_super_by_names_result clone() {
      return new get_slice_super_by_names_result(this);
    }

    public int getSuccessSize() {
      return (this.success == null) ? 0 : this.success.size();
    }

    public java.util.Iterator<superColumn_t> getSuccessIterator() {
      return (this.success == null) ? null : this.success.iterator();
    }

    public void addToSuccess(superColumn_t elem) {
      if (this.success == null) {
        this.success = new ArrayList<superColumn_t>();
      }
      this.success.add(elem);
    }

    public List<superColumn_t> getSuccess() {
      return this.success;
    }

    public void setSuccess(List<superColumn_t> success) {
      this.success = success;
    }

    public void unsetSuccess() {
      this.success = null;
    }

    // Returns true if field success is set (has been asigned a value) and false otherwise
    public boolean isSetSuccess() {
      return this.success != null;
    }

    public void setSuccessIsSet(boolean value) {
      if (!value) {
        this.success = null;
      }
    }

    public InvalidRequestException getIre() {
      return this.ire;
    }

    public void setIre(InvalidRequestException ire) {
      this.ire = ire;
    }

    public void unsetIre() {
      this.ire = null;
    }

    // Returns true if field ire is set (has been asigned a value) and false otherwise
    public boolean isSetIre() {
      return this.ire != null;
    }

    public void setIreIsSet(boolean value) {
      if (!value) {
        this.ire = null;
      }
    }

    public void setFieldValue(int fieldID, Object value) {
      switch (fieldID) {
      case SUCCESS:
        if (value == null) {
          unsetSuccess();
        } else {
          setSuccess((List<superColumn_t>)value);
        }
        break;

      case IRE:
        if (value == null) {
          unsetIre();
        } else {
          setIre((InvalidRequestException)value);
        }
        break;

      default:
        throw new IllegalArgumentException("Field " + fieldID + " doesn't exist!");
      }
    }

    public Object getFieldValue(int fieldID) {
      switch (fieldID) {
      case SUCCESS:
        return getSuccess();

      case IRE:
        return getIre();

      default:
        throw new IllegalArgumentException("Field " + fieldID + " doesn't exist!");
      }
    }

    // Returns true if field corresponding to fieldID is set (has been asigned a value) and false otherwise
    public boolean isSet(int fieldID) {
      switch (fieldID) {
      case SUCCESS:
        return isSetSuccess();
      case IRE:
        return isSetIre();
      default:
        throw new IllegalArgumentException("Field " + fieldID + " doesn't exist!");
      }
    }

    @Override
    public boolean equals(Object that) {
      if (that == null)
        return false;
      if (that instanceof get_slice_super_by_names_result)
        return this.equals((get_slice_super_by_names_result)that);
      return false;
    }

    public boolean equals(get_slice_super_by_names_result that) {
      if (that == null)
        return false;

      boolean this_present_success = true && this.isSetSuccess();
      boolean that_present_success = true && that.isSetSuccess();
      if (this_present_success || that_present_success) {
        if (!(this_present_success && that_present_success))
          return false;
        if (!this.success.equals(that.success))
          return false;
      }

      boolean this_present_ire = true && this.isSetIre();
      boolean that_present_ire = true && that.isSetIre();
      if (this_present_ire || that_present_ire) {
        if (!(this_present_ire && that_present_ire))
          return false;
        if (!this.ire.equals(that.ire))
          return false;
      }

      return true;
    }

    @Override
    public int hashCode() {
      return 0;
    }

    public void read(TProtocol iprot) throws TException {
      TField field;
      iprot.readStructBegin();
      while (true)
      {
        field = iprot.readFieldBegin();
        if (field.type == TType.STOP) { 
          break;
        }
        switch (field.id)
        {
          case SUCCESS:
            if (field.type == TType.LIST) {
              {
                TList _list55 = iprot.readListBegin();
                this.success = new ArrayList<superColumn_t>(_list55.size);
                for (int _i56 = 0; _i56 < _list55.size; ++_i56)
                {
                  superColumn_t _elem57;
                  _elem57 = new superColumn_t();
                  _elem57.read(iprot);
                  this.success.add(_elem57);
                }
                iprot.readListEnd();
              }
            } else { 
              TProtocolUtil.skip(iprot, field.type);
            }
            break;
          case IRE:
            if (field.type == TType.STRUCT) {
              this.ire = new InvalidRequestException();
              this.ire.read(iprot);
            } else { 
              TProtocolUtil.skip(iprot, field.type);
            }
            break;
          default:
            TProtocolUtil.skip(iprot, field.type);
            break;
        }
        iprot.readFieldEnd();
      }
      iprot.readStructEnd();


      // check for required fields of primitive type, which can't be checked in the validate method
      validate();
    }

    public void write(TProtocol oprot) throws TException {
      oprot.writeStructBegin(STRUCT_DESC);

      if (this.isSetSuccess()) {
        oprot.writeFieldBegin(SUCCESS_FIELD_DESC);
        {
          oprot.writeListBegin(new TList(TType.STRUCT, this.success.size()));
          for (superColumn_t _iter58 : this.success)          {
            _iter58.write(oprot);
          }
          oprot.writeListEnd();
        }
        oprot.writeFieldEnd();
      } else if (this.isSetIre()) {
        oprot.writeFieldBegin(IRE_FIELD_DESC);
        this.ire.write(oprot);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

    @Override
    public String toString() {
      StringBuilder sb = new StringBuilder("get_slice_super_by_names_result(");
      boolean first = true;

      sb.append("success:");
      if (this.success == null) {
        sb.append("null");
      } else {
        sb.append(this.success);
      }
      first = false;
      if (!first) sb.append(", ");
      sb.append("ire:");
      if (this.ire == null) {
        sb.append("null");
      } else {
        sb.append(this.ire);
      }
      first = false;
      sb.append(")");
      return sb.toString();
    }

    public void validate() throws TException {
      // check for required fields
      // check that fields of type enum have valid values
    }

  }

  public static class get_superColumn_args implements TBase, java.io.Serializable, Cloneable   {
    private static final TStruct STRUCT_DESC = new TStruct("get_superColumn_args");
    private static final TField TABLENAME_FIELD_DESC = new TField("tablename", TType.STRING, (short)1);
    private static final TField KEY_FIELD_DESC = new TField("key", TType.STRING, (short)2);
    private static final TField SUPER_COLUMN_PATH_FIELD_DESC = new TField("superColumnPath", TType.STRING, (short)3);

    public String tablename;
    public static final int TABLENAME = 1;
    public String key;
    public static final int KEY = 2;
    public String superColumnPath;
    public static final int SUPERCOLUMNPATH = 3;

    private final Isset __isset = new Isset();
    private static final class Isset implements java.io.Serializable {
    }

    public static final Map<Integer, FieldMetaData> metaDataMap = Collections.unmodifiableMap(new HashMap<Integer, FieldMetaData>() {{
      put(TABLENAME, new FieldMetaData("tablename", TFieldRequirementType.DEFAULT, 
          new FieldValueMetaData(TType.STRING)));
      put(KEY, new FieldMetaData("key", TFieldRequirementType.DEFAULT, 
          new FieldValueMetaData(TType.STRING)));
      put(SUPERCOLUMNPATH, new FieldMetaData("superColumnPath", TFieldRequirementType.DEFAULT, 
          new FieldValueMetaData(TType.STRING)));
    }});

    static {
      FieldMetaData.addStructMetaDataMap(get_superColumn_args.class, metaDataMap);
    }

    public get_superColumn_args() {
    }

    public get_superColumn_args(
      String tablename,
      String key,
      String superColumnPath)
    {
      this();
      this.tablename = tablename;
      this.key = key;
      this.superColumnPath = superColumnPath;
    }

    /**
     * Performs a deep copy on <i>other</i>.
     */
    public get_superColumn_args(get_superColumn_args other) {
      if (other.isSetTablename()) {
        this.tablename = other.tablename;
      }
      if (other.isSetKey()) {
        this.key = other.key;
      }
      if (other.isSetSuperColumnPath()) {
        this.superColumnPath = other.superColumnPath;
      }
    }

    @Override
    public get_superColumn_args clone() {
      return new get_superColumn_args(this);
    }

    public String getTablename() {
      return this.tablename;
    }

    public void setTablename(String tablename) {
      this.tablename = tablename;
    }

    public void unsetTablename() {
      this.tablename = null;
    }

    // Returns true if field tablename is set (has been asigned a value) and false otherwise
    public boolean isSetTablename() {
      return this.tablename != null;
    }

    public void setTablenameIsSet(boolean value) {
      if (!value) {
        this.tablename = null;
      }
    }

    public String getKey() {
      return this.key;
    }

    public void setKey(String key) {
      this.key = key;
    }

    public void unsetKey() {
      this.key = null;
    }

    // Returns true if field key is set (has been asigned a value) and false otherwise
    public boolean isSetKey() {
      return this.key != null;
    }

    public void setKeyIsSet(boolean value) {
      if (!value) {
        this.key = null;
      }
    }

    public String getSuperColumnPath() {
      return this.superColumnPath;
    }

    public void setSuperColumnPath(String superColumnPath) {
      this.superColumnPath = superColumnPath;
    }

    public void unsetSuperColumnPath() {
      this.superColumnPath = null;
    }

    // Returns true if field superColumnPath is set (has been asigned a value) and false otherwise
    public boolean isSetSuperColumnPath() {
      return this.superColumnPath != null;
    }

    public void setSuperColumnPathIsSet(boolean value) {
      if (!value) {
        this.superColumnPath = null;
      }
    }

    public void setFieldValue(int fieldID, Object value) {
      switch (fieldID) {
      case TABLENAME:
        if (value == null) {
          unsetTablename();
        } else {
          setTablename((String)value);
        }
        break;

      case KEY:
        if (value == null) {
          unsetKey();
        } else {
          setKey((String)value);
        }
        break;

      case SUPERCOLUMNPATH:
        if (value == null) {
          unsetSuperColumnPath();
        } else {
          setSuperColumnPath((String)value);
        }
        break;

      default:
        throw new IllegalArgumentException("Field " + fieldID + " doesn't exist!");
      }
    }

    public Object getFieldValue(int fieldID) {
      switch (fieldID) {
      case TABLENAME:
        return getTablename();

      case KEY:
        return getKey();

      case SUPERCOLUMNPATH:
        return getSuperColumnPath();

      default:
        throw new IllegalArgumentException("Field " + fieldID + " doesn't exist!");
      }
    }

    // Returns true if field corresponding to fieldID is set (has been asigned a value) and false otherwise
    public boolean isSet(int fieldID) {
      switch (fieldID) {
      case TABLENAME:
        return isSetTablename();
      case KEY:
        return isSetKey();
      case SUPERCOLUMNPATH:
        return isSetSuperColumnPath();
      default:
        throw new IllegalArgumentException("Field " + fieldID + " doesn't exist!");
      }
    }

    @Override
    public boolean equals(Object that) {
      if (that == null)
        return false;
      if (that instanceof get_superColumn_args)
        return this.equals((get_superColumn_args)that);
      return false;
    }

    public boolean equals(get_superColumn_args that) {
      if (that == null)
        return false;

      boolean this_present_tablename = true && this.isSetTablename();
      boolean that_present_tablename = true && that.isSetTablename();
      if (this_present_tablename || that_present_tablename) {
        if (!(this_present_tablename && that_present_tablename))
          return false;
        if (!this.tablename.equals(that.tablename))
          return false;
      }

      boolean this_present_key = true && this.isSetKey();
      boolean that_present_key = true && that.isSetKey();
      if (this_present_key || that_present_key) {
        if (!(this_present_key && that_present_key))
          return false;
        if (!this.key.equals(that.key))
          return false;
      }

      boolean this_present_superColumnPath = true && this.isSetSuperColumnPath();
      boolean that_present_superColumnPath = true && that.isSetSuperColumnPath();
      if (this_present_superColumnPath || that_present_superColumnPath) {
        if (!(this_present_superColumnPath && that_present_superColumnPath))
          return false;
        if (!this.superColumnPath.equals(that.superColumnPath))
          return false;
      }

      return true;
    }

    @Override
    public int hashCode() {
      return 0;
    }

    public void read(TProtocol iprot) throws TException {
      TField field;
      iprot.readStructBegin();
      while (true)
      {
        field = iprot.readFieldBegin();
        if (field.type == TType.STOP) { 
          break;
        }
        switch (field.id)
        {
          case TABLENAME:
            if (field.type == TType.STRING) {
              this.tablename = iprot.readString();
            } else { 
              TProtocolUtil.skip(iprot, field.type);
            }
            break;
          case KEY:
            if (field.type == TType.STRING) {
              this.key = iprot.readString();
            } else { 
              TProtocolUtil.skip(iprot, field.type);
            }
            break;
          case SUPERCOLUMNPATH:
            if (field.type == TType.STRING) {
              this.superColumnPath = iprot.readString();
            } else { 
              TProtocolUtil.skip(iprot, field.type);
            }
            break;
          default:
            TProtocolUtil.skip(iprot, field.type);
            break;
        }
        iprot.readFieldEnd();
      }
      iprot.readStructEnd();


      // check for required fields of primitive type, which can't be checked in the validate method
      validate();
    }

    public void write(TProtocol oprot) throws TException {
      validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (this.tablename != null) {
        oprot.writeFieldBegin(TABLENAME_FIELD_DESC);
        oprot.writeString(this.tablename);
        oprot.writeFieldEnd();
      }
      if (this.key != null) {
        oprot.writeFieldBegin(KEY_FIELD_DESC);
        oprot.writeString(this.key);
        oprot.writeFieldEnd();
      }
      if (this.superColumnPath != null) {
        oprot.writeFieldBegin(SUPER_COLUMN_PATH_FIELD_DESC);
        oprot.writeString(this.superColumnPath);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

    @Override
    public String toString() {
      StringBuilder sb = new StringBuilder("get_superColumn_args(");
      boolean first = true;

      sb.append("tablename:");
      if (this.tablename == null) {
        sb.append("null");
      } else {
        sb.append(this.tablename);
      }
      first = false;
      if (!first) sb.append(", ");
      sb.append("key:");
      if (this.key == null) {
        sb.append("null");
      } else {
        sb.append(this.key);
      }
      first = false;
      if (!first) sb.append(", ");
      sb.append("superColumnPath:");
      if (this.superColumnPath == null) {
        sb.append("null");
      } else {
        sb.append(this.superColumnPath);
      }
      first = false;
      sb.append(")");
      return sb.toString();
    }

    public void validate() throws TException {
      // check for required fields
      // check that fields of type enum have valid values
    }

  }

  public static class get_superColumn_result implements TBase, java.io.Serializable, Cloneable   {
    private static final TStruct STRUCT_DESC = new TStruct("get_superColumn_result");
    private static final TField SUCCESS_FIELD_DESC = new TField("success", TType.STRUCT, (short)0);
    private static final TField IRE_FIELD_DESC = new TField("ire", TType.STRUCT, (short)1);
    private static final TField NFE_FIELD_DESC = new TField("nfe", TType.STRUCT, (short)2);

    public superColumn_t success;
    public static final int SUCCESS = 0;
    public InvalidRequestException ire;
    public static final int IRE = 1;
    public NotFoundException nfe;
    public static final int NFE = 2;

    private final Isset __isset = new Isset();
    private static final class Isset implements java.io.Serializable {
    }

    public static final Map<Integer, FieldMetaData> metaDataMap = Collections.unmodifiableMap(new HashMap<Integer, FieldMetaData>() {{
      put(SUCCESS, new FieldMetaData("success", TFieldRequirementType.DEFAULT, 
          new StructMetaData(TType.STRUCT, superColumn_t.class)));
      put(IRE, new FieldMetaData("ire", TFieldRequirementType.DEFAULT, 
          new FieldValueMetaData(TType.STRUCT)));
      put(NFE, new FieldMetaData("nfe", TFieldRequirementType.DEFAULT, 
          new FieldValueMetaData(TType.STRUCT)));
    }});

    static {
      FieldMetaData.addStructMetaDataMap(get_superColumn_result.class, metaDataMap);
    }

    public get_superColumn_result() {
    }

    public get_superColumn_result(
      superColumn_t success,
      InvalidRequestException ire,
      NotFoundException nfe)
    {
      this();
      this.success = success;
      this.ire = ire;
      this.nfe = nfe;
    }

    /**
     * Performs a deep copy on <i>other</i>.
     */
    public get_superColumn_result(get_superColumn_result other) {
      if (other.isSetSuccess()) {
        this.success = new superColumn_t(other.success);
      }
      if (other.isSetIre()) {
        this.ire = new InvalidRequestException(other.ire);
      }
      if (other.isSetNfe()) {
        this.nfe = new NotFoundException(other.nfe);
      }
    }

    @Override
    public get_superColumn_result clone() {
      return new get_superColumn_result(this);
    }

    public superColumn_t getSuccess() {
      return this.success;
    }

    public void setSuccess(superColumn_t success) {
      this.success = success;
    }

    public void unsetSuccess() {
      this.success = null;
    }

    // Returns true if field success is set (has been asigned a value) and false otherwise
    public boolean isSetSuccess() {
      return this.success != null;
    }

    public void setSuccessIsSet(boolean value) {
      if (!value) {
        this.success = null;
      }
    }

    public InvalidRequestException getIre() {
      return this.ire;
    }

    public void setIre(InvalidRequestException ire) {
      this.ire = ire;
    }

    public void unsetIre() {
      this.ire = null;
    }

    // Returns true if field ire is set (has been asigned a value) and false otherwise
    public boolean isSetIre() {
      return this.ire != null;
    }

    public void setIreIsSet(boolean value) {
      if (!value) {
        this.ire = null;
      }
    }

    public NotFoundException getNfe() {
      return this.nfe;
    }

    public void setNfe(NotFoundException nfe) {
      this.nfe = nfe;
    }

    public void unsetNfe() {
      this.nfe = null;
    }

    // Returns true if field nfe is set (has been asigned a value) and false otherwise
    public boolean isSetNfe() {
      return this.nfe != null;
    }

    public void setNfeIsSet(boolean value) {
      if (!value) {
        this.nfe = null;
      }
    }

    public void setFieldValue(int fieldID, Object value) {
      switch (fieldID) {
      case SUCCESS:
        if (value == null) {
          unsetSuccess();
        } else {
          setSuccess((superColumn_t)value);
        }
        break;

      case IRE:
        if (value == null) {
          unsetIre();
        } else {
          setIre((InvalidRequestException)value);
        }
        break;

      case NFE:
        if (value == null) {
          unsetNfe();
        } else {
          setNfe((NotFoundException)value);
        }
        break;

      default:
        throw new IllegalArgumentException("Field " + fieldID + " doesn't exist!");
      }
    }

    public Object getFieldValue(int fieldID) {
      switch (fieldID) {
      case SUCCESS:
        return getSuccess();

      case IRE:
        return getIre();

      case NFE:
        return getNfe();

      default:
        throw new IllegalArgumentException("Field " + fieldID + " doesn't exist!");
      }
    }

    // Returns true if field corresponding to fieldID is set (has been asigned a value) and false otherwise
    public boolean isSet(int fieldID) {
      switch (fieldID) {
      case SUCCESS:
        return isSetSuccess();
      case IRE:
        return isSetIre();
      case NFE:
        return isSetNfe();
      default:
        throw new IllegalArgumentException("Field " + fieldID + " doesn't exist!");
      }
    }

    @Override
    public boolean equals(Object that) {
      if (that == null)
        return false;
      if (that instanceof get_superColumn_result)
        return this.equals((get_superColumn_result)that);
      return false;
    }

    public boolean equals(get_superColumn_result that) {
      if (that == null)
        return false;

      boolean this_present_success = true && this.isSetSuccess();
      boolean that_present_success = true && that.isSetSuccess();
      if (this_present_success || that_present_success) {
        if (!(this_present_success && that_present_success))
          return false;
        if (!this.success.equals(that.success))
          return false;
      }

      boolean this_present_ire = true && this.isSetIre();
      boolean that_present_ire = true && that.isSetIre();
      if (this_present_ire || that_present_ire) {
        if (!(this_present_ire && that_present_ire))
          return false;
        if (!this.ire.equals(that.ire))
          return false;
      }

      boolean this_present_nfe = true && this.isSetNfe();
      boolean that_present_nfe = true && that.isSetNfe();
      if (this_present_nfe || that_present_nfe) {
        if (!(this_present_nfe && that_present_nfe))
          return false;
        if (!this.nfe.equals(that.nfe))
          return false;
      }

      return true;
    }

    @Override
    public int hashCode() {
      return 0;
    }

    public void read(TProtocol iprot) throws TException {
      TField field;
      iprot.readStructBegin();
      while (true)
      {
        field = iprot.readFieldBegin();
        if (field.type == TType.STOP) { 
          break;
        }
        switch (field.id)
        {
          case SUCCESS:
            if (field.type == TType.STRUCT) {
              this.success = new superColumn_t();
              this.success.read(iprot);
            } else { 
              TProtocolUtil.skip(iprot, field.type);
            }
            break;
          case IRE:
            if (field.type == TType.STRUCT) {
              this.ire = new InvalidRequestException();
              this.ire.read(iprot);
            } else { 
              TProtocolUtil.skip(iprot, field.type);
            }
            break;
          case NFE:
            if (field.type == TType.STRUCT) {
              this.nfe = new NotFoundException();
              this.nfe.read(iprot);
            } else { 
              TProtocolUtil.skip(iprot, field.type);
            }
            break;
          default:
            TProtocolUtil.skip(iprot, field.type);
            break;
        }
        iprot.readFieldEnd();
      }
      iprot.readStructEnd();


      // check for required fields of primitive type, which can't be checked in the validate method
      validate();
    }

    public void write(TProtocol oprot) throws TException {
      oprot.writeStructBegin(STRUCT_DESC);

      if (this.isSetSuccess()) {
        oprot.writeFieldBegin(SUCCESS_FIELD_DESC);
        this.success.write(oprot);
        oprot.writeFieldEnd();
      } else if (this.isSetIre()) {
        oprot.writeFieldBegin(IRE_FIELD_DESC);
        this.ire.write(oprot);
        oprot.writeFieldEnd();
      } else if (this.isSetNfe()) {
        oprot.writeFieldBegin(NFE_FIELD_DESC);
        this.nfe.write(oprot);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

    @Override
    public String toString() {
      StringBuilder sb = new StringBuilder("get_superColumn_result(");
      boolean first = true;

      sb.append("success:");
      if (this.success == null) {
        sb.append("null");
      } else {
        sb.append(this.success);
      }
      first = false;
      if (!first) sb.append(", ");
      sb.append("ire:");
      if (this.ire == null) {
        sb.append("null");
      } else {
        sb.append(this.ire);
      }
      first = false;
      if (!first) sb.append(", ");
      sb.append("nfe:");
      if (this.nfe == null) {
        sb.append("null");
      } else {
        sb.append(this.nfe);
      }
      first = false;
      sb.append(")");
      return sb.toString();
    }

    public void validate() throws TException {
      // check for required fields
      // check that fields of type enum have valid values
    }

  }

  public static class batch_insert_superColumn_args implements TBase, java.io.Serializable, Cloneable   {
    private static final TStruct STRUCT_DESC = new TStruct("batch_insert_superColumn_args");
    private static final TField BATCH_MUTATION_SUPER_FIELD_DESC = new TField("batchMutationSuper", TType.STRUCT, (short)1);
    private static final TField BLOCK_FIELD_DESC = new TField("block", TType.BOOL, (short)2);

    public batch_mutation_super_t batchMutationSuper;
    public static final int BATCHMUTATIONSUPER = 1;
    public boolean block;
    public static final int BLOCK = 2;

    private final Isset __isset = new Isset();
    private static final class Isset implements java.io.Serializable {
      public boolean block = false;
    }

    public static final Map<Integer, FieldMetaData> metaDataMap = Collections.unmodifiableMap(new HashMap<Integer, FieldMetaData>() {{
      put(BATCHMUTATIONSUPER, new FieldMetaData("batchMutationSuper", TFieldRequirementType.DEFAULT, 
          new StructMetaData(TType.STRUCT, batch_mutation_super_t.class)));
      put(BLOCK, new FieldMetaData("block", TFieldRequirementType.DEFAULT, 
          new FieldValueMetaData(TType.BOOL)));
    }});

    static {
      FieldMetaData.addStructMetaDataMap(batch_insert_superColumn_args.class, metaDataMap);
    }

    public batch_insert_superColumn_args() {
      this.block = false;

    }

    public batch_insert_superColumn_args(
      batch_mutation_super_t batchMutationSuper,
      boolean block)
    {
      this();
      this.batchMutationSuper = batchMutationSuper;
      this.block = block;
      this.__isset.block = true;
    }

    /**
     * Performs a deep copy on <i>other</i>.
     */
    public batch_insert_superColumn_args(batch_insert_superColumn_args other) {
      if (other.isSetBatchMutationSuper()) {
        this.batchMutationSuper = new batch_mutation_super_t(other.batchMutationSuper);
      }
      __isset.block = other.__isset.block;
      this.block = other.block;
    }

    @Override
    public batch_insert_superColumn_args clone() {
      return new batch_insert_superColumn_args(this);
    }

    public batch_mutation_super_t getBatchMutationSuper() {
      return this.batchMutationSuper;
    }

    public void setBatchMutationSuper(batch_mutation_super_t batchMutationSuper) {
      this.batchMutationSuper = batchMutationSuper;
    }

    public void unsetBatchMutationSuper() {
      this.batchMutationSuper = null;
    }

    // Returns true if field batchMutationSuper is set (has been asigned a value) and false otherwise
    public boolean isSetBatchMutationSuper() {
      return this.batchMutationSuper != null;
    }

    public void setBatchMutationSuperIsSet(boolean value) {
      if (!value) {
        this.batchMutationSuper = null;
      }
    }

    public boolean isBlock() {
      return this.block;
    }

    public void setBlock(boolean block) {
      this.block = block;
      this.__isset.block = true;
    }

    public void unsetBlock() {
      this.__isset.block = false;
    }

    // Returns true if field block is set (has been asigned a value) and false otherwise
    public boolean isSetBlock() {
      return this.__isset.block;
    }

    public void setBlockIsSet(boolean value) {
      this.__isset.block = value;
    }

    public void setFieldValue(int fieldID, Object value) {
      switch (fieldID) {
      case BATCHMUTATIONSUPER:
        if (value == null) {
          unsetBatchMutationSuper();
        } else {
          setBatchMutationSuper((batch_mutation_super_t)value);
        }
        break;

      case BLOCK:
        if (value == null) {
          unsetBlock();
        } else {
          setBlock((Boolean)value);
        }
        break;

      default:
        throw new IllegalArgumentException("Field " + fieldID + " doesn't exist!");
      }
    }

    public Object getFieldValue(int fieldID) {
      switch (fieldID) {
      case BATCHMUTATIONSUPER:
        return getBatchMutationSuper();

      case BLOCK:
        return new Boolean(isBlock());

      default:
        throw new IllegalArgumentException("Field " + fieldID + " doesn't exist!");
      }
    }

    // Returns true if field corresponding to fieldID is set (has been asigned a value) and false otherwise
    public boolean isSet(int fieldID) {
      switch (fieldID) {
      case BATCHMUTATIONSUPER:
        return isSetBatchMutationSuper();
      case BLOCK:
        return isSetBlock();
      default:
        throw new IllegalArgumentException("Field " + fieldID + " doesn't exist!");
      }
    }

    @Override
    public boolean equals(Object that) {
      if (that == null)
        return false;
      if (that instanceof batch_insert_superColumn_args)
        return this.equals((batch_insert_superColumn_args)that);
      return false;
    }

    public boolean equals(batch_insert_superColumn_args that) {
      if (that == null)
        return false;

      boolean this_present_batchMutationSuper = true && this.isSetBatchMutationSuper();
      boolean that_present_batchMutationSuper = true && that.isSetBatchMutationSuper();
      if (this_present_batchMutationSuper || that_present_batchMutationSuper) {
        if (!(this_present_batchMutationSuper && that_present_batchMutationSuper))
          return false;
        if (!this.batchMutationSuper.equals(that.batchMutationSuper))
          return false;
      }

      boolean this_present_block = true;
      boolean that_present_block = true;
      if (this_present_block || that_present_block) {
        if (!(this_present_block && that_present_block))
          return false;
        if (this.block != that.block)
          return false;
      }

      return true;
    }

    @Override
    public int hashCode() {
      return 0;
    }

    public void read(TProtocol iprot) throws TException {
      TField field;
      iprot.readStructBegin();
      while (true)
      {
        field = iprot.readFieldBegin();
        if (field.type == TType.STOP) { 
          break;
        }
        switch (field.id)
        {
          case BATCHMUTATIONSUPER:
            if (field.type == TType.STRUCT) {
              this.batchMutationSuper = new batch_mutation_super_t();
              this.batchMutationSuper.read(iprot);
            } else { 
              TProtocolUtil.skip(iprot, field.type);
            }
            break;
          case BLOCK:
            if (field.type == TType.BOOL) {
              this.block = iprot.readBool();
              this.__isset.block = true;
            } else { 
              TProtocolUtil.skip(iprot, field.type);
            }
            break;
          default:
            TProtocolUtil.skip(iprot, field.type);
            break;
        }
        iprot.readFieldEnd();
      }
      iprot.readStructEnd();


      // check for required fields of primitive type, which can't be checked in the validate method
      validate();
    }

    public void write(TProtocol oprot) throws TException {
      validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (this.batchMutationSuper != null) {
        oprot.writeFieldBegin(BATCH_MUTATION_SUPER_FIELD_DESC);
        this.batchMutationSuper.write(oprot);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldBegin(BLOCK_FIELD_DESC);
      oprot.writeBool(this.block);
      oprot.writeFieldEnd();
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

    @Override
    public String toString() {
      StringBuilder sb = new StringBuilder("batch_insert_superColumn_args(");
      boolean first = true;

      sb.append("batchMutationSuper:");
      if (this.batchMutationSuper == null) {
        sb.append("null");
      } else {
        sb.append(this.batchMutationSuper);
      }
      first = false;
      if (!first) sb.append(", ");
      sb.append("block:");
      sb.append(this.block);
      first = false;
      sb.append(")");
      return sb.toString();
    }

    public void validate() throws TException {
      // check for required fields
      // check that fields of type enum have valid values
    }

  }

  public static class batch_insert_superColumn_result implements TBase, java.io.Serializable, Cloneable   {
    private static final TStruct STRUCT_DESC = new TStruct("batch_insert_superColumn_result");
    private static final TField IRE_FIELD_DESC = new TField("ire", TType.STRUCT, (short)1);
    private static final TField UE_FIELD_DESC = new TField("ue", TType.STRUCT, (short)2);

    public InvalidRequestException ire;
    public static final int IRE = 1;
    public UnavailableException ue;
    public static final int UE = 2;

    private final Isset __isset = new Isset();
    private static final class Isset implements java.io.Serializable {
    }

    public static final Map<Integer, FieldMetaData> metaDataMap = Collections.unmodifiableMap(new HashMap<Integer, FieldMetaData>() {{
      put(IRE, new FieldMetaData("ire", TFieldRequirementType.DEFAULT, 
          new FieldValueMetaData(TType.STRUCT)));
      put(UE, new FieldMetaData("ue", TFieldRequirementType.DEFAULT, 
          new FieldValueMetaData(TType.STRUCT)));
    }});

    static {
      FieldMetaData.addStructMetaDataMap(batch_insert_superColumn_result.class, metaDataMap);
    }

    public batch_insert_superColumn_result() {
    }

    public batch_insert_superColumn_result(
      InvalidRequestException ire,
      UnavailableException ue)
    {
      this();
      this.ire = ire;
      this.ue = ue;
    }

    /**
     * Performs a deep copy on <i>other</i>.
     */
    public batch_insert_superColumn_result(batch_insert_superColumn_result other) {
      if (other.isSetIre()) {
        this.ire = new InvalidRequestException(other.ire);
      }
      if (other.isSetUe()) {
        this.ue = new UnavailableException(other.ue);
      }
    }

    @Override
    public batch_insert_superColumn_result clone() {
      return new batch_insert_superColumn_result(this);
    }

    public InvalidRequestException getIre() {
      return this.ire;
    }

    public void setIre(InvalidRequestException ire) {
      this.ire = ire;
    }

    public void unsetIre() {
      this.ire = null;
    }

    // Returns true if field ire is set (has been asigned a value) and false otherwise
    public boolean isSetIre() {
      return this.ire != null;
    }

    public void setIreIsSet(boolean value) {
      if (!value) {
        this.ire = null;
      }
    }

    public UnavailableException getUe() {
      return this.ue;
    }

    public void setUe(UnavailableException ue) {
      this.ue = ue;
    }

    public void unsetUe() {
      this.ue = null;
    }

    // Returns true if field ue is set (has been asigned a value) and false otherwise
    public boolean isSetUe() {
      return this.ue != null;
    }

    public void setUeIsSet(boolean value) {
      if (!value) {
        this.ue = null;
      }
    }

    public void setFieldValue(int fieldID, Object value) {
      switch (fieldID) {
      case IRE:
        if (value == null) {
          unsetIre();
        } else {
          setIre((InvalidRequestException)value);
        }
        break;

      case UE:
        if (value == null) {
          unsetUe();
        } else {
          setUe((UnavailableException)value);
        }
        break;

      default:
        throw new IllegalArgumentException("Field " + fieldID + " doesn't exist!");
      }
    }

    public Object getFieldValue(int fieldID) {
      switch (fieldID) {
      case IRE:
        return getIre();

      case UE:
        return getUe();

      default:
        throw new IllegalArgumentException("Field " + fieldID + " doesn't exist!");
      }
    }

    // Returns true if field corresponding to fieldID is set (has been asigned a value) and false otherwise
    public boolean isSet(int fieldID) {
      switch (fieldID) {
      case IRE:
        return isSetIre();
      case UE:
        return isSetUe();
      default:
        throw new IllegalArgumentException("Field " + fieldID + " doesn't exist!");
      }
    }

    @Override
    public boolean equals(Object that) {
      if (that == null)
        return false;
      if (that instanceof batch_insert_superColumn_result)
        return this.equals((batch_insert_superColumn_result)that);
      return false;
    }

    public boolean equals(batch_insert_superColumn_result that) {
      if (that == null)
        return false;

      boolean this_present_ire = true && this.isSetIre();
      boolean that_present_ire = true && that.isSetIre();
      if (this_present_ire || that_present_ire) {
        if (!(this_present_ire && that_present_ire))
          return false;
        if (!this.ire.equals(that.ire))
          return false;
      }

      boolean this_present_ue = true && this.isSetUe();
      boolean that_present_ue = true && that.isSetUe();
      if (this_present_ue || that_present_ue) {
        if (!(this_present_ue && that_present_ue))
          return false;
        if (!this.ue.equals(that.ue))
          return false;
      }

      return true;
    }

    @Override
    public int hashCode() {
      return 0;
    }

    public void read(TProtocol iprot) throws TException {
      TField field;
      iprot.readStructBegin();
      while (true)
      {
        field = iprot.readFieldBegin();
        if (field.type == TType.STOP) { 
          break;
        }
        switch (field.id)
        {
          case IRE:
            if (field.type == TType.STRUCT) {
              this.ire = new InvalidRequestException();
              this.ire.read(iprot);
            } else { 
              TProtocolUtil.skip(iprot, field.type);
            }
            break;
          case UE:
            if (field.type == TType.STRUCT) {
              this.ue = new UnavailableException();
              this.ue.read(iprot);
            } else { 
              TProtocolUtil.skip(iprot, field.type);
            }
            break;
          default:
            TProtocolUtil.skip(iprot, field.type);
            break;
        }
        iprot.readFieldEnd();
      }
      iprot.readStructEnd();


      // check for required fields of primitive type, which can't be checked in the validate method
      validate();
    }

    public void write(TProtocol oprot) throws TException {
      oprot.writeStructBegin(STRUCT_DESC);

      if (this.isSetIre()) {
        oprot.writeFieldBegin(IRE_FIELD_DESC);
        this.ire.write(oprot);
        oprot.writeFieldEnd();
      } else if (this.isSetUe()) {
        oprot.writeFieldBegin(UE_FIELD_DESC);
        this.ue.write(oprot);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

    @Override
    public String toString() {
      StringBuilder sb = new StringBuilder("batch_insert_superColumn_result(");
      boolean first = true;

      sb.append("ire:");
      if (this.ire == null) {
        sb.append("null");
      } else {
        sb.append(this.ire);
      }
      first = false;
      if (!first) sb.append(", ");
      sb.append("ue:");
      if (this.ue == null) {
        sb.append("null");
      } else {
        sb.append(this.ue);
      }
      first = false;
      sb.append(")");
      return sb.toString();
    }

    public void validate() throws TException {
      // check for required fields
      // check that fields of type enum have valid values
    }

  }

  public static class get_key_range_args implements TBase, java.io.Serializable, Cloneable   {
    private static final TStruct STRUCT_DESC = new TStruct("get_key_range_args");
    private static final TField TABLENAME_FIELD_DESC = new TField("tablename", TType.STRING, (short)1);
    private static final TField START_WITH_FIELD_DESC = new TField("startWith", TType.STRING, (short)2);
    private static final TField STOP_AT_FIELD_DESC = new TField("stopAt", TType.STRING, (short)3);
    private static final TField MAX_RESULTS_FIELD_DESC = new TField("maxResults", TType.I32, (short)4);

    public String tablename;
    public static final int TABLENAME = 1;
    public String startWith;
    public static final int STARTWITH = 2;
    public String stopAt;
    public static final int STOPAT = 3;
    public int maxResults;
    public static final int MAXRESULTS = 4;

    private final Isset __isset = new Isset();
    private static final class Isset implements java.io.Serializable {
      public boolean maxResults = false;
    }

    public static final Map<Integer, FieldMetaData> metaDataMap = Collections.unmodifiableMap(new HashMap<Integer, FieldMetaData>() {{
      put(TABLENAME, new FieldMetaData("tablename", TFieldRequirementType.DEFAULT, 
          new FieldValueMetaData(TType.STRING)));
      put(STARTWITH, new FieldMetaData("startWith", TFieldRequirementType.DEFAULT, 
          new FieldValueMetaData(TType.STRING)));
      put(STOPAT, new FieldMetaData("stopAt", TFieldRequirementType.DEFAULT, 
          new FieldValueMetaData(TType.STRING)));
      put(MAXRESULTS, new FieldMetaData("maxResults", TFieldRequirementType.DEFAULT, 
          new FieldValueMetaData(TType.I32)));
    }});

    static {
      FieldMetaData.addStructMetaDataMap(get_key_range_args.class, metaDataMap);
    }

    public get_key_range_args() {
      this.startWith = "";

      this.stopAt = "";

      this.maxResults = 1000;

    }

    public get_key_range_args(
      String tablename,
      String startWith,
      String stopAt,
      int maxResults)
    {
      this();
      this.tablename = tablename;
      this.startWith = startWith;
      this.stopAt = stopAt;
      this.maxResults = maxResults;
      this.__isset.maxResults = true;
    }

    /**
     * Performs a deep copy on <i>other</i>.
     */
    public get_key_range_args(get_key_range_args other) {
      if (other.isSetTablename()) {
        this.tablename = other.tablename;
      }
      if (other.isSetStartWith()) {
        this.startWith = other.startWith;
      }
      if (other.isSetStopAt()) {
        this.stopAt = other.stopAt;
      }
      __isset.maxResults = other.__isset.maxResults;
      this.maxResults = other.maxResults;
    }

    @Override
    public get_key_range_args clone() {
      return new get_key_range_args(this);
    }

    public String getTablename() {
      return this.tablename;
    }

    public void setTablename(String tablename) {
      this.tablename = tablename;
    }

    public void unsetTablename() {
      this.tablename = null;
    }

    // Returns true if field tablename is set (has been asigned a value) and false otherwise
    public boolean isSetTablename() {
      return this.tablename != null;
    }

    public void setTablenameIsSet(boolean value) {
      if (!value) {
        this.tablename = null;
      }
    }

    public String getStartWith() {
      return this.startWith;
    }

    public void setStartWith(String startWith) {
      this.startWith = startWith;
    }

    public void unsetStartWith() {
      this.startWith = null;
    }

    // Returns true if field startWith is set (has been asigned a value) and false otherwise
    public boolean isSetStartWith() {
      return this.startWith != null;
    }

    public void setStartWithIsSet(boolean value) {
      if (!value) {
        this.startWith = null;
      }
    }

    public String getStopAt() {
      return this.stopAt;
    }

    public void setStopAt(String stopAt) {
      this.stopAt = stopAt;
    }

    public void unsetStopAt() {
      this.stopAt = null;
    }

    // Returns true if field stopAt is set (has been asigned a value) and false otherwise
    public boolean isSetStopAt() {
      return this.stopAt != null;
    }

    public void setStopAtIsSet(boolean value) {
      if (!value) {
        this.stopAt = null;
      }
    }

    public int getMaxResults() {
      return this.maxResults;
    }

    public void setMaxResults(int maxResults) {
      this.maxResults = maxResults;
      this.__isset.maxResults = true;
    }

    public void unsetMaxResults() {
      this.__isset.maxResults = false;
    }

    // Returns true if field maxResults is set (has been asigned a value) and false otherwise
    public boolean isSetMaxResults() {
      return this.__isset.maxResults;
    }

    public void setMaxResultsIsSet(boolean value) {
      this.__isset.maxResults = value;
    }

    public void setFieldValue(int fieldID, Object value) {
      switch (fieldID) {
      case TABLENAME:
        if (value == null) {
          unsetTablename();
        } else {
          setTablename((String)value);
        }
        break;

      case STARTWITH:
        if (value == null) {
          unsetStartWith();
        } else {
          setStartWith((String)value);
        }
        break;

      case STOPAT:
        if (value == null) {
          unsetStopAt();
        } else {
          setStopAt((String)value);
        }
        break;

      case MAXRESULTS:
        if (value == null) {
          unsetMaxResults();
        } else {
          setMaxResults((Integer)value);
        }
        break;

      default:
        throw new IllegalArgumentException("Field " + fieldID + " doesn't exist!");
      }
    }

    public Object getFieldValue(int fieldID) {
      switch (fieldID) {
      case TABLENAME:
        return getTablename();

      case STARTWITH:
        return getStartWith();

      case STOPAT:
        return getStopAt();

      case MAXRESULTS:
        return new Integer(getMaxResults());

      default:
        throw new IllegalArgumentException("Field " + fieldID + " doesn't exist!");
      }
    }

    // Returns true if field corresponding to fieldID is set (has been asigned a value) and false otherwise
    public boolean isSet(int fieldID) {
      switch (fieldID) {
      case TABLENAME:
        return isSetTablename();
      case STARTWITH:
        return isSetStartWith();
      case STOPAT:
        return isSetStopAt();
      case MAXRESULTS:
        return isSetMaxResults();
      default:
        throw new IllegalArgumentException("Field " + fieldID + " doesn't exist!");
      }
    }

    @Override
    public boolean equals(Object that) {
      if (that == null)
        return false;
      if (that instanceof get_key_range_args)
        return this.equals((get_key_range_args)that);
      return false;
    }

    public boolean equals(get_key_range_args that) {
      if (that == null)
        return false;

      boolean this_present_tablename = true && this.isSetTablename();
      boolean that_present_tablename = true && that.isSetTablename();
      if (this_present_tablename || that_present_tablename) {
        if (!(this_present_tablename && that_present_tablename))
          return false;
        if (!this.tablename.equals(that.tablename))
          return false;
      }

      boolean this_present_startWith = true && this.isSetStartWith();
      boolean that_present_startWith = true && that.isSetStartWith();
      if (this_present_startWith || that_present_startWith) {
        if (!(this_present_startWith && that_present_startWith))
          return false;
        if (!this.startWith.equals(that.startWith))
          return false;
      }

      boolean this_present_stopAt = true && this.isSetStopAt();
      boolean that_present_stopAt = true && that.isSetStopAt();
      if (this_present_stopAt || that_present_stopAt) {
        if (!(this_present_stopAt && that_present_stopAt))
          return false;
        if (!this.stopAt.equals(that.stopAt))
          return false;
      }

      boolean this_present_maxResults = true;
      boolean that_present_maxResults = true;
      if (this_present_maxResults || that_present_maxResults) {
        if (!(this_present_maxResults && that_present_maxResults))
          return false;
        if (this.maxResults != that.maxResults)
          return false;
      }

      return true;
    }

    @Override
    public int hashCode() {
      return 0;
    }

    public void read(TProtocol iprot) throws TException {
      TField field;
      iprot.readStructBegin();
      while (true)
      {
        field = iprot.readFieldBegin();
        if (field.type == TType.STOP) { 
          break;
        }
        switch (field.id)
        {
          case TABLENAME:
            if (field.type == TType.STRING) {
              this.tablename = iprot.readString();
            } else { 
              TProtocolUtil.skip(iprot, field.type);
            }
            break;
          case STARTWITH:
            if (field.type == TType.STRING) {
              this.startWith = iprot.readString();
            } else { 
              TProtocolUtil.skip(iprot, field.type);
            }
            break;
          case STOPAT:
            if (field.type == TType.STRING) {
              this.stopAt = iprot.readString();
            } else { 
              TProtocolUtil.skip(iprot, field.type);
            }
            break;
          case MAXRESULTS:
            if (field.type == TType.I32) {
              this.maxResults = iprot.readI32();
              this.__isset.maxResults = true;
            } else { 
              TProtocolUtil.skip(iprot, field.type);
            }
            break;
          default:
            TProtocolUtil.skip(iprot, field.type);
            break;
        }
        iprot.readFieldEnd();
      }
      iprot.readStructEnd();


      // check for required fields of primitive type, which can't be checked in the validate method
      validate();
    }

    public void write(TProtocol oprot) throws TException {
      validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (this.tablename != null) {
        oprot.writeFieldBegin(TABLENAME_FIELD_DESC);
        oprot.writeString(this.tablename);
        oprot.writeFieldEnd();
      }
      if (this.startWith != null) {
        oprot.writeFieldBegin(START_WITH_FIELD_DESC);
        oprot.writeString(this.startWith);
        oprot.writeFieldEnd();
      }
      if (this.stopAt != null) {
        oprot.writeFieldBegin(STOP_AT_FIELD_DESC);
        oprot.writeString(this.stopAt);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldBegin(MAX_RESULTS_FIELD_DESC);
      oprot.writeI32(this.maxResults);
      oprot.writeFieldEnd();
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

    @Override
    public String toString() {
      StringBuilder sb = new StringBuilder("get_key_range_args(");
      boolean first = true;

      sb.append("tablename:");
      if (this.tablename == null) {
        sb.append("null");
      } else {
        sb.append(this.tablename);
      }
      first = false;
      if (!first) sb.append(", ");
      sb.append("startWith:");
      if (this.startWith == null) {
        sb.append("null");
      } else {
        sb.append(this.startWith);
      }
      first = false;
      if (!first) sb.append(", ");
      sb.append("stopAt:");
      if (this.stopAt == null) {
        sb.append("null");
      } else {
        sb.append(this.stopAt);
      }
      first = false;
      if (!first) sb.append(", ");
      sb.append("maxResults:");
      sb.append(this.maxResults);
      first = false;
      sb.append(")");
      return sb.toString();
    }

    public void validate() throws TException {
      // check for required fields
      // check that fields of type enum have valid values
    }

  }

  public static class get_key_range_result implements TBase, java.io.Serializable, Cloneable   {
    private static final TStruct STRUCT_DESC = new TStruct("get_key_range_result");
    private static final TField SUCCESS_FIELD_DESC = new TField("success", TType.LIST, (short)0);
    private static final TField IRE_FIELD_DESC = new TField("ire", TType.STRUCT, (short)1);

    public List<String> success;
    public static final int SUCCESS = 0;
    public InvalidRequestException ire;
    public static final int IRE = 1;

    private final Isset __isset = new Isset();
    private static final class Isset implements java.io.Serializable {
    }

    public static final Map<Integer, FieldMetaData> metaDataMap = Collections.unmodifiableMap(new HashMap<Integer, FieldMetaData>() {{
      put(SUCCESS, new FieldMetaData("success", TFieldRequirementType.DEFAULT, 
          new ListMetaData(TType.LIST, 
              new FieldValueMetaData(TType.STRING))));
      put(IRE, new FieldMetaData("ire", TFieldRequirementType.DEFAULT, 
          new FieldValueMetaData(TType.STRUCT)));
    }});

    static {
      FieldMetaData.addStructMetaDataMap(get_key_range_result.class, metaDataMap);
    }

    public get_key_range_result() {
    }

    public get_key_range_result(
      List<String> success,
      InvalidRequestException ire)
    {
      this();
      this.success = success;
      this.ire = ire;
    }

    /**
     * Performs a deep copy on <i>other</i>.
     */
    public get_key_range_result(get_key_range_result other) {
      if (other.isSetSuccess()) {
        List<String> __this__success = new ArrayList<String>();
        for (String other_element : other.success) {
          __this__success.add(other_element);
        }
        this.success = __this__success;
      }
      if (other.isSetIre()) {
        this.ire = new InvalidRequestException(other.ire);
      }
    }

    @Override
    public get_key_range_result clone() {
      return new get_key_range_result(this);
    }

    public int getSuccessSize() {
      return (this.success == null) ? 0 : this.success.size();
    }

    public java.util.Iterator<String> getSuccessIterator() {
      return (this.success == null) ? null : this.success.iterator();
    }

    public void addToSuccess(String elem) {
      if (this.success == null) {
        this.success = new ArrayList<String>();
      }
      this.success.add(elem);
    }

    public List<String> getSuccess() {
      return this.success;
    }

    public void setSuccess(List<String> success) {
      this.success = success;
    }

    public void unsetSuccess() {
      this.success = null;
    }

    // Returns true if field success is set (has been asigned a value) and false otherwise
    public boolean isSetSuccess() {
      return this.success != null;
    }

    public void setSuccessIsSet(boolean value) {
      if (!value) {
        this.success = null;
      }
    }

    public InvalidRequestException getIre() {
      return this.ire;
    }

    public void setIre(InvalidRequestException ire) {
      this.ire = ire;
    }

    public void unsetIre() {
      this.ire = null;
    }

    // Returns true if field ire is set (has been asigned a value) and false otherwise
    public boolean isSetIre() {
      return this.ire != null;
    }

    public void setIreIsSet(boolean value) {
      if (!value) {
        this.ire = null;
      }
    }

    public void setFieldValue(int fieldID, Object value) {
      switch (fieldID) {
      case SUCCESS:
        if (value == null) {
          unsetSuccess();
        } else {
          setSuccess((List<String>)value);
        }
        break;

      case IRE:
        if (value == null) {
          unsetIre();
        } else {
          setIre((InvalidRequestException)value);
        }
        break;

      default:
        throw new IllegalArgumentException("Field " + fieldID + " doesn't exist!");
      }
    }

    public Object getFieldValue(int fieldID) {
      switch (fieldID) {
      case SUCCESS:
        return getSuccess();

      case IRE:
        return getIre();

      default:
        throw new IllegalArgumentException("Field " + fieldID + " doesn't exist!");
      }
    }

    // Returns true if field corresponding to fieldID is set (has been asigned a value) and false otherwise
    public boolean isSet(int fieldID) {
      switch (fieldID) {
      case SUCCESS:
        return isSetSuccess();
      case IRE:
        return isSetIre();
      default:
        throw new IllegalArgumentException("Field " + fieldID + " doesn't exist!");
      }
    }

    @Override
    public boolean equals(Object that) {
      if (that == null)
        return false;
      if (that instanceof get_key_range_result)
        return this.equals((get_key_range_result)that);
      return false;
    }

    public boolean equals(get_key_range_result that) {
      if (that == null)
        return false;

      boolean this_present_success = true && this.isSetSuccess();
      boolean that_present_success = true && that.isSetSuccess();
      if (this_present_success || that_present_success) {
        if (!(this_present_success && that_present_success))
          return false;
        if (!this.success.equals(that.success))
          return false;
      }

      boolean this_present_ire = true && this.isSetIre();
      boolean that_present_ire = true && that.isSetIre();
      if (this_present_ire || that_present_ire) {
        if (!(this_present_ire && that_present_ire))
          return false;
        if (!this.ire.equals(that.ire))
          return false;
      }

      return true;
    }

    @Override
    public int hashCode() {
      return 0;
    }

    public void read(TProtocol iprot) throws TException {
      TField field;
      iprot.readStructBegin();
      while (true)
      {
        field = iprot.readFieldBegin();
        if (field.type == TType.STOP) { 
          break;
        }
        switch (field.id)
        {
          case SUCCESS:
            if (field.type == TType.LIST) {
              {
                TList _list59 = iprot.readListBegin();
                this.success = new ArrayList<String>(_list59.size);
                for (int _i60 = 0; _i60 < _list59.size; ++_i60)
                {
                  String _elem61;
                  _elem61 = iprot.readString();
                  this.success.add(_elem61);
                }
                iprot.readListEnd();
              }
            } else { 
              TProtocolUtil.skip(iprot, field.type);
            }
            break;
          case IRE:
            if (field.type == TType.STRUCT) {
              this.ire = new InvalidRequestException();
              this.ire.read(iprot);
            } else { 
              TProtocolUtil.skip(iprot, field.type);
            }
            break;
          default:
            TProtocolUtil.skip(iprot, field.type);
            break;
        }
        iprot.readFieldEnd();
      }
      iprot.readStructEnd();


      // check for required fields of primitive type, which can't be checked in the validate method
      validate();
    }

    public void write(TProtocol oprot) throws TException {
      oprot.writeStructBegin(STRUCT_DESC);

      if (this.isSetSuccess()) {
        oprot.writeFieldBegin(SUCCESS_FIELD_DESC);
        {
          oprot.writeListBegin(new TList(TType.STRING, this.success.size()));
          for (String _iter62 : this.success)          {
            oprot.writeString(_iter62);
          }
          oprot.writeListEnd();
        }
        oprot.writeFieldEnd();
      } else if (this.isSetIre()) {
        oprot.writeFieldBegin(IRE_FIELD_DESC);
        this.ire.write(oprot);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

    @Override
    public String toString() {
      StringBuilder sb = new StringBuilder("get_key_range_result(");
      boolean first = true;

      sb.append("success:");
      if (this.success == null) {
        sb.append("null");
      } else {
        sb.append(this.success);
      }
      first = false;
      if (!first) sb.append(", ");
      sb.append("ire:");
      if (this.ire == null) {
        sb.append("null");
      } else {
        sb.append(this.ire);
      }
      first = false;
      sb.append(")");
      return sb.toString();
    }

    public void validate() throws TException {
      // check for required fields
      // check that fields of type enum have valid values
    }

  }

  public static class getStringProperty_args implements TBase, java.io.Serializable, Cloneable   {
    private static final TStruct STRUCT_DESC = new TStruct("getStringProperty_args");
    private static final TField PROPERTY_NAME_FIELD_DESC = new TField("propertyName", TType.STRING, (short)-1);

    public String propertyName;
    public static final int PROPERTYNAME = -1;

    private final Isset __isset = new Isset();
    private static final class Isset implements java.io.Serializable {
    }

    public static final Map<Integer, FieldMetaData> metaDataMap = Collections.unmodifiableMap(new HashMap<Integer, FieldMetaData>() {{
      put(PROPERTYNAME, new FieldMetaData("propertyName", TFieldRequirementType.DEFAULT, 
          new FieldValueMetaData(TType.STRING)));
    }});

    static {
      FieldMetaData.addStructMetaDataMap(getStringProperty_args.class, metaDataMap);
    }

    public getStringProperty_args() {
    }

    public getStringProperty_args(
      String propertyName)
    {
      this();
      this.propertyName = propertyName;
    }

    /**
     * Performs a deep copy on <i>other</i>.
     */
    public getStringProperty_args(getStringProperty_args other) {
      if (other.isSetPropertyName()) {
        this.propertyName = other.propertyName;
      }
    }

    @Override
    public getStringProperty_args clone() {
      return new getStringProperty_args(this);
    }

    public String getPropertyName() {
      return this.propertyName;
    }

    public void setPropertyName(String propertyName) {
      this.propertyName = propertyName;
    }

    public void unsetPropertyName() {
      this.propertyName = null;
    }

    // Returns true if field propertyName is set (has been asigned a value) and false otherwise
    public boolean isSetPropertyName() {
      return this.propertyName != null;
    }

    public void setPropertyNameIsSet(boolean value) {
      if (!value) {
        this.propertyName = null;
      }
    }

    public void setFieldValue(int fieldID, Object value) {
      switch (fieldID) {
      case PROPERTYNAME:
        if (value == null) {
          unsetPropertyName();
        } else {
          setPropertyName((String)value);
        }
        break;

      default:
        throw new IllegalArgumentException("Field " + fieldID + " doesn't exist!");
      }
    }

    public Object getFieldValue(int fieldID) {
      switch (fieldID) {
      case PROPERTYNAME:
        return getPropertyName();

      default:
        throw new IllegalArgumentException("Field " + fieldID + " doesn't exist!");
      }
    }

    // Returns true if field corresponding to fieldID is set (has been asigned a value) and false otherwise
    public boolean isSet(int fieldID) {
      switch (fieldID) {
      case PROPERTYNAME:
        return isSetPropertyName();
      default:
        throw new IllegalArgumentException("Field " + fieldID + " doesn't exist!");
      }
    }

    @Override
    public boolean equals(Object that) {
      if (that == null)
        return false;
      if (that instanceof getStringProperty_args)
        return this.equals((getStringProperty_args)that);
      return false;
    }

    public boolean equals(getStringProperty_args that) {
      if (that == null)
        return false;

      boolean this_present_propertyName = true && this.isSetPropertyName();
      boolean that_present_propertyName = true && that.isSetPropertyName();
      if (this_present_propertyName || that_present_propertyName) {
        if (!(this_present_propertyName && that_present_propertyName))
          return false;
        if (!this.propertyName.equals(that.propertyName))
          return false;
      }

      return true;
    }

    @Override
    public int hashCode() {
      return 0;
    }

    public void read(TProtocol iprot) throws TException {
      TField field;
      iprot.readStructBegin();
      while (true)
      {
        field = iprot.readFieldBegin();
        if (field.type == TType.STOP) { 
          break;
        }
        switch (field.id)
        {
          case PROPERTYNAME:
            if (field.type == TType.STRING) {
              this.propertyName = iprot.readString();
            } else { 
              TProtocolUtil.skip(iprot, field.type);
            }
            break;
          default:
            TProtocolUtil.skip(iprot, field.type);
            break;
        }
        iprot.readFieldEnd();
      }
      iprot.readStructEnd();


      // check for required fields of primitive type, which can't be checked in the validate method
      validate();
    }

    public void write(TProtocol oprot) throws TException {
      validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (this.propertyName != null) {
        oprot.writeFieldBegin(PROPERTY_NAME_FIELD_DESC);
        oprot.writeString(this.propertyName);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

    @Override
    public String toString() {
      StringBuilder sb = new StringBuilder("getStringProperty_args(");
      boolean first = true;

      sb.append("propertyName:");
      if (this.propertyName == null) {
        sb.append("null");
      } else {
        sb.append(this.propertyName);
      }
      first = false;
      sb.append(")");
      return sb.toString();
    }

    public void validate() throws TException {
      // check for required fields
      // check that fields of type enum have valid values
    }

  }

  public static class getStringProperty_result implements TBase, java.io.Serializable, Cloneable   {
    private static final TStruct STRUCT_DESC = new TStruct("getStringProperty_result");
    private static final TField SUCCESS_FIELD_DESC = new TField("success", TType.STRING, (short)0);

    public String success;
    public static final int SUCCESS = 0;

    private final Isset __isset = new Isset();
    private static final class Isset implements java.io.Serializable {
    }

    public static final Map<Integer, FieldMetaData> metaDataMap = Collections.unmodifiableMap(new HashMap<Integer, FieldMetaData>() {{
      put(SUCCESS, new FieldMetaData("success", TFieldRequirementType.DEFAULT, 
          new FieldValueMetaData(TType.STRING)));
    }});

    static {
      FieldMetaData.addStructMetaDataMap(getStringProperty_result.class, metaDataMap);
    }

    public getStringProperty_result() {
    }

    public getStringProperty_result(
      String success)
    {
      this();
      this.success = success;
    }

    /**
     * Performs a deep copy on <i>other</i>.
     */
    public getStringProperty_result(getStringProperty_result other) {
      if (other.isSetSuccess()) {
        this.success = other.success;
      }
    }

    @Override
    public getStringProperty_result clone() {
      return new getStringProperty_result(this);
    }

    public String getSuccess() {
      return this.success;
    }

    public void setSuccess(String success) {
      this.success = success;
    }

    public void unsetSuccess() {
      this.success = null;
    }

    // Returns true if field success is set (has been asigned a value) and false otherwise
    public boolean isSetSuccess() {
      return this.success != null;
    }

    public void setSuccessIsSet(boolean value) {
      if (!value) {
        this.success = null;
      }
    }

    public void setFieldValue(int fieldID, Object value) {
      switch (fieldID) {
      case SUCCESS:
        if (value == null) {
          unsetSuccess();
        } else {
          setSuccess((String)value);
        }
        break;

      default:
        throw new IllegalArgumentException("Field " + fieldID + " doesn't exist!");
      }
    }

    public Object getFieldValue(int fieldID) {
      switch (fieldID) {
      case SUCCESS:
        return getSuccess();

      default:
        throw new IllegalArgumentException("Field " + fieldID + " doesn't exist!");
      }
    }

    // Returns true if field corresponding to fieldID is set (has been asigned a value) and false otherwise
    public boolean isSet(int fieldID) {
      switch (fieldID) {
      case SUCCESS:
        return isSetSuccess();
      default:
        throw new IllegalArgumentException("Field " + fieldID + " doesn't exist!");
      }
    }

    @Override
    public boolean equals(Object that) {
      if (that == null)
        return false;
      if (that instanceof getStringProperty_result)
        return this.equals((getStringProperty_result)that);
      return false;
    }

    public boolean equals(getStringProperty_result that) {
      if (that == null)
        return false;

      boolean this_present_success = true && this.isSetSuccess();
      boolean that_present_success = true && that.isSetSuccess();
      if (this_present_success || that_present_success) {
        if (!(this_present_success && that_present_success))
          return false;
        if (!this.success.equals(that.success))
          return false;
      }

      return true;
    }

    @Override
    public int hashCode() {
      return 0;
    }

    public void read(TProtocol iprot) throws TException {
      TField field;
      iprot.readStructBegin();
      while (true)
      {
        field = iprot.readFieldBegin();
        if (field.type == TType.STOP) { 
          break;
        }
        switch (field.id)
        {
          case SUCCESS:
            if (field.type == TType.STRING) {
              this.success = iprot.readString();
            } else { 
              TProtocolUtil.skip(iprot, field.type);
            }
            break;
          default:
            TProtocolUtil.skip(iprot, field.type);
            break;
        }
        iprot.readFieldEnd();
      }
      iprot.readStructEnd();


      // check for required fields of primitive type, which can't be checked in the validate method
      validate();
    }

    public void write(TProtocol oprot) throws TException {
      oprot.writeStructBegin(STRUCT_DESC);

      if (this.isSetSuccess()) {
        oprot.writeFieldBegin(SUCCESS_FIELD_DESC);
        oprot.writeString(this.success);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

    @Override
    public String toString() {
      StringBuilder sb = new StringBuilder("getStringProperty_result(");
      boolean first = true;

      sb.append("success:");
      if (this.success == null) {
        sb.append("null");
      } else {
        sb.append(this.success);
      }
      first = false;
      sb.append(")");
      return sb.toString();
    }

    public void validate() throws TException {
      // check for required fields
      // check that fields of type enum have valid values
    }

  }

  public static class getStringListProperty_args implements TBase, java.io.Serializable, Cloneable   {
    private static final TStruct STRUCT_DESC = new TStruct("getStringListProperty_args");
    private static final TField PROPERTY_NAME_FIELD_DESC = new TField("propertyName", TType.STRING, (short)-1);

    public String propertyName;
    public static final int PROPERTYNAME = -1;

    private final Isset __isset = new Isset();
    private static final class Isset implements java.io.Serializable {
    }

    public static final Map<Integer, FieldMetaData> metaDataMap = Collections.unmodifiableMap(new HashMap<Integer, FieldMetaData>() {{
      put(PROPERTYNAME, new FieldMetaData("propertyName", TFieldRequirementType.DEFAULT, 
          new FieldValueMetaData(TType.STRING)));
    }});

    static {
      FieldMetaData.addStructMetaDataMap(getStringListProperty_args.class, metaDataMap);
    }

    public getStringListProperty_args() {
    }

    public getStringListProperty_args(
      String propertyName)
    {
      this();
      this.propertyName = propertyName;
    }

    /**
     * Performs a deep copy on <i>other</i>.
     */
    public getStringListProperty_args(getStringListProperty_args other) {
      if (other.isSetPropertyName()) {
        this.propertyName = other.propertyName;
      }
    }

    @Override
    public getStringListProperty_args clone() {
      return new getStringListProperty_args(this);
    }

    public String getPropertyName() {
      return this.propertyName;
    }

    public void setPropertyName(String propertyName) {
      this.propertyName = propertyName;
    }

    public void unsetPropertyName() {
      this.propertyName = null;
    }

    // Returns true if field propertyName is set (has been asigned a value) and false otherwise
    public boolean isSetPropertyName() {
      return this.propertyName != null;
    }

    public void setPropertyNameIsSet(boolean value) {
      if (!value) {
        this.propertyName = null;
      }
    }

    public void setFieldValue(int fieldID, Object value) {
      switch (fieldID) {
      case PROPERTYNAME:
        if (value == null) {
          unsetPropertyName();
        } else {
          setPropertyName((String)value);
        }
        break;

      default:
        throw new IllegalArgumentException("Field " + fieldID + " doesn't exist!");
      }
    }

    public Object getFieldValue(int fieldID) {
      switch (fieldID) {
      case PROPERTYNAME:
        return getPropertyName();

      default:
        throw new IllegalArgumentException("Field " + fieldID + " doesn't exist!");
      }
    }

    // Returns true if field corresponding to fieldID is set (has been asigned a value) and false otherwise
    public boolean isSet(int fieldID) {
      switch (fieldID) {
      case PROPERTYNAME:
        return isSetPropertyName();
      default:
        throw new IllegalArgumentException("Field " + fieldID + " doesn't exist!");
      }
    }

    @Override
    public boolean equals(Object that) {
      if (that == null)
        return false;
      if (that instanceof getStringListProperty_args)
        return this.equals((getStringListProperty_args)that);
      return false;
    }

    public boolean equals(getStringListProperty_args that) {
      if (that == null)
        return false;

      boolean this_present_propertyName = true && this.isSetPropertyName();
      boolean that_present_propertyName = true && that.isSetPropertyName();
      if (this_present_propertyName || that_present_propertyName) {
        if (!(this_present_propertyName && that_present_propertyName))
          return false;
        if (!this.propertyName.equals(that.propertyName))
          return false;
      }

      return true;
    }

    @Override
    public int hashCode() {
      return 0;
    }

    public void read(TProtocol iprot) throws TException {
      TField field;
      iprot.readStructBegin();
      while (true)
      {
        field = iprot.readFieldBegin();
        if (field.type == TType.STOP) { 
          break;
        }
        switch (field.id)
        {
          case PROPERTYNAME:
            if (field.type == TType.STRING) {
              this.propertyName = iprot.readString();
            } else { 
              TProtocolUtil.skip(iprot, field.type);
            }
            break;
          default:
            TProtocolUtil.skip(iprot, field.type);
            break;
        }
        iprot.readFieldEnd();
      }
      iprot.readStructEnd();


      // check for required fields of primitive type, which can't be checked in the validate method
      validate();
    }

    public void write(TProtocol oprot) throws TException {
      validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (this.propertyName != null) {
        oprot.writeFieldBegin(PROPERTY_NAME_FIELD_DESC);
        oprot.writeString(this.propertyName);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

    @Override
    public String toString() {
      StringBuilder sb = new StringBuilder("getStringListProperty_args(");
      boolean first = true;

      sb.append("propertyName:");
      if (this.propertyName == null) {
        sb.append("null");
      } else {
        sb.append(this.propertyName);
      }
      first = false;
      sb.append(")");
      return sb.toString();
    }

    public void validate() throws TException {
      // check for required fields
      // check that fields of type enum have valid values
    }

  }

  public static class getStringListProperty_result implements TBase, java.io.Serializable, Cloneable   {
    private static final TStruct STRUCT_DESC = new TStruct("getStringListProperty_result");
    private static final TField SUCCESS_FIELD_DESC = new TField("success", TType.LIST, (short)0);

    public List<String> success;
    public static final int SUCCESS = 0;

    private final Isset __isset = new Isset();
    private static final class Isset implements java.io.Serializable {
    }

    public static final Map<Integer, FieldMetaData> metaDataMap = Collections.unmodifiableMap(new HashMap<Integer, FieldMetaData>() {{
      put(SUCCESS, new FieldMetaData("success", TFieldRequirementType.DEFAULT, 
          new ListMetaData(TType.LIST, 
              new FieldValueMetaData(TType.STRING))));
    }});

    static {
      FieldMetaData.addStructMetaDataMap(getStringListProperty_result.class, metaDataMap);
    }

    public getStringListProperty_result() {
    }

    public getStringListProperty_result(
      List<String> success)
    {
      this();
      this.success = success;
    }

    /**
     * Performs a deep copy on <i>other</i>.
     */
    public getStringListProperty_result(getStringListProperty_result other) {
      if (other.isSetSuccess()) {
        List<String> __this__success = new ArrayList<String>();
        for (String other_element : other.success) {
          __this__success.add(other_element);
        }
        this.success = __this__success;
      }
    }

    @Override
    public getStringListProperty_result clone() {
      return new getStringListProperty_result(this);
    }

    public int getSuccessSize() {
      return (this.success == null) ? 0 : this.success.size();
    }

    public java.util.Iterator<String> getSuccessIterator() {
      return (this.success == null) ? null : this.success.iterator();
    }

    public void addToSuccess(String elem) {
      if (this.success == null) {
        this.success = new ArrayList<String>();
      }
      this.success.add(elem);
    }

    public List<String> getSuccess() {
      return this.success;
    }

    public void setSuccess(List<String> success) {
      this.success = success;
    }

    public void unsetSuccess() {
      this.success = null;
    }

    // Returns true if field success is set (has been asigned a value) and false otherwise
    public boolean isSetSuccess() {
      return this.success != null;
    }

    public void setSuccessIsSet(boolean value) {
      if (!value) {
        this.success = null;
      }
    }

    public void setFieldValue(int fieldID, Object value) {
      switch (fieldID) {
      case SUCCESS:
        if (value == null) {
          unsetSuccess();
        } else {
          setSuccess((List<String>)value);
        }
        break;

      default:
        throw new IllegalArgumentException("Field " + fieldID + " doesn't exist!");
      }
    }

    public Object getFieldValue(int fieldID) {
      switch (fieldID) {
      case SUCCESS:
        return getSuccess();

      default:
        throw new IllegalArgumentException("Field " + fieldID + " doesn't exist!");
      }
    }

    // Returns true if field corresponding to fieldID is set (has been asigned a value) and false otherwise
    public boolean isSet(int fieldID) {
      switch (fieldID) {
      case SUCCESS:
        return isSetSuccess();
      default:
        throw new IllegalArgumentException("Field " + fieldID + " doesn't exist!");
      }
    }

    @Override
    public boolean equals(Object that) {
      if (that == null)
        return false;
      if (that instanceof getStringListProperty_result)
        return this.equals((getStringListProperty_result)that);
      return false;
    }

    public boolean equals(getStringListProperty_result that) {
      if (that == null)
        return false;

      boolean this_present_success = true && this.isSetSuccess();
      boolean that_present_success = true && that.isSetSuccess();
      if (this_present_success || that_present_success) {
        if (!(this_present_success && that_present_success))
          return false;
        if (!this.success.equals(that.success))
          return false;
      }

      return true;
    }

    @Override
    public int hashCode() {
      return 0;
    }

    public void read(TProtocol iprot) throws TException {
      TField field;
      iprot.readStructBegin();
      while (true)
      {
        field = iprot.readFieldBegin();
        if (field.type == TType.STOP) { 
          break;
        }
        switch (field.id)
        {
          case SUCCESS:
            if (field.type == TType.LIST) {
              {
                TList _list63 = iprot.readListBegin();
                this.success = new ArrayList<String>(_list63.size);
                for (int _i64 = 0; _i64 < _list63.size; ++_i64)
                {
                  String _elem65;
                  _elem65 = iprot.readString();
                  this.success.add(_elem65);
                }
                iprot.readListEnd();
              }
            } else { 
              TProtocolUtil.skip(iprot, field.type);
            }
            break;
          default:
            TProtocolUtil.skip(iprot, field.type);
            break;
        }
        iprot.readFieldEnd();
      }
      iprot.readStructEnd();


      // check for required fields of primitive type, which can't be checked in the validate method
      validate();
    }

    public void write(TProtocol oprot) throws TException {
      oprot.writeStructBegin(STRUCT_DESC);

      if (this.isSetSuccess()) {
        oprot.writeFieldBegin(SUCCESS_FIELD_DESC);
        {
          oprot.writeListBegin(new TList(TType.STRING, this.success.size()));
          for (String _iter66 : this.success)          {
            oprot.writeString(_iter66);
          }
          oprot.writeListEnd();
        }
        oprot.writeFieldEnd();
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

    @Override
    public String toString() {
      StringBuilder sb = new StringBuilder("getStringListProperty_result(");
      boolean first = true;

      sb.append("success:");
      if (this.success == null) {
        sb.append("null");
      } else {
        sb.append(this.success);
      }
      first = false;
      sb.append(")");
      return sb.toString();
    }

    public void validate() throws TException {
      // check for required fields
      // check that fields of type enum have valid values
    }

  }

  public static class describeTable_args implements TBase, java.io.Serializable, Cloneable   {
    private static final TStruct STRUCT_DESC = new TStruct("describeTable_args");
    private static final TField TABLE_NAME_FIELD_DESC = new TField("tableName", TType.STRING, (short)-1);

    public String tableName;
    public static final int TABLENAME = -1;

    private final Isset __isset = new Isset();
    private static final class Isset implements java.io.Serializable {
    }

    public static final Map<Integer, FieldMetaData> metaDataMap = Collections.unmodifiableMap(new HashMap<Integer, FieldMetaData>() {{
      put(TABLENAME, new FieldMetaData("tableName", TFieldRequirementType.DEFAULT, 
          new FieldValueMetaData(TType.STRING)));
    }});

    static {
      FieldMetaData.addStructMetaDataMap(describeTable_args.class, metaDataMap);
    }

    public describeTable_args() {
    }

    public describeTable_args(
      String tableName)
    {
      this();
      this.tableName = tableName;
    }

    /**
     * Performs a deep copy on <i>other</i>.
     */
    public describeTable_args(describeTable_args other) {
      if (other.isSetTableName()) {
        this.tableName = other.tableName;
      }
    }

    @Override
    public describeTable_args clone() {
      return new describeTable_args(this);
    }

    public String getTableName() {
      return this.tableName;
    }

    public void setTableName(String tableName) {
      this.tableName = tableName;
    }

    public void unsetTableName() {
      this.tableName = null;
    }

    // Returns true if field tableName is set (has been asigned a value) and false otherwise
    public boolean isSetTableName() {
      return this.tableName != null;
    }

    public void setTableNameIsSet(boolean value) {
      if (!value) {
        this.tableName = null;
      }
    }

    public void setFieldValue(int fieldID, Object value) {
      switch (fieldID) {
      case TABLENAME:
        if (value == null) {
          unsetTableName();
        } else {
          setTableName((String)value);
        }
        break;

      default:
        throw new IllegalArgumentException("Field " + fieldID + " doesn't exist!");
      }
    }

    public Object getFieldValue(int fieldID) {
      switch (fieldID) {
      case TABLENAME:
        return getTableName();

      default:
        throw new IllegalArgumentException("Field " + fieldID + " doesn't exist!");
      }
    }

    // Returns true if field corresponding to fieldID is set (has been asigned a value) and false otherwise
    public boolean isSet(int fieldID) {
      switch (fieldID) {
      case TABLENAME:
        return isSetTableName();
      default:
        throw new IllegalArgumentException("Field " + fieldID + " doesn't exist!");
      }
    }

    @Override
    public boolean equals(Object that) {
      if (that == null)
        return false;
      if (that instanceof describeTable_args)
        return this.equals((describeTable_args)that);
      return false;
    }

    public boolean equals(describeTable_args that) {
      if (that == null)
        return false;

      boolean this_present_tableName = true && this.isSetTableName();
      boolean that_present_tableName = true && that.isSetTableName();
      if (this_present_tableName || that_present_tableName) {
        if (!(this_present_tableName && that_present_tableName))
          return false;
        if (!this.tableName.equals(that.tableName))
          return false;
      }

      return true;
    }

    @Override
    public int hashCode() {
      return 0;
    }

    public void read(TProtocol iprot) throws TException {
      TField field;
      iprot.readStructBegin();
      while (true)
      {
        field = iprot.readFieldBegin();
        if (field.type == TType.STOP) { 
          break;
        }
        switch (field.id)
        {
          case TABLENAME:
            if (field.type == TType.STRING) {
              this.tableName = iprot.readString();
            } else { 
              TProtocolUtil.skip(iprot, field.type);
            }
            break;
          default:
            TProtocolUtil.skip(iprot, field.type);
            break;
        }
        iprot.readFieldEnd();
      }
      iprot.readStructEnd();


      // check for required fields of primitive type, which can't be checked in the validate method
      validate();
    }

    public void write(TProtocol oprot) throws TException {
      validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (this.tableName != null) {
        oprot.writeFieldBegin(TABLE_NAME_FIELD_DESC);
        oprot.writeString(this.tableName);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

    @Override
    public String toString() {
      StringBuilder sb = new StringBuilder("describeTable_args(");
      boolean first = true;

      sb.append("tableName:");
      if (this.tableName == null) {
        sb.append("null");
      } else {
        sb.append(this.tableName);
      }
      first = false;
      sb.append(")");
      return sb.toString();
    }

    public void validate() throws TException {
      // check for required fields
      // check that fields of type enum have valid values
    }

  }

  public static class describeTable_result implements TBase, java.io.Serializable, Cloneable   {
    private static final TStruct STRUCT_DESC = new TStruct("describeTable_result");
    private static final TField SUCCESS_FIELD_DESC = new TField("success", TType.STRING, (short)0);

    public String success;
    public static final int SUCCESS = 0;

    private final Isset __isset = new Isset();
    private static final class Isset implements java.io.Serializable {
    }

    public static final Map<Integer, FieldMetaData> metaDataMap = Collections.unmodifiableMap(new HashMap<Integer, FieldMetaData>() {{
      put(SUCCESS, new FieldMetaData("success", TFieldRequirementType.DEFAULT, 
          new FieldValueMetaData(TType.STRING)));
    }});

    static {
      FieldMetaData.addStructMetaDataMap(describeTable_result.class, metaDataMap);
    }

    public describeTable_result() {
    }

    public describeTable_result(
      String success)
    {
      this();
      this.success = success;
    }

    /**
     * Performs a deep copy on <i>other</i>.
     */
    public describeTable_result(describeTable_result other) {
      if (other.isSetSuccess()) {
        this.success = other.success;
      }
    }

    @Override
    public describeTable_result clone() {
      return new describeTable_result(this);
    }

    public String getSuccess() {
      return this.success;
    }

    public void setSuccess(String success) {
      this.success = success;
    }

    public void unsetSuccess() {
      this.success = null;
    }

    // Returns true if field success is set (has been asigned a value) and false otherwise
    public boolean isSetSuccess() {
      return this.success != null;
    }

    public void setSuccessIsSet(boolean value) {
      if (!value) {
        this.success = null;
      }
    }

    public void setFieldValue(int fieldID, Object value) {
      switch (fieldID) {
      case SUCCESS:
        if (value == null) {
          unsetSuccess();
        } else {
          setSuccess((String)value);
        }
        break;

      default:
        throw new IllegalArgumentException("Field " + fieldID + " doesn't exist!");
      }
    }

    public Object getFieldValue(int fieldID) {
      switch (fieldID) {
      case SUCCESS:
        return getSuccess();

      default:
        throw new IllegalArgumentException("Field " + fieldID + " doesn't exist!");
      }
    }

    // Returns true if field corresponding to fieldID is set (has been asigned a value) and false otherwise
    public boolean isSet(int fieldID) {
      switch (fieldID) {
      case SUCCESS:
        return isSetSuccess();
      default:
        throw new IllegalArgumentException("Field " + fieldID + " doesn't exist!");
      }
    }

    @Override
    public boolean equals(Object that) {
      if (that == null)
        return false;
      if (that instanceof describeTable_result)
        return this.equals((describeTable_result)that);
      return false;
    }

    public boolean equals(describeTable_result that) {
      if (that == null)
        return false;

      boolean this_present_success = true && this.isSetSuccess();
      boolean that_present_success = true && that.isSetSuccess();
      if (this_present_success || that_present_success) {
        if (!(this_present_success && that_present_success))
          return false;
        if (!this.success.equals(that.success))
          return false;
      }

      return true;
    }

    @Override
    public int hashCode() {
      return 0;
    }

    public void read(TProtocol iprot) throws TException {
      TField field;
      iprot.readStructBegin();
      while (true)
      {
        field = iprot.readFieldBegin();
        if (field.type == TType.STOP) { 
          break;
        }
        switch (field.id)
        {
          case SUCCESS:
            if (field.type == TType.STRING) {
              this.success = iprot.readString();
            } else { 
              TProtocolUtil.skip(iprot, field.type);
            }
            break;
          default:
            TProtocolUtil.skip(iprot, field.type);
            break;
        }
        iprot.readFieldEnd();
      }
      iprot.readStructEnd();


      // check for required fields of primitive type, which can't be checked in the validate method
      validate();
    }

    public void write(TProtocol oprot) throws TException {
      oprot.writeStructBegin(STRUCT_DESC);

      if (this.isSetSuccess()) {
        oprot.writeFieldBegin(SUCCESS_FIELD_DESC);
        oprot.writeString(this.success);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

    @Override
    public String toString() {
      StringBuilder sb = new StringBuilder("describeTable_result(");
      boolean first = true;

      sb.append("success:");
      if (this.success == null) {
        sb.append("null");
      } else {
        sb.append(this.success);
      }
      first = false;
      sb.append(")");
      return sb.toString();
    }

    public void validate() throws TException {
      // check for required fields
      // check that fields of type enum have valid values
    }

  }

  public static class executeQuery_args implements TBase, java.io.Serializable, Cloneable   {
    private static final TStruct STRUCT_DESC = new TStruct("executeQuery_args");
    private static final TField QUERY_FIELD_DESC = new TField("query", TType.STRING, (short)-1);

    public String query;
    public static final int QUERY = -1;

    private final Isset __isset = new Isset();
    private static final class Isset implements java.io.Serializable {
    }

    public static final Map<Integer, FieldMetaData> metaDataMap = Collections.unmodifiableMap(new HashMap<Integer, FieldMetaData>() {{
      put(QUERY, new FieldMetaData("query", TFieldRequirementType.DEFAULT, 
          new FieldValueMetaData(TType.STRING)));
    }});

    static {
      FieldMetaData.addStructMetaDataMap(executeQuery_args.class, metaDataMap);
    }

    public executeQuery_args() {
    }

    public executeQuery_args(
      String query)
    {
      this();
      this.query = query;
    }

    /**
     * Performs a deep copy on <i>other</i>.
     */
    public executeQuery_args(executeQuery_args other) {
      if (other.isSetQuery()) {
        this.query = other.query;
      }
    }

    @Override
    public executeQuery_args clone() {
      return new executeQuery_args(this);
    }

    public String getQuery() {
      return this.query;
    }

    public void setQuery(String query) {
      this.query = query;
    }

    public void unsetQuery() {
      this.query = null;
    }

    // Returns true if field query is set (has been asigned a value) and false otherwise
    public boolean isSetQuery() {
      return this.query != null;
    }

    public void setQueryIsSet(boolean value) {
      if (!value) {
        this.query = null;
      }
    }

    public void setFieldValue(int fieldID, Object value) {
      switch (fieldID) {
      case QUERY:
        if (value == null) {
          unsetQuery();
        } else {
          setQuery((String)value);
        }
        break;

      default:
        throw new IllegalArgumentException("Field " + fieldID + " doesn't exist!");
      }
    }

    public Object getFieldValue(int fieldID) {
      switch (fieldID) {
      case QUERY:
        return getQuery();

      default:
        throw new IllegalArgumentException("Field " + fieldID + " doesn't exist!");
      }
    }

    // Returns true if field corresponding to fieldID is set (has been asigned a value) and false otherwise
    public boolean isSet(int fieldID) {
      switch (fieldID) {
      case QUERY:
        return isSetQuery();
      default:
        throw new IllegalArgumentException("Field " + fieldID + " doesn't exist!");
      }
    }

    @Override
    public boolean equals(Object that) {
      if (that == null)
        return false;
      if (that instanceof executeQuery_args)
        return this.equals((executeQuery_args)that);
      return false;
    }

    public boolean equals(executeQuery_args that) {
      if (that == null)
        return false;

      boolean this_present_query = true && this.isSetQuery();
      boolean that_present_query = true && that.isSetQuery();
      if (this_present_query || that_present_query) {
        if (!(this_present_query && that_present_query))
          return false;
        if (!this.query.equals(that.query))
          return false;
      }

      return true;
    }

    @Override
    public int hashCode() {
      return 0;
    }

    public void read(TProtocol iprot) throws TException {
      TField field;
      iprot.readStructBegin();
      while (true)
      {
        field = iprot.readFieldBegin();
        if (field.type == TType.STOP) { 
          break;
        }
        switch (field.id)
        {
          case QUERY:
            if (field.type == TType.STRING) {
              this.query = iprot.readString();
            } else { 
              TProtocolUtil.skip(iprot, field.type);
            }
            break;
          default:
            TProtocolUtil.skip(iprot, field.type);
            break;
        }
        iprot.readFieldEnd();
      }
      iprot.readStructEnd();


      // check for required fields of primitive type, which can't be checked in the validate method
      validate();
    }

    public void write(TProtocol oprot) throws TException {
      validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (this.query != null) {
        oprot.writeFieldBegin(QUERY_FIELD_DESC);
        oprot.writeString(this.query);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

    @Override
    public String toString() {
      StringBuilder sb = new StringBuilder("executeQuery_args(");
      boolean first = true;

      sb.append("query:");
      if (this.query == null) {
        sb.append("null");
      } else {
        sb.append(this.query);
      }
      first = false;
      sb.append(")");
      return sb.toString();
    }

    public void validate() throws TException {
      // check for required fields
      // check that fields of type enum have valid values
    }

  }

  public static class executeQuery_result implements TBase, java.io.Serializable, Cloneable   {
    private static final TStruct STRUCT_DESC = new TStruct("executeQuery_result");
    private static final TField SUCCESS_FIELD_DESC = new TField("success", TType.STRUCT, (short)0);

    public CqlResult_t success;
    public static final int SUCCESS = 0;

    private final Isset __isset = new Isset();
    private static final class Isset implements java.io.Serializable {
    }

    public static final Map<Integer, FieldMetaData> metaDataMap = Collections.unmodifiableMap(new HashMap<Integer, FieldMetaData>() {{
      put(SUCCESS, new FieldMetaData("success", TFieldRequirementType.DEFAULT, 
          new StructMetaData(TType.STRUCT, CqlResult_t.class)));
    }});

    static {
      FieldMetaData.addStructMetaDataMap(executeQuery_result.class, metaDataMap);
    }

    public executeQuery_result() {
    }

    public executeQuery_result(
      CqlResult_t success)
    {
      this();
      this.success = success;
    }

    /**
     * Performs a deep copy on <i>other</i>.
     */
    public executeQuery_result(executeQuery_result other) {
      if (other.isSetSuccess()) {
        this.success = new CqlResult_t(other.success);
      }
    }

    @Override
    public executeQuery_result clone() {
      return new executeQuery_result(this);
    }

    public CqlResult_t getSuccess() {
      return this.success;
    }

    public void setSuccess(CqlResult_t success) {
      this.success = success;
    }

    public void unsetSuccess() {
      this.success = null;
    }

    // Returns true if field success is set (has been asigned a value) and false otherwise
    public boolean isSetSuccess() {
      return this.success != null;
    }

    public void setSuccessIsSet(boolean value) {
      if (!value) {
        this.success = null;
      }
    }

    public void setFieldValue(int fieldID, Object value) {
      switch (fieldID) {
      case SUCCESS:
        if (value == null) {
          unsetSuccess();
        } else {
          setSuccess((CqlResult_t)value);
        }
        break;

      default:
        throw new IllegalArgumentException("Field " + fieldID + " doesn't exist!");
      }
    }

    public Object getFieldValue(int fieldID) {
      switch (fieldID) {
      case SUCCESS:
        return getSuccess();

      default:
        throw new IllegalArgumentException("Field " + fieldID + " doesn't exist!");
      }
    }

    // Returns true if field corresponding to fieldID is set (has been asigned a value) and false otherwise
    public boolean isSet(int fieldID) {
      switch (fieldID) {
      case SUCCESS:
        return isSetSuccess();
      default:
        throw new IllegalArgumentException("Field " + fieldID + " doesn't exist!");
      }
    }

    @Override
    public boolean equals(Object that) {
      if (that == null)
        return false;
      if (that instanceof executeQuery_result)
        return this.equals((executeQuery_result)that);
      return false;
    }

    public boolean equals(executeQuery_result that) {
      if (that == null)
        return false;

      boolean this_present_success = true && this.isSetSuccess();
      boolean that_present_success = true && that.isSetSuccess();
      if (this_present_success || that_present_success) {
        if (!(this_present_success && that_present_success))
          return false;
        if (!this.success.equals(that.success))
          return false;
      }

      return true;
    }

    @Override
    public int hashCode() {
      return 0;
    }

    public void read(TProtocol iprot) throws TException {
      TField field;
      iprot.readStructBegin();
      while (true)
      {
        field = iprot.readFieldBegin();
        if (field.type == TType.STOP) { 
          break;
        }
        switch (field.id)
        {
          case SUCCESS:
            if (field.type == TType.STRUCT) {
              this.success = new CqlResult_t();
              this.success.read(iprot);
            } else { 
              TProtocolUtil.skip(iprot, field.type);
            }
            break;
          default:
            TProtocolUtil.skip(iprot, field.type);
            break;
        }
        iprot.readFieldEnd();
      }
      iprot.readStructEnd();


      // check for required fields of primitive type, which can't be checked in the validate method
      validate();
    }

    public void write(TProtocol oprot) throws TException {
      oprot.writeStructBegin(STRUCT_DESC);

      if (this.isSetSuccess()) {
        oprot.writeFieldBegin(SUCCESS_FIELD_DESC);
        this.success.write(oprot);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

    @Override
    public String toString() {
      StringBuilder sb = new StringBuilder("executeQuery_result(");
      boolean first = true;

      sb.append("success:");
      if (this.success == null) {
        sb.append("null");
      } else {
        sb.append(this.success);
      }
      first = false;
      sb.append(")");
      return sb.toString();
    }

    public void validate() throws TException {
      // check for required fields
      // check that fields of type enum have valid values
    }

  }

}
