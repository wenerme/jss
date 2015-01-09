package jss.proto.packet.text;

import jss.proto.define.Command;

/**
 * @see jss.proto.define.Command#COM_DROP_DB
 */
public class COM_DROP_DB extends CommandPacket
{
    public String schema = "";

    public COM_DROP_DB() {super(Command.COM_DROP_DB);}

}
