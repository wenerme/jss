package jss.proto.packet.text;

import jss.proto.define.Command;
import jss.proto.packet.AbstractProtocolText;

/**
 * @see jss.proto.define.Command#COM_RESET_CONNECTION
 */
public class COM_RESET_CONNECTION extends AbstractProtocolText
{
    public COM_RESET_CONNECTION() {super(Command.COM_RESET_CONNECTION);}
}
