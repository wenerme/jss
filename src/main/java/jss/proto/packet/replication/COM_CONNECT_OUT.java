package jss.proto.packet.replication;

import jss.proto.define.Command;
import jss.proto.packet.text.CommandPacket;

/**
 * @see jss.proto.define.Command#COM_CONNECT_OUT
 */
public class COM_CONNECT_OUT extends CommandPacket
{
    public COM_CONNECT_OUT() {super(Command.COM_CONNECT_OUT);}
}