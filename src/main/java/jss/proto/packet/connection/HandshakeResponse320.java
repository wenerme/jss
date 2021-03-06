package jss.proto.packet.connection;

import jss.proto.define.Flags;
import jss.proto.packet.Packet;

/**
 * <pre>
 * Protocol::HandshakeResponse320:
 * Old Handshake Response Packet used by old clients or if the server doesn't support CLIENT_PROTOCOL_41 capability.
 *
 * Payload
 * 2              capability flags, CLIENT_PROTOCOL_41 never set
 * 3              max-packet size
 * string[NUL]    username
 * if capabilities & CLIENT_CONNECT_WITH_DB {
 * string[NUL]    auth-response
 * string[NUL]    database
 * } else {
 * string[EOF]    auth-response
 * }
 * Fields
 * capability_flags (2) -- capability flags of the client as defined in Protocol::CapabilityFlags
 *
 * max_packet_size (3) -- max size of a command packet that the client wants to send to the server
 *
 * auth-response (string.NUL) -- opaque authentication response data generated by Authentication Method indicated by the plugin name field.
 *
 * database (string.NUL) -- initail database for the connection -- this string should be interpreted using the character set indicated by character set field.
 *
 * Example
 * 11 00 00 01 85 24 00 00    00 6f 6c 64 00 47 44 53    .....$...old.GDS
 * 43 51 59 52 5f                                        CQYR_
 * Note
 * if auth-response field is followed by a database field it has to be 0-terminated.
 * </pre>
 */
public class HandshakeResponse320 implements Packet
{

    public long capabilityFlags = Flags.CLIENT_PROTOCOL_41;
    public long maxPacketSize = 0;
    public String username = "";
    public String authResponse = "";
    public String database = "";
}
