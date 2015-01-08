package jss.proto.packet;

/**
 * If CLIENT_PROTOCOL_41 is enabled, the EOF packet contains a warning count and status flags.
 * <pre>
 * <b>Caution</b>
 * the EOF packet may appear in places where a Protocol::LengthEncodedInteger may appear. You must check whether the packet length is less than 9 to make sure that it is a EOF packet.
 *
 * <b>Payload</b>
 * Type	Name	Description
 * int&lt;1>	header	[fe] EOF header
 * if capabilities & CLIENT_PROTOCOL_41 {
 * int&lt;2>	warnings	number of warnings
 * int&lt;2>	status_flags	Status Flags
 * }
 *
 * <b>Example</b>
 * a 4.1 EOF packet with: 0 warnings, AUTOCOMMIT enabled.
 * 05 00 00 05 fe 00 00 02 00     ..........
 * <pre/>
 */
public class EOF_Packet implements Packet
{
    public int header = 0xfe;
    public int warnings = 0;
    public int statusFlags = 0;
}
