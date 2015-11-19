
public class CqlCompiler
{
    // ANTLR does not provide case-insensitive tokenization support
    // out of the box. So we override the LA (lookahead) function
    // of the ANTLRStringStream class. Note: This doesn't change the
    // token text-- but just relaxes the matching rules to match
    // in upper case. [Logic borrowed from Hive code.]
    // 
    // Also see discussion on this topic in:
    // http://www.antlr.org/wiki/pages/viewpage.action?pageId=1782.
    public class ANTLRNoCaseStringStream  extends ANTLRStringStream
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

    // Override CQLParser. This gives flexibility in altering default error
    // messages as well as accumulating multiple errors.
    public class CqlParserX extends CqlParser
    {
        private ArrayList<ParseError> errors;

        public CqlParserX(TokenStream input)
        {
            super(input);
            errors = new ArrayList<ParseError>();
        }

        protected void mismatch(IntStream input, int ttype, BitSet follow) 
        {
            throw new MismatchedTokenException(ttype, input);
        }

        public Object recoverFromMismatchedSet(IntStream input,
                                             RecognitionException re,
                                             BitSet follow) 
        {
            throw re;
        }

        public void displayRecognitionError(String[] tokenNames,
                                            RecognitionException e)
        {
            errors.add(new ParseError(this, e, tokenNames));
        }

        public ArrayList<ParseError> getErrors()
        {
            return errors;
        }
    }

    // Compile a CQL query
    public Plan compileQuery(String query) throws ParseException, SemanticException
    {
        CommonTree queryTree = null;
        CqlLexer lexer = null;
        CqlParserX parser = null;
        CommonTokenStream tokens = null;

        ANTLRStringStream input = new ANTLRNoCaseStringStream(query);

        lexer = new CqlLexer(input);
        tokens = new CommonTokenStream(lexer);
        parser = new CqlParserX(tokens);

        // built AST
        queryTree = (CommonTree)(parser.root().getTree());
        
        if (!parser.getErrors().isEmpty())
        {
            throw new ParseException(parser.getErrors());
        }

        if (!parser.errors.isEmpty())
        {
            throw new ParseException("parser error");
        }

        // Semantic analysis and code-gen.
        // Eventually, I anticipate, I'll be forking these off into two separate phases.
        return SemanticPhase.doSemanticAnalysis(queryTree);
    }
}
