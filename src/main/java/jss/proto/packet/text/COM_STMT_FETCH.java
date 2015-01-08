package jss.proto.packet.text;

import jss.proto.define.Command;
import jss.proto.packet.AbstractProtocolText;

/**
 * @see jss.proto.define.Command#COM_STMT_FETCH
 */
public class COM_STMT_FETCH extends AbstractProtocolText
{
    public COM_STMT_FETCH() {super(Command.COM_STMT_FETCH);}
}
