package jss.server.net;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.util.ReferenceCountUtil;
import jss.proto.codec.PacketCodec;
import jss.proto.codec.PacketReader;
import jss.proto.packet.Packet;
import jss.proto.packet.PacketData;
import jss.server.ServerConnection;

public class PacketEncoder extends MessageToByteEncoder<Packet> implements JSSDefine
{
    private PacketData data = new PacketData();
    private ServerConnection connection;

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception
    {
        connection = ctx.channel().attr(SERVER_CONNECTION_ATTRIBUTE).get();
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Packet msg, ByteBuf out) throws Exception
    {
        out = Buffers.order(out);
        if (msg.getClass() == PacketData.class)
        {
            PacketCodec.writePacket(out, (PacketData) msg, 0);
            ctx.writeAndFlush(out);
            return;
        }
        try
        {
            data.payload = Buffers.buffer();
            PacketReader.writeGenericPacket(data.payload, msg, connection.getCapabilityFlags());
            data.payloadLength = data.payload.readableBytes();
            PacketCodec.writePacket(out, data, 0);
            ctx.writeAndFlush(out);
        } finally
        {
            ReferenceCountUtil.release(data.payload);
            data.payload = null;
        }
    }
}
