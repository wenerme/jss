package jss.proto;

import static java.nio.charset.StandardCharsets.UTF_8;
import static jss.proto.PacketReader.readPacket;
import static org.junit.Assert.assertEquals;

import com.github.mpjct.jmpjct.mysql.proto.define.Flags;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import java.nio.ByteOrder;
import java.util.regex.Pattern;
import jss.proto.packet.ERR_Packet;
import jss.proto.packet.OK_Packet;
import jss.proto.packet.PacketData;
import org.junit.Test;

public class PacketReaderExampleTest implements Flags
{
    public static ByteBuf fromDumpBytes(String dump)
    {
        ByteBuf buf = Unpooled.buffer();
        Pattern reg = Pattern.compile("\\s{5,}.*$", Pattern.MULTILINE);
//        dump = dump.replace("\\s{5,}.*$", "");// 把具体内容去除
        dump = reg.matcher(dump).replaceAll("");
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

        return buf.order(ByteOrder.LITTLE_ENDIAN);
    }

    @Test
    public void testPacketData()
    {
/*
A COM_QUIT looks like this:

01 00 00 00 01
length: 1
sequence_id: x00
payload: 0x01
 */
        PacketData data = dumpBytesToData("01 00 00 00 01");
        assertEquals(1, data.payloadLength);
        assertEquals(0, data.sequenceId);
        assertEquals(1, data.payload.readByte());

    }

    @Test
    public void testErrPacketExample()
    {
        String dump = "17 00 00 01 ff 48 04 23    48 59 30 30 30 4e 6f 20       .....H.#HY000No\n" +
                "74 61 62 6c 65 73 20 75    73 65 64                      tables used";
        PacketData data = dumpBytesToData(dump);
        ERR_Packet errPacket = readPacket(data.payload, new ERR_Packet(), CLIENT_PROTOCOL_41);
        assertEquals("#", errPacket.sqlStateMarker.toString(UTF_8));
        assertEquals("HY000", errPacket.sqlState.toString(UTF_8));
        assertEquals("No tables used", errPacket.errorMessage.toString(UTF_8));

        System.out.println(errPacket);
    }

    @Test
    public void testOkPacketExample()
    {
        // OK with CLIENT_PROTOCOL_41. 0 affected rows, last-insert-id was 0, AUTOCOMMIT, 0 warnings. No further info.
        PacketData packet = dumpBytesToData("07 00 00 02 00 00 00 02    00 00 00");
        System.out.println(packet);
        OK_Packet okPacket = readPacket(packet.payload, new OK_Packet(), CLIENT_PROTOCOL_41);

        assertEquals(0, okPacket.affectedRows);
        assertEquals(0, okPacket.lastInsertId);
        assertEquals(0, okPacket.warnings);
        assertEquals(SERVER_STATUS_AUTOCOMMIT, okPacket.statusFlags);

    }

    private PacketData dumpBytesToData(String dump)
    {
        ByteBuf buf = fromDumpBytes(dump);
        return readPacket(buf, new PacketData(), 0);
    }
}
