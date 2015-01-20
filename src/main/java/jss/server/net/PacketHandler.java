package jss.server.net;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import java.nio.charset.StandardCharsets;
import jss.proto.define.CapabilityFlags;
import jss.proto.define.Flags;
import jss.proto.define.StatusFlags;
import jss.proto.packet.OK_Packet;
import jss.proto.packet.Packet;
import jss.proto.packet.connection.HandshakeResponse41;
import jss.proto.packet.connection.HandshakeV10;
import jss.proto.packet.text.COM_QUERY;
import jss.server.ServerConnection;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PacketHandler extends SimpleChannelInboundHandler<Packet> implements JSSDefine
{

    private ServerConnection connection;

    public static ByteBuf buf(String s)
    {
        return Unpooled.wrappedBuffer(s.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception
    {
        connection = ctx.channel().attr(SERVER_CONNECTION_ATTRIBUTE).get();
    }

    public void channelActive(ChannelHandlerContext ctx) throws Exception
    {
        HandshakeV10 handshake = new HandshakeV10();
        handshake.serverVersion = buf(connection.getServerVersion());
        handshake.capabilityFlags = CapabilityFlags.CLIENT_BASIC_FLAGS;
        handshake.challenge1 = buf("12345678");
        handshake.connectionId = 1;
        handshake.characterSet = 33;
        handshake.challenge2 = buf("1234567890123");
        byte[] reserved = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        handshake.reserved = Unpooled.wrappedBuffer(reserved);
        handshake.authPluginName = buf(Flags.MYSQL_NATIVE_PASSWORD);
        handshake.statusFlags = Flags.SERVER_STATUS_AUTOCOMMIT;
        ctx.writeAndFlush(handshake);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Packet msg) throws Exception
    {
        System.out.println(msg);
        if (msg.getClass() == HandshakeResponse41.class)
        {
            OK_Packet ok = new OK_Packet();
            ok.statusFlags = StatusFlags.SERVER_STATUS_AUTOCOMMIT;
            ctx.writeAndFlush(ok);
        }

        if (msg.getClass() == COM_QUERY.class)
        {
            throw new RuntimeException("I can not handle");
        }
    }
}
