package jss.proto.packet.text;

import jss.proto.define.Command;

/**
 * @see jss.proto.define.Command#COM_INIT_DB
 */
public class COM_INIT_DB extends CommandPacket
{
    public String schema = "";

    public COM_INIT_DB() {super(Command.COM_INIT_DB);}
}
