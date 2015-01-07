package jss.proto.packate.com;

import jss.proto.define.Command;
import jss.proto.packate.AbstractProtocolText;

/**
 * @see jss.proto.define.Command#COM_STMT_FETCH
 */
public class COM_STMT_FETCH extends AbstractProtocolText
{
    public COM_STMT_FETCH() {super(Command.COM_STMT_FETCH);}
}
