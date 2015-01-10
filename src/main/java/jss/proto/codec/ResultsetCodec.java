package jss.proto.codec;

import static jss.proto.codec.Codec.*;
import static jss.proto.codec.PacketCodec.hasFlag;
import static jss.proto.codec.PacketCodec.readPacket;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import io.netty.buffer.ByteBuf;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import jss.proto.define.Flags;
import jss.proto.packet.EOF_Packet;
import jss.proto.packet.ERR_Packet;
import jss.proto.packet.Packet;
import jss.proto.packet.PacketData;
import jss.proto.packet.text.ColumnDefinition41;
import jss.proto.packet.text.ResultsetRow;

/**
 * 结果集读取
 * <pre>
 * a packet containing a Protocol::LengthEncodedInteger column_count
 *
 * column_count * Protocol::ColumnDefinition packets
 *
 * if CLIENT_DEPRECATE_EOF isn't set, EOF_Packet
 *
 * one or more ProtocolText::ResultsetRow packets, each containing column_count values
 *
 * if CLIENT_DEPRECATE_EOF isn't set, EOF_Packet otherwise OK_Packet or ERR_Packet in case of error.
 *
 * If the SERVER_MORE_RESULTS_EXISTS flag is set in the last EOF_Packet another ProtocolText::Resultset will follow (see Multi-resultset).
 *
 *
 * <b>Example</b>
 * see ProtocolText::Resultset
 * </pre>
 *
 * @see <a href=http://dev.mysql.com/doc/internals/en/com-query-response.html#packet-ProtocolText::Resultset>Resultset</a>
 */
public class ResultsetCodec implements Packet, Iterator<ResultsetRow>
{
    // field count
    // field *
    // eof
    // row *
    // eof | err

    private final List<ColumnDefinition41> columns = Lists.newArrayList();
    private final List<ResultsetRow> rows = Lists.newArrayList();
    private long fieldCount;
    private EOF_Packet lastEofPacket = new EOF_Packet();
    private ERR_Packet lastErrPacket = new ERR_Packet();
    private Boolean hasNext;
    private boolean hasError = false;
    private ByteBuf buf;
    private ResultsetRow next;
    private PacketData data = new PacketData();
    private int capabilityFlags;

    public ResultsetCodec()
    {
    }

    public boolean hasError()
    {
        return hasError;
    }

    public ERR_Packet getLastErrPacket()
    {
        return lastErrPacket;
    }

    public void reset()
    {
        fieldCount = 0;
        columns.clear();
//        lastEofPacket = null;
        hasNext = null;
    }

    public ByteBuf byteBuf()
    {
        return buf;
    }

    public ResultsetCodec read(ByteBuf byteBuf, int flags)
    {
        reset();
        this.buf = byteBuf;
        capabilityFlags = flags;
        read();
        fieldCount = int_lenenc(data.payload);
        for (long i = 0; i < fieldCount; i++)
        {
            read();
            ColumnDefinition41 packet = readPacket(data.payload, new ColumnDefinition41(), 0);
            columns.add(packet);
        }
        if (!hasFlag(flags, Flags.CLIENT_DEPRECATE_EOF))
        {
            read();
            readPacket(data.payload, lastEofPacket, capabilityFlags);
        }

        read();
        readNext();
        return this;
    }

    private void readNext()
    {
        read();
        Codec.Status status = getStatus(data.payload);
        hasError = false;
        switch (status)
        {
            case EOF:
                hasNext = false;
                lastEofPacket = readPacket(data.payload, lastEofPacket, capabilityFlags);
                break;
            case ERR:
                hasNext = false;
                hasError = true;
                lastErrPacket = readPacket(data.payload, lastErrPacket, capabilityFlags);
                break;
            case OK:
                throw new RuntimeException("此时不应该有 OK 包");
            case UNKNOWN:
            {
                hasNext = true;
                next = readRow();
            }
            break;
        }
    }

    private void read()
    {
        readPacket(buf, data, capabilityFlags);
    }

    public ResultsetCodec write(ByteBuf byteBuf)
    {
        reset();
        this.buf = byteBuf;

        return this;
    }

    public long fieldCount()
    {
        return fieldCount;
    }

    public List<ColumnDefinition41> columns()
    {
        return ImmutableList.copyOf(columns);
    }

    public EOF_Packet lastEofPacket()
    {
        return lastEofPacket;
    }

    @Override
    public boolean hasNext()
    {
        return hasNext;
    }

    @Override
    public ResultsetRow next()
    {
        if (next == null || !hasNext)
            throw new NoSuchElementException();
        ResultsetRow current = next;
        rows.add(current);
        readNext();
        return current;
    }

    protected ResultsetRow readRow()
    {
        ResultsetRow row = new ResultsetRow();
        row.cells = Lists.newArrayList();
        for (long i = 0; i < fieldCount; i++)
        {
            row.cells.add(string_lenenc(data.payload));
        }
        return row;
    }

    @Override
    public void remove()
    {
        throw new UnsupportedOperationException();
    }

}
