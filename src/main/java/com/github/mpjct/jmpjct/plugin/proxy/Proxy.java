package com.github.mpjct.jmpjct.plugin.proxy;

import com.github.mpjct.jmpjct.Engine;
import com.github.mpjct.jmpjct.JMP;
import com.github.mpjct.jmpjct.mysql.proto.Com_Initdb;
import com.github.mpjct.jmpjct.mysql.proto.Com_Query;
import com.github.mpjct.jmpjct.mysql.proto.Handshake;
import com.github.mpjct.jmpjct.mysql.proto.HandshakeResponse;
import com.github.mpjct.jmpjct.mysql.proto.Packet;
import com.github.mpjct.jmpjct.mysql.proto.ResultSet;
import com.github.mpjct.jmpjct.mysql.proto.define.Flags;
import com.github.mpjct.jmpjct.plugin.PluginAdapter;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Proxy extends PluginAdapter
{
    private final static Logger log = LoggerFactory.getLogger("Plugin.Proxy");

    // MySql server stuff
    public String mysqlHost = "";
    public int mysqlPort = 0;
    public Socket mysqlSocket = null;
    public InputStream mysqlIn = null;
    public OutputStream mysqlOut = null;
    private byte[] packet;
    private Engine context;

    public void init(Engine context) throws IOException, UnknownHostException
    {
        log.trace("init");
        this.context = context;

        String[] phs = JMP.config.getProperty("proxyHosts").split(",");
        for (String ph : phs)
        {
            String[] hi = ph.split(":");
            if (context.port == Integer.parseInt(hi[0].trim()))
            {
                this.mysqlHost = hi[1].trim();
                this.mysqlPort = Integer.parseInt(hi[2].trim());
                break;
            }
        }

        // Connect to the mysql server on the other side
        this.mysqlSocket = new Socket(this.mysqlHost, this.mysqlPort);
        this.mysqlSocket.setPerformancePreferences(0, 2, 1);
        this.mysqlSocket.setTcpNoDelay(true);
        this.mysqlSocket.setTrafficClass(0x10);
        this.mysqlSocket.setKeepAlive(true);

        log.info("Connected to mysql server at " + this.mysqlHost + ":" + this.mysqlPort);
        this.mysqlIn = new BufferedInputStream(this.mysqlSocket.getInputStream(), 16384);
        this.mysqlOut = this.mysqlSocket.getOutputStream();
    }

    public void read_handshake(Engine context) throws IOException
    {
        log.trace("read_handshake");
        setPacket(Packet.read_packet(this.mysqlIn));

        context.handshake = Handshake.loadFromPacket(packet);

        // Remove some flags from the reply
        context.handshake.removeCapabilityFlag(Flags.CLIENT_COMPRESS);
        context.handshake.removeCapabilityFlag(Flags.CLIENT_SSL);
        context.handshake.removeCapabilityFlag(Flags.CLIENT_LOCAL_FILES);
        context.handshake.removeCapabilityFlag(Flags.CLIENT_SECURE_CONNECTION);

        // Set the default result set creation to the server's character set
        ResultSet.characterSet = context.handshake.characterSet;

        // Set Replace the packet in the buffer
        context.buffer.add(context.handshake.toPacket());
    }

    public void send_handshake(Engine context) throws IOException
    {
        log.trace("send_handshake");
        Packet.write(context.clientOut, context.buffer);
        context.clear_buffer();
    }

    public void read_auth(Engine context) throws IOException
    {
        log.trace("read_auth");
        setPacket(Packet.read_packet(context.clientIn));
        context.buffer.add(packet);

        context.authReply = HandshakeResponse.loadFromPacket(packet);

        if (!context.authReply.hasCapabilityFlag(Flags.CLIENT_PROTOCOL_41))
        {
            log.error("We do not support Protocols under 4.1");
            context.halt();
            return;
        }

        context.authReply.removeCapabilityFlag(Flags.CLIENT_COMPRESS);
        context.authReply.removeCapabilityFlag(Flags.CLIENT_SSL);
        context.authReply.removeCapabilityFlag(Flags.CLIENT_LOCAL_FILES);

        context.schema = context.authReply.schema;
    }

    public void send_auth(Engine context) throws IOException
    {
        log.trace("send_auth");
        Packet.write(this.mysqlOut, context.buffer);
        context.clear_buffer();
    }

    public void read_auth_result(Engine context) throws IOException
    {
        log.trace("read_auth_result");
        setPacket(Packet.read_packet(this.mysqlIn));
        context.buffer.add(packet);
        if (Packet.getType(packet) != Flags.OK)
        {
            log.error("Auth is not okay!");
        }
    }

    public void send_auth_result(Engine context) throws IOException
    {
        log.trace("read_auth_result");
        Packet.write(context.clientOut, context.buffer);
        context.clear_buffer();
    }

    public void read_query(Engine context) throws IOException
    {
        log.trace("read_query");
        context.bufferResultSet = false;

        setPacket(Packet.read_packet(context.clientIn));
        context.buffer.add(packet);

        context.sequenceId = Packet.getSequenceId(packet);
        log.trace("Client sequenceId: " + context.sequenceId);

        byte type = Packet.getType(packet);
        switch (type)
        {
            case Flags.COM_QUIT:
                log.trace("COM_QUIT");
                context.halt();
                break;

            // Extract out the new default schema
            case Flags.COM_INIT_DB:
                log.trace("COM_INIT_DB");
                context.schema = Com_Initdb.loadFromPacket(packet).schema;
                log.trace("SCHEMA: " + context.schema);
                break;

            // Query
            case Flags.COM_QUERY:
                log.trace("COM_QUERY");
                context.query = Com_Query.loadFromPacket(packet).query;
                break;

            default:
                log.warn("Other Packet Type {}", type);
                break;
        }
    }

    public void send_query(Engine context) throws IOException
    {
        log.trace("send_query");
        Packet.write(this.mysqlOut, context.buffer);
        context.clear_buffer();
    }

    public void read_query_result(Engine context) throws IOException
    {
        log.trace("read_query_result");

        setPacket(Packet.read_packet(this.mysqlIn));
        context.buffer.add(packet);

        context.sequenceId = Packet.getSequenceId(packet);

        switch (Packet.getType(packet))
        {
            case Flags.OK:
            case Flags.ERR:
                break;

            default:
                context.buffer = Packet
                        .read_full_result_set(this.mysqlIn, context.clientOut, context.buffer, context.bufferResultSet);
                break;
        }
    }

    public void send_query_result(Engine context) throws IOException
    {
        log.trace("send_query_result");
        Packet.write(context.clientOut, context.buffer);
        context.clear_buffer();
    }

    public void cleanup(Engine context)
    {
        log.trace("cleanup");
        if (this.mysqlSocket == null)
        {
            return;
        }

        try
        {
            this.mysqlSocket.close();
        } catch (IOException e) {}
    }

    public void setPacket(byte[] packet)
    {
        this.packet = packet;
        context.packet = packet;
    }
}
