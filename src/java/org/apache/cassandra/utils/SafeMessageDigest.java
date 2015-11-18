
public class SafeMessageDigest
{
    private MessageDigest md_ = null;

    public static SafeMessageDigest digest_;
    static
    {
        try
        {
            digest_ = new SafeMessageDigest(MessageDigest.getInstance("SHA-1"));
        }
        catch (NoSuchAlgorithmException e)
        {
            assert (false);
        }
    }

    public SafeMessageDigest(MessageDigest md)
    {
        md_ = md;
    }

    public synchronized void update(byte[] theBytes)
    {
        md_.update(theBytes);
    }

    //NOTE: This should be used instead of seperate update() and then digest()
    public synchronized byte[] digest(byte[] theBytes)
    {
        //this does an implicit update()
        return md_.digest(theBytes);
    }

    public synchronized byte[] digest()
    {
        return md_.digest();
    }

    public byte[] unprotectedDigest()
    {
        return md_.digest();
    }

    public void unprotectedUpdate(byte[] theBytes)
    {
        md_.update(theBytes);
    }

    public byte[] unprotectedDigest(byte[] theBytes)
    {
        return md_.digest(theBytes);
    }

    public int getDigestLength()
    {
        return md_.getDigestLength();
    }
}
