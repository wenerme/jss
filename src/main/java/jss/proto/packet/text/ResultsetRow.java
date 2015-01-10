package jss.proto.packet.text;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import io.netty.buffer.ByteBuf;
import java.util.List;
import jss.proto.packet.Packet;
import jss.proto.util.Stringer;

/**
 * <pre>
 * ProtocolText::ResultsetRow:
 * A row with the data for each column.
 *
 * NULL is sent as 0xfb
 *
 * everything else is converted into a string and is sent as Protocol::LengthEncodedString.
 * </pre>
 *
 * @see <a href=http://dev.mysql.com/doc/internals/en/com-query-response.html>com-query-response</a>
 */
public class ResultsetRow implements Packet
{
    private static final Joiner JOINER = Joiner.on(',');


    public List<ByteBuf> cells;

    @Override
    public String toString()
    {
        List<String> strings = Lists.newArrayList();
        if (cells != null)
        {
            for (ByteBuf cell : cells)
            {
                strings.add(Stringer.string(cell));
            }
        }

        return "ResultsetRow{" +
                "cells=[" + JOINER.join(strings) + "]}";
    }
}
