package jss.proto.packet;

import com.github.mpjct.jmpjct.mysql.proto.define.Flags;
import io.netty.buffer.ByteBuf;

/**
 * <pre>
 * 1              [0a] protocol version
 * string[NUL]    server version
 * 4              connection id
 * string[8]      auth-plugin-data-part-1
 * 1              [00] filler
 * 2              capability flags (lower 2 bytes)
 * if more data in the packet:
 * 1              character set
 * 2              status flags
 * 2              capability flags (upper 2 bytes)
 * if capabilities & CLIENT_PLUGIN_AUTH {
 * 1              length of auth-plugin-data
 * } else {
 * 1              [00]
 * }
 * string[10]     reserved (all [00])
 * if capabilities & CLIENT_SECURE_CONNECTION {
 * string[$len]   auth-plugin-data-part-2 ($len=MAX(13, length of auth-plugin-data - 8))
 * if capabilities & CLIENT_PLUGIN_AUTH {
 * string[NUL]    auth-plugin name
 * }
 *
 * Fields
 * protocol_version (1) -- 0x0a protocol_version
 *
 * server_version (string.NUL) -- human-readable server version
 *
 * connection_id (4) -- connection id
 *
 * auth_plugin_data_part_1 (string.fix_len) -- [len=8] first 8 bytes of the auth-plugin data
 *
 * filler_1 (1) -- 0x00
 *
 * capability_flag_1 (2) -- lower 2 bytes of the Protocol::CapabilityFlags (optional)
 *
 * character_set (1) -- default server character-set, only the lower 8-bits Protocol::CharacterSet (optional)
 *
 * status_flags (2) -- Protocol::StatusFlags (optional)
 *
 * capability_flags_2 (2) -- upper 2 bytes of the Protocol::CapabilityFlags
 *
 * auth_plugin_data_len (1) -- length of the combined auth_plugin_data, if auth_plugin_data_len is > 0
 *
 * auth_plugin_name (string.NUL) -- name of the auth_method that the auth_plugin_data belongs to
 *
 * <b>Note</b>
 * Due to Bug#59453 the auth-plugin-name is missing the terminating NUL-char in versions prior to 5.5.10 and 5.6.2.
 * </pre>
 *
 * @see <a href=http://dev.mysql.com/doc/internals/en/connection-phase-packets.html#packet-Protocol::HandshakeV10>HandshakeV10</a>
 */
public class HandshakeV10 implements Packet
{
    public int protocolVersion = 0x0a;
    public ByteBuf serverVersion;
    public int connectionId = 0;
    public ByteBuf challenge1;
    public int filter = 0;
    public int capabilityFlags = Flags.CLIENT_PROTOCOL_41;
    public int characterSet = 0;
    public int statusFlags = 0;
    public ByteBuf challenge2;
    public int authPluginDataLength = 0;
    public ByteBuf authPluginName;
    public ByteBuf reserved;
}
