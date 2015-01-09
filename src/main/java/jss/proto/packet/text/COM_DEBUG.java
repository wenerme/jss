package jss.proto.packet.text;

import jss.proto.define.Command;

/**
 * @see jss.proto.define.Command#COM_DEBUG
 */
public class COM_DEBUG extends CommandPacket
{
    public COM_DEBUG() {super(Command.COM_DEBUG);}
}