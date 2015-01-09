package jss.proto.codec;

import static jss.proto.define.Flags.*;

import com.google.common.base.Preconditions;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import jss.proto.packet.binary.BinaryValue;
import jss.proto.packet.binary.DateValue;
import jss.proto.packet.binary.Field;
import jss.proto.packet.binary.Fields;
import jss.proto.packet.binary.FloatValue;
import jss.proto.packet.binary.IntegerValue;
import jss.proto.packet.binary.TimeValue;
import lombok.extern.slf4j.Slf4j;

/**
 * @see <a href=http://dev.mysql.com/doc/internals/en/describing-packets.html>packets</a>
 */
@Slf4j
public class Codec
{

    public static final Charset CHARSET = StandardCharsets.UTF_8;

    //region 字段类型编码

    public static TimeValue readTime(ByteBuf buf, TimeValue packet)
    {
        // 0 8 12
        int length = int1(buf);
        if (length == 0)
        {
            packet.isNegative = false;
            packet.hours = packet.minutes = packet.seconds = 0;
            packet.microSeconds = 0;
            return packet;
        }
        packet.isNegative = int1(buf) == 1;
        packet.days = int4(buf);
        packet.hours = int1(buf);
        packet.minutes = int1(buf);
        packet.seconds = int1(buf);
        if (length == 8)
            return packet;
        packet.microSeconds = int4(buf);
        return packet;
    }

    public static DateValue readDate(ByteBuf buf, DateValue packet)
    {
        // 0 4 7 11
        int length = int1(buf);
        if (length == 0)
        {
            packet.year = packet.month = packet.day = packet.hour = 0;
            packet.minute = packet.second = packet.microSecond = 0;
            return packet;
        }
        packet.year = int2(buf);
        packet.month = int1(buf);
        packet.day = int1(buf);
        if (length == 4)
            return packet;
        packet.hour = int1(buf);
        packet.minute = int1(buf);
        packet.second = int1(buf);
        if (length == 7)
            return packet;
        packet.microSecond = int4(buf);

        return packet;
    }

    public static Field readField(ByteBuf buf, int type)
    {
        // 没有处理 MYSQL_TYPE_DATE2 等类型
        Field field = Fields.createField(type);
        switch (type)
        {
            case MYSQL_TYPE_STRING:
            case MYSQL_TYPE_VARCHAR:
            case MYSQL_TYPE_VAR_STRING:
            case MYSQL_TYPE_ENUM:
            case MYSQL_TYPE_SET:
            case MYSQL_TYPE_LONG_BLOB:
            case MYSQL_TYPE_MEDIUM_BLOB:
            case MYSQL_TYPE_BLOB:
            case MYSQL_TYPE_TINY_BLOB:
            case MYSQL_TYPE_GEOMETRY:
            case MYSQL_TYPE_BIT:
            case MYSQL_TYPE_DECIMAL:
            case MYSQL_TYPE_NEWDECIMAL:
                ((BinaryValue) field).value = string_lenenc(buf);
                break;
            case MYSQL_TYPE_LONGLONG:
                ((IntegerValue) field).value = int8(buf);
                break;
            case MYSQL_TYPE_LONG:
            case MYSQL_TYPE_INT24:
                ((IntegerValue) field).value = int4(buf);
                break;
            case MYSQL_TYPE_SHORT:
            case MYSQL_TYPE_YEAR:
                ((IntegerValue) field).value = int2(buf);
                break;
            case MYSQL_TYPE_TINY:
                ((IntegerValue) field).value = int1(buf);
                break;
            case MYSQL_TYPE_DOUBLE:
                ((FloatValue) field).value = buf.readDouble();
                break;
            case MYSQL_TYPE_FLOAT:
                ((FloatValue) field).value = buf.readFloat();
                break;
            case MYSQL_TYPE_DATE:
            case MYSQL_TYPE_DATETIME:
            case MYSQL_TYPE_TIMESTAMP:
                readDate(buf, ((DateValue) field));
                break;
            case MYSQL_TYPE_TIME:
                readTime(buf, (TimeValue) field);
                break;
            case MYSQL_TYPE_NULL:
//                break;
            default:
                throw new AssertionError("No suitable method to read type " + field);
        }
        return field;
    }

    //endregion

    // region 整数类型操作

    /**
     * 写入一个长度编码的整数
     *
     * @return 返回长度, 1 2 3 8
     */
    public static int int_lenenc(ByteBuf buf, long value)
    {
        int size;
        if (value < 251)
        {
            size = 1;
            buf.writeByte(1);
        } else if (value < 65535)
        {
            size = 2;
            buf.writeByte(0xFC);
        } else if (value < 16777215)
        {
            size = 3;
            buf.writeByte(0xFD);
        } else
        {
            size = 8;
            buf.writeByte(0xFE);
        }

        return int_fixed(buf, value, size);
    }

    /**
     * 写入固定长度的整数
     *
     * @param size 整数长度 1 2 3 4 6 8
     * @return size
     */
    public static int int_fixed(ByteBuf buf, long value, int size)
    {
        int i = (int) value;
        switch (size)
        {
            case 1:
                buf.writeByte(i);
                break;
            case 2:
                buf.writeShort(i);
                break;
            case 3:
                buf.writeMedium(i);
                break;
            case 4:
                buf.writeInt(i);
                break;
            case 6:
                // 2+4
                buf.writeShort((int) (value >> 32 & 0xffff))
                   .writeInt(i);
                break;
            case 8:
                buf.writeLong(value);
                break;
            default:
                throw new AssertionError();
        }
        return size;
    }

    /**
     * 读取长度编码的整数
     * <p/>
     * <b>Note</b> 长度只可能为 1 2 3 8
     */
    public static long int_lenenc(ByteBuf buf)
    {
        int i = buf.readUnsignedByte();

        int size;
        // 1 byte int
        if (i < 251)
        {
//            size = 1;
            return i;
        }
        // 2 byte int
        else if (i == 252)
        {
            size = 2;
        }
        // 3 byte int
        else if (i == 253)
        {
            size = 3;
        }
        // 8 byte int
        else if (i == 254)
        {
            size = 8;
        } else
        {
            log.error("Unknown int size,Decoding int at offset {} get {}.",
                    buf.readerIndex(), i);
            return -1;
        }

        return int_fixed(buf, size);
    }

    /**
     * 读取固定长度的整数值
     *
     * @param size 整数长度
     * @return 返回读取到的整数值
     */
    public static long int_fixed(ByteBuf buf, int size)
    {
        switch (size)
        {
            case 1:
                return buf.readByte();
            case 2:
                return buf.readShort();
            case 3:
                return buf.readMedium();
            case 4:
                return buf.readInt();
            case 6:
                // 4 + 2
                return (buf.readInt() << 16) | buf.readShort();
            case 8:
                return buf.readLong();
            default:
                throw new AssertionError();
        }
    }

    public static byte int1(ByteBuf buf)
    {
        return (byte) int_fixed(buf, 1);
    }

    public static short int2(ByteBuf buf)
    {
        return (short) int_fixed(buf, 2);
    }

    public static int int3(ByteBuf buf)
    {
        return (int) int_fixed(buf, 3);
    }

    public static int int4(ByteBuf buf)
    {
        return (int) int_fixed(buf, 4);
    }

    public static long int6(ByteBuf buf)
    {
        return int_fixed(buf, 6);
    }

    public static long int8(ByteBuf buf)
    {
        return int_fixed(buf, 8);
    }

    // endregion

    // region 字符串操作


    public static ByteBuf string_lenenc(ByteBuf buf)
    {
        return string_fix(buf, (int) int_lenenc(buf));
    }

    public static ByteBuf string_fix(ByteBuf buf, int size)
    {
        return buf.readBytes(size);
    }

    public static ByteBuf string_var(ByteBuf buf, int size)
    {
        return buf.readBytes(size);
    }

    public static ByteBuf string_eof(ByteBuf buf)
    {
        return buf.readBytes(buf.readableBytes());
    }

    public static ByteBuf string_nul(ByteBuf buf)
    {
        int size = buf.indexOf(buf.readerIndex(), buf.readableBytes() + buf.readerIndex(), (byte) 0);
        Preconditions.checkArgument(size >= 0);
        size = size - buf.readerIndex();
        if (size == 0)
        {
            return buf.alloc().buffer(0);
        }
        // do not read the null
        ByteBuf result = string_fix(buf, size);
        buf.readByte();// consume the null byte
        return result;
    }

    public static ByteBuf string_lenenc(ByteBuf buf, String string)
    {
        ByteBuf wrappedBuffer = Unpooled.wrappedBuffer(string.getBytes(CHARSET));
        string_lenenc(buf, wrappedBuffer);
        return buf;
    }

    public static ByteBuf string_lenenc(ByteBuf buf, ByteBuf string)
    {
        int_lenenc(buf, string.readableBytes());
        return buf.writeBytes(string);
    }

    public static ByteBuf string_fix(ByteBuf buf, String string, int size)
    {
        buf.writeBytes(string.substring(0, size).getBytes(CHARSET));
        return buf;
    }

    public static ByteBuf string_fix(ByteBuf buf, ByteBuf string, int size)
    {
        buf.writeBytes(string, size);
        return string;
    }

    public static ByteBuf string(ByteBuf buf, ByteBuf string)
    {
        buf.writeBytes(string, string.readableBytes());
        return string;
    }

    public static ByteBuf string_nul(ByteBuf buf, ByteBuf string)
    {
        buf.writeBytes(string).writeByte(0);
        return string;
    }

    public static String asString(ByteBuf buf)
    {
        return buf.toString(CHARSET);
    }

    public static byte[] asBytes(String string)
    {
        return asBytes(string, string.length());
    }

    public static byte[] asBytes(String string, int length)
    {
        return string.substring(0, length)
                     .getBytes(CHARSET);
    }

    // endregion

    public static enum Type
    {
        int_lenenc, int1, int2, int3, int4, int6, int8,
        string_lenenc, string_var, string_fix, string_eof, string_nul
    }
}
