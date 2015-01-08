package jss.proto;

import static com.github.mpjct.jmpjct.mysql.proto.define.CapabilityFlags.*;
import static com.github.mpjct.jmpjct.mysql.proto.define.Flags.CLIENT_SESSION_TRACK;
import static com.github.mpjct.jmpjct.mysql.proto.define.Flags.CLIENT_TRANSACTIONS;
import static jss.proto.Codec.*;

import com.github.mpjct.jmpjct.mysql.proto.define.Flags;
import com.google.common.collect.Maps;
import io.netty.buffer.ByteBuf;
import java.nio.charset.StandardCharsets;
import jss.proto.packet.EOF_Packet;
import jss.proto.packet.ERR_Packet;
import jss.proto.packet.HandshakeResponse41;
import jss.proto.packet.HandshakeV10;
import jss.proto.packet.OK_Packet;
import jss.proto.packet.Packet;
import jss.proto.packet.PacketData;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PacketReader
{
    // ok,err,eof


    public static HandshakeResponse41 readPacket(ByteBuf buf, HandshakeResponse41 packet, int flags)
    {
        flags = packet.capabilityFlags = int4(buf);
        packet.maxPacketSize = int4(buf);
        packet.characterSet = int1(buf);
        packet.reserved = string_fix(buf, 23);
        packet.username = string_nul(buf);

        if (hasFlag(flags, CLIENT_PLUGIN_AUTH_LENENC_CLIENT_DATA))
        {
            packet.authResponseLen = int_lenenc(buf);
            packet.authResponse = string_fix(buf, (int) packet.authResponseLen);
        } else if (hasFlag(flags, CLIENT_SECURE_CONNECTION))
        {
            packet.authResponseLen = int1(buf);
            packet.authResponse = string_fix(buf, (int) packet.authResponseLen);
        } else
        {
            packet.authResponse = string_nul(buf);
        }

        if (hasFlag(flags, CLIENT_CONNECT_WITH_DB))
        {
            packet.database = string_nul(buf);
        }

        if (hasFlag(flags, CLIENT_PLUGIN_AUTH))
        {
            packet.authPluginName = string_nul(buf);
        }

        if (hasFlag(flags, CLIENT_CONNECT_ATTRS))
        {
            packet.keyValuesLength = int_lenenc(buf);
//            packet.key = string_lenenc(buf);
//            packet.value = string_lenenc(buf);

            packet.attributes = Maps.newHashMap();
            if (hasMore(buf))
            {
                do
                {
                    packet.attributes.put(
                            string_lenenc(buf).toString(StandardCharsets.UTF_8),
                            string_lenenc(buf).toString(StandardCharsets.UTF_8));
                } while (hasMore(buf));
            }
        }

        return packet;
    }

    public static HandshakeV10 readPacket(ByteBuf buf, HandshakeV10 packet, int flags)
    {
        packet.protocolVersion = int1(buf);
        packet.serverVersion = string_nul(buf);
        packet.connectionId = int4(buf);
        packet.challenge1 = string_fix(buf, 8);
        packet.filter = int1(buf);
        packet.capabilityFlags = int2(buf);
        if (hasMore(buf))
        {
            packet.characterSet = int1(buf);
            packet.statusFlags = int2(buf);
            packet.capabilityFlags |= (int2(buf) << 16);// upper 2 bytes

            if (hasFlag(flags, CLIENT_PLUGIN_AUTH))
            {
                packet.authPluginDataLength = int1(buf);
            } else
            {
                buf.readByte();// consume this byte
            }

            packet.reserved = string_fix(buf, 10);

            if (hasFlag(flags, CLIENT_SECURE_CONNECTION))
            {
                packet.challenge2 = string_fix(buf, Math.max(13, packet.authPluginDataLength - 8));
            }

            if (hasFlag(flags, CLIENT_PLUGIN_AUTH))
            {
                packet.authPluginName = string_nul(buf);
            }
        }
        return packet;
    }

    private static boolean hasMore(ByteBuf buf) {return buf.readableBytes() > 0;}

    public static EOF_Packet readPacket(ByteBuf buf, EOF_Packet packet, int flags)
    {
        packet.header = int1(buf);
        if (hasFlag(flags, CLIENT_PROTOCOL_41))
        {
            packet.warnings = int2(buf);
            packet.statusFlags = int2(buf);
        }
        return packet;
    }

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
