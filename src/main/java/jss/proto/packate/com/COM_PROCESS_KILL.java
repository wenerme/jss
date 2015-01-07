package jss.proto.packate.com;

import jss.proto.define.Command;
import jss.proto.packate.AbstractProtocolText;

/**
 * @see jss.proto.define.Command#COM_PROCESS_KILL
 */
public class COM_PROCESS_KILL extends AbstractProtocolText
{
    public long connectionId = 0;

    public COM_PROCESS_KILL() {super(Command.COM_PROCESS_KILL);}
}