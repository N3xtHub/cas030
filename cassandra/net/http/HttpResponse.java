
public class HttpResponse
{
    private Map<String, String> headersMap_ = new HashMap<String, String>();;
    private String sBody_ = null;
    private String method_ = null;
    private String path_ = null;
    private String version_ = null;


    public String getMethod()
    {
        return method_;
    }
    
    public String getPath()
    {
        return path_;
    }
    
    public String getVersion()
    {
        return "HTTP/1.1";
    }

    public void addHeader(String name, String value)
    {
        headersMap_.put(name, value);
    }

    public String getHeader(String name)
    {
        return headersMap_.get(name).toString();
    }

    public void setBody(List<ByteBuffer> bodyBuffers)
    {
        StringBuffer sb = new StringBuffer();
        while(bodyBuffers.size() > 0)
        {
            sb.append(bodyBuffers.remove(0).asCharBuffer().toString());
        }
        sBody_ = sb.toString();
    }
    
    public String getBody()
    {
        return sBody_;
    }
    
    public void setStartLine(String method, String path, String version)
    {
        method_ = method;
        path_ = path;
        version_ = version;
    }    

    public String toString()
    {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        
        pw.println("HttpResponse-------->");
        pw.println("method = " + method_ + ", path = " + path_ + ", version = " + version_);
        pw.println("Headers: " + headersMap_.toString());
        pw.println("Body: " + sBody_);
        pw.println("<--------HttpResponse");
        
        return sw.toString();
    }
}
