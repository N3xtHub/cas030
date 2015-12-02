
public class CliOptions {

    private static Options options = null; // Info about command line options
    private CommandLine cmd = null;        // Command Line arguments

    // Command line options
    private static final String HOST_OPTION = "host";
    private static final String PORT_OPTION = "port";

    // Default values for optional command line arguments
    private static final int    DEFAULT_THRIFT_PORT = 9160;

    // Register the command line options and their properties (such as
    // whether they take an extra argument, etc.
    static
    {
        options = new Options();
        options.addOption(HOST_OPTION, true, "cassandra server's host name");
        options.addOption(PORT_OPTION, true, "cassandra server's thrift port");  
    }

    private static void printUsage()
    {
        System.err.println("");
        System.err.println("Usage: cascli --host hostname [--port <portname>]");
        System.err.println("");
    }

    public void processArgs(CliSessionState css, String[] args)
    {
        CommandLineParser parser = new PosixParser();

        cmd = parser.parse(options, args);


        if (!cmd.hasOption(HOST_OPTION))
        {
            // host name not specified in command line.
            // In this case, we don't implicitly connect at CLI startup. In this case,
            // the user must use the "connect" CLI statement to connect.
            //
            css.hostName = null;
            
            // HelpFormatter formatter = new HelpFormatter();
            // formatter.printHelp("java com.facebook.infrastructure.cli.CliMain ", options);
            // System.exit(1);
        }
        else 
        {
            css.hostName = cmd.getOptionValue(HOST_OPTION);
        }

        // Look for optional args.
        if (cmd.hasOption(PORT_OPTION))
        {
            css.thriftPort = Integer.parseInt(cmd.getOptionValue(PORT_OPTION));
        }
        else
        {
            css.thriftPort = DEFAULT_THRIFT_PORT;
        }
    }
}
