//
// Cassandra Command Line Interface (CLI) Main
//
public class CliMain
{
    public final static String PROMPT = "cassandra";
    public final static String HISTORYFILE = ".cassandra.history";

    private static TTransport transport_ = null;
    private static Cassandra.Client thriftClient_ = null;
    private static CliSessionState css_ = new CliSessionState();
    private static CliClient cliClient_;

    // Establish a thrift connection to cassandra instance
    public static void connect(String server, int port)
    {
        TSocket socket = new TSocket(server, port);

        if (transport_ != null)
            transport_.close();

        transport_ = socket;

        TBinaryProtocol binaryProtocol = new TBinaryProtocol(transport_, false, false);
        Cassandra.Client cassandraClient = new Cassandra.Client(binaryProtocol);

        transport_.open();

        thriftClient_ = cassandraClient;
        cliClient_ = new CliClient(css_, thriftClient_);

        css_.out.printf("Connected to %s/%d\n", server, port);
    }

    // Disconnect thrift connection to cassandra instance
    public static void disconnect()
    {
        if (transport_ != null)
        {
            transport_.close();
            transport_ = null;
        }
    }

    private static void printBanner()
    {
        css_.out.println("Welcome to cassandra CLI.\n");
        css_.out.println("Type 'help' or '?' for help. Type 'quit' or 'exit' to quit.");
    }

    public static boolean isConnected()
    {
        return (thriftClient_ != null);
    }
    
    private static void processServerQuery(String query)
    {
        if (!isConnected())
            return;

        cliClient_.executeQueryOnServer(query);
    }

    private static void processCLIStmt(String query)
    {
            cliClient_.executeCLIStmt(query);
    }

    private static void processLine(String line)
    {
        StringTokenizer tokenizer = new StringTokenizer(line);
        if (tokenizer.hasMoreTokens())
        {
            // Use first token for now to determine if this statement is
            // a CQL statement. Technically, the line could start with
            // a comment token followed by a CQL statement. That case
            // isn't handled right now.
            String token = tokenizer.nextToken().toUpperCase();
            if (token.startsWith("GET")
                || token.startsWith("SELECT")
                || token.startsWith("SET")
                || token.startsWith("DELETE")
                || token.startsWith("EXPLAIN")) // explain plan statement
            {
                // these are CQL Statements that are compiled and executed on server-side
                processServerQuery(line);
            }
            else 
            {
                // These are CLI statements processed locally
                processCLIStmt(line);
            }
        }
    } 

    public static void main(String args[]) throws IOException  
    {
        // process command line args
        CliOptions cliOptions = new CliOptions();
        cliOptions.processArgs(css_, args);

        // connect to cassandra server if host argument specified.
        if (css_.hostName != null)
        {
            connect(css_.hostName, css_.thriftPort);
        }
        else
        {
            // If not, client must connect explicitly using the "connect" CLI statement.
            cliClient_ = new CliClient(css_, null);
        }

        ConsoleReader reader = new ConsoleReader(); 
        reader.setBellEnabled(false);

        String historyFile = System.getProperty("user.home") + File.separator  + HISTORYFILE;

        reader.setHistory(new History(new File(historyFile)));

        printBanner();

        String line;
        while ((line = reader.readLine(PROMPT+"> ")) != null)
        {
            processLine(line);
        }
    }
}
