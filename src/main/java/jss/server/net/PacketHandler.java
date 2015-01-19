package jss.server.net;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import java.nio.charset.StandardCharsets;
import jss.proto.define.CapabilityFlags;
import jss.proto.packet.Packet;
import jss.proto.packet.connection.HandshakeV10;

public class PacketHandler extends SimpleChannelInboundHandler<Packet>
{

    public static ByteBuf buf(String s) {return Unpooled.wrappedBuffer(s.getBytes(StandardCharsets.UTF_8));}

    public void channelRegistered(ChannelHandlerContext ctx) throws Exception
    {
        HandshakeV10 handshake = new HandshakeV10();
        handshake.serverVersion = buf("JSS v1.0");
        handshake.capabilityFlags = CapabilityFlags.CLIENT_BASIC_FLAGS;
        handshake.challenge1 = buf("simple challenge");
        handshake.connectionId = 1;
        handshake.characterSet = 33;
        handshake.challenge2 = buf("second challenge");
        handshake.reserved = Unpooled
                .wrappedBuffer(new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0});
        handshake.authPluginName = buf("mysql_native_password");
        handshake.authPluginDataLength = handshake.authPluginName.readableBytes();
        handshake.statusFlags = 10;
        ctx.writeAndFlush(handshake);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Packet msg) throws Exception
    {

    }
}
