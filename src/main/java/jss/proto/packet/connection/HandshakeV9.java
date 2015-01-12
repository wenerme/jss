package jss.proto.packet.connection;

import jss.proto.packet.Packet;

public class HandshakeV9 implements Packet
{
    public long protocolVersion = 0x09;
    public String serverVersion = "";
    public long connectionId = 0;
    public String scramble = "";
}
