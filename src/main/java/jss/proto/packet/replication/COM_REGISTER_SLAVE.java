package jss.proto.packet.replication;

import jss.proto.define.Command;
import jss.proto.packet.text.AbstractProtocolText;

/**
 * @see jss.proto.define.Command#COM_REGISTER_SLAVE
 */
public class COM_REGISTER_SLAVE extends AbstractProtocolText
{
    public COM_REGISTER_SLAVE() {super(Command.COM_REGISTER_SLAVE);}
}
