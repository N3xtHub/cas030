
/**
 * An implementation of the DataInputStream interface. This instance is completely thread 
 * unsafe.
 */

public final class DataInputBuffer extends DataInputStream
{
    private static class Buffer extends ByteArrayInputStream
    {        
        public Buffer()
        {
            super(new byte[] {});
        }

        public void reset(byte[] input, int start, int length)
        {
            this.buf = input;
            this.count = start + length;
            this.mark = start;
            this.pos = start;
        }
         

        public int getLength()
        {
            return count;
        }
    }

    private Buffer buffer_;

    /** Constructs a new empty buffer. */
    public DataInputBuffer()
    {
        this(new Buffer());
    }

    private DataInputBuffer(Buffer buffer)
    {
        super(buffer);
        this.buffer_ = buffer;
    }
   
    /** Resets the data that the buffer reads. */
    public void reset(byte[] input, int length)
    {
        buffer_.reset(input, 0, length);
    }

    /** Resets the data that the buffer reads. */
    public void reset(byte[] input, int start, int length)
    {
        buffer_.reset(input, start, length);
    }

    /** Returns the length of the input. */
    public int getLength()
    {
        return buffer_.getLength();
    }

    public int getPosition()
    {
        return buffer_.getPosition();
    }
}
