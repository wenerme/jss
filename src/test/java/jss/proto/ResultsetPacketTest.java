package jss.proto;

import static java.nio.charset.StandardCharsets.UTF_8;
import static jss.proto.codec.Codec.int_lenenc;
import static jss.proto.codec.Codec.string_lenenc;
import static jss.proto.codec.PacketCodec.readPacket;

import com.google.common.collect.Lists;
import io.netty.buffer.ByteBuf;
import java.util.List;
import jss.proto.codec.ResultsetCodec;
import jss.proto.define.CapabilityFlags;
import jss.proto.packet.EOF_Packet;
import jss.proto.packet.PacketData;
import jss.proto.packet.text.ColumnDefinition41;
import jss.proto.util.Dumper;
import org.junit.Ignore;
import org.junit.Test;

public class ResultsetPacketTest extends TestUtil
{

    /**
     * 有两列返回内容
     */
    public static final String BASIC_DUMP = "00000000 01 00 00 01 02 54 00 00 02 03 64 65 66 12 69 6E .....T....def.in\n" +
            "00000010 66 6F 72 6D 61 74 69 6F 6E 5F 73 63 68 65 6D 61 formation_schema\n" +
            "00000020 09 56 41 52 49 41 42 4C 45 53 09 56 41 52 49 41 .VARIABLES.VARIA\n" +
            "00000030 42 4C 45 53 0D 56 61 72 69 61 62 6C 65 5F 6E 61 BLES.Variable_na\n" +
            "00000040 6D 65 0D 56 41 52 49 41 42 4C 45 5F 4E 41 4D 45 me.VARIABLE_NAME\n" +
            "00000050 0C 21 00 C0 00 00 00 FD 01 00 00 00 00 4D 00 00 .!...........M..\n" +
            "00000060 03 03 64 65 66 12 69 6E 66 6F 72 6D 61 74 69 6F ..def.informatio\n" +
            "00000070 6E 5F 73 63 68 65 6D 61 09 56 41 52 49 41 42 4C n_schema.VARIABL\n" +
            "00000080 45 53 09 56 41 52 49 41 42 4C 45 53 05 56 61 6C ES.VARIABLES.Val\n" +
            "00000090 75 65 0E 56 41 52 49 41 42 4C 45 5F 56 41 4C 55 ue.VARIABLE_VALU\n" +
            "000000A0 45 0C 21 00 00 0C 00 00 FD 00 00 00 00 00 05 00 E.!.............\n" +
            "000000B0 00 04 FE 00 00 22 00 1A 00 00 05 14 63 68 61 72 .....\"......char\n" +
            "000000C0 61 63 74 65 72 5F 73 65 74 5F 63 6C 69 65 6E 74 acter_set_client\n" +
            "000000D0 04 75 74 66 38 1E 00 00 06 18 63 68 61 72 61 63 .utf8.....charac\n" +
            "000000E0 74 65 72 5F 73 65 74 5F 63 6F 6E 6E 65 63 74 69 ter_set_connecti\n" +
            "000000F0 6F 6E 04 75 74 66 38 1B 00 00 07 15 63 68 61 72 on.utf8.....char\n" +
            "00000100 61 63 74 65 72 5F 73 65 74 5F 72 65 73 75 6C 74 acter_set_result\n" +
            "00000110 73 04 75 74 66 38 1A 00 00 08 14 63 68 61 72 61 s.utf8.....chara\n" +
            "00000120 63 74 65 72 5F 73 65 74 5F 73 65 72 76 65 72 04 cter_set_server.\n" +
            "00000130 75 74 66 38 0E 00 00 09 0C 69 6E 69 74 5F 63 6F utf8.....init_co\n" +
            "00000140 6E 6E 65 63 74 00 1A 00 00 0A 13 69 6E 74 65 72 nnect......inter\n" +
            "00000150 61 63 74 69 76 65 5F 74 69 6D 65 6F 75 74 05 32 active_timeout.2\n" +
            "00000160 38 38 30 30 0C 00 00 0B 07 6C 69 63 65 6E 73 65 8800.....license\n" +
            "00000170 03 47 50 4C 19 00 00 0C 16 6C 6F 77 65 72 5F 63 .GPL.....lower_c\n" +
            "00000180 61 73 65 5F 74 61 62 6C 65 5F 6E 61 6D 65 73 01 ase_table_names.\n" +
            "00000190 32 1B 00 00 0D 12 6D 61 78 5F 61 6C 6C 6F 77 65 2.....max_allowe\n" +
            "000001A0 64 5F 70 61 63 6B 65 74 07 31 30 34 38 35 37 36 d_packet.1048576\n" +
            "000001B0 18 00 00 0E 11 6E 65 74 5F 62 75 66 66 65 72 5F .....net_buffer_\n" +
            "000001C0 6C 65 6E 67 74 68 05 31 36 33 38 34 15 00 00 0F length.16384....\n" +
            "000001D0 11 6E 65 74 5F 77 72 69 74 65 5F 74 69 6D 65 6F .net_write_timeo\n" +
            "000001E0 75 74 02 36 30 13 00 00 10 10 71 75 65 72 79 5F ut.60.....query_\n" +
            "000001F0 63 61 63 68 65 5F 73 69 7A 65 01 30 14 00 00 11 cache_size.0....\n" +
            "00000200 10 71 75 65 72 79 5F 63 61 63 68 65 5F 74 79 70 .query_cache_typ\n" +
            "00000210 65 02 4F 4E 0A 00 00 12 08 73 71 6C 5F 6D 6F 64 e.ON.....sql_mod\n" +
            "00000220 65 00 15 00 00 13 10 73 79 73 74 65 6D 5F 74 69 e......system_ti\n" +
            "00000230 6D 65 5F 7A 6F 6E 65 03 43 53 54 11 00 00 14 09 me_zone.CST.....\n" +
            "00000240 74 69 6D 65 5F 7A 6F 6E 65 06 53 59 53 54 45 4D time_zone.SYSTEM\n" +
            "00000250 1D 00 00 15 0C 74 78 5F 69 73 6F 6C 61 74 69 6F .....tx_isolatio\n" +
            "00000260 6E 0F 52 45 50 45 41 54 41 42 4C 45 2D 52 45 41 n.REPEATABLE-REA\n" +
            "00000270 44 13 00 00 16 0C 77 61 69 74 5F 74 69 6D 65 6F D.....wait_timeo\n" +
            "00000280 75 74 05 32 38 38 30 30 05 00 00 17 FE 00 00 22 ut.28800.......\"\n" +
            "00000290 00                                              .";


    @Test
    public void testResultsetReader()
    {
        ResultsetCodec reader = new ResultsetCodec()
                .read(fromDumpBytes(BASIC_DUMP), CapabilityFlags.CLIENT_BASIC_FLAGS);

        for (ColumnDefinition41 col : reader.columns())
        {
            System.out.println(col);
        }
        while (reader.hasNext())
        {
            System.out.println(reader.next());
        }
    }

    /**
     * 尝试解析结果集
     */
    @Test
    @Ignore
    public void testResultset()
    {
        ByteBuf buf = fromDumpBytes(BASIC_DUMP);
        System.out.println();

        int flags = CapabilityFlags.CLIENT_BASIC_FLAGS;
        PacketData data = new PacketData();

        readPacket(buf, data, flags);

        ByteBuf payload = data.payload;
        Dumper.hexDumpOut(payload);

        long fieldCount = int_lenenc(payload);

        readPacket(buf, data, flags);
        payload = data.payload;
        Dumper.hexDumpOut(payload);
        List<ColumnDefinition41> columns = Lists.newArrayList();
        for (long i = 0; i < fieldCount; i++)
        {
            ColumnDefinition41 packet = readPacket(payload, new ColumnDefinition41(), 0);
            System.out.println(packet);
//            System.out.println("该包剩余内容\n" + Dumper.hexDumpReadable(payload));
//            System.out.println("总剩余\n" + Dumper.hexDumpReadable(buf));
            readPacket(buf, data, 0);
            payload = data.payload;
            columns.add(packet);
        }
        readPacket(buf, data, flags);
        payload = data.payload;
        System.out.println(readPacket(payload, new EOF_Packet(), flags));

        readPacket(buf, data, flags);
        payload = data.payload;
//        System.out.println("总剩余\n" + Dumper.hexDumpReadable(buf));

        while (true)
        {
            readPacket(buf, data, flags);
            payload = data.payload;

            if (payload.getUnsignedByte(payload.readerIndex()) == 0xfe)
            {
                break;
            }

            System.out.printf("| ");
            for (long i = 0; i < fieldCount; i++)
            {
                System.out.printf(string_lenenc(payload).toString(UTF_8) + " | ");
            }
            System.out.println();
        }
        System.out.println(readPacket(payload, new EOF_Packet(), flags));
        System.out.println("总剩余\n" + Dumper.hexDumpReadable(buf));

    }

}
