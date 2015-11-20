
public class KeyType {

    protected Boolean optimizeIt;
    protected String combiner;
    protected FieldCollection fields;
}

public class SuperColumnType {

    protected FieldCollection fields;
    protected Boolean tokenize;
}

public class TimestampType {

    protected Integer field;
}


public class ValueType {

    protected Integer field;
}

public class ColumnType {

    protected ValueType value;
    protected TimestampType timestamp;
    protected String name;
    protected Integer field;
}

public class Importer {

    protected String table;
    protected KeyType key;
    protected ColumnFamilyType columnFamily;
}

public class ColumnFamilyType {

    protected List<ColumnType> column;
    protected SuperColumnType superColumn;
    protected String directory;
    protected String delimiter;
    protected String name;
}

