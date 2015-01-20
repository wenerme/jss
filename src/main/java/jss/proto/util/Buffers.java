package jss.proto.util;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import java.nio.ByteOrder;

public class Buffers
{
    private Buffers() {}

    public static ByteBuf order(ByteBuf buf)
    {
        return buf.order(ByteOrder.LITTLE_ENDIAN);
    }

    public static ByteBuf buffer()
    {
        return Unpooled.buffer().order(ByteOrder.LITTLE_ENDIAN);
    }

    public static int length(ByteBuf buf)
    {
        return buf == null ? 0 : buf.readableBytes();
    }
}
