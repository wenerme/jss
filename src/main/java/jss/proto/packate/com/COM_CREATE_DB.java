package jss.proto.packate.com;

import jss.proto.define.Command;
import jss.proto.packate.AbstractProtocolText;

/**
 * @see jss.proto.define.Command#COM_CREATE_DB
 */
public class COM_CREATE_DB extends AbstractProtocolText
{
    public String schema = "";

    public COM_CREATE_DB() {super(Command.COM_CREATE_DB);}
}
