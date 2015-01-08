package jss.server.net;

import io.netty.util.AttributeKey;
import jss.server.ServerConnection;

public interface JSSDefine
{
    public static final AttributeKey<ServerConnection> SERVER_CONNECTION_ATTRIBUTE =
            AttributeKey.valueOf("SERVER_CONNECTION_ATTRIBUTE");
}
