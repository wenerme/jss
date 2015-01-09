package jss.proto.packet.text;

import jss.proto.define.Command;

/**
 * @see jss.proto.define.Command#COM_STMT_RESET
 */
public class COM_STMT_RESET extends AbstractProtocolText
{
    public byte[] data;

    public COM_STMT_RESET() {super(Command.COM_STMT_RESET);}
}
