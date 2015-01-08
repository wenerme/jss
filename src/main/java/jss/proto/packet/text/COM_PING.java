package jss.proto.packet.text;

import jss.proto.define.Command;
import jss.proto.packet.AbstractProtocolText;

/**
 * @see jss.proto.define.Command#COM_PING
 */
public class COM_PING extends AbstractProtocolText
{
    public COM_PING() {super(Command.COM_PING);}
}
