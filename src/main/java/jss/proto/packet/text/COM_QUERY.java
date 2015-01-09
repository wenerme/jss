package jss.proto.packet.text;

import io.netty.buffer.ByteBuf;
import jss.proto.define.Command;
import jss.proto.util.Stringer;

/**
 * @see jss.proto.define.Command#COM_QUERY
 */
public class COM_QUERY extends CommandPacket
{
    public ByteBuf query;

    public COM_QUERY() {super(Command.COM_QUERY);}

    @Override
    public String toString()
    {
        return "COM_QUERY{" +
                "query=" + Stringer.string(query) +
                "} " + super.toString();
    }
}
