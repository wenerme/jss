package com.github.mpjct.jmpjct.plugin.debug;

import com.github.mpjct.jmpjct.Engine;
import com.github.mpjct.jmpjct.plugin.PluginAdapter;
import java.io.IOException;
import jss.proto.codec.PacketReader;
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
        byte[] packet = context.packet;
        System.out.println(Dumper.hexDump(packet));
        HandshakeV10 handshake = PacketReader.readPacketPayload(packet, new HandshakeV10(), 0);
        System.out.println(handshake);
    }

    @Override
    public void send_handshake(Engine context) throws IOException
    {
        super.send_handshake(context);
    }

    @Override
    public void read_auth(Engine context) throws IOException
    {
    }

    @Override
    public void send_auth(Engine context) throws IOException
    {
        super.send_auth(context);
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
