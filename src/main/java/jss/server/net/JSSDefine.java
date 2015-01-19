package jss.server.net;

import io.netty.util.AttributeKey;
import jss.server.JSSServer;
import jss.server.ServerConnection;

public interface JSSDefine
{
    public static final AttributeKey<ServerConnection> SERVER_CONNECTION_ATTRIBUTE =
            AttributeKey.valueOf("SERVER_CONNECTION_ATTRIBUTE");
    public static final AttributeKey<JSSServer> JSS_SERVER_ATTRIBUTE =
            AttributeKey.valueOf("JSS_SERVER_ATTRIBUTE");
}
