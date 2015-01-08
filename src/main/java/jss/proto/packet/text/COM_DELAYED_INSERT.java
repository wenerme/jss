package jss.proto.packet.text;

import jss.proto.define.Command;
import jss.proto.packet.AbstractProtocolText;

/**
 * @see jss.proto.define.Command#COM_DELAYED_INSERT
 */
public class COM_DELAYED_INSERT extends AbstractProtocolText
{
    public COM_DELAYED_INSERT() {super(Command.COM_DELAYED_INSERT);}
}
