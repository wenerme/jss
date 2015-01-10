package jss.proto.util;

import static java.nio.charset.StandardCharsets.UTF_8;

import io.netty.buffer.ByteBuf;
import jss.proto.define.Flags;

/**
 * 将相应类型转换为字符串
 */
public class Stringer
{
    public static String string(byte[] bytes)
    {
        return new String(bytes, UTF_8);
    }

    /**
     * 将 {@link jss.proto.packet.text.ResultsetRow} 的一个单元格内容转为 String
     */
    public static String cell(ByteBuf cell)
    {
        if (cell != null && cell.readableBytes() > 0 && cell.getUnsignedByte(cell.readerIndex()) == Flags.NULL_CELL)
        {
            return "NULL";
        } else
        {
            return string(cell);
        }
    }

    public static String string(ByteBuf buf)
    {
        return buf == null ? null : buf.toString(UTF_8);
    }
}
