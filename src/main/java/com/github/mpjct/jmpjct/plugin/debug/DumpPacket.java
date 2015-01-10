package com.github.mpjct.jmpjct.plugin.debug;

import static jss.proto.codec.PacketReader.readPacketPayload;

import com.github.mpjct.jmpjct.Engine;
import com.github.mpjct.jmpjct.plugin.PluginAdapter;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import java.io.IOException;
import java.nio.ByteOrder;
import jss.proto.codec.Codec;
import jss.proto.codec.PacketCodec;
import jss.proto.codec.PacketReader;
import jss.proto.codec.ResultsetCodec;
import jss.proto.packet.Packet;
import jss.proto.packet.PacketData;
import jss.proto.packet.connection.HandshakeResponse41;
import jss.proto.packet.connection.HandshakeV10;
import jss.proto.packet.text.ColumnDefinition41;
import jss.proto.util.Dumper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DumpPacket extends PluginAdapter
{
    private static void dump(Engine context, Packet packetObject)
    {
        try
        {
            byte[] packet = context.packet;
            System.out.println(Dumper.hexDump(packet));
            System.out.println(readPacketPayload(packet, packetObject, (int) context.handshake.capabilityFlags));
        } catch (Exception e)
        {
            log.error("Dump failed: expected type " + packetObject.getClass().getSimpleName(), e);
            throw new Error(e);
        }
    }

    @Override
    public void init(Engine context) throws IOException
    {
        super.init(context);
    }


    private void dumpResult(Engine context)
    {
        try
        {
            byte[] packet = context.packet;
            System.out.print(Dumper.hexDump(packet));
            PacketData data = PacketCodec.readPacket(Unpooled.wrappedBuffer(context.packet)
                                                             .order(ByteOrder.LITTLE_ENDIAN), new PacketData(), 0);

            System.out.println(PacketReader.readResultPacket(data.payload, (int) context.handshake.capabilityFlags));
            System.out.println();
        } catch (Exception e)
        {
            throw new Error(e);
        }
    }

    private void dumpQueryResult(Engine context)
    {
        byte[] packet = context.packet;
        try
        {
            long capabilityFlags = context.handshake.capabilityFlags;

            ByteBuf buf = Unpooled.wrappedBuffer(context.packet).order(ByteOrder.LITTLE_ENDIAN);

            if (Codec.getPacketStatus(buf).unknown())
            {
                ResultsetCodec reader = new ResultsetCodec().read(buf, (int) capabilityFlags);

                for (ColumnDefinition41 col : reader.columns())
                {
                    System.out.println(col);
                }
                while (reader.hasNext())
                {
                    System.out.println(reader.next());
                }
            } else
            {
                System.out.println(Codec.readStatus(buf, new PacketData(), (int) capabilityFlags));
            }
            System.out.println();

        } catch (Exception e)
        {
            System.out.print(Dumper.hexDump(packet));
            throw new Error(e);
        }
    }

    @Override
    public void read_handshake(Engine context) throws IOException
    {
        dump(context, new HandshakeV10());
    }

    @Override
    public void read_auth(Engine context) throws IOException
    {
        dump(context, new HandshakeResponse41());
    }

    @Override
    public void read_auth_result(Engine context) throws IOException
    {
        dumpResult(context);
    }

    @Override
    public void read_query(Engine context) throws IOException
    {
        dumpCommand(context);
    }

    private void dumpCommand(Engine context)
    {
        byte[] packet = context.packet;
        System.out.print(Dumper.hexDump(packet));
        ByteBuf buf = Unpooled.wrappedBuffer(context.packet).order(ByteOrder.LITTLE_ENDIAN);
        PacketData data = PacketCodec.readPacket(buf, new PacketData(), 0);
        try
        {
            System.out.println(PacketReader.readCommandPacket(data.payload, (int) context.handshake.capabilityFlags));
            System.out.println();

        } catch (Exception e)
        {
            throw new Error(e);
        }
    }

    @Override
    public void read_query_result(Engine context) throws IOException
    {
        dumpQueryResult(context);
    }

    @Override
    public void cleanup(Engine context) throws IOException
    {
    }
}
