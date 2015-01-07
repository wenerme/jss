package jss.proto.packate;

public class AuthSwitchRequest implements Packet
{
    /*
1              [fe]
string[NUL]    plugin name
string[EOF]    auth plugin data
     */
    public long status = 0xfe;
    public String pluginName = "";
    public String authPluginData = "";
}
