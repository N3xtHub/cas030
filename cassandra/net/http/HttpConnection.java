public class HttpConnection extends SelectionKeyHandler implements HttpStartLineParser.Callback, HttpHeaderParser.Callback
{
    private static Logger logger_ = Logger.getLogger(StorageService.class);
    public static final String httpRequestVerbHandler_ = "HTTP-REQUEST-VERB-HANDLER";
    public static final String httpStage_ = "HTTP-STAGE";

    private static ExecutorService executor_ = new DebuggableThreadPoolExecutor("HTTP-CONNECTION");

    /*
     * These are the callbacks into who ever intends
     * to listen on the client socket.
     */
    public interface HttpConnectionListener
    {
        public void onRequest(org.apache.cassandra.net.http.HttpRequest httpRequest);
        public void onResponse(HttpResponse httpResponse);
    }

    enum HttpMessageType
    {
        UNKNOWN,
        REQUEST,
        RESPONSE
    }

    enum ParseState
    {
        IN_NEW,
        IN_START,
        IN_HEADERS, IN_BODY
    }

    private ParseState parseState_ = ParseState.IN_NEW;
    private long parseStartTime_ = 0;
    private HttpMessageType currentMsgType_ = HttpMessageType.UNKNOWN;
    private int contentLength_ = 0;
    private List<ByteBuffer> bodyBuffers_ = new LinkedList<ByteBuffer>();
    private boolean shouldClose_ = false;
    private String defaultContentType_ = "text/html";
    private HttpRequest currentRequest_ = null;
    private HttpResponse currentResponse_ = null;
    private HttpStartLineParser startLineParser_ = new HttpStartLineParser(this);
    private HttpHeaderParser headerParser_ = new HttpHeaderParser(this);
    /* Selection Key associated with this HTTP Connection */
    private SelectionKey httpKey_;
    /* SocketChannel associated with this HTTP Connection */
    private SocketChannel httpChannel_;
    /* HTTPReader instance associated with this HTTP Connection */
    private HTTPReader httpReader_ = new HTTPReader();

    /*
     * This abstraction starts reading the data that comes in
     * on a HTTP request. It accumulates the bytes read into
     * a buffer and passes the buffer to the HTTP parser.
    */

    class HTTPReader implements Runnable
    {
        /* We read 256 bytes at a time from a HTTP connection */
        private static final int bufferSize_ = 256;

        /*
         * Read buffers from the input stream into the byte buffer.
         */
        public void run()
        {
            ByteBuffer readBuffer = ByteBuffer.allocate(HTTPReader.bufferSize_);
            try
            {
                int bytesRead = httpChannel_.read(readBuffer);
                readBuffer.flip();
                if ( readBuffer.remaining() > 0 )
                    HttpConnection.this.parse(readBuffer);
            }
            catch ( IOException ex )
            {
                logger_.warn(LogUtil.throwableToString(ex));
            }
        }
    }

    /*
     *  Read called on the Selector thread. This is called
     *  when there is some HTTP request that needs to be
     *  processed.
    */
    public void read(SelectionKey key)
    {
        if ( httpKey_ == null )
        {
            httpKey_ = key;
            httpChannel_ = (SocketChannel)key.channel();
        }
        /* deregister interest for read */
        turnOffInterestOps(key, SelectionKey.OP_READ);
        /* Add a task to process the HTTP request */
        MessagingService.getReadExecutor().execute(httpReader_);
    }
    private void resetParserState()
    {
        startLineParser_.resetParserState();
        headerParser_.resetParserState();
        parseState_ = ParseState.IN_NEW;
        contentLength_ = 0;
        bodyBuffers_ = new LinkedList<ByteBuffer>();
        currentMsgType_ = HttpMessageType.UNKNOWN;
        currentRequest_ = null;
        currentResponse_ = null;
    }

    public void close()
    {        
        logger_.info("Closing HTTP socket ...");
        if ( httpKey_ != null )
        {
            httpKey_.cancel();
            try
            {
                httpKey_.channel().close();
            }
            catch (IOException e) {}
        }
    }

    /*
     * Process the HTTP commands sent from the client. Reads
     * the socket and parses the HTTP request.
    */
    public void parse(ByteBuffer bb)
    {
        try
        {
            logger_.debug("Processing http request from socket ...");
            switch (parseState_)
            {
                case IN_NEW:
                    parseState_ = ParseState.IN_START;
                    parseStartTime_ = System.currentTimeMillis();

                // fall through
                case IN_START:
                    if (startLineParser_.onMoreBytesNew(bb) == false)
                    {
                        break;
                    }
                    else
                    {
                        /* Already done through the callback */
                        parseState_ = ParseState.IN_HEADERS;
                    }

                // fall through
                case IN_HEADERS:
                    if (headerParser_.onMoreBytesNew(bb) == false)
                    {

                        break; // need more bytes
                    }
                    else
                    {
                        String len;
                        if (currentMsgType_ == HttpMessageType.REQUEST)
                        {
                            len = currentRequest_.getHeader(HttpProtocolConstants.CONTENT_LENGTH);

                            // find if we should close method
                            if (currentRequest_.getVersion().equalsIgnoreCase("HTTP/1.1"))
                            {
                                /*
                                 * Scan all of the headers for close messages
                                 */
                                String val = currentRequest_.getHeader(HttpProtocolConstants.CONNECTION);

                                if (val != null && val.equalsIgnoreCase(HttpProtocolConstants.CLOSE))
                                {
                                    shouldClose_ = true;
                                }
                            } else if (currentRequest_.getVersion().equalsIgnoreCase("HTTP/1.0"))
                            {
                                /* By default no keep-alive */
                                shouldClose_ = true;

                                /*
                                 * Scan all of the headers for keep-alive
                                 * messages
                                 */
                                String val = currentRequest_.getHeader(HttpProtocolConstants.CONNECTION);

                                if (val != null && val.equalsIgnoreCase(HttpProtocolConstants.KEEP_ALIVE))
                                {
                                    shouldClose_ = false;
                                }
                            } else
                            {
                                /* Assume 0.9 */
                                shouldClose_ = true;
                            }
                        }
                        else if (currentMsgType_ == HttpMessageType.RESPONSE)
                        {
                            len = currentResponse_.getHeader(HttpProtocolConstants.CONTENT_LENGTH);

                        // TODO: pay attention to keep-alive and
                        // close headers
                        }
                        else
                        {
                            logger_.warn("in HttpConnection::processInput_() Message type is not set");
                            return;
                        }

                        if (len != null)
                        {
                            try
                            {
                                if(len == null || len.equals(""))
                                    contentLength_ = 0;
                                else
                                    contentLength_ = Integer.parseInt(len);
                            }
                            catch (NumberFormatException ex)
                            {
                                throw new HttpParsingException();
                            }
                        }
                        parseState_ = ParseState.IN_BODY;
                    }

                // fall through
                case IN_BODY:
                    boolean done = false;

                    if (contentLength_ > 0)
                    {
                        if (bb.remaining() > contentLength_)
                        {
                            int newLimit = bb.position() + contentLength_;
                            bodyBuffers_.add(((ByteBuffer) bb.duplicate().limit(newLimit)).slice());
                            bb.position(newLimit);
                            contentLength_ = 0;
                        }
                        else
                        {
                            contentLength_ -= bb.remaining();
                            bodyBuffers_.add(bb.duplicate());
                            bb.position(bb.limit());
                        }
                    }

                if (contentLength_ == 0)
                {
                    done = true;
                }

                if (done)
                {
                    logger_.debug("... done parsing request for " + currentRequest_.getPath());
                    if (currentMsgType_ == HttpMessageType.REQUEST)
                    {
                        currentRequest_.setBody(bodyBuffers_);

                        if (currentRequest_.getHeader("Content-Type") == null)
                        {
                            currentRequest_.addHeader("Content-Type", defaultContentType_);
                        }

                        executor_.submit(new HttpRequestHandler(currentRequest_));
                    }
                    else if (currentMsgType_ == HttpMessageType.RESPONSE)
                    {
                        logger_.error("Holy shit! We are not supposed to be processing responses here!");
                    }
                    else
                    {
                        logger_.error("Http message type is still unset after we finish parsing the body?");
                    }

                    resetParserState();
                }
            }

        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
        finally
        {
            turnOnInterestOps(httpKey_, SelectionKey.OP_READ);
        }
    }

    public void write(ByteBuffer buffer)
    {
        /*
         * TODO: Make this a non blocking write.
        */
        try
        {
            while ( buffer.remaining() > 0 )
            {
                httpChannel_.write(buffer);
            }
            close();
        }
        catch ( IOException ex )
        {
            logger_.warn(LogUtil.throwableToString(ex));
        }
    }

    // HttpStartLineParser.Callback interface implementation
    public void onStartLine(String method, String path, String query, String version)
    {
        logger_.debug("Startline method=" + method + " path=" + path + " query=" + query + " version=" + version);

        if (method.startsWith("HTTP"))
        {
                // response
                currentMsgType_ = HttpMessageType.RESPONSE;
                currentResponse_ = new HttpResponse();
                currentResponse_.setStartLine(method, path, version);
        }
        else
        {
                // request
                currentMsgType_ = HttpMessageType.REQUEST;
                currentRequest_ = new HttpRequest(this);
                currentRequest_.setStartLine(method, path, query, version);
        }
    }

    // HttpHeaderParser.Callback interface implementation
    public void onHeader(String name, String value)
    {
        if (currentMsgType_ == HttpMessageType.REQUEST)
        {
                currentRequest_.addHeader(name, value);
        }
        else if (currentMsgType_ == HttpMessageType.RESPONSE)
        {
                currentResponse_.addHeader(name, value);
        }
        else
        {
            logger_.warn("Unknown message type -- HttpConnection::onHeader()");
        }

        logger_.debug(name + " : " + value);
    }
}



