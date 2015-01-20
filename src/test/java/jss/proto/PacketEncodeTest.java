package jss.proto;

import static jss.proto.codec.PacketCodec.readPacket;
import static jss.proto.codec.Packets.writeGenericPacket;
import static org.junit.Assert.assertEquals;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import jss.proto.packet.Packet;
import jss.proto.packet.connection.AuthSwitchRequest;
import jss.proto.util.Dumper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

@Slf4j
public class PacketEncodeTest extends TestUtil
{

    @Test
    public void testAutoSwitch()
    {
        ByteBuf buf = Unpooled.buffer(40);
        final Packet a, b;
        {
            AuthSwitchRequest packet = new AuthSwitchRequest();
            a = packet;
            packet.status = 23;
            packet.authPluginData = buf("authPluginData");
            packet.pluginName = buf("pluginName");
            writeGenericPacket(buf, packet, 0);
        }
        Dumper.hexDumpOut(buf);
        {
            buf = Unpooled.copiedBuffer(buf);
            AuthSwitchRequest packet = readPacket(buf, new AuthSwitchRequest(), 0);
            b = packet;
            assertEquals(23, packet.status);
            assertBufEquals("authPluginData", packet.authPluginData);
            assertBufEquals("pluginName", packet.pluginName);
        }

        System.out.println(a);
        System.out.println(b);
        assertPacketEquals(a, b);
        assertEncode(a, 0);
    }

}
