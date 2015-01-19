package jss.server.net;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import jss.proto.codec.PacketCodec;
import jss.proto.codec.PacketReader;
import jss.proto.packet.Packet;
import jss.proto.packet.PacketData;
import jss.server.ServerConnection;

public class PacketEncoder extends MessageToByteEncoder<Packet> implements JSSDefine
{
    private ThreadLocal<PacketData> localData = new ThreadLocal<PacketData>()
    {
        @Override
        protected PacketData initialValue()
        {
            return new PacketData();
        }
    };
    private ServerConnection connection;

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception
    {
        connection = ctx.attr(SERVER_CONNECTION_ATTRIBUTE).get();
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Packet msg, ByteBuf out) throws Exception
    {
        if (msg.getClass() == PacketData.class)
        {
            PacketCodec.writePacket(out, (PacketData) msg, 0);
            ctx.writeAndFlush(out);
            return;
        }

        PacketData data = localData.get();
        data.payload = Unpooled.buffer();
        PacketReader.writeGenericPacket(data.payload, msg, 0);
        data.payloadLength = data.payload.readableBytes();
        PacketCodec.writePacket(out, data, 0);
        data.payload.release();
        data.payload = null;
        ctx.writeAndFlush(out);
    }
}
