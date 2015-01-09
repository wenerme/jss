package jss.proto.packet.text;

import jss.proto.define.Command;

/**
 * @see jss.proto.define.Command#COM_SET_OPTION
 */
public class COM_SET_OPTION extends CommandPacket
{
    public long operation = 0;

    public COM_SET_OPTION() {super(Command.COM_SET_OPTION);}
}
