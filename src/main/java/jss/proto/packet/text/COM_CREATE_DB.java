package jss.proto.packet.text;

import jss.proto.define.Command;
import jss.proto.packet.AbstractProtocolText;

/**
 * @see jss.proto.define.Command#COM_CREATE_DB
 */
public class COM_CREATE_DB extends AbstractProtocolText
{
    public String schema = "";

    public COM_CREATE_DB() {super(Command.COM_CREATE_DB);}
}