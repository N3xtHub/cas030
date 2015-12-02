public class HttpWriteResponse
{
    private HttpRequest httpRequest_ = null;
    private StringBuilder body_ = new StringBuilder();

    public HttpWriteResponse(HttpRequest httpRequest)
    {
        httpRequest_ = httpRequest;
    }

    public void println(String responseLine)
    {
        if(responseLine != null)
        {
            body_.append(responseLine);
            body_.append( System.getProperty("line.separator"));
        }
    }

    public ByteBuffer flush()
    {
        StringBuilder sb = new StringBuilder();
        // write out the HTTP response headers first
        sb.append(httpRequest_.getVersion() + " 200 OK\r\n");
        sb.append("Content-Type: text/html\r\n");
        if(body_.length() > 0)
        	sb.append("Content-Length: " + body_.length() + "\r\n");
        sb.append("Cache-Control: no-cache\r\n");
        sb.append("Pragma: no-cache\r\n");

        // terminate the headers
        sb.append("\r\n");

        // now write out the HTTP response body
        if(body_.length() > 0)
            sb.append(body_.toString());

        // terminate the body
        //sb.append("\r\n");
        //sb.append("\r\n");
        ByteBuffer buffer = ByteBuffer.wrap(sb.toString().getBytes());
        return buffer;
    }
}
