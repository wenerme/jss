package jss.server.net;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import jss.proto.packet.Packet;

public class PacketEncoder extends MessageToByteEncoder<Packet>
{
    @Override
    protected void encode(ChannelHandlerContext ctx, Packet msg, ByteBuf out) throws Exception
    {

    }
}
