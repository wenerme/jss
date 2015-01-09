package jss.proto.packet.connection;

import jss.proto.packet.Packet;
import jss.proto.packet.PacketType;

public class HandshakeV9 implements Packet
{
    /*
1              [09] protocol_version
string[NUL]    server_version
4              connection_id
string[NUL]    scramble
     */

    public long protocolVersion = 0x09;
    public String serverVersion = "";
    public long connectionId = 0;
    public String scramble = "";

    public PacketType type()
    {
        return PacketType.HandshakeV9;
    }
}
