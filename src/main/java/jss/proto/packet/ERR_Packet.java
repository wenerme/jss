package jss.proto.packet;

/**
 * This packet signals that an error occurred. It contains a SQL state value if CLIENT_PROTOCOL_41 is enabled.
 * <pre>
 * <b>Payload</b>
 * Type	Name	Description
 * int<1>	header	[ff] header of the ERR packet
 * int<2>	error_code	error-code
 * if capabilities & CLIENT_PROTOCOL_41 {
 * string[1]	sql_state_marker	# marker of the SQL State
 * string[5]	sql_state	SQL State
 * }
 * string&lt;EOF>	error_message	human readable error message
 * <b>Example</b>
 * 17 00 00 01 ff 48 04 23    48 59 30 30 30 4e 6f 20       .....H.#HY000No
 * 74 61 62 6c 65 73 20 75    73 65 64                      tables used
 * </pre>
 *
 * @see <a href=http://dev.mysql.com/doc/internals/en/packet-ERR_Packet.html>ERR_Packet</a>
 */
public class ERR_Packet implements Packet
{
}
