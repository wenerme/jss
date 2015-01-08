package jss.proto;

import static com.github.mpjct.jmpjct.mysql.proto.define.CapabilityFlags.CLIENT_PROTOCOL_41;
import static com.github.mpjct.jmpjct.mysql.proto.define.Flags.CLIENT_SESSION_TRACK;
import static com.github.mpjct.jmpjct.mysql.proto.define.Flags.CLIENT_TRANSACTIONS;
import static jss.proto.Codec.*;

import com.github.mpjct.jmpjct.mysql.proto.define.Flags;
import io.netty.buffer.ByteBuf;
import jss.proto.packet.ERR_Packet;
import jss.proto.packet.OK_Packet;
import jss.proto.packet.Packet;
import jss.proto.packet.PacketData;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PacketReader
{

    public static ERR_Packet readPacket(ByteBuf buf, ERR_Packet packet, int flags)
    {
        packet.header = int1(buf);
        packet.errorCode = int2(buf);
        if (hasFlag(flags, CLIENT_PROTOCOL_41))
        {
            packet.sqlStateMarker = string_fix(buf, 1);
            packet.sqlState = string_fix(buf, 5);
        }
        packet.errorMessage = string_eof(buf);
        return packet;
    }

    public static OK_Packet readPacket(ByteBuf buf, OK_Packet packet, int flags)
    {
        packet.header = int1(buf);
        packet.affectedRows = int_lenenc(buf);
        packet.lastInsertId = int_lenenc(buf);
        if (hasFlag(flags, CLIENT_PROTOCOL_41))
        {
            packet.statusFlags = int2(buf);
            packet.warnings = int2(buf);
        } else if (hasFlag(flags, CLIENT_TRANSACTIONS))
        {
            packet.statusFlags = int2(buf);
        }

        if (hasFlag(flags, CLIENT_SESSION_TRACK))
        {
            packet.info = string_lenenc(buf);

            if ((packet.statusFlags & Flags.SERVER_SESSION_STATE_CHANGED) > 0)
            {
                packet.sessionStateChanges = string_lenenc(buf);
            }
        } else
        {
            packet.info = string_eof(buf);
        }


        return packet;
    }

    private static boolean hasFlag(int flags, int flag) {return (flags & flag) > 0;}

    public static PacketData readPacket(ByteBuf buf, PacketData packet, int flags)
    {
        packet.payloadLength = Codec.int3(buf);
        packet.sequenceId = Codec.int1(buf);
        packet.payload = Codec.string_var(buf, packet.payloadLength);
        return packet;
    }

    interface Reader<P extends Packet>
    {
        P readPacket(ByteBuf buf, P packet, int flags);
    }
}
