
public interface IScanner<T> extends Closeable
{
    public boolean hasNext() throws IOException;
    public T next() throws IOException;
    public void fetch(String key, String cf) throws IOException;    
}
