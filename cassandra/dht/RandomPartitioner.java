
/**
 * This class generates a BigIntegerToken using MD5 hash.
 */
public class RandomPartitioner implements IPartitioner
{
    private static final Comparator<String> comparator = new Comparator<String>()
    {
        public int compare(String o1, String o2)
        {
            String[] split1 = o1.split(":", 2);
            String[] split2 = o2.split(":", 2);
            BigInteger i1 = new BigInteger(split1[0]);
            BigInteger i2 = new BigInteger(split2[0]);
            int v = i1.compareTo(i2);
            if (v != 0) {
                return v;
            }
            return split1[1].compareTo(split2[1]);
        }
    };
    private static final Comparator<String> rcomparator = new Comparator<String>()
    {
        public int compare(String o1, String o2)
        {
            return -comparator.compare(o1, o2);
        }
    };

    public String decorateKey(String key)
    {
        return FBUtilities.hash(key).toString() + ":" + key;
    }

    public String undecorateKey(String decoratedKey)
    {
        return decoratedKey.split(":", 2)[1];
    }

    public Comparator<String> getDecoratedKeyComparator()
    {
        return comparator;
    }

    public Comparator<String> getReverseDecoratedKeyComparator()
    {
        return rcomparator;
    }

    public BigIntegerToken getDefaultToken()
    {
        String guid = GuidGenerator.guid();
        BigInteger token = FBUtilities.hash(guid);
        if ( token.signum() == -1 )
            token = token.multiply(BigInteger.valueOf(-1L));
        return new BigIntegerToken(token);
    }

    private final Token.TokenFactory<BigInteger> tokenFactory = new Token.TokenFactory<BigInteger>() {
        public byte[] toByteArray(Token<BigInteger> bigIntegerToken)
        {
            return bigIntegerToken.token.toByteArray();
        }

        public Token<BigInteger> fromByteArray(byte[] bytes)
        {
            return new BigIntegerToken(new BigInteger(bytes));
        }

        public String toString(Token<BigInteger> bigIntegerToken)
        {
            return bigIntegerToken.token.toString();
        }

        public Token<BigInteger> fromString(String string)
        {
            return new BigIntegerToken(new BigInteger(string));
        }
    };

    public Token.TokenFactory<BigInteger> getTokenFactory()
    {
        return tokenFactory;
    }

    public Token getInitialToken(String key)
    {
        return new BigIntegerToken(FBUtilities.hash(key));
    }
}