package jss.proto.packate.com;

import jss.proto.define.Command;
import jss.proto.packate.AbstractProtocolText;

/**
 * @see jss.proto.define.Command#COM_DEBUG
 */
public class COM_DEBUG extends AbstractProtocolText
{
    public COM_DEBUG() {super(Command.COM_DEBUG);}
}