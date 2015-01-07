package jss.proto.packet.text;

import jss.proto.define.Command;
import jss.proto.packet.AbstractProtocolText;

/**
 * @see jss.proto.define.Command#COM_DAEMON
 */
public class COM_DAEMON extends AbstractProtocolText
{
    public COM_DAEMON() {super(Command.COM_DAEMON);}
}
