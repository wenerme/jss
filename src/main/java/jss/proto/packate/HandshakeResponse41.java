package jss.proto.packate;

import com.github.mpjct.jmpjct.mysql.proto.Flags;

public class HandshakeResponse41 implements Packet
{
    /*
4              capability flags, CLIENT_PROTOCOL_41 always set
4              max-packet size
1              character set
string[23]     reserved (all [0])
string[NUL]    username
  if capabilities & CLIENT_PLUGIN_AUTH_LENENC_CLIENT_DATA {
lenenc-int     length of auth-response
string[n]      auth-response
  } else if capabilities & CLIENT_SECURE_CONNECTION {
1              length of auth-response
string[n]      auth-response
  } else {
string[NUL]    auth-response
  }
  if capabilities & CLIENT_CONNECT_WITH_DB {
string[NUL]    database
  }
  if capabilities & CLIENT_PLUGIN_AUTH {
string[NUL]    auth plugin name
  }
  if capabilities & CLIENT_CONNECT_ATTRS {
lenenc-int     length of all key-values
lenenc-str     key
lenenc-str     value
   if-more data in 'length of all key-values', more keys and value pairs
  }
     */

    public long capabilityFlags = Flags.CLIENT_PROTOCOL_41;
    public long maxPacketSize = 0;
    public long characterSet = 0;
    public String reserved = "";
    public String username = "";
    public long authResponseLen = 0;
    public String authResponse = "";
    public String database = "";
    public String authPluginName = "";
    public long clientAttributesLen = 0;
    public String clientAttributes = "";

    public PacketType type()
    {
        return PacketType.HandshakeResponse41;
    }
}
