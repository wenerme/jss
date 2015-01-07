package jss.proto.packate.com;

import jss.proto.define.Command;
import jss.proto.packate.AbstractProtocolText;

/**
 * @see jss.proto.define.Command#COM_STMT_PREPARE
 */
public class COM_STMT_PREPARE extends AbstractProtocolText
{
    public String query = "";

    public COM_STMT_PREPARE() {super(Command.COM_STMT_PREPARE);}
}
