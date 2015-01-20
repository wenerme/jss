package jss.server.net;

import static jss.proto.codec.PacketCodec.readPacket;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import java.util.List;
import jss.proto.codec.Codec;
import jss.proto.codec.Packets;
import jss.proto.packet.PacketData;
import jss.proto.packet.ProtocolText;
import jss.proto.packet.connection.HandshakeResponse41;
import jss.proto.util.Buffers;
import jss.server.ServerConnection;

public class PacketDecoder extends ByteToMessageDecoder implements JSSDefine
{

    private ServerConnection connection;
    private State state = State.HandshakeResponse;

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception
    {
        connection = ctx.channel().attr(SERVER_CONNECTION_ATTRIBUTE).get();
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception
    {
        if (in.readableBytes() < 4)
            return;

        in = Buffers.order(in);
        in.markReaderIndex();
        int length = Codec.int3(in);
        in.resetReaderIndex();
        if (in.readableBytes() < length + 4)
            return;

        PacketData data = readPacket(in, new PacketData(), connection.getCapabilityFlags());
        switch (state)
        {
            case HandshakeResponse:
            {
                HandshakeResponse41 response = readPacket(data.payload, new HandshakeResponse41(), connection
                        .getCapabilityFlags());
                out.add(response);
                state = State.Command;
            }
            break;
            case Command:
            {
                ProtocolText command = Packets.readCommandPacket(data.payload, connection.getCapabilityFlags());
                out.add(command);
            }
            break;
        }

    }

    public <T> T create(Class<T> type)
    {
        try
        {
            return type.newInstance();
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    public enum State
    {
        HandshakeResponse, Command
    }

}
