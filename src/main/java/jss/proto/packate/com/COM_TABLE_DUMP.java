package jss.proto.packate.com;

import jss.proto.define.Command;
import jss.proto.packate.AbstractProtocolText;

/**
 * @see jss.proto.define.Command#COM_TABLE_DUMP
 */
public class COM_TABLE_DUMP extends AbstractProtocolText
{
    public COM_TABLE_DUMP() {super(Command.COM_TABLE_DUMP);}
}