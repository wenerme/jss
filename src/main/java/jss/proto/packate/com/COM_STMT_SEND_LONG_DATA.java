package jss.proto.packate.com;

import jss.proto.define.Command;
import jss.proto.packate.AbstractProtocolText;

/**
 * @see jss.proto.define.Command#COM_STMT_SEND_LONG_DATA
 */
public class COM_STMT_SEND_LONG_DATA extends AbstractProtocolText
{
    public byte[] data;

    public COM_STMT_SEND_LONG_DATA() {super(Command.COM_STMT_SEND_LONG_DATA);}
}

