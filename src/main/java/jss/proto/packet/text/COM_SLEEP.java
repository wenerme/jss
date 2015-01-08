package jss.proto.packet.text;

import jss.proto.define.Command;
import jss.proto.packet.AbstractProtocolText;

/**
 * @see jss.proto.define.Command#COM_SLEEP
 */
public class COM_SLEEP extends AbstractProtocolText
{
    public COM_SLEEP() {super(Command.COM_SLEEP);}
}
