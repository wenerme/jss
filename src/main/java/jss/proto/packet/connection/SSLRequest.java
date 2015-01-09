package jss.proto.packet.connection;

import jss.proto.packet.Packet;

/**
 * <pre>
 * Protocol::SSLRequest:
 * SSL Connection Request Packet. It is like Handshake Response Packet but is truncated right before username field. If server supports CLIENT_SSL capability, client can send this packet to request a secure SSL connection. The CLIENT_SSL capability flag must be set inside the SSL Connection Request Packet.
 *
 * Payload
 * 4              capability flags, CLIENT_SSL always set
 * 4              max-packet size
 * 1              character set
 * string[23]     reserved (all [0])
 * Protocol::AuthSwitchRequest:
 * Authentication Method Switch Request Packet. If both server and client support CLIENT_PLUGIN_AUTH capability, server can send this packet to ask client to use another authentication method.
 *
 * Payload
 * 1              [fe]
 * string[NUL]    plugin name
 * string[EOF]    auth plugin data
 * Fields
 * status (1) -- 0xfe
 *
 * auth_method_name (string.NUL) -- name of the authentication method to switch to
 *
 * auth_method_data (string.EOF) -- initial auth-data for that authentication method
 *
 * Returns
 * Protocol::AuthSwitchResponse or connection close
 *
 * Example
 * If CLIENT_PLUGIN_AUTH was set and the server wants the client to authenticate with the Authentication::Native41 method it sends:
 *
 * 2c 00 00 02 fe 6d 79 73    71 6c 5f 6e 61 74 69 76    ,....mysql_nativ
 * 65 5f 70 61 73 73 77 6f    72 64 00 7a 51 67 34 69    e_password.zQg4i
 * 36 6f 4e 79 36 3d 72 48    4e 2f 3e 2d 62 29 41 00    6oNy6=rHN/>-b)A.
 * </pre>
 */
public class SSLRequest implements Packet
{
}
