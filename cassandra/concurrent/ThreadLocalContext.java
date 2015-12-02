
/**
 * Use this implementation over Java's ThreadLocal or InheritableThreadLocal when 
 * you need to add multiple key/value pairs into ThreadLocalContext for a given thread.
 */


public class ThreadLocalContext
{
    private static InheritableThreadLocal<Context> tls_ = new InheritableThreadLocal<Context>();

    public static void put(Context value)
    {
        tls_.set(value);
    }

    public static Context get()
    {
        return tls_.get();
    }
}