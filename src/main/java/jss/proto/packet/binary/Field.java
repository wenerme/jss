package jss.proto.packet.binary;

import jss.proto.packet.ProtocolBinary;

/**
 * 字段
 */
public class Field implements ProtocolBinary
{
    public final int expectedType;
    public int type;

    public Field(int expectedType) {type = this.expectedType = expectedType;}

    @Override
    public String toString()
    {
        return "FieldPacket{" +
                "expectedType=" + expectedType +
                ", type=" + type +
                '}';
    }
}
