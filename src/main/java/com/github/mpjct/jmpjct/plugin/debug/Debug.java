package com.github.mpjct.jmpjct.plugin.debug;

/*
 * Debug plugin
 * Output packet debugging information
 */

import com.github.mpjct.jmpjct.Engine;
import com.github.mpjct.jmpjct.mysql.proto.Flags;
import com.github.mpjct.jmpjct.mysql.proto.Packet;
import com.github.mpjct.jmpjct.plugin.PluginAdapter;
import java.io.IOException;
import java.util.ArrayList;
import org.apache.log4j.Logger;
import org.slf4j.LoggerFactory;

public class Debug extends PluginAdapter
{
    public org.slf4j.Logger log = LoggerFactory.getLogger("Plugin.Debug");


    public Debug()
    {
    }

    public static final void dump_buffer(ArrayList<byte[]> buffer)
    {
        Logger logger = Logger.getLogger("Plugin.Debug");

        if (!logger.isTraceEnabled())
            return;

        for (byte[] packet : buffer)
        {
            Packet.dump(packet);
        }
    }

    public static final void dump_buffer(Engine context)
    {
        Logger logger = Logger.getLogger("Plugin.Debug");

        if (!logger.isTraceEnabled())
            return;

        for (byte[] packet : context.buffer)
        {
            Packet.dump(packet);
        }
    }

    public static final String dump_capability_flags(long capabilityFlags)
    {
        String out = "";
        if ((capabilityFlags & Flags.CLIENT_LONG_PASSWORD) != 0)
            out += " CLIENT_LONG_PASSWORD";
        if ((capabilityFlags & Flags.CLIENT_FOUND_ROWS) != 0)
            out += " CLIENT_FOUND_ROWS";
        if ((capabilityFlags & Flags.CLIENT_LONG_FLAG) != 0)
            out += " CLIENT_LONG_FLAG";
        if ((capabilityFlags & Flags.CLIENT_CONNECT_WITH_DB) != 0)
            out += " CLIENT_CONNECT_WITH_DB";
        if ((capabilityFlags & Flags.CLIENT_NO_SCHEMA) != 0)
            out += " CLIENT_NO_SCHEMA";
        if ((capabilityFlags & Flags.CLIENT_COMPRESS) != 0)
            out += " CLIENT_COMPRESS";
        if ((capabilityFlags & Flags.CLIENT_ODBC) != 0)
            out += " CLIENT_ODBC";
        if ((capabilityFlags & Flags.CLIENT_LOCAL_FILES) != 0)
            out += " CLIENT_LOCAL_FILES";
        if ((capabilityFlags & Flags.CLIENT_IGNORE_SPACE) != 0)
            out += " CLIENT_IGNORE_SPACE";
        if ((capabilityFlags & Flags.CLIENT_PROTOCOL_41) != 0)
            out += " CLIENT_PROTOCOL_41";
        if ((capabilityFlags & Flags.CLIENT_INTERACTIVE) != 0)
            out += " CLIENT_INTERACTIVE";
        if ((capabilityFlags & Flags.CLIENT_SSL) != 0)
            out += " CLIENT_SSL";
        if ((capabilityFlags & Flags.CLIENT_IGNORE_SIGPIPE) != 0)
            out += " CLIENT_IGNORE_SIGPIPE";
        if ((capabilityFlags & Flags.CLIENT_TRANSACTIONS) != 0)
            out += " CLIENT_TRANSACTIONS";
        if ((capabilityFlags & Flags.CLIENT_RESERVED) != 0)
            out += " CLIENT_RESERVED";
        if ((capabilityFlags & Flags.CLIENT_SECURE_CONNECTION) != 0)
            out += " CLIENT_SECURE_CONNECTION";
        return out;
    }

    public static final String dump_status_flags(long statusFlags)
    {
        String out = "";
        if ((statusFlags & Flags.SERVER_STATUS_IN_TRANS) != 0)
            out += " SERVER_STATUS_IN_TRANS";
        if ((statusFlags & Flags.SERVER_STATUS_AUTOCOMMIT) != 0)
            out += " SERVER_STATUS_AUTOCOMMIT";
        if ((statusFlags & Flags.SERVER_MORE_RESULTS_EXISTS) != 0)
            out += " SERVER_MORE_RESULTS_EXISTS";
        if ((statusFlags & Flags.SERVER_STATUS_NO_GOOD_INDEX_USED) != 0)
            out += " SERVER_STATUS_NO_GOOD_INDEX_USED";
        if ((statusFlags & Flags.SERVER_STATUS_NO_INDEX_USED) != 0)
            out += " SERVER_STATUS_NO_INDEX_USED";
        if ((statusFlags & Flags.SERVER_STATUS_CURSOR_EXISTS) != 0)
            out += " SERVER_STATUS_CURSOR_EXISTS";
        if ((statusFlags & Flags.SERVER_STATUS_LAST_ROW_SENT) != 0)
            out += " SERVER_STATUS_LAST_ROW_SENT";
        if ((statusFlags & Flags.SERVER_STATUS_LAST_ROW_SENT) != 0)
            out += " SERVER_STATUS_LAST_ROW_SENT";
        if ((statusFlags & Flags.SERVER_STATUS_DB_DROPPED) != 0)
            out += " SERVER_STATUS_DB_DROPPED";
        if ((statusFlags & Flags.SERVER_STATUS_NO_BACKSLASH_ESCAPES) != 0)
            out += " SERVER_STATUS_NO_BACKSLASH_ESCAPES";
        if ((statusFlags & Flags.SERVER_STATUS_METADATA_CHANGED) != 0)
            out += " SERVER_STATUS_METADATA_CHANGED";
        if ((statusFlags & Flags.SERVER_QUERY_WAS_SLOW) != 0)
            out += " SERVER_QUERY_WAS_SLOW";
        if ((statusFlags & Flags.SERVER_PS_OUT_PARAMS) != 0)
            out += " SERVER_PS_OUT_PARAMS";
        return out;
    }

    public void read_handshake(Engine context)
    {
        log.debug("<- HandshakePacket");
        log.debug("   Server Version: " + context.handshake.serverVersion);
        log.debug("   Connection Id: " + context.handshake.connectionId);
        log.debug("   Server Capability Flags: {}", Debug.dump_capability_flags(context.handshake.capabilityFlags));
        log.debug("   Status Flags: {}", Debug.dump_status_flags(context.handshake.statusFlags));
        log.debug("   Auth Plugin Name: {}", context.handshake.authPluginName);

    }

    public void read_auth(Engine context)
    {
        log.debug("-> AuthResponsePacket");
        log.debug("   Max Packet Size: " + context.authReply.maxPacketSize);
        log.debug("   User: " + context.authReply.username);
        log.debug("   Schema: " + context.authReply.schema);

        log.debug("   Client Capability Flags: "
                + Debug.dump_capability_flags(context.authReply.capabilityFlags));
    }

    @Override
    public void send_query_result(Engine context) throws IOException
    {

    }

    public void read_query(Engine context)
    {
        switch (Packet.getType(context.buffer.get(context.buffer.size() - 1)))
        {
            case Flags.COM_QUIT:
                log.info("-> COM_QUIT");
                break;

            // Extract out the new default schema
            case Flags.COM_INIT_DB:
                log.info("-> USE " + context.schema);
                break;

            // Query
            case Flags.COM_QUERY:
                log.info("-> " + context.query);
                break;

            default:
                log.debug("Packet is " + Packet.getType(context.buffer.get(context.buffer.size() - 1)) + " type.");
                Debug.dump_buffer(context);
                break;
        }
        context.buffer_result_set();
    }

    public void read_query_result(Engine context)
    {
        if (!context.bufferResultSet)
            return;

        switch (Packet.getType(context.buffer.get(context.buffer.size() - 1)))
        {
            case Flags.OK:
                log.info("<- OK");
                break;

            case Flags.ERR:
                log.info("<- ERR");
                break;

            default:
                log.debug("Result set or Packet is " + Packet
                        .getType(context.buffer.get(context.buffer.size() - 1)) + " type.");
                break;
        }
    }
}
