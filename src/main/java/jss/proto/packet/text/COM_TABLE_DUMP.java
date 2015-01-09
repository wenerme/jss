package jss.proto.packet.text;

import jss.proto.define.Command;

/**
 * @see jss.proto.define.Command#COM_TABLE_DUMP
 */
public class COM_TABLE_DUMP extends CommandPacket
{
    public COM_TABLE_DUMP() {super(Command.COM_TABLE_DUMP);}
}