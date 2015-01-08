package jss.proto.packet;

import com.github.mpjct.jmpjct.mysql.proto.define.Flags;

public class HandshakeV10 implements Packet
{
    /*
1              [0a] protocol version
string[NUL]    server version
4              connection id
string[8]      auth-plugin-data-part-1
1              [00] filler
2              capability flags (lower 2 bytes)
  if more data in the packet:
1              character set
2              status flags
2              capability flags (upper 2 bytes)
  if capabilities & CLIENT_PLUGIN_AUTH {
1              length of auth-plugin-data
  } else {
1              [00]
  }
string[10]     reserved (all [00])
  if capabilities & CLIENT_SECURE_CONNECTION {
string[$len]   auth-plugin-data-part-2 ($len=MAX(13, length of auth-plugin-data - 8))
  if capabilities & CLIENT_PLUGIN_AUTH {
string[NUL]    auth-plugin name
  }
     */

    public long protocolVersion = 0x0a;
    public String serverVersion = "";
    public long connectionId = 0;
    public String challenge1 = "";
    public long capabilityFlags = Flags.CLIENT_PROTOCOL_41;
    public long characterSet = 0;
    public long statusFlags = 0;
    public String challenge2 = "";
    public long authPluginDataLength = 0;
    public String authPluginName = "";

    public PacketType type()
    {
        return PacketType.HandshakeV10;
    }
}
