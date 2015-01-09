package com.github.mpjct.jmpjct;

/*
 * Base proxy code. This should really just move data back and forth
 * Calling plugins as needed
 */

import com.github.mpjct.jmpjct.mysql.proto.Handshake;
import com.github.mpjct.jmpjct.mysql.proto.HandshakeResponse;
import com.github.mpjct.jmpjct.mysql.proto.define.Flags;
import com.github.mpjct.jmpjct.plugin.PluginAdapter;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import org.apache.log4j.Logger;

public class Engine implements Runnable
{
    public Logger logger = Logger.getLogger("Engine");

    public int port = 0;

    public Socket clientSocket = null;
    public InputStream clientIn = null;
    public OutputStream clientOut = null;

    // Plugins
    public ArrayList<PluginAdapter> plugins = new ArrayList<PluginAdapter>();

    // Packet Buffer. ArrayList so we can grow/shrink dynamically
    public ArrayList<byte[]> buffer = new ArrayList<byte[]>();
    public int offset = 0;

    // Stop the thread?
    public boolean running = true;

    // What sorta of result set should we expect?
    public int expectedResultSet = Flags.RS_OK;

    // Connection info
    public Handshake handshake = null;
    public HandshakeResponse authReply = null;

    public String schema = "";
    public String query = "";
    public long statusFlags = 0;
    public long sequenceId = 0;

    // Buffer or directly pass though the data
    public boolean bufferResultSet = true;
    public boolean packResultSet = true;

    // Modes
    public int mode = Flags.MODE_INIT;

    // Allow plugins to muck with the modes
    public int nextMode = Flags.MODE_INIT;
    public byte[] packet;

    public Engine(int port, Socket clientSocket, ArrayList<PluginAdapter> plugins) throws IOException
    {
        this.port = port;
        this.plugins = plugins;

        this.clientSocket = clientSocket;
        clientSocket.setPerformancePreferences(0, 2, 1);
        clientSocket.setTcpNoDelay(true);
        clientSocket.setTrafficClass(0x10);
        clientSocket.setKeepAlive(true);

        clientIn = new BufferedInputStream(clientSocket.getInputStream(), 16384);
        clientOut = clientSocket.getOutputStream();
    }

    public void run()
    {
        try
        {
            while (running)
            {
                switch (mode)
                {
                    case Flags.MODE_INIT:
                        logger.trace("MODE_INIT");
                        nextMode = Flags.MODE_READ_HANDSHAKE;
                        for (PluginAdapter plugin : plugins)
                            plugin.init(this);
                        break;

                    case Flags.MODE_READ_HANDSHAKE:
                        logger.trace("MODE_READ_HANDSHAKE");
                        nextMode = Flags.MODE_SEND_HANDSHAKE;
                        for (PluginAdapter plugin : plugins)
                            plugin.read_handshake(this);
                        break;

                    case Flags.MODE_SEND_HANDSHAKE:
                        logger.trace("MODE_SEND_HANDSHAKE");
                        nextMode = Flags.MODE_READ_AUTH;
                        for (PluginAdapter plugin : plugins)
                            plugin.send_handshake(this);
                        break;

                    case Flags.MODE_READ_AUTH:
                        logger.trace("MODE_READ_AUTH");
                        nextMode = Flags.MODE_SEND_AUTH;
                        for (PluginAdapter plugin : plugins)
                            plugin.read_auth(this);
                        break;

                    case Flags.MODE_SEND_AUTH:
                        logger.trace("MODE_SEND_AUTH");
                        nextMode = Flags.MODE_READ_AUTH_RESULT;
                        for (PluginAdapter plugin : plugins)
                            plugin.send_auth(this);
                        break;

                    case Flags.MODE_READ_AUTH_RESULT:
                        logger.trace("MODE_READ_AUTH_RESULT");
                        nextMode = Flags.MODE_SEND_AUTH_RESULT;
                        for (PluginAdapter plugin : plugins)
                            plugin.read_auth_result(this);
                        break;

                    case Flags.MODE_SEND_AUTH_RESULT:
                        logger.trace("MODE_SEND_AUTH_RESULT");
                        nextMode = Flags.MODE_READ_QUERY;
                        for (PluginAdapter plugin : plugins)
                            plugin.send_auth_result(this);
                        break;

                    case Flags.MODE_READ_QUERY:
                        logger.trace("MODE_READ_QUERY");
                        nextMode = Flags.MODE_SEND_QUERY;
                        for (PluginAdapter plugin : plugins)
                            plugin.read_query(this);
                        break;

                    case Flags.MODE_SEND_QUERY:
                        logger.trace("MODE_SEND_QUERY");
                        nextMode = Flags.MODE_READ_QUERY_RESULT;
                        for (PluginAdapter plugin : plugins)
                            plugin.send_query(this);
                        break;

                    case Flags.MODE_READ_QUERY_RESULT:
                        logger.trace("MODE_READ_QUERY_RESULT");
                        nextMode = Flags.MODE_SEND_QUERY_RESULT;
                        for (PluginAdapter plugin : plugins)
                            plugin.read_query_result(this);
                        break;

                    case Flags.MODE_SEND_QUERY_RESULT:
                        logger.trace("MODE_SEND_QUERY_RESULT");
                        nextMode = Flags.MODE_READ_QUERY;
                        for (PluginAdapter plugin : plugins)
                            plugin.send_query_result(this);
                        break;

                    case Flags.MODE_CLEANUP:
                        logger.trace("MODE_CLEANUP");
                        nextMode = Flags.MODE_CLEANUP;
                        for (PluginAdapter plugin : plugins)
                            plugin.cleanup(this);
                        halt();
                        break;

                    default:
                        logger.fatal("UNKNOWN MODE " + mode);
                        halt();
                        break;
                }
                mode = nextMode;
            }

            logger.info("Exiting thread.");
            clientSocket.close();
        } catch (Throwable e)
        {
            logger.fatal(e);
            e.printStackTrace();
        } finally
        {
            try
            {
                clientSocket.close();
            } catch (IOException e) {}

            try
            {
                for (PluginAdapter plugin : plugins)
                    plugin.cleanup(this);
            } catch (IOException e) {}
        }
    }

    public void buffer_result_set()
    {
        if (!bufferResultSet)
            bufferResultSet = true;
    }

    public void halt()
    {
        logger.trace("Halting!");
        running = false;
    }

    public void clear_buffer()
    {
        logger.trace("Clearing Buffer.");
        offset = 0;

        // With how ehcache works, if we clear the buffer via .clear(), it also
        // clears the cached value. Create a new ArrayList and count on java
        // cleaning up after ourselves.
        buffer = new ArrayList<byte[]>();
    }
}
