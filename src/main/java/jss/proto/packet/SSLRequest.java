package jss.proto.packet;

import com.github.mpjct.jmpjct.mysql.proto.define.Flags;

public class SSLRequest
{
    /*
4              capability flags, CLIENT_SSL always set
4              max-packet size
1              character set
string[23]     reserved (all [0])
     */
    public long capabilityFlags = Flags.CLIENT_PROTOCOL_41;
    public long maxPacketSize = 0;
    public long characterSet = 0;
    public String reserved = "";
}
