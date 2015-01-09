package jss.proto.packet.connection;

import jss.proto.packet.Packet;

/**
 * <pre>
 * Protocol::OldAuthSwitchRequest:
 * Old Authentication Method Switch Request Packet consisting of a single 0xfe byte. It is sent by server to request client to switch to Old Password Authentication if CLIENT_PLUGIN_AUTH capability is not supported (by either the client or the server)
 *
 * Payload
 * 1     [fe]
 * Fields
 * status (1) -- 0xfe
 *
 * Returns
 * Protocol::AuthSwitchResponse with old password hash
 *
 * Example
 * 01 00 00 02 fe
 * </pre>
 */
public class OldAuthSwitchRequest implements Packet
{
}
