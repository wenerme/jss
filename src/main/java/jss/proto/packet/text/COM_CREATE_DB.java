package jss.proto.packet.text;

import jss.proto.define.Command;

/**
 * @see jss.proto.define.Command#COM_CREATE_DB
 */
public class COM_CREATE_DB extends CommandPacket
{
    public String schema = "";

    public COM_CREATE_DB() {super(Command.COM_CREATE_DB);}
}
