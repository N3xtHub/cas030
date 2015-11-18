
public class XMLUtils
{
	private Document document_;
    private XPath xpath_;

    public XMLUtils(String xmlSrc) throws FileNotFoundException, ParserConfigurationException, SAXException, IOException
    {        
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        document_ = db.parse(xmlSrc);
        
        XPathFactory xpathFactory = XPathFactory.newInstance();
        xpath_ = xpathFactory.newXPath();
    }

	public String getNodeValue(String xql) throws XPathExpressionException
	{        
        XPathExpression expr = xpath_.compile(xql);
        String value = expr.evaluate(document_);
        if ( value != null && value.equals("") )
            value = null;
        return value;	
    }
        
	public String[] getNodeValues(String xql) throws XPathExpressionException
	{
        XPathExpression expr = xpath_.compile(xql);        
        NodeList nl = (NodeList)expr.evaluate(document_, XPathConstants.NODESET);
        int size = nl.getLength();
        String[] values = new String[size];
        
        for ( int i = 0; i < size; ++i )
        {
            Node node = nl.item(i);
            node = node.getFirstChild();
            values[i] = node.getNodeValue();
        }
        return values;       		
	}

	public NodeList getRequestedNodeList(String xql) throws XPathExpressionException
	{
        XPathExpression expr = xpath_.compile(xql);
        NodeList nodeList = (NodeList)expr.evaluate(document_, XPathConstants.NODESET);		
		return nodeList;
	}

	public static String getAttributeValue(Node node, String attrName) throws TransformerException
	{        
		String value = null;
		node = node.getAttributes().getNamedItem(attrName);
		if ( node != null )
		{
		    value = node.getNodeValue();
		}
		return value;
	}

    public static void main(String[] args) throws Throwable
    {
        XMLUtils xmlUtils = new XMLUtils("C:\\Engagements\\Cassandra-Golden\\storage-conf.xml");
        String[] value = xmlUtils.getNodeValues("/Storage/Seeds/Seed");
        System.out.println(value);
    }
}
