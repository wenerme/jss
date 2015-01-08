package jss.proto.packet;

import com.github.mpjct.jmpjct.mysql.proto.define.Flags;

public class HandshakeResponse320 implements Packet
{
    /*
2              capability flags, CLIENT_PROTOCOL_41 never set
3              max-packet size
string[NUL]    username
  if capabilities & CLIENT_CONNECT_WITH_DB {
string[NUL]    auth-response
string[NUL]    database
  } else {
string[EOF]    auth-response
  }
     */

    public long capabilityFlags = Flags.CLIENT_PROTOCOL_41;
    public long maxPacketSize = 0;
    public String username = "";
    public String authResponse = "";
    public String database = "";
}
