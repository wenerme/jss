package jss.proto.packate.com;

import jss.proto.define.Command;
import jss.proto.packate.AbstractProtocolText;

/**
 * @see jss.proto.define.Command#COM_BINLOG_DUMP
 */
public class COM_BINLOG_DUMP extends AbstractProtocolText
{
    public COM_BINLOG_DUMP() {super(Command.COM_BINLOG_DUMP);}
}
