package jss.proto.packet.text;

import jss.proto.define.Command;

/**
 * @see jss.proto.define.Command#COM_STMT_PREPARE
 */
public class COM_STMT_PREPARE extends CommandPacket
{
    public String query = "";

    public COM_STMT_PREPARE() {super(Command.COM_STMT_PREPARE);}
}
