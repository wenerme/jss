package jss.proto.packet.text;

import jss.proto.define.Command;
import jss.proto.packet.AbstractProtocolText;

/**
 * @see jss.proto.define.Command#COM_QUIT
 */
public class COM_QUIT extends AbstractProtocolText
{
    public COM_QUIT() {super(Command.COM_QUIT);}
}
