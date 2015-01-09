package jss.proto.packet.text;

import jss.proto.define.Command;

/**
 * @see jss.proto.define.Command#COM_STMT_CLOSE
 */
public class COM_STMT_CLOSE extends CommandPacket
{
    public byte[] data;

    public COM_STMT_CLOSE() {super(Command.COM_STMT_CLOSE);}
}
