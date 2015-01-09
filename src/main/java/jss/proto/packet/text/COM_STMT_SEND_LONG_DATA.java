package jss.proto.packet.text;

import jss.proto.define.Command;

/**
 * @see jss.proto.define.Command#COM_STMT_SEND_LONG_DATA
 */
public class COM_STMT_SEND_LONG_DATA extends AbstractProtocolText
{
    public byte[] data;

    public COM_STMT_SEND_LONG_DATA() {super(Command.COM_STMT_SEND_LONG_DATA);}
}

