
class BitSetSerializer
{
    public static void serialize(BitSet bs, DataOutputStream dos) throws IOException
    {
        ObjectOutputStream oos = new ObjectOutputStream(dos);
        oos.writeObject(bs);
        oos.flush();
    }

    public static BitSet deserialize(DataInputStream dis) throws IOException
    {
        ObjectInputStream ois = new ObjectInputStream(dis);
        try
        {
            return (BitSet) ois.readObject();
        }
        catch (ClassNotFoundException e)
        {
            throw new RuntimeException(e);
        }
    }
}
