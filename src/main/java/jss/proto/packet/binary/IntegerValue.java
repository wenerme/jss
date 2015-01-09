package jss.proto.packet.binary;

public class IntegerValue extends Field
{
    public long value;

    public IntegerValue(int expectedType)
    {
        super(expectedType);
    }

    public Long number()
    {
        return value;
    }
}
