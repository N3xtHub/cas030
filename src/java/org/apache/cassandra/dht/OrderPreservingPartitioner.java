 
public class OrderPreservingPartitioner implements IPartitioner
{
    // TODO make locale configurable.  But don't just leave it up to the OS or you could really screw
    // people over if they deploy on nodes with different OS locales.
    static final Collator collator = Collator.getInstance(new Locale("en", "US")); 

    private static final Comparator<String> comparator = new Comparator<String>() {
        public int compare(String o1, String o2)
        {
            return collator.compare(o1, o2);
        }
    };
    private static final Comparator<String> reverseComparator = new Comparator<String>() {
        public int compare(String o1, String o2)
        {
            return -comparator.compare(o1, o2);
        }
    };

    public String decorateKey(String key)
    {
        return key;
    }

    public String undecorateKey(String decoratedKey)
    {
        return decoratedKey;
    }

    public Comparator<String> getDecoratedKeyComparator()
    {
        return comparator;
    }

    public Comparator<String> getReverseDecoratedKeyComparator()
    {
        return reverseComparator;
    }

    public StringToken getDefaultToken()
    {
        String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random r = new Random();
        StringBuffer buffer = new StringBuffer();
        for (int j = 0; j < 16; j++) {
            buffer.append(chars.charAt(r.nextInt(chars.length())));
        }
        return new StringToken(buffer.toString());
    }

    private final Token.TokenFactory<String> tokenFactory = new Token.TokenFactory<String>() {
        public byte[] toByteArray(Token<String> stringToken)
        {
            return stringToken.token.getBytes("UTF-8");
        }

        public Token<String> fromByteArray(byte[] bytes)
        {
            return new StringToken(new String(bytes, "UTF-8"));
        }

        public String toString(Token<String> stringToken)
        {
            return stringToken.token;
        }

        public Token<String> fromString(String string)
        {
            return new StringToken(string);
        }
    };

    public Token.TokenFactory<String> getTokenFactory()
    {
        return tokenFactory;
    }

    public Token getInitialToken(String key)
    {
        return new StringToken(key);
    }
}
