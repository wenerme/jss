package jss.proto.packet.text;

import jss.proto.define.Command;

/**
 * @see jss.proto.define.Command#COM_PING
 */
public class COM_PING extends AbstractProtocolText
{
    public COM_PING() {super(Command.COM_PING);}
}
