package jss.proto.packet.replication;

import jss.proto.define.Command;
import jss.proto.packet.text.CommandPacket;

/**
 * @see jss.proto.define.Command#COM_REGISTER_SLAVE
 */
public class COM_REGISTER_SLAVE extends CommandPacket
{
    public COM_REGISTER_SLAVE() {super(Command.COM_REGISTER_SLAVE);}
}
