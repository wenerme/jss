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

    /*
    SHOW FULL COLUMNS FROM `ALL_PLUGINS` FROM `information_schema` LIKE '%';
+------------------------+-------------+-----------------+------+-----+---------+-------+------------+---------+
| Field                  | Type        | Collation       | Null | Key | Default | Extra | Privileges | Comment |
+------------------------+-------------+-----------------+------+-----+---------+-------+------------+---------+
| PLUGIN_NAME            | varchar(64) | utf8_general_ci | NO   |     |         |       | select     |         |
| PLUGIN_VERSION         | varchar(20) | utf8_general_ci | NO   |     |         |       | select     |         |
| PLUGIN_STATUS          | varchar(16) | utf8_general_ci | NO   |     |         |       | select     |         |
| PLUGIN_TYPE            | varchar(80) | utf8_general_ci | NO   |     |         |       | select     |         |
| PLUGIN_TYPE_VERSION    | varchar(20) | utf8_general_ci | NO   |     |         |       | select     |         |
| PLUGIN_LIBRARY         | varchar(64) | utf8_general_ci | YES  |     | NULL    |       | select     |         |
| PLUGIN_LIBRARY_VERSION | varchar(20) | utf8_general_ci | YES  |     | NULL    |       | select     |         |
| PLUGIN_AUTHOR          | varchar(64) | utf8_general_ci | YES  |     | NULL    |       | select     |         |
| PLUGIN_DESCRIPTION     | longtext    | utf8_general_ci | YES  |     | NULL    |       | select     |         |
| PLUGIN_LICENSE         | varchar(80) | utf8_general_ci | NO   |     |         |       | select     |         |
| LOAD_OPTION            | varchar(64) | utf8_general_ci | NO   |     |         |       | select     |         |
| PLUGIN_MATURITY        | varchar(12) | utf8_general_ci | NO   |     |         |       | select     |         |
| PLUGIN_AUTH_VERSION    | varchar(80) | utf8_general_ci | YES  |     | NULL    |       | select     |         |
+------------------------+-------------+-----------------+------+-----+---------+-------+------------+---------+
     */
    public static final String COLUMN_DUMP = "00000000 01 00 00 01 09 46 00 00 02 03 64 65 66 12 69 6E .....F....def.in\n" +
            "00000010 66 6F 72 6D 61 74 69 6F 6E 5F 73 63 68 65 6D 61 formation_schema\n" +
            "00000020 07 43 4F 4C 55 4D 4E 53 07 43 4F 4C 55 4D 4E 53 .COLUMNS.COLUMNS\n" +
            "00000030 05 46 69 65 6C 64 0B 43 4F 4C 55 4D 4E 5F 4E 41 .Field.COLUMN_NA\n" +
            "00000040 4D 45 0C 21 00 C0 00 00 00 FD 01 00 00 00 00 45 ME.!...........E\n" +
            "00000050 00 00 03 03 64 65 66 12 69 6E 66 6F 72 6D 61 74 ....def.informat\n" +
            "00000060 69 6F 6E 5F 73 63 68 65 6D 61 07 43 4F 4C 55 4D ion_schema.COLUM\n" +
            "00000070 4E 53 07 43 4F 4C 55 4D 4E 53 04 54 79 70 65 0B NS.COLUMNS.Type.\n" +
            "00000080 43 4F 4C 55 4D 4E 5F 54 59 50 45 0C 21 00 FD FF COLUMN_TYPE.!...\n" +
            "00000090 02 00 FC 11 00 00 00 00 4D 00 00 04 03 64 65 66 ........M....def\n" +
            "000000A0 12 69 6E 66 6F 72 6D 61 74 69 6F 6E 5F 73 63 68 .information_sch\n" +
            "000000B0 65 6D 61 07 43 4F 4C 55 4D 4E 53 07 43 4F 4C 55 ema.COLUMNS.COLU\n" +
            "000000C0 4D 4E 53 09 43 6F 6C 6C 61 74 69 6F 6E 0E 43 4F MNS.Collation.CO\n" +
            "000000D0 4C 4C 41 54 49 4F 4E 5F 4E 41 4D 45 0C 21 00 60 LLATION_NAME.!.`\n" +
            "000000E0 00 00 00 FD 00 00 00 00 00 45 00 00 05 03 64 65 .........E....de\n" +
            "000000F0 66 12 69 6E 66 6F 72 6D 61 74 69 6F 6E 5F 73 63 f.information_sc\n" +
            "00000100 68 65 6D 61 07 43 4F 4C 55 4D 4E 53 07 43 4F 4C hema.COLUMNS.COL\n" +
            "00000110 55 4D 4E 53 04 4E 75 6C 6C 0B 49 53 5F 4E 55 4C UMNS.Null.IS_NUL\n" +
            "00000120 4C 41 42 4C 45 0C 21 00 09 00 00 00 FD 01 00 00 LABLE.!.........\n" +
            "00000130 00 00 43 00 00 06 03 64 65 66 12 69 6E 66 6F 72 ..C....def.infor\n" +
            "00000140 6D 61 74 69 6F 6E 5F 73 63 68 65 6D 61 07 43 4F mation_schema.CO\n" +
            "00000150 4C 55 4D 4E 53 07 43 4F 4C 55 4D 4E 53 03 4B 65 LUMNS.COLUMNS.Ke\n" +
            "00000160 79 0A 43 4F 4C 55 4D 4E 5F 4B 45 59 0C 21 00 09 y.COLUMN_KEY.!..\n" +
            "00000170 00 00 00 FD 01 00 00 00 00 4B 00 00 07 03 64 65 .........K....de\n" +
            "00000180 66 12 69 6E 66 6F 72 6D 61 74 69 6F 6E 5F 73 63 f.information_sc\n" +
            "00000190 68 65 6D 61 07 43 4F 4C 55 4D 4E 53 07 43 4F 4C hema.COLUMNS.COL\n" +
            "000001A0 55 4D 4E 53 07 44 65 66 61 75 6C 74 0E 43 4F 4C UMNS.Default.COL\n" +
            "000001B0 55 4D 4E 5F 44 45 46 41 55 4C 54 0C 21 00 FD FF UMN_DEFAULT.!...\n" +
            "000001C0 02 00 FC 10 00 00 00 00 40 00 00 08 03 64 65 66 ........@....def\n" +
            "000001D0 12 69 6E 66 6F 72 6D 61 74 69 6F 6E 5F 73 63 68 .information_sch\n" +
            "000001E0 65 6D 61 07 43 4F 4C 55 4D 4E 53 07 43 4F 4C 55 ema.COLUMNS.COLU\n" +
            "000001F0 4D 4E 53 05 45 78 74 72 61 05 45 58 54 52 41 0C MNS.Extra.EXTRA.\n" +
            "00000200 21 00 51 00 00 00 FD 01 00 00 00 00 4A 00 00 09 !.Q.........J...\n" +
            "00000210 03 64 65 66 12 69 6E 66 6F 72 6D 61 74 69 6F 6E .def.information\n" +
            "00000220 5F 73 63 68 65 6D 61 07 43 4F 4C 55 4D 4E 53 07 _schema.COLUMNS.\n" +
            "00000230 43 4F 4C 55 4D 4E 53 0A 50 72 69 76 69 6C 65 67 COLUMNS.Privileg\n" +
            "00000240 65 73 0A 50 52 49 56 49 4C 45 47 45 53 0C 21 00 es.PRIVILEGES.!.\n" +
            "00000250 F0 00 00 00 FD 01 00 00 00 00 4B 00 00 0A 03 64 ..........K....d\n" +
            "00000260 65 66 12 69 6E 66 6F 72 6D 61 74 69 6F 6E 5F 73 ef.information_s\n" +
            "00000270 63 68 65 6D 61 07 43 4F 4C 55 4D 4E 53 07 43 4F chema.COLUMNS.CO\n" +
            "00000280 4C 55 4D 4E 53 07 43 6F 6D 6D 65 6E 74 0E 43 4F LUMNS.Comment.CO\n" +
            "00000290 4C 55 4D 4E 5F 43 4F 4D 4D 45 4E 54 0C 21 00 00 LUMN_COMMENT.!..\n" +
            "000002A0 0C 00 00 FD 01 00 00 00 00 05 00 00 0B FE 00 00 ................\n" +
            "000002B0 22 00 36 00 00 0C 0B 50 4C 55 47 49 4E 5F 4E 41 \".6....PLUGIN_NA\n" +
            "000002C0 4D 45 0B 76 61 72 63 68 61 72 28 36 34 29 0F 75 ME.varchar(64).u\n" +
            "000002D0 74 66 38 5F 67 65 6E 65 72 61 6C 5F 63 69 02 4E tf8_general_ci.N\n" +
            "000002E0 4F 00 00 00 06 73 65 6C 65 63 74 00 39 00 00 0D O....select.9...\n" +
            "000002F0 0E 50 4C 55 47 49 4E 5F 56 45 52 53 49 4F 4E 0B .PLUGIN_VERSION.\n" +
            "00000300 76 61 72 63 68 61 72 28 32 30 29 0F 75 74 66 38 varchar(20).utf8\n" +
            "00000310 5F 67 65 6E 65 72 61 6C 5F 63 69 02 4E 4F 00 00 _general_ci.NO..\n" +
            "00000320 00 06 73 65 6C 65 63 74 00 38 00 00 0E 0D 50 4C ..select.8....PL\n" +
            "00000330 55 47 49 4E 5F 53 54 41 54 55 53 0B 76 61 72 63 UGIN_STATUS.varc\n" +
            "00000340 68 61 72 28 31 36 29 0F 75 74 66 38 5F 67 65 6E har(16).utf8_gen\n" +
            "00000350 65 72 61 6C 5F 63 69 02 4E 4F 00 00 00 06 73 65 eral_ci.NO....se\n" +
            "00000360 6C 65 63 74 00 36 00 00 0F 0B 50 4C 55 47 49 4E lect.6....PLUGIN\n" +
            "00000370 5F 54 59 50 45 0B 76 61 72 63 68 61 72 28 38 30 _TYPE.varchar(80\n" +
            "00000380 29 0F 75 74 66 38 5F 67 65 6E 65 72 61 6C 5F 63 ).utf8_general_c\n" +
            "00000390 69 02 4E 4F 00 00 00 06 73 65 6C 65 63 74 00 3E i.NO....select.>\n" +
            "000003A0 00 00 10 13 50 4C 55 47 49 4E 5F 54 59 50 45 5F ....PLUGIN_TYPE_\n" +
            "000003B0 56 45 52 53 49 4F 4E 0B 76 61 72 63 68 61 72 28 VERSION.varchar(\n" +
            "000003C0 32 30 29 0F 75 74 66 38 5F 67 65 6E 65 72 61 6C 20).utf8_general\n" +
            "000003D0 5F 63 69 02 4E 4F 00 00 00 06 73 65 6C 65 63 74 _ci.NO....select\n" +
            "000003E0 00 3A 00 00 11 0E 50 4C 55 47 49 4E 5F 4C 49 42 .:....PLUGIN_LIB\n" +
            "000003F0 52 41 52 59 0B 76 61 72 63 68 61 72 28 36 34 29 RARY.varchar(64)\n" +
            "00000400 0F 75 74 66 38 5F 67 65 6E 65 72 61 6C 5F 63 69 .utf8_general_ci\n" +
            "00000410 03 59 45 53 00 FB 00 06 73 65 6C 65 63 74 00 42 .YES....select.B\n" +
            "00000420 00 00 12 16 50 4C 55 47 49 4E 5F 4C 49 42 52 41 ....PLUGIN_LIBRA\n" +
            "00000430 52 59 5F 56 45 52 53 49 4F 4E 0B 76 61 72 63 68 RY_VERSION.varch\n" +
            "00000440 61 72 28 32 30 29 0F 75 74 66 38 5F 67 65 6E 65 ar(20).utf8_gene\n" +
            "00000450 72 61 6C 5F 63 69 03 59 45 53 00 FB 00 06 73 65 ral_ci.YES....se\n" +
            "00000460 6C 65 63 74 00 39 00 00 13 0D 50 4C 55 47 49 4E lect.9....PLUGIN\n" +
            "00000470 5F 41 55 54 48 4F 52 0B 76 61 72 63 68 61 72 28 _AUTHOR.varchar(\n" +
            "00000480 36 34 29 0F 75 74 66 38 5F 67 65 6E 65 72 61 6C 64).utf8_general\n" +
            "00000490 5F 63 69 03 59 45 53 00 FB 00 06 73 65 6C 65 63 _ci.YES....selec\n" +
            "000004A0 74 00 3B 00 00 14 12 50 4C 55 47 49 4E 5F 44 45 t.;....PLUGIN_DE\n" +
            "000004B0 53 43 52 49 50 54 49 4F 4E 08 6C 6F 6E 67 74 65 SCRIPTION.longte\n" +
            "000004C0 78 74 0F 75 74 66 38 5F 67 65 6E 65 72 61 6C 5F xt.utf8_general_\n" +
            "000004D0 63 69 03 59 45 53 00 FB 00 06 73 65 6C 65 63 74 ci.YES....select\n" +
            "000004E0 00 39 00 00 15 0E 50 4C 55 47 49 4E 5F 4C 49 43 .9....PLUGIN_LIC\n" +
            "000004F0 45 4E 53 45 0B 76 61 72 63 68 61 72 28 38 30 29 ENSE.varchar(80)\n" +
            "00000500 0F 75 74 66 38 5F 67 65 6E 65 72 61 6C 5F 63 69 .utf8_general_ci\n" +
            "00000510 02 4E 4F 00 00 00 06 73 65 6C 65 63 74 00 36 00 .NO....select.6.\n" +
            "00000520 00 16 0B 4C 4F 41 44 5F 4F 50 54 49 4F 4E 0B 76 ...LOAD_OPTION.v\n" +
            "00000530 61 72 63 68 61 72 28 36 34 29 0F 75 74 66 38 5F archar(64).utf8_\n" +
            "00000540 67 65 6E 65 72 61 6C 5F 63 69 02 4E 4F 00 00 00 general_ci.NO...\n" +
            "00000550 06 73 65 6C 65 63 74 00 3A 00 00 17 0F 50 4C 55 .select.:....PLU\n" +
            "00000560 47 49 4E 5F 4D 41 54 55 52 49 54 59 0B 76 61 72 GIN_MATURITY.var\n" +
            "00000570 63 68 61 72 28 31 32 29 0F 75 74 66 38 5F 67 65 char(12).utf8_ge\n" +
            "00000580 6E 65 72 61 6C 5F 63 69 02 4E 4F 00 00 00 06 73 neral_ci.NO....s\n" +
            "00000590 65 6C 65 63 74 00 3F 00 00 18 13 50 4C 55 47 49 elect.?....PLUGI\n" +
            "000005A0 4E 5F 41 55 54 48 5F 56 45 52 53 49 4F 4E 0B 76 N_AUTH_VERSION.v\n" +
            "000005B0 61 72 63 68 61 72 28 38 30 29 0F 75 74 66 38 5F archar(80).utf8_\n" +
            "000005C0 67 65 6E 65 72 61 6C 5F 63 69 03 59 45 53 00 FB general_ci.YES..\n" +
            "000005D0 00 06 73 65 6C 65 63 74 00 05 00 00 19 FE 00 00 ..select........\n" +
            "000005E0 22 00                                           \".";
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
                .read(fromDumpBytes(COLUMN_DUMP), CapabilityFlags.CLIENT_BASIC_FLAGS);

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
