package jss.proto.util;

import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.EnumSet;
import java.util.List;
import jss.proto.codec.ResultsetCodec;
import jss.proto.define.MySQLType;
import jss.proto.packet.text.ColumnDefinition41;
import jss.proto.packet.text.ResultsetRow;
import jss.util.IsInteger;
import jss.util.Values;
import jss.util.jdbc.QueryResult;
import org.apache.commons.io.HexDump;

/**
 * 与 Stringer 类似,但是会输出更多调试相关的信息
 */
public class Dumper
{
    // region 数据 dump
    public static <T extends Enum<T> & IsInteger> String dump(long flags, Class<T> type)
    {
        EnumSet<T> set = Values.asEnumSet(flags, type);
        return String.format("%s -> %s", Long.toBinaryString(flags), set);
    }


    public static String dump(ByteBuf buf)
    {
        return buf == null ? null : ByteBufUtil.hexDump(buf);
    }

    public static String hexDumpReadable(ByteBuf buf)
    {
        if (buf.readableBytes() == 0)
        {
            return "00000000 \n";
        }

        return hexDump(buf.array(), buf.readerIndex());
    }

    public static String hexDump(ByteBuf buf)
    {
        return hexDump(buf.array());
    }

    public static String hexDumpOut(byte[] buf)
    {
        String dump = hexDump(buf);
        System.out.println(dump);
        return dump;
    }

    public static String hexDumpOut(ByteBuf buf)
    {
        String dump = hexDump(buf.array());
        System.out.println(dump);
        return dump;
    }

    public static String hexDump(byte[] bytes)
    {
        return hexDump(bytes, 0);
    }

    public static String hexDump(byte[] bytes, int index)
    {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try
        {
            HexDump.dump(bytes, 0, os, index);
        } catch (IOException e)
        {
            Throwables.propagate(e);
        }
        return Stringer.string(os.toByteArray());
    }

    // endregion

    public static String dump(ResultsetCodec rs)
    {

        return null;
    }


    public static String charset(int charset)
    {
        String cs = CharsetUtil.getCharset(charset);
        if (cs == null)
        {
            return "未知的编码类型 (" + charset + ")";
        }
        return String.format("%s -> %s", charset, cs);
    }

    public static String sqlType(int sqlType)
    {
        MySQLType type = Values.fromValue(MySQLType.class, sqlType);
        if (type == null)
        {
            return "未知的 SQL 类型 (" + sqlType + ")";
        }
        return String.format("%s -> %s", sqlType, type);
    }

    public static List<String> toStringList(ResultsetRow row)
    {
        List<String> to = Lists.newArrayList();
        for (ByteBuf cell : row.cells)
        {
            to.add(Stringer.cell(cell));
        }
        return to;
    }

    @SuppressWarnings("unchecked")
    public static QueryResult toQueryResult(ResultsetCodec rs)
    {
        QueryResult qr = new QueryResult();
        for (ColumnDefinition41 col : rs.columns())
        {
            qr.getColumn().add(Stringer.string(col.name));
        }

        while (rs.hasNext())/*预加载*/ rs.next();

        for (ResultsetRow row : rs.rows())
        {
            qr.getRows().add((List) toStringList(row));
        }

        return qr;
    }
}
