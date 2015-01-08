package jss.proto;

import io.netty.buffer.ByteBuf;
import java.util.EnumSet;
import jss.proto.define.CapabilityFlag;
import jss.proto.packet.Packet;

public class PacketWriter
{
    interface Writer<T extends Packet>
    {
        void write(ByteBuf buf, T packet, EnumSet<CapabilityFlag> capabilityFlags);
    }
}
