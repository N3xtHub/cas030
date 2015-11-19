

public class CliCompiler
{

    // ANTLR does not provide case-insensitive tokenization support
    // out of the box. So we override the LA (lookahead) function
    // of the ANTLRStringStream class. Note: This doesn't change the
    // token text-- but just relaxes the matching rules to match
    // in upper case. [Logic borrowed from Hive code.]
    // 
    // Also see discussion on this topic in:
    // http://www.antlr.org/wiki/pages/viewpage.action?pageId=1782.
    public static class ANTLRNoCaseStringStream  extends ANTLRStringStream
    {
        public ANTLRNoCaseStringStream(String input)
        {
            super(input);
        }
    
        public int LA(int i)
        {
            int returnChar = super.LA(i);
            if (returnChar == CharStream.EOF)
            {
                return returnChar; 
            }
            else if (returnChar == 0) 
            {
                return returnChar;
            }

            return Character.toUpperCase((char)returnChar);
        }
    }

    public static CommonTree compileQuery(String query)
    {
        ANTLRStringStream input = new ANTLRNoCaseStringStream(query);

        CliLexer lexer = new CliLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);

        CliParser parser = new CliParser(tokens);

        // start parsing...
        CommonTree queryTree = (CommonTree)(parser.root().getTree());


        return queryTree;
    }
    /*
     * NODE_COLUMN_ACCESS related functions.
     */
    public static String getTableName(CommonTree astNode)
    {
        return astNode.getChild(0).getText();
    }

    public static String getColumnFamily(CommonTree astNode)
    {
        return astNode.getChild(1).getText();
    }

    public static String getKey(CommonTree astNode)
    {
        return Utils.unescapeSQLString(astNode.getChild(2).getText());
    }

    public static int numColumnSpecifiers(CommonTree astNode)
    {
        // Skip over table, column family and rowKey
        return astNode.getChildCount() - 3;
    }

    // Returns the pos'th (0-based index) column specifier in the astNode
    public static String getColumn(CommonTree astNode, int pos)
    {
        // Skip over table, column family and rowKey
        return Utils.unescapeSQLString(astNode.getChild(pos + 3).getText()); 
    }
 
}
