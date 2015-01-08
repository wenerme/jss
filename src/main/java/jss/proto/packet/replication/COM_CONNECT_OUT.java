package jss.proto.packet.replication;

import jss.proto.define.Command;
import jss.proto.packet.AbstractProtocolText;

/**
 * @see jss.proto.define.Command#COM_CONNECT_OUT
 */
public class COM_CONNECT_OUT extends AbstractProtocolText
{
    public COM_CONNECT_OUT() {super(Command.COM_CONNECT_OUT);}
}