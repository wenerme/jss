package jss.proto.packet.text;

import io.netty.buffer.ByteBuf;
import jss.proto.define.Command;
import jss.proto.util.Dumper;

/**
 * @see jss.proto.define.Command#COM_QUERY
 */
public class COM_QUERY extends AbstractProtocolText
{
    public ByteBuf query;

    public COM_QUERY() {super(Command.COM_QUERY);}

    @Override
    public String toString()
    {
        return "COM_QUERY{" +
                "query=" + Dumper.string(query) +
                "} " + super.toString();
    }
}
