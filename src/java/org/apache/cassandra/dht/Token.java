
public abstract class Token<T extends Comparable> implements Comparable<Token<T>>
{
    private static final TokenSerializer serializer = new TokenSerializer();

    T token;

    protected Token(T token)
    {
        this.token = token;
    }

    /**
     * This determines the comparison for node destination purposes.
     */
    public int compareTo(Token<T> o)
    {
        return token.compareTo(o.token);
    }

    public boolean equals(Object obj)
    {
        if (!(obj instanceof Token)) {
            return false;
        }
        return token.equals(((Token)obj).token);
    }

    public static abstract class TokenFactory<T extends Comparable>
    {
        public abstract byte[] toByteArray(Token<T> token);
        public abstract Token<T> fromByteArray(byte[] bytes);
        public abstract String toString(Token<T> token); // serialize as string, not necessarily human-readable
        public abstract Token<T> fromString(String string); // deserialize
    }
}
