
public class CliSessionState {

    public boolean timingOn = false;
    public String  hostName;       // cassandra server name
    public int     thriftPort;     // cassandra server's thrift port

    /*
     * Streams to read/write from
     */
    public InputStream in;
    public PrintStream out;
    public PrintStream err;

    public CliSessionState() {
        in = System.in;
        out = System.out;
        err = System.err;
    }
}
