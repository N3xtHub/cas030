
public class CqlResult_t implements TBase, java.io.Serializable, Cloneable {
  private static final TStruct STRUCT_DESC = new TStruct("CqlResult_t");
  private static final TField ERROR_CODE_FIELD_DESC = new TField("errorCode", TType.I32, (short)1);
  private static final TField ERROR_TXT_FIELD_DESC = new TField("errorTxt", TType.STRING, (short)2);
  private static final TField RESULT_SET_FIELD_DESC = new TField("resultSet", TType.LIST, (short)3);

  public int errorCode;
  public static final int ERRORCODE = 1;
  public String errorTxt;
  public static final int ERRORTXT = 2;
  public List<Map<String,String>> resultSet;
  public static final int RESULTSET = 3;

  private final Isset __isset = new Isset();
  private static final class Isset implements java.io.Serializable {
    public boolean errorCode = false;
  }

  public static final Map<Integer, FieldMetaData> metaDataMap = Collections.unmodifiableMap(new HashMap<Integer, FieldMetaData>() {{
    put(ERRORCODE, new FieldMetaData("errorCode", TFieldRequirementType.DEFAULT, 
        new FieldValueMetaData(TType.I32)));
    put(ERRORTXT, new FieldMetaData("errorTxt", TFieldRequirementType.DEFAULT, 
        new FieldValueMetaData(TType.STRING)));
    put(RESULTSET, new FieldMetaData("resultSet", TFieldRequirementType.DEFAULT, 
        new FieldValueMetaData(TType.LIST)));
  }});

  static {
    FieldMetaData.addStructMetaDataMap(CqlResult_t.class, metaDataMap);
  }

  public CqlResult_t() {
  }

  public CqlResult_t(
    int errorCode,
    String errorTxt,
    List<Map<String,String>> resultSet)
  {
    this();
    this.errorCode = errorCode;
    this.__isset.errorCode = true;
    this.errorTxt = errorTxt;
    this.resultSet = resultSet;
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public CqlResult_t(CqlResult_t other) {
    __isset.errorCode = other.__isset.errorCode;
    this.errorCode = other.errorCode;
    if (other.isSetErrorTxt()) {
      this.errorTxt = other.errorTxt;
    }
    if (other.isSetResultSet()) {
      this.resultSet = other.resultSet;
    }
  }

  @Override
  public CqlResult_t clone() {
    return new CqlResult_t(this);
  }

  public int getErrorCode() {
    return this.errorCode;
  }

  public void setErrorCode(int errorCode) {
    this.errorCode = errorCode;
    this.__isset.errorCode = true;
  }

  public void unsetErrorCode() {
    this.__isset.errorCode = false;
  }

  // Returns true if field errorCode is set (has been asigned a value) and false otherwise
  public boolean isSetErrorCode() {
    return this.__isset.errorCode;
  }

  public void setErrorCodeIsSet(boolean value) {
    this.__isset.errorCode = value;
  }

  public String getErrorTxt() {
    return this.errorTxt;
  }

  public void setErrorTxt(String errorTxt) {
    this.errorTxt = errorTxt;
  }

  public void unsetErrorTxt() {
    this.errorTxt = null;
  }

  // Returns true if field errorTxt is set (has been asigned a value) and false otherwise
  public boolean isSetErrorTxt() {
    return this.errorTxt != null;
  }

  public void setErrorTxtIsSet(boolean value) {
    if (!value) {
      this.errorTxt = null;
    }
  }

  public int getResultSetSize() {
    return (this.resultSet == null) ? 0 : this.resultSet.size();
  }

  public java.util.Iterator<Map<String,String>> getResultSetIterator() {
    return (this.resultSet == null) ? null : this.resultSet.iterator();
  }

  public void addToResultSet(Map<String,String> elem) {
    if (this.resultSet == null) {
      this.resultSet = new ArrayList<Map<String,String>>();
    }
    this.resultSet.add(elem);
  }

  public List<Map<String,String>> getResultSet() {
    return this.resultSet;
  }

  public void setResultSet(List<Map<String,String>> resultSet) {
    this.resultSet = resultSet;
  }

  public void unsetResultSet() {
    this.resultSet = null;
  }

  // Returns true if field resultSet is set (has been asigned a value) and false otherwise
  public boolean isSetResultSet() {
    return this.resultSet != null;
  }

  public void setResultSetIsSet(boolean value) {
    if (!value) {
      this.resultSet = null;
    }
  }

  public void setFieldValue(int fieldID, Object value) {
    switch (fieldID) {
    case ERRORCODE:
      if (value == null) {
        unsetErrorCode();
      } else {
        setErrorCode((Integer)value);
      }
      break;

    case ERRORTXT:
      if (value == null) {
        unsetErrorTxt();
      } else {
        setErrorTxt((String)value);
      }
      break;

    case RESULTSET:
      if (value == null) {
        unsetResultSet();
      } else {
        setResultSet((List<Map<String,String>>)value);
      }
      break;

    default:
      throw new IllegalArgumentException("Field " + fieldID + " doesn't exist!");
    }
  }

  public Object getFieldValue(int fieldID) {
    switch (fieldID) {
    case ERRORCODE:
      return new Integer(getErrorCode());

    case ERRORTXT:
      return getErrorTxt();

    case RESULTSET:
      return getResultSet();

    default:
      throw new IllegalArgumentException("Field " + fieldID + " doesn't exist!");
    }
  }

  // Returns true if field corresponding to fieldID is set (has been asigned a value) and false otherwise
  public boolean isSet(int fieldID) {
    switch (fieldID) {
    case ERRORCODE:
      return isSetErrorCode();
    case ERRORTXT:
      return isSetErrorTxt();
    case RESULTSET:
      return isSetResultSet();
    default:
      throw new IllegalArgumentException("Field " + fieldID + " doesn't exist!");
    }
  }

  @Override
  public boolean equals(Object that) {
    if (that == null)
      return false;
    if (that instanceof CqlResult_t)
      return this.equals((CqlResult_t)that);
    return false;
  }

  public boolean equals(CqlResult_t that) {
    if (that == null)
      return false;

    boolean this_present_errorCode = true;
    boolean that_present_errorCode = true;
    if (this_present_errorCode || that_present_errorCode) {
      if (!(this_present_errorCode && that_present_errorCode))
        return false;
      if (this.errorCode != that.errorCode)
        return false;
    }

    boolean this_present_errorTxt = true && this.isSetErrorTxt();
    boolean that_present_errorTxt = true && that.isSetErrorTxt();
    if (this_present_errorTxt || that_present_errorTxt) {
      if (!(this_present_errorTxt && that_present_errorTxt))
        return false;
      if (!this.errorTxt.equals(that.errorTxt))
        return false;
    }

    boolean this_present_resultSet = true && this.isSetResultSet();
    boolean that_present_resultSet = true && that.isSetResultSet();
    if (this_present_resultSet || that_present_resultSet) {
      if (!(this_present_resultSet && that_present_resultSet))
        return false;
      if (!this.resultSet.equals(that.resultSet))
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
        case ERRORCODE:
          if (field.type == TType.I32) {
            this.errorCode = iprot.readI32();
            this.__isset.errorCode = true;
          } else { 
            TProtocolUtil.skip(iprot, field.type);
          }
          break;
        case ERRORTXT:
          if (field.type == TType.STRING) {
            this.errorTxt = iprot.readString();
          } else { 
            TProtocolUtil.skip(iprot, field.type);
          }
          break;
        case RESULTSET:
          if (field.type == TType.LIST) {
            {
              TList _list22 = iprot.readListBegin();
              this.resultSet = new ArrayList<Map<String,String>>(_list22.size);
              for (int _i23 = 0; _i23 < _list22.size; ++_i23)
              {
                Map<String,String> _elem24;
                {
                  TMap _map25 = iprot.readMapBegin();
                  _elem24 = new HashMap<String,String>(2*_map25.size);
                  for (int _i26 = 0; _i26 < _map25.size; ++_i26)
                  {
                    String _key27;
                    String _val28;
                    _key27 = iprot.readString();
                    _val28 = iprot.readString();
                    _elem24.put(_key27, _val28);
                  }
                  iprot.readMapEnd();
                }
                this.resultSet.add(_elem24);
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
    oprot.writeFieldBegin(ERROR_CODE_FIELD_DESC);
    oprot.writeI32(this.errorCode);
    oprot.writeFieldEnd();
    if (this.errorTxt != null) {
      oprot.writeFieldBegin(ERROR_TXT_FIELD_DESC);
      oprot.writeString(this.errorTxt);
      oprot.writeFieldEnd();
    }
    if (this.resultSet != null) {
      oprot.writeFieldBegin(RESULT_SET_FIELD_DESC);
      {
        oprot.writeListBegin(new TList(TType.MAP, this.resultSet.size()));
        for (Map<String,String> _iter29 : this.resultSet)        {
          {
            oprot.writeMapBegin(new TMap(TType.STRING, TType.STRING, _iter29.size()));
            for (Map.Entry<String, String> _iter30 : _iter29.entrySet())            {
              oprot.writeString(_iter30.getKey());
              oprot.writeString(_iter30.getValue());
            }
            oprot.writeMapEnd();
          }
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
    StringBuilder sb = new StringBuilder("CqlResult_t(");
    boolean first = true;

    sb.append("errorCode:");
    sb.append(this.errorCode);
    first = false;
    if (!first) sb.append(", ");
    sb.append("errorTxt:");
    if (this.errorTxt == null) {
      sb.append("null");
    } else {
      sb.append(this.errorTxt);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("resultSet:");
    if (this.resultSet == null) {
      sb.append("null");
    } else {
      sb.append(this.resultSet);
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

