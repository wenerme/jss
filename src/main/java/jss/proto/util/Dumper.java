package jss.proto.util;

import static java.nio.charset.StandardCharsets.UTF_8;

import com.google.common.base.Throwables;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.EnumSet;
import jss.util.IsInteger;
import jss.util.Values;
import org.apache.commons.io.HexDump;

public class Dumper
{
    public static <T extends Enum<T> & IsInteger> String dump(long flags, Class<T> type)
    {
        EnumSet<T> set = Values.asEnumSet(flags, type);
        return String.format("%s -> %s", Long.toBinaryString(flags), set);
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
        return string(os.toByteArray());
    }

    public static String string(byte[] bytes)
    {
        return new String(bytes, UTF_8);
    }

    public static String string(ByteBuf buf)
    {
        return buf == null ? null : buf.toString(UTF_8);
    }
}
