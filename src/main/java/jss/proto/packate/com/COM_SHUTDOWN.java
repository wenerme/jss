package jss.proto.packate.com;

import com.github.mpjct.jmpjct.mysql.proto.Flags;
import jss.proto.define.Command;
import jss.proto.packate.AbstractProtocolText;

/**
 * @see jss.proto.define.Command#COM_SHUTDOWN
 */
public class COM_SHUTDOWN extends AbstractProtocolText
{
    public long shutdownType = Flags.SHUTDOWN_DEFAULT;

    public COM_SHUTDOWN() {super(Command.COM_SHUTDOWN);}
}