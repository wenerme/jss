package jss.proto.packet.connection;

import io.netty.buffer.ByteBuf;
import jss.proto.packet.Packet;
import jss.proto.util.Dumper;

/**
 * <pre>
 * Protocol::AuthSwitchResponse:
 * Authentication Method Switch Response Packet which contains response data generated by the authenticatication method requested in Authentication Method Switch Request Packet. This data is opaque to the protocol.
 *
 * Payload
 * string[EOF]    auth plugin response
 * Fields
 * data (string.EOF) -- authentication response data
 *
 * Returns
 * Protocol::AuthMoreData or OK_Packet or ERR_Packet
 *
 * Example
 * If the client sends a mysql_native_password response, but the server has a mysql_old_password for that user, it will ask the client to switch to mysql_old_password and client would reply with:
 *
 * 09 00 00 03 5c 49 4d 5e    4e 58 4f 47 00             ....\IM^NXOG.
 * In the case it is the other way around (mysql --default-auth=mysql_old_password against a mysql_native_password user) the client will respond with the reply of the mysql_native_password plugin:
 *
 * 14 00 00 03 f4 17 96 1f    79 f3 ac 10 0b da a6 b3    ........y.......
 * b5 c2 0e ab 59 85 ff b8                               ....Y...
 * More examples in Auth Method Switch
 * </pre>
 */
public class AuthSwitchResponse implements Packet
{
    public ByteBuf authPluginResponse;

    @Override
    public String toString()
    {
        return "AuthSwitchResponse{" +
                "authPluginResponse=" + Dumper.string(authPluginResponse) +
                '}';
    }
}