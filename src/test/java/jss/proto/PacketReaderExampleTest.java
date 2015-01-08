package jss.proto;

import static java.nio.charset.StandardCharsets.UTF_8;
import static jss.proto.PacketReader.readPacket;
import static org.junit.Assert.assertEquals;

import com.github.mpjct.jmpjct.mysql.proto.define.Flags;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import java.io.IOException;
import java.nio.ByteOrder;
import java.util.regex.Pattern;
import jss.proto.packet.EOF_Packet;
import jss.proto.packet.ERR_Packet;
import jss.proto.packet.HandshakeResponse41;
import jss.proto.packet.OK_Packet;
import jss.proto.packet.PacketData;
import org.apache.commons.io.HexDump;
import org.junit.Test;

public class PacketReaderExampleTest implements Flags
{
    public static ByteBuf fromDumpBytes(String dump)
    {
        ByteBuf buf = Unpooled.buffer();
        Pattern reg = Pattern.compile("\\s{2,}([^ \r\n]*[^0-9a-fA-F]+[^ \r\n]*)$", Pattern.MULTILINE);
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
    public void testHandshakeResponse41_1()
    {
        PacketData data = dumpBytesToData(
                "54 00 00 01 8d a6 0f 00    00 00 00 01 08 00 00 00    T...............\n" +
                        "00 00 00 00 00 00 00 00    00 00 00 00 00 00 00 00    ................\n" +
                        "00 00 00 00 70 61 6d 00    14 ab 09 ee f6 bc b1 32    ....pam........2\n" +
                        "3e 61 14 38 65 c0 99 1d    95 7d 75 d4 47 74 65 73    >a.8e....}u.Gtes\n" +
                        "74 00 6d 79 73 71 6c 5f    6e 61 74 69 76 65 5f 70    t.mysql_native_p\n" +
                        "61 73 73 77 6f 72 64 00                               assword.");
        HandshakeResponse41 handshakeResponse41 = readPacket(data.payload, new HandshakeResponse41(), CLIENT_PROTOCOL_41 | CLIENT_PLUGIN_AUTH | CLIENT_SECURE_CONNECTION | CLIENT_CONNECT_WITH_DB);

        System.out.println(handshakeResponse41);
    }

    @Test
    public void testEOFPacket()
    {
//        a 4.1 EOF packet with: 0 warnings, AUTOCOMMIT enabled.
        PacketData data = dumpBytesToData("05 00 00 05 fe 00 00 02 00     ..........");
        EOF_Packet eof_packet = readPacket(data.payload, new EOF_Packet(), CLIENT_PROTOCOL_41);
        assertEquals(SERVER_STATUS_AUTOCOMMIT, eof_packet.statusFlags);
        assertEquals(0, eof_packet.warnings);
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
        String dump =
                "17 00 00 01 ff 48 04 23    48 59 30 30 30 4e 6f 20       .....H.#HY000No\n" +
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

    public PacketData dumpBytesToData(String dump)
    {
        ByteBuf buf = fromDumpBytes(dump);
        try
        {
            System.out.println("原始内容");
            System.out.println(dump);
            System.out.println("解析结果");
            HexDump.dump(buf.copy().array(), 0, System.out, 0);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return readPacket(buf, new PacketData(), 0);
    }
}
