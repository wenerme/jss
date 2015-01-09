package jss.proto.packet.text;

import jss.proto.define.Command;

/**
 * @see jss.proto.define.Command#COM_CHANGE_USER
 */
public class COM_CHANGE_USER extends AbstractProtocolText
{
    public String user = "";
    public String authResponse = "";
    public String schema = "";
    public long characterSet = 0;
    public long capabilityFlags = 0;

    public COM_CHANGE_USER() {super(Command.COM_CHANGE_USER);}
}
