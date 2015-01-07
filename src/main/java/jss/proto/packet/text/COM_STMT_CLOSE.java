package jss.proto.packet.text;

import jss.proto.define.Command;
import jss.proto.packet.AbstractProtocolText;

/**
 * @see jss.proto.define.Command#COM_STMT_CLOSE
 */
public class COM_STMT_CLOSE extends AbstractProtocolText
{
    public byte[] data;

    public COM_STMT_CLOSE() {super(Command.COM_STMT_CLOSE);}
}
