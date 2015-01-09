package jss.proto.packet.text;

import jss.proto.define.Command;

/**
 * @see jss.proto.define.Command#COM_STATISTICS
 */
public class COM_STATISTICS extends AbstractProtocolText
{
    public COM_STATISTICS() {super(Command.COM_STATISTICS);}
}