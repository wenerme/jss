package jss.proto.codec;

import static jss.proto.define.Flags.*;

import com.google.common.base.Preconditions;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import jss.proto.packet.EOF_Packet;
import jss.proto.packet.Packet;
import jss.proto.packet.PacketData;
import jss.proto.packet.binary.BinaryValue;
import jss.proto.packet.binary.DateValue;
import jss.proto.packet.binary.Field;
import jss.proto.packet.binary.Fields;
import jss.proto.packet.binary.FloatValue;
import jss.proto.packet.binary.IntegerValue;
import jss.proto.packet.binary.TimeValue;
import jss.util.IsInteger;
import jss.util.Values;
import lombok.extern.slf4j.Slf4j;

/**
 * 包内基本元素编码
 *
 * @see <a href=http://dev.mysql.com/doc/internals/en/describing-packets.html>packets</a>
 */
@Slf4j
public class Codec
{

    private static final Charset CHARSET = StandardCharsets.UTF_8;


    /**
     * 读取状态包.
     * <p/>
     * <ul>
     * <li>读取一个 PacketData</li>
     * <li>从 PacketData.payload 中读取状态包</li>
     * </ul>
     *
     * @return 状态包, EOF, ERR, OK 或 null
     */
    public static Packet readStatus(ByteBuf buf, PacketData packet, int flags)
    {
        PacketCodec.readPacket(buf, packet, flags);
        return readStatus(packet.payload, flags);
    }

    /**
     * 读取状态包.会认为 buf 目前是 PacketData 的 payload
     *
     * @return 状态包, EOF, ERR, OK 或 null
     */
    public static Packet readStatus(ByteBuf buf, int flags)
    {
        Status status = getStatus(buf);
        switch (status)
        {
            case EOF:
                return PacketCodec.readPacket(buf, new EOF_Packet(), flags);
            case ERR:
                return PacketCodec.readPacket(buf, new EOF_Packet(), flags);
            case OK:
                return PacketCodec.readPacket(buf, new EOF_Packet(), flags);
            case UNKNOWN:
                return null;
            default:
                throw new AssertionError();
        }
    }

    /**
     * 获取 buf 表示的状态包.会认为 buf 目前是 PacketData 的 payload
     * <p/>
     * 不改变 buf 状态
     */
    public static Status getStatus(ByteBuf buf)
    {
        return Values.fromValue(Status.class, (int) buf.getUnsignedByte(buf.readerIndex()), Status.UNKNOWN);
    }

    /**
     * 获取一个包,并返回其状态.会认为 buf 目前是一个 PacketData
     * <p/>
     * 不改变 buf 状态
     */
    public static Status getPacketStatus(ByteBuf buf)
    {
        int status = (int) buf.getUnsignedByte(buf.readerIndex() + 4);
        return Values.fromValue(Status.class, status, Status.UNKNOWN);
    }

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
    public static ByteBuf int_lenenc(ByteBuf buf, long value)
    {
        int size;
        if (value < 251)
        {
            size = 1;
            // 为 1 的不写入长度
            return buf.writeByte((int) value);
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
     * @return buf
     */
    public static ByteBuf int_fixed(ByteBuf buf, long value, int size)
    {
        int v = (int) value;
        switch (size)
        {
            case 1:
                buf.writeByte(v);
                break;
            case 2:
                buf.writeShort(v);
                break;
            case 3:
                buf.writeMedium(v);
                break;
            case 4:
                buf.writeInt(v);
                break;
            case 6:
                // 2+4
                buf.writeShort((int) (value >> 32))
                   .writeInt(v);
                break;
            case 8:
                buf.writeLong(value);
                break;
            default:
                throw new AssertionError();
        }
        return buf;
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

    public static ByteBuf int_fixed(ByteBuf buf, int v, int size)
    {
        switch (size)
        {
            case 1:
                return buf.writeByte(v);
            case 2:
                return buf.writeShort(v);
            case 3:
                return buf.writeMedium(v);
            case 4:
                return buf.writeInt(v);
            case 6:
                buf.writeShort(0);
                return buf.writeInt(v);
            case 8:
                return buf.writeLong(v);
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

    public static ByteBuf int1(ByteBuf buf, int val)
    {
        return int_fixed(buf, val, 1);
    }

    public static ByteBuf int2(ByteBuf buf, int val)
    {
        return int_fixed(buf, val, 2);
    }

    public static ByteBuf int3(ByteBuf buf, int val)
    {
        return int_fixed(buf, val, 3);
    }

    public static ByteBuf int4(ByteBuf buf, int val)
    {
        return int_fixed(buf, val, 4);
    }

    public static ByteBuf int1(ByteBuf buf, long val)
    {
        return int_fixed(buf, val, 1);
    }

    public static ByteBuf int2(ByteBuf buf, long val)
    {
        return int_fixed(buf, val, 2);
    }

    public static ByteBuf int3(ByteBuf buf, long val)
    {
        return int_fixed(buf, val, 3);
    }

    public static ByteBuf int4(ByteBuf buf, long val)
    {
        return int_fixed(buf, val, 4);
    }

    public static ByteBuf int6(ByteBuf buf, long val)
    {
        return int_fixed(buf, val, 6);
    }

    public static ByteBuf int8(ByteBuf buf, long val)
    {
        return int_fixed(buf, val, 8);
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

    public static ByteBuf string_var(ByteBuf buf, ByteBuf string, int size)
    {
        return buf.writeBytes(string, string.readerIndex(), size);
    }

    public static ByteBuf string_eof(ByteBuf buf)
    {
        return buf.readBytes(buf.readableBytes());
    }

    public static ByteBuf string_eof(ByteBuf buf, ByteBuf string)
    {
        return writeReadable(buf, string);
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
        return writeReadable(buf, string);
    }

    public static ByteBuf string_fix(ByteBuf buf, String string, int size)
    {
        return buf.writeBytes(string.substring(0, size).getBytes(CHARSET));
    }

    public static ByteBuf string_fix(ByteBuf buf, ByteBuf string, int size)
    {
        return buf.writeBytes(string, string.readerIndex(), size);
    }

    public static ByteBuf string(ByteBuf buf, ByteBuf string)
    {
        return string_eof(buf, string);
    }

    public static ByteBuf string_nul(ByteBuf buf, ByteBuf string)
    {
//        if (string != null)
        writeReadable(buf, string);
        return buf.writeByte(0);
    }

    /**
     * 不会影响 src 的 readerIndex
     */
    private static ByteBuf writeReadable(ByteBuf to, ByteBuf src)
    {
        return to.writeBytes(src, src.readerIndex(), src.readableBytes());
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

    public static enum Status implements IsInteger
    {
        EOF(0xfe), ERR(0xff), OK(0x00), UNKNOWN(-1);

        private final int value;

        Status(int value) {this.value = value;}

        @Override
        public Integer get()
        {
            return value;
        }

        public boolean ok()
        {
            return this == OK;
        }

        public boolean err()
        {
            return this == ERR;
        }

        public boolean eof()
        {
            return this == EOF;
        }

        public boolean unknown()
        {
            return this == UNKNOWN;
        }

    }
}
