package jss.server;

import java.util.concurrent.atomic.AtomicInteger;

public class JSSServer
{
    private AtomicInteger connectionId = new AtomicInteger();

    static
    {
        JSSInitializer.initialize();
    }

    public JSSServer()
    {
    }

    public static void main(String[] args)
    {

    }

    public void start()
    {
    }

    private ServerConnection createConnection()
    {
        return new ServerConnection();
    }

    public void stop()
    {
    }
}
