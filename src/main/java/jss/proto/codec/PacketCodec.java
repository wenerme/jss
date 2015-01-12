package jss.proto.codec;

import static java.nio.charset.StandardCharsets.UTF_8;
import static jss.proto.codec.Codec.*;
import static jss.proto.define.CapabilityFlags.*;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import java.util.Map;
import jss.proto.define.Flags;
import jss.proto.packet.EOF_Packet;
import jss.proto.packet.ERR_Packet;
import jss.proto.packet.OK_Packet;
import jss.proto.packet.PacketData;
import jss.proto.packet.connection.AuthSwitchRequest;
import jss.proto.packet.connection.HandshakeResponse41;
import jss.proto.packet.connection.HandshakeV10;
import jss.proto.packet.text.ColumnDefinition320;
import jss.proto.packet.text.ColumnDefinition41;
import jss.proto.packet.text.CommandPacket;

/**
 * 单个独立包编码操作
 */
// 方法操作替换
// ([\w.]+)[^=]*=\s*([^,)\n\t]+)(.[^\n\r]+)
// $2,$1$3
public class PacketCodec
{
    public static AuthSwitchRequest readPacket(ByteBuf buf, AuthSwitchRequest packet, int flags)
    {
        packet.status = int1(buf);
        packet.pluginName = string_nul(buf);
        packet.authPluginData = string_eof(buf);
        return packet;
    }

    public static ByteBuf writePacket(ByteBuf buf, AuthSwitchRequest packet, int flags)
    {
        int1(buf, (int) packet.status);
        string_nul(buf, packet.pluginName);
        string_eof(buf, packet.authPluginData);

        return buf;
    }

    public static HandshakeResponse41 readPacket(ByteBuf buf, HandshakeResponse41 packet, int flags)
    {
        // 这里使用的是解析出来的 flags
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
            packet.attributes = Maps.newHashMap();

            if (hasMore(buf))
            {
                do
                {
                    String key = string_lenenc(buf).toString(UTF_8);
                    String value = string_lenenc(buf).toString(UTF_8);
                    packet.attributes.put(key, value);
                } while (hasMore(buf));
            }
        }

        return packet;
    }

    public static ByteBuf writePacket(ByteBuf buf, HandshakeResponse41 packet, int flags)
    {
        // 这里使用的是解析出来的 flags
        int4(buf, packet.capabilityFlags);
        int4(buf, packet.maxPacketSize);
        int1(buf, packet.characterSet);
        string_fix(buf, packet.reserved, 23);
        string_nul(buf, packet.username);

        if (hasFlag(flags, CLIENT_PLUGIN_AUTH_LENENC_CLIENT_DATA))
        {
            int_lenenc(buf, packet.authResponseLen);
            string_fix(buf, packet.authResponse, (int) packet.authResponseLen);
        } else if (hasFlag(flags, CLIENT_SECURE_CONNECTION))
        {
            int1(buf, (int) packet.authResponseLen);
            string_fix(buf, packet.authResponse, (int) packet.authResponseLen);
        } else
        {
            string_nul(buf, packet.authResponse);
        }

        if (hasFlag(flags, CLIENT_CONNECT_WITH_DB))
        {
            string_nul(buf, packet.database);
        }

        if (hasFlag(flags, CLIENT_PLUGIN_AUTH))
        {
            string_nul(buf, packet.authPluginName);
        }

        if (hasFlag(flags, CLIENT_CONNECT_ATTRS))
        {
            int_lenenc(buf, packet.keyValuesLength);
            if (packet.attributes != null)
            {
                for (Map.Entry<String, String> entry : packet.attributes.entrySet())
                {
                    string_lenenc(buf, Unpooled.wrappedBuffer(entry.getKey().getBytes(UTF_8)));
                    string_lenenc(buf, Unpooled.wrappedBuffer(entry.getValue().getBytes(UTF_8)));
                }
            }
        }

        return buf;
    }

    public static HandshakeV10 readPacket(ByteBuf buf, HandshakeV10 packet, int flags)
    {
        packet.protocolVersion = int1(buf);
        packet.serverVersion = string_nul(buf);
        packet.connectionId = int4(buf);
        packet.challenge1 = string_fix(buf, 8);
        packet.filter = int1(buf);
        flags = packet.capabilityFlags = int2(buf);// lower 2 bytes
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

    public static ByteBuf writePacket(ByteBuf buf, HandshakeV10 packet, int flags)
    {
        int1(buf, packet.protocolVersion);
        string_nul(buf, packet.serverVersion);
        int4(buf, packet.connectionId);
        string_fix(buf, packet.challenge1, 8);
        int1(buf, packet.filter);
        int2(buf, packet.capabilityFlags & 0xffff);// lower 2 bytes

        if (hasMore(buf))
        {
            int1(buf, packet.characterSet);
            int2(buf, packet.statusFlags);
            int2(buf, packet.capabilityFlags >> 16);// upper 2 bytes

            if (hasFlag(flags, CLIENT_PLUGIN_AUTH))
            {
                int1(buf, packet.authPluginDataLength);
            } else
            {
                buf.readByte();// consume this byte
            }

            string_fix(buf, packet.reserved, 10);

            if (hasFlag(flags, CLIENT_SECURE_CONNECTION))
            {
                string_fix(buf, packet.challenge2, Math.max(13, packet.authPluginDataLength - 8));
            }

            if (hasFlag(flags, CLIENT_PLUGIN_AUTH))
            {
                string_nul(buf, packet.authPluginName);
            }
        }
        return buf;
    }

    static boolean hasMore(ByteBuf buf) {return buf.readableBytes() > 0;}

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

    public static ByteBuf writePacket(ByteBuf buf, EOF_Packet packet, int flags)
    {
        int1(buf, packet.header);
        if (hasFlag(flags, CLIENT_PROTOCOL_41))
        {
            int2(buf, packet.warnings);
            int2(buf, packet.statusFlags);
        }
        return buf;
    }

    public static ByteBuf writePacket(ByteBuf buf, ERR_Packet packet, int flags)
    {
        int1(buf, packet.header);
        int2(buf, packet.errorCode);
        if (hasFlag(flags, CLIENT_PROTOCOL_41))
        {
            string_fix(buf, packet.sqlStateMarker, 1);
            string_fix(buf, packet.sqlState, 5);
        }
        string_eof(buf, packet.errorMessage);
        return buf;
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

    public static ByteBuf writePacket(ByteBuf buf, OK_Packet packet, int flags)
    {
        int1(buf, packet.header);
        int_lenenc(buf, packet.affectedRows);
        int_lenenc(buf, packet.lastInsertId);
        if (hasFlag(flags, CLIENT_PROTOCOL_41))
        {
            int2(buf, packet.statusFlags);
            int2(buf, packet.warnings);
        } else if (hasFlag(flags, CLIENT_TRANSACTIONS))
        {
            int2(buf, packet.statusFlags);
        }

        if (hasFlag(flags, CLIENT_SESSION_TRACK))
        {
            string_lenenc(buf, packet.info);

            if ((packet.statusFlags & Flags.SERVER_SESSION_STATE_CHANGED) > 0)
            {
                string_lenenc(buf, packet.sessionStateChanges);
            }
        } else
        {
            string_eof(buf, packet.info);
        }


        return buf;
    }

    static boolean hasFlag(int flags, int flag) {return (flags & flag) > 0;}

    public static PacketData readPacket(ByteBuf buf, PacketData packet, int flags)
    {
        packet.payloadLength = int3(buf);
        packet.sequenceId = int1(buf);
        packet.payload = string_var(buf, packet.payloadLength);
        return packet;
    }

    public static ByteBuf writePacket(ByteBuf buf, PacketData packet, int flags)
    {
        int3(buf, packet.payloadLength);
        int1(buf, packet.sequenceId);
        string_var(buf, packet.payload, packet.payloadLength);
        return buf;
    }

    public static ColumnDefinition320 readPacket(ByteBuf buf, ColumnDefinition320 packet, int flags, CommandPacket command)
    {
        packet.table = string_lenenc(buf);
        packet.name = string_lenenc(buf);
        packet.columnLengthFieldLength = (int) int_lenenc(buf);
        packet.columnLength = int3(buf);
        packet.typeFieldLength = (int) int_lenenc(buf);
        packet.type = int1(buf);
        if (hasFlag(flags, CLIENT_LONG_FLAG))
        {
            packet.flagsDecimalsFieldsLength = (int) int_lenenc(buf);
            packet.flags = int2(buf);
            packet.decimals = int1(buf);
        } else
        {
            packet.flagsDecimalsFieldsLength = int1(buf);
            packet.flags = int1(buf);
            packet.decimals = int1(buf);
        }
        if (command.command == Flags.COM_FIELD_LIST)
        {
            packet.defaultValuesLength = (int) int_lenenc(buf);
            packet.defaultValues = Lists.newArrayList();
            do
            {
                packet.defaultValues.add(string_lenenc(buf).toString(UTF_8));
            } while (hasMore(buf));
        }
        return packet;
    }

    public static ColumnDefinition41 readPacket(ByteBuf buf, ColumnDefinition41 packet, int flags)
    {
        packet.catalog = string_lenenc(buf);
        packet.schema = string_lenenc(buf);
        packet.table = string_lenenc(buf);
        packet.orgTable = string_lenenc(buf);
        packet.name = string_lenenc(buf);
        packet.orgName = string_lenenc(buf);
        packet.fixedLengthFieldsLength = (int) int_lenenc(buf);
        packet.characterSet = int2(buf);
        packet.columnLength = int4(buf);
        packet.type = int1(buf);
        packet.flags = int2(buf);
        packet.decimals = int1(buf);
        packet.filler = int2(buf);
        return packet;
    }

    public static ByteBuf writePacket(ByteBuf buf, ColumnDefinition41 packet, int flags)
    {
        string_lenenc(buf, packet.catalog);
        string_lenenc(buf, packet.schema);
        string_lenenc(buf, packet.table);
        string_lenenc(buf, packet.orgTable);
        string_lenenc(buf, packet.name);
        string_lenenc(buf, packet.orgName);
        int_lenenc(buf, packet.fixedLengthFieldsLength);
        int2(buf, packet.characterSet);
        int4(buf, packet.columnLength);
        int1(buf, packet.type);
        int2(buf, packet.flags);
        int1(buf, packet.decimals);
        int2(buf, packet.filler);
        return buf;
    }

    public static ColumnDefinition41 readPacketForCOM_FIELD_LIST(ByteBuf buf, ColumnDefinition41 packet, int flags)
    {
        readPacket(buf, packet, flags);

        long len = int_lenenc(buf);
        ByteBuf defaultValue = string_fix(buf, (int) len);

//        packet.defaultValuesLength = (int) int_lenenc(buf);
//        packet.defaultValues = Lists.newArrayList();
//        do
//        {
//            packet.defaultValues.add(string_lenenc(buf).toString(StandardCharsets.UTF_8));
//        } while (hasMore(buf));
        return packet;
    }
}
