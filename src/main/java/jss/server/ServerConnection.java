package jss.server;

import jss.proto.define.CapabilityFlags;
import jss.proto.define.ConnectionPhase;
import jss.server.net.JSSDefine;
import lombok.Data;

@Data
public class ServerConnection implements AutoCloseable, JSSDefine
{
    private ConnectionPhase connectionPhase;
    private JSSServer server;
    private int capabilityFlags = CapabilityFlags.CLIENT_BASIC_FLAGS;

    public String serverVersion()
    {
        return "5.5.5-10.0.15-MariaDB-JSS v0.1";
    }

    /**
     * Close this connection
     */
    @Override
    public void close() throws Exception
    {
    }
}
