package jss.proto;

import static java.nio.charset.StandardCharsets.UTF_8;
import static jss.proto.codec.PacketCodec.readPacket;
import static org.junit.Assert.assertEquals;

import io.netty.buffer.ByteBuf;
import jss.proto.define.Flags;
import jss.proto.packet.EOF_Packet;
import jss.proto.packet.ERR_Packet;
import jss.proto.packet.OK_Packet;
import jss.proto.packet.PacketData;
import jss.proto.packet.connection.AuthSwitchRequest;
import jss.proto.packet.connection.HandshakeResponse41;
import jss.proto.packet.text.ColumnDefinition41;
import jss.proto.util.Dumper;
import jss.proto.util.Stringer;
import org.junit.Test;

public class PacketReaderExampleTest extends TestUtil implements Flags
{
    /**
     * @see <a href=http://dev.mysql.com/doc/internals/en/com-field-list-response.html>com-field-list-response</a>
     */
    @Test
    public void testCOM_FIELDS_LISTResponse()
    {

        String dump = "31 00 00 01 03 64 65 66    04 74 65 73 74 09 66 69    1....def.test.fi\n" +
                "65 6c 64 6c 69 73 74 09    66 69 65 6c 64 6c 69 73    eldlist.fieldlis\n" +
                "74 02 69 64 02 69 64 0c    3f 00 0b 00 00 00 03 00    t.id.id.?.......\n" +
                "00 00 00 00 fb 05 00 00    02 fe 00 00 02 00          ..............";

        ByteBuf buf = fromDumpBytes(dump);
        PacketData data = readPacket(buf, new PacketData(), 0);
        assertEncode(data, 0);
        // 使用 readPacketForCOM_FIELD_LIST 读取数据长度不一致
        ColumnDefinition41 packet = readPacket(data.payload, new ColumnDefinition41(), 0);
        assertEquals("def", Stringer.string(packet.catalog));
        assertEquals("test", Stringer.string(packet.schema));
        assertEquals("fieldlist", Stringer.string(packet.table));
        assertEquals("fieldlist", Stringer.string(packet.orgTable));
        assertEquals("id", Stringer.string(packet.name));
        assertEquals(12, packet.fixedLengthFieldsLength);
        assertEquals(63, packet.characterSet);
        assertEquals(11, packet.columnLength);
        assertEquals(3, packet.type);
        assertEquals(0, packet.flags);
        assertEquals(0, packet.decimals);

        assertEncode(packet, 0);
        // TODO 最后还有 FB 未读
        System.out.println(Dumper.hexDumpReadable(data.payload));

        System.out.println(packet);

        readPacket(buf, data, 0);
        EOF_Packet eof = readPacket(data.payload, new EOF_Packet(), 0);
        assertEncode(eof, 0);
        System.out.println(eof);
    }

    @Test
    public void testAuthSwitchRequest()
    {
        PacketData data = dumpBytesToData("2c 00 00 02 fe 6d 79 73    71 6c 5f 6e 61 74 69 76    ,....mysql_nativ\n" +
                "65 5f 70 61 73 73 77 6f    72 64 00 7a 51 67 34 69    e_password.zQg4i\n" +
                "36 6f 4e 79 36 3d 72 48    4e 2f 3e 2d 62 29 41 00    6oNy6=rHN>-b)A.");
        AuthSwitchRequest authSwitchRequest = readPacket(data.payload, new AuthSwitchRequest(), 0);
        assertEquals("mysql_native_password", Stringer.string(authSwitchRequest.pluginName));
        System.out.println(authSwitchRequest);
        assertEncode(authSwitchRequest, 0);
    }

    @Test
    public void testHandshakeResponse41_2()
    {
        PacketData data = dumpBytesToData("b2 00 00 01 85 a2 1e 00    00 00 00 40 08 00 00 00    ...........@....\n" +
                "00 00 00 00 00 00 00 00    00 00 00 00 00 00 00 00    ................\n" +
                "00 00 00 00 72 6f 6f 74    00 14 22 50 79 a2 12 d4    ....root..\"Py...\n" +
                "e8 82 e5 b3 f4 1a 97 75    6b c8 be db 9f 80 6d 79    .......uk.....my\n" +
                "73 71 6c 5f 6e 61 74 69    76 65 5f 70 61 73 73 77    sql_native_passw\n" +
                "6f 72 64 00 61 03 5f 6f    73 09 64 65 62 69 61 6e    ord.a._os.debian\n" +
                "36 2e 30 0c 5f 63 6c 69    65 6e 74 5f 6e 61 6d 65    6.0._client_name\n" +
                "08 6c 69 62 6d 79 73 71    6c 04 5f 70 69 64 05 32    .libmysql._pid.2\n" +
                "32 33 34 34 0f 5f 63 6c    69 65 6e 74 5f 76 65 72    2344._client_ver\n" +
                "73 69 6f 6e 08 35 2e 36    2e 36 2d 6d 39 09 5f 70    sion.5.6.6-m9._p\n" +
                "6c 61 74 66 6f 72 6d 06    78 38 36 5f 36 34 03 66    latform.x86_64.f\n" +
                "6f 6f 03 62 61 72                                     oo.bar");

        int flags = CLIENT_PROTOCOL_41 | CLIENT_PLUGIN_AUTH | CLIENT_SECURE_CONNECTION | CLIENT_CONNECT_WITH_DB;
        HandshakeResponse41 handshakeResponse41 = readPacket(data.payload, new HandshakeResponse41(), flags);
        flags = handshakeResponse41.capabilityFlags;
        System.out.println(handshakeResponse41);
        assertEncode(handshakeResponse41, flags);
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
        assertEncode(handshakeResponse41, handshakeResponse41.capabilityFlags);

    }

    @Test
    public void testEOFPacket()
    {
//        a 4.1 EOF packet with: 0 warnings, AUTOCOMMIT enabled.
        PacketData data = dumpBytesToData("05 00 00 05 fe 00 00 02 00     ..........");
        EOF_Packet eof_packet = readPacket(data.payload, new EOF_Packet(), CLIENT_PROTOCOL_41);
        assertEquals(SERVER_STATUS_AUTOCOMMIT, eof_packet.statusFlags);
        assertEquals(0, eof_packet.warnings);
        assertEncode(eof_packet, CLIENT_PROTOCOL_41);
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
        assertEquals(1, data.payload.getByte(data.payload.readerIndex()));

        assertEncode(data, CLIENT_PROTOCOL_41);
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
        assertEncode(errPacket, CLIENT_PROTOCOL_41);
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

        assertEncode(okPacket, CLIENT_PROTOCOL_41);

    }

}
