
public interface IPartitioner
{
    /**
     * transform key to on-disk format s.t. keys are stored in node comparison order.
     * this lets bootstrap rip out parts of the sstable sequentially instead of doing random seeks.
     *
     * @param key the raw, client-facing key
     * @return decorated on-disk version of key
     */
    public String decorateKey(String key);

    public String undecorateKey(String decoratedKey);

    public Comparator<String> getDecoratedKeyComparator();

    public Comparator<String> getReverseDecoratedKeyComparator();

    /**
     * @return the token to use for this node if none was saved
     */
    public Token getInitialToken(String key);

    public Token getDefaultToken();

    public Token.TokenFactory getTokenFactory();
}
