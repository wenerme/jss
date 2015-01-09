package jss.proto.packet.text;

import jss.proto.define.Command;

/**
 * @see jss.proto.define.Command#COM_RESET_CONNECTION
 */
public class COM_RESET_CONNECTION extends AbstractProtocolText
{
    public COM_RESET_CONNECTION() {super(Command.COM_RESET_CONNECTION);}
}
