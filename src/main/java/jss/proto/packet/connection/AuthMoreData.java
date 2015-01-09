package jss.proto.packet.connection;

import io.netty.buffer.ByteBuf;

/**
 * <pre>
 * Protocol::AuthMoreData:
 * Payload
 * 1              [01]
 * string[EOF]    plugin data
 * Fields
 * status (1) -- 0x01
 *
 * auth_method_data (string.EOF) -- extra auth-data beyond the initial challenge
 * </pre>
 *
 * @see <a href=http://dev.mysql.com/doc/internals/en/connection-phase-packets.html>connection-phase-packets</a>
 */
public class AuthMoreData
{
    public byte status = 1;
    public ByteBuf pluginData;
}
