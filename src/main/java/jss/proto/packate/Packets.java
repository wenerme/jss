package jss.proto.packate;

import io.netty.buffer.ByteBuf;
import jss.proto.Protocol;
import jss.server.ServerConnection;

public class Packets
{
    private Packets() {}

    public static Protocol read(ServerConnection connection, ByteBuf buf)
    {
        return null;
    }

    public static void write(ServerConnection connection, ByteBuf buf, Protocol packet)
    {
    }
}
