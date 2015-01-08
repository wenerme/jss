package jss.proto.packet.text;

import jss.proto.define.Command;
import jss.proto.packet.AbstractProtocolText;

/**
 * @see jss.proto.define.Command#COM_DEBUG
 */
public class COM_DEBUG extends AbstractProtocolText
{
    public COM_DEBUG() {super(Command.COM_DEBUG);}
}