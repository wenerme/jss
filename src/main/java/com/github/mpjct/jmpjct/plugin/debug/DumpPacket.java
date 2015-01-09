package com.github.mpjct.jmpjct.plugin.debug;

import static jss.proto.codec.PacketReader.readPacketPayload;

import com.github.mpjct.jmpjct.Engine;
import com.github.mpjct.jmpjct.plugin.PluginAdapter;
import java.io.IOException;
import jss.proto.packet.Packet;
import jss.proto.packet.connection.HandshakeResponse41;
import jss.proto.packet.connection.HandshakeV10;
import jss.proto.util.Dumper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DumpPacket extends PluginAdapter
{
    @Override
    public void init(Engine context) throws IOException
    {
        super.init(context);
    }

    @Override
    public void read_handshake(Engine context) throws IOException
    {
        dump(context, new HandshakeV10());
    }

    private void dump(Engine context, Packet packetObject)
    {
        try
        {
            byte[] packet = context.packet;
            System.out.println(Dumper.hexDump(packet));
            System.out.println(readPacketPayload(packet, packetObject, (int) context.handshake.capabilityFlags));
        } catch (Exception e)
        {
            log.error("Dump failed: expected type " + packetObject.getClass().getSimpleName(), e);
        }
    }

    @Override
    public void send_handshake(Engine context) throws IOException
    {
    }

    @Override
    public void read_auth(Engine context) throws IOException
    {
        dump(context, new HandshakeResponse41());
    }

    @Override
    public void send_auth(Engine context) throws IOException
    {
    }

    @Override
    public void read_auth_result(Engine context) throws IOException
    {
        super.read_auth_result(context);
    }

    @Override
    public void send_auth_result(Engine context) throws IOException
    {
        super.send_auth_result(context);
    }

    @Override
    public void read_query(Engine context) throws IOException
    {
        super.read_query(context);
    }

    @Override
    public void send_query(Engine context) throws IOException
    {
        super.send_query(context);
    }

    @Override
    public void read_query_result(Engine context) throws IOException
    {
        super.read_query_result(context);
    }

    @Override
    public void send_query_result(Engine context) throws IOException
    {
        super.send_query_result(context);
    }

    @Override
    public void cleanup(Engine context) throws IOException
    {
        super.cleanup(context);
    }
}
