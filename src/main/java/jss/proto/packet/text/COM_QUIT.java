package jss.proto.packet.text;

import jss.proto.define.Command;

/**
 * @see jss.proto.define.Command#COM_QUIT
 */
public class COM_QUIT extends CommandPacket
{
    public COM_QUIT() {super(Command.COM_QUIT);}
}
