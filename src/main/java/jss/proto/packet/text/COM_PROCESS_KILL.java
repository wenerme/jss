package jss.proto.packet.text;

import jss.proto.define.Command;

/**
 * @see jss.proto.define.Command#COM_PROCESS_KILL
 */
public class COM_PROCESS_KILL extends CommandPacket
{
    public long connectionId = 0;

    public COM_PROCESS_KILL() {super(Command.COM_PROCESS_KILL);}
}