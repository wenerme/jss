package jss.proto.packet.binary;

public class FloatValue extends Field
{
    public double value;

    public FloatValue(int expectedType)
    {
        super(expectedType);
    }

    public Double number()
    {
        return value;
    }
}
