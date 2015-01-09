package jss.proto.packet.text;

import jss.proto.define.Command;

/**
 * @see jss.proto.define.Command#COM_STMT_FETCH
 */
public class COM_STMT_FETCH extends CommandPacket
{
    public COM_STMT_FETCH() {super(Command.COM_STMT_FETCH);}
}
