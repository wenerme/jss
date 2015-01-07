package jss.server;

import jss.proto.define.ConnectionPhase;
import lombok.Data;

@Data
public class ServerConnection implements AutoCloseable
{
    private ConnectionPhase connectionPhase;
    private JSSServer server;


    /**
     * Close this connection
     */
    @Override
    public void close() throws Exception
    {
    }
}
