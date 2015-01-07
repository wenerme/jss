package jss.proto.packate.com;

import jss.proto.define.Command;
import jss.proto.packate.AbstractProtocolText;

/**
 * @see jss.proto.define.Command#COM_PING
 */
public class COM_PING extends AbstractProtocolText
{
    public COM_PING() {super(Command.COM_PING);}
}
