package jss.server.net;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import java.nio.charset.StandardCharsets;
import jss.proto.define.CapabilityFlags;
import jss.proto.packet.Packet;
import jss.proto.packet.connection.HandshakeV10;
import jss.server.ServerConnection;

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
        handshake.challenge1 = buf("simple challenge");
        handshake.connectionId = 1;
        handshake.characterSet = 33;
        handshake.challenge2 = buf("second challenge");
        byte[] reserved = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        handshake.reserved = Unpooled.wrappedBuffer(reserved);
        handshake.authPluginName = buf("mysql_native_password");
        handshake.authPluginDataLength = handshake.authPluginName.readableBytes();
        handshake.statusFlags = 10;
        ctx.writeAndFlush(handshake);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Packet msg) throws Exception
    {
        System.out.println(msg);
    }
}
