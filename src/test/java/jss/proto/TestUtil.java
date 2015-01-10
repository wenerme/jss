package jss.proto;

import static jss.proto.codec.PacketCodec.readPacket;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import java.io.IOException;
import java.nio.ByteOrder;
import java.util.regex.Pattern;
import jss.proto.packet.PacketData;
import org.apache.commons.io.HexDump;

public class TestUtil extends JSSTest
{
    public static final Pattern MATCH_HEX_DATA = Pattern
            .compile("\\s{2,}([^ \r\n]*[^0-9a-fA-F]+[^ \r\n]*)$", Pattern.MULTILINE);
    public static final Pattern MATCH_OFFSET = Pattern
            .compile("^[^\\s]+\\s", Pattern.MULTILINE);

    public static ByteBuf fromDumpBytes(String dump)
    {
        ByteBuf buf = Unpooled.buffer();
        // 删除偏移值
        if (dump.startsWith("00000000 "))
            dump = MATCH_OFFSET.matcher(dump).replaceAll("");
        dump = MATCH_HEX_DATA.matcher(dump).replaceAll("");
        String[] lines = dump.split("[\n\r]+");
        for (String line : lines)
        {
            String[] split = line.split("\\s+");
            // 一行最多16个
            for (int i = 0; i < split.length && i < 16; i++)
            {
                String b = split[i];
                buf.writeByte(Integer.parseInt(b, 16));
            }
        }

        try
        {
            System.out.println("原始内容");
            System.out.println(dump);
            System.out.println("解析结果 长度:" + buf.readableBytes());
            HexDump.dump(buf.copy().array(), 0, System.out, 0);
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        return buf.order(ByteOrder.LITTLE_ENDIAN);
    }

    public static PacketData dumpBytesToData(String dump)
    {
        ByteBuf buf = fromDumpBytes(dump);
        return readPacket(buf, new PacketData(), 0);
    }
}
