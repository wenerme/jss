package jss.proto.packet.text;

import jss.proto.define.Command;
import jss.proto.packet.AbstractProtocolText;

/**
 * @see jss.proto.define.Command#COM_INIT_DB
 */
public class COM_INIT_DB extends AbstractProtocolText
{
    public String schema = "";

    public COM_INIT_DB() {super(Command.COM_INIT_DB);}
}
