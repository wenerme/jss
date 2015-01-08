package jss.proto.packet.replication;

import jss.proto.define.Command;
import jss.proto.packet.AbstractProtocolText;

/**
 * @see jss.proto.define.Command#COM_BINLOG_DUMP
 */
public class COM_BINLOG_DUMP extends AbstractProtocolText
{
    public COM_BINLOG_DUMP() {super(Command.COM_BINLOG_DUMP);}
}