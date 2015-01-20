package jss.server;

import com.google.common.collect.Maps;
import java.util.Map;
import jss.proto.define.CapabilityFlags;
import jss.proto.define.ConnectionPhase;
import jss.server.net.JSSDefine;
import lombok.Getter;

public class ServerConnection implements AutoCloseable, JSSDefine
{
    private int connectionId = 0;
    private ConnectionPhase connectionPhase;
    private JSSServer server;
    @Getter
    private int capabilityFlags = CapabilityFlags.CLIENT_BASIC_FLAGS;
    private Map<String, String> attributes = Maps.newConcurrentMap();
    private Map<String, String> variables = Maps.newConcurrentMap();
    private int sequenceId;

    public int getConnectionId()
    {
        return connectionId;
    }

    public String getServerVersion()
    {
        return SERVER_VERSION;
    }

    public String attributes(String key)
    {
        return attributes.get(key);
    }

    public ServerConnection attributes(String key, String val)
    {
        attributes.put(key, val);
        return this;
    }

    public String variables(String key)
    {
        return variables.get(key);
    }

    public ServerConnection variables(String key, String val)
    {
        variables.put(key, val);
        return this;
    }

    /**
     * Close this connection
     */
    @Override
    public void close() throws Exception
    {
    }

    public int getSequenceId()
    {
        return sequenceId++;
    }
}
