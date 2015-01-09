package jss.proto.packet.text;

import com.github.mpjct.jmpjct.mysql.proto.define.Flags;
import jss.proto.define.Command;

/**
 * @see jss.proto.define.Command#COM_SHUTDOWN
 */
public class COM_SHUTDOWN extends CommandPacket
{
    public long shutdownType = Flags.SHUTDOWN_DEFAULT;

    public COM_SHUTDOWN() {super(Command.COM_SHUTDOWN);}
}