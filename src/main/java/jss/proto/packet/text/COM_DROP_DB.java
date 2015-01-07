package jss.proto.packet.text;

import jss.proto.define.Command;
import jss.proto.packet.AbstractProtocolText;

/**
 * @see jss.proto.define.Command#COM_DROP_DB
 */
public class COM_DROP_DB extends AbstractProtocolText
{
    public String schema = "";

    public COM_DROP_DB() {super(Command.COM_DROP_DB);}

}
