package jss.proto.packet.text;

import jss.proto.define.Command;
import jss.proto.packet.AbstractProtocolText;

/**
 * @see jss.proto.define.Command#COM_CONNECT
 */
public class COM_CONNECT extends AbstractProtocolText
{
    public COM_CONNECT() {super(Command.COM_CONNECT);}
}
