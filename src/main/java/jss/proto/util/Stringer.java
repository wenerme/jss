package jss.proto.util;

import static java.nio.charset.StandardCharsets.UTF_8;

import io.netty.buffer.ByteBuf;

/**
 * 将相应类型转换为字符串
 */
public class Stringer
{
    public static String string(byte[] bytes)
    {
        return new String(bytes, UTF_8);
    }

    public static String string(ByteBuf buf)
    {
        return buf == null ? null : buf.toString(UTF_8);
    }
}
