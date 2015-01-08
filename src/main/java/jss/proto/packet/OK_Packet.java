package jss.proto.packet;

import io.netty.buffer.ByteBuf;

/**
 * An OK packet is sent from the server to the client to signal successful completion of a command.
 * <p/>
 * If CLIENT_PROTOCOL_41 is set, the packet contains a warning count.
 * <pre>
 * <b>Payload</b>
 * Type	Name	Description
 * int&lt;1>	header	[00] the OK packet header
 * int&lt;lenenc>	affected_rows	affected rows
 * int&lt;lenenc>	last_insert_id	last insert-id
 * if capabilities & CLIENT_PROTOCOL_41 {
 * int&lt;2>	status_flags	Status Flags
 * int&lt;2>	warnings	number of warnings
 * } elseif capabilities & CLIENT_TRANSACTIONS {
 * int&lt;2>	status_flags	Status Flags
 * }
 * if capabilities & CLIENT_SESSION_TRACK {
 * string&lt;lenenc>	info	human readable status information
 * if status_flags & SERVER_SESSION_STATE_CHANGED {
 * string&lt;lenenc>	session_state_changes	session state info
 * }
 * } else {
 * string&lt;EOF>	info	human readable status information
 * }
 * <b>Example</b>
 * OK with CLIENT_PROTOCOL_41. 0 affected rows, last-insert-id was 0, AUTOCOMMIT, 0 warnings. No further info.
 *
 * 07 00 00 02 00 00 00 02    00 00 00
 * ...........
 * </pre>
 *
 * @see <a href=http://dev.mysql.com/doc/internals/en/packet-OK_Packet.html>OK_Packet</a>
 */
public class OK_Packet implements Packet
{
    public byte header = 0;
    public long affectedRows = 0;
    public long lastInsertId = 0;
    public long statusFlags = 0;
    public long warnings = 0;

    public ByteBuf info;
    public ByteBuf sessionStateChanges;
}
