package jss.proto.codec;

import io.netty.buffer.ByteBuf;
import jss.proto.packet.Packet;

public class PacketWriter
{
    interface Writer<T extends Packet>
    {
        ByteBuf write(ByteBuf buf, T packet, int capabilityFlags);
    }
}
