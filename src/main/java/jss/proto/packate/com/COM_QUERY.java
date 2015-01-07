package jss.proto.packate.com;

import jss.proto.define.Command;
import jss.proto.packate.AbstractProtocolText;

/**
 * @see jss.proto.define.Command#COM_QUERY
 */
public class COM_QUERY extends AbstractProtocolText
{
    public String query = "";

    public COM_QUERY() {super(Command.COM_QUERY);}
}
