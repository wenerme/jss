package jss.server.net;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import java.util.List;
import jss.server.ServerConnection;

public class PacketDecoder extends ByteToMessageDecoder implements JSSDefine
{
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception
    {
        ServerConnection serverConnection = ctx.attr(SERVER_CONNECTION_ATTRIBUTE).get();
        switch (serverConnection.getConnectionPhase())
        {

        }
    }
}
