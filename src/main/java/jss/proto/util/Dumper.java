package jss.proto.util;

import com.google.common.base.Throwables;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.EnumSet;
import jss.proto.define.MySQLType;
import jss.util.IsInteger;
import jss.util.Values;
import org.apache.commons.io.HexDump;

/**
 * 与 Dumper 类似,但是会输出更多调试相关的信息
 */
public class Dumper
{
    public static <T extends Enum<T> & IsInteger> String dump(long flags, Class<T> type)
    {
        EnumSet<T> set = Values.asEnumSet(flags, type);
        return String.format("%s -> %s", Long.toBinaryString(flags), set);
    }

    public static String charset(int charset)
    {
        String cs = CharsetUtil.getCharset(charset);
        if (cs == null)
        {
            return "未知的编码类型 (" + charset + ")";
        }
        return String.format("%s -> %s", charset, cs);
    }

    public static String sqlType(int sqlType)
    {
        MySQLType type = Values.fromValue(MySQLType.class, sqlType);
        if (type == null)
        {
            return "未知的 SQL 类型 (" + sqlType + ")";
        }
        return String.format("%s -> %s", sqlType, type);
    }

    public static String dump(ByteBuf buf)
    {
        return buf == null ? null : ByteBufUtil.hexDump(buf);
    }

    public static String hexDumpReadable(ByteBuf buf)
    {
        return hexDump(buf.array(), buf.readerIndex());
    }

    public static String hexDump(ByteBuf buf)
    {
        return hexDump(buf.array());
    }

    public static String hexDump(byte[] bytes)
    {
        return hexDump(bytes, 0);
    }

    public static String hexDump(byte[] bytes, int index)
    {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try
        {
            HexDump.dump(bytes, 0, os, index);
        } catch (IOException e)
        {
            Throwables.propagate(e);
        }
        return Stringer.string(os.toByteArray());
    }
}
