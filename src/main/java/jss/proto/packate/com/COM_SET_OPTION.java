package jss.proto.packate.com;

import jss.proto.define.Command;
import jss.proto.packate.AbstractProtocolText;

/**
 * @see jss.proto.define.Command#COM_SET_OPTION
 */
public class COM_SET_OPTION extends AbstractProtocolText
{
    public long operation = 0;

    public COM_SET_OPTION() {super(Command.COM_SET_OPTION);}
}
