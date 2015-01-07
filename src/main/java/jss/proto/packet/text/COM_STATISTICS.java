package jss.proto.packet.text;

import jss.proto.define.Command;
import jss.proto.packet.AbstractProtocolText;

/**
 * @see jss.proto.define.Command#COM_STATISTICS
 */
public class COM_STATISTICS extends AbstractProtocolText
{
    public COM_STATISTICS() {super(Command.COM_STATISTICS);}
}