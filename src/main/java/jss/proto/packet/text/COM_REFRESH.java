package jss.proto.packet.text;

import jss.proto.define.Command;

/**
 * @see jss.proto.define.Command#COM_REFRESH
 */
public class COM_REFRESH extends AbstractProtocolText
{
    public long flags = 0x00;

    public COM_REFRESH() {super(Command.COM_REFRESH);}
}
