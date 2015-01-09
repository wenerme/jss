package jss.proto.packet.text;

import jss.proto.define.Command;

/**
 * @see jss.proto.define.Command#COM_DELAYED_INSERT
 */
public class COM_DELAYED_INSERT extends AbstractProtocolText
{
    public COM_DELAYED_INSERT() {super(Command.COM_DELAYED_INSERT);}
}
