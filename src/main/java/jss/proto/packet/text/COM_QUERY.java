package jss.proto.packet.text;

import jss.proto.define.Command;
import jss.proto.packet.AbstractProtocolText;

/**
 * @see jss.proto.define.Command#COM_QUERY
 */
public class COM_QUERY extends AbstractProtocolText
{
    public String query = "";

    public COM_QUERY() {super(Command.COM_QUERY);}
}
