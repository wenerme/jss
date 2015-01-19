package jss.server.net;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import java.nio.ByteOrder;

public class Buffers
{
    public static ByteBuf order(ByteBuf buf)
    {
        return buf.order(ByteOrder.LITTLE_ENDIAN);
    }

    public static ByteBuf buffer()
    {
        return Unpooled.buffer().order(ByteOrder.LITTLE_ENDIAN);
    }
}
