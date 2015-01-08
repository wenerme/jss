package jss.proto;

import org.apache.commons.codec.binary.Base64;

@SuppressWarnings("PointlessBitwiseExpression")
public class Protocols
{
    private Protocols() {}

    public static byte[] buildFixedInt(int size, long value)
    {
        byte[] packet = new byte[size];
        switch (size)
        {
            case 8:
                packet[0] = (byte) ((value >> 0) & 0xFF);
                packet[1] = (byte) ((value >> 8) & 0xFF);
                packet[2] = (byte) ((value >> 16) & 0xFF);
                packet[3] = (byte) ((value >> 24) & 0xFF);
                packet[4] = (byte) ((value >> 32) & 0xFF);
                packet[5] = (byte) ((value >> 40) & 0xFF);
                packet[6] = (byte) ((value >> 48) & 0xFF);
                packet[7] = (byte) ((value >> 56) & 0xFF);
                break;
            case 6:
                packet[0] = (byte) ((value >> 0) & 0xFF);
                packet[1] = (byte) ((value >> 8) & 0xFF);
                packet[2] = (byte) ((value >> 16) & 0xFF);
                packet[3] = (byte) ((value >> 24) & 0xFF);
                packet[4] = (byte) ((value >> 32) & 0xFF);
                packet[5] = (byte) ((value >> 40) & 0xFF);
                break;
            case 4:
                packet[0] = (byte) ((value >> 0) & 0xFF);
                packet[1] = (byte) ((value >> 8) & 0xFF);
                packet[2] = (byte) ((value >> 16) & 0xFF);
                packet[3] = (byte) ((value >> 24) & 0xFF);
                break;
            case 3:
                packet[0] = (byte) ((value >> 0) & 0xFF);
                packet[1] = (byte) ((value >> 8) & 0xFF);
                packet[2] = (byte) ((value >> 16) & 0xFF);
                break;
            case 2:

                packet[0] = (byte) ((value >> 0) & 0xFF);
                packet[1] = (byte) ((value >> 8) & 0xFF);
                break;
            case 1:
                packet[0] = (byte) ((value >> 0) & 0xFF);
                break;
            default:
                throw new AssertionError();
        }
        return packet;
    }

    public static byte[] buildLenencInt(long value)
    {
        byte[] packet;
        if (value < 251)
        {
            packet = new byte[1];
            packet[0] = (byte) ((value >> 0) & 0xFF);
        } else if (value < 65535)
        {
            packet = new byte[3];
            packet[0] = (byte) 0xFC;
            packet[1] = (byte) ((value >> 0) & 0xFF);
            packet[2] = (byte) ((value >> 8) & 0xFF);
        } else if (value < 16777215)
        {
            packet = new byte[4];
            packet[0] = (byte) 0xFD;
            packet[1] = (byte) ((value >> 0) & 0xFF);
            packet[2] = (byte) ((value >> 8) & 0xFF);
            packet[3] = (byte) ((value >> 16) & 0xFF);
        } else
        {
            packet = new byte[9];
            packet[0] = (byte) 0xFE;
            packet[1] = (byte) ((value >> 0) & 0xFF);
            packet[2] = (byte) ((value >> 8) & 0xFF);
            packet[3] = (byte) ((value >> 16) & 0xFF);
            packet[4] = (byte) ((value >> 24) & 0xFF);
            packet[5] = (byte) ((value >> 32) & 0xFF);
            packet[6] = (byte) ((value >> 40) & 0xFF);
            packet[7] = (byte) ((value >> 48) & 0xFF);
            packet[8] = (byte) ((value >> 56) & 0xFF);
        }

        return packet;
    }

    public static byte[] buildLenencStr(String str)
    {
        return buildLenencStr(str, false);
    }

    public static byte[] buildLenencStr(String str, boolean base64)
    {
        if (str.equals(""))
        {
            byte[] packet = new byte[1];
            packet[0] = 0x00;
            return packet;
        }

        int strsize = str.length();
        if (base64)
            strsize = Base64.decodeBase64(str).length;

        byte[] size = buildLenencInt(strsize);
        byte[] strByte = buildFixedStr(strsize, str, base64);
        byte[] packet = new byte[size.length + strByte.length];
        System.arraycopy(size, 0, packet, 0, size.length);
        System.arraycopy(strByte, 0, packet, size.length, strByte.length);
        return packet;
    }

    public static byte[] buildNullStr(String str)
    {
        return buildNullStr(str, false);
    }

    public static byte[] buildNullStr(String str, boolean base64)
    {
        int size = str.length() + 1;
        if (base64)
            size = Base64.decodeBase64(str).length + 1;
        return buildFixedStr(str.length() + 1, str, base64);
    }

    public static byte[] buildFixedStr(long size, String str)
    {
        return buildFixedStr((int) size, str);
    }

    public static byte[] buildFixedStr(long size, String str, boolean base64)
    {
        return buildFixedStr((int) size, str, base64);
    }

    public static byte[] buildFixedStr(int size, String str)
    {
        return buildFixedStr(size, str, false);
    }

    public static byte[] buildFixedStr(int size, String str, boolean base64)
    {
        byte[] packet = new byte[size];
        byte[] strByte = null;

        if (base64)
            strByte = Base64.decodeBase64(str);
        else
            strByte = str.getBytes();

        if (strByte.length < packet.length)
            size = strByte.length;
        System.arraycopy(strByte, 0, packet, 0, size);
        return packet;
    }

    public static byte[] buildEopStr(String str)
    {
        return buildEopStr(str, false);
    }

    public static byte[] buildEopStr(String str, boolean base64)
    {
        int size = str.length();
        if (base64)
            size = Base64.decodeBase64(str).length;
        return buildFixedStr(size, str, base64);
    }

    public static byte[] buildFiller(int len)
    {
        return buildFiller(len, (byte) 0x00);
    }

    public static byte[] buildFiller(int len, int filler_value)
    {
        return buildFiller(len, (byte) filler_value);
    }

    public static byte[] buildFiller(int len, byte filler_value)
    {
        byte[] filler = new byte[len];
        for (int i = 0; i < len; i++)
            filler[i] = filler_value;
        return filler;
    }

    public static byte[] buildByte(byte value)
    {
        byte[] field = new byte[1];
        field[0] = value;
        return field;
    }

    public static char int2char(byte i)
    {
        return (char) i;
    }

    public static byte char2int(char i)
    {
        return (byte) i;
    }

}
