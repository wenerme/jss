package jss.proto.packet;

public class AuthMoreData
{
    /*
1              [01]
string[EOF]    plugin data
     */
    public byte status = 1;
    public String pluginData = "";
}
