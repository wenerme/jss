package jss.proto;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import java.nio.ByteOrder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import jss.proto.codec.PacketCodec;
import jss.proto.codec.Packets;
import jss.proto.define.CapabilityFlags;
import jss.proto.define.Flags;
import jss.proto.packet.PacketData;
import jss.proto.packet.connection.HandshakeV10;
import jss.proto.util.Buffers;
import jss.proto.util.Dumper;
import jss.server.ServerConnection;
import jss.server.net.JSSDefine;
import jss.server.net.PacketDecoder;
import jss.server.net.PacketEncoder;
import jss.server.net.PacketHandler;
import org.junit.Test;

public class AuthTest extends TestUtil implements JSSDefine
{
    public String handshakeDump =
            "59 00 00 00 0a 35 2e 35    2e 35 2d 31 30 2e 30 2e    Y....5.5.5-10.0.\n" +
                    "31 35 2d 4d 61 72 69 61    44 42 00 5c 01 00 00 65    15-MariaDB.\\...e\n" +
                    "30 5e 67 6b 65 39 2b 00    ff f7 21 02 00 3f a0 15    0^gke9+...!..?..\n" +
                    "00 00 00 00 00 00 00 00    00 00 68 4c 26 75 3e 74    ..........hL&u>t\n" +
                    "4f 6d 56 46 3e 44 00 6d    79 73 71 6c 5f 6e 61 74    OmVF>D.mysql_nat\n" +
                    "69 76 65 5f 70 61 73 73    77 6f 72 64 00             ive_password.";
    public String responseForHandshakeResponse =
            "07 00 00 02 00 00 00 02    00 00 00                   ...........";

    @Test
    public void test() throws InterruptedException
    {
        // Configure the server.
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try
        {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
             .channel(NioServerSocketChannel.class)
             .option(ChannelOption.SO_BACKLOG, 100)
             .handler(new LoggingHandler(LogLevel.INFO))
             .childHandler(new ChannelInitializer<SocketChannel>()
             {
                 @Override
                 public void initChannel(SocketChannel ch) throws Exception
                 {
                     ch.attr(SERVER_CONNECTION_ATTRIBUTE).set(new ServerConnection());

                     ChannelPipeline p = ch.pipeline();
                     p.addLast(new LoggingHandler(LogLevel.INFO));
                     p.addLast(new PacketDecoder());
                     p.addLast(new PacketEncoder());
                     p.addLast(new PacketHandler());
                     p.addLast("exception", new ChannelDuplexHandler()
                     {
                         @Override
                         public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception
                         {
                             cause.printStackTrace();
                         }
                     });
                 }
             });
            // Start the server.
            ChannelFuture f = b.bind(8788).sync();
//            ChannelFuture f = b.bind(8788);

            Channel channel = f.channel();
//            channel.read()


            // Wait until the server socket is closed.
            channel.closeFuture().sync();
        } finally
        {
            // Shut down all event loops to terminate all threads.
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    @Test
    public void client() throws SQLException
    {
        Connection c = DriverManager.getConnection("jdbc:mysql://localhost:8788/test", "root", "");
    }

    @Test
    public void clientLocal() throws SQLException
    {
        Connection c = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "");
    }

    @Test
    public void testHandshakePacket()
    {
        HandshakeV10 handshake = new HandshakeV10();
        handshake.serverVersion = buf(JSSDefine.SERVER_VERSION);
        handshake.capabilityFlags = CapabilityFlags.CLIENT_BASIC_FLAGS;
        handshake.challenge1 = buf("12345678");
        handshake.connectionId = 1;
        handshake.characterSet = 33;
        handshake.challenge2 = buf("1234567890123");
        byte[] reserved = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        handshake.reserved = Unpooled.wrappedBuffer(reserved);
        handshake.authPluginName = buf(Flags.MYSQL_NATIVE_PASSWORD);
        handshake.statusFlags = 10;
        {
            PacketData data = Packets.wrapPacket(handshake, 0, CapabilityFlags.CLIENT_BASIC_FLAGS);
            ByteBuf buf = Packets.writeGenericPacket(Buffers.buffer(), data, CapabilityFlags.CLIENT_BASIC_FLAGS);
            Dumper.hexDumpOut(buf);
        }
        {
            PacketData data = Packets.wrapPacket(handshake, 0, CapabilityFlags.CLIENT_BASIC_FLAGS);
            ByteBuf buf = Packets.writeGenericPacket(Buffers.buffer(), data, CapabilityFlags.CLIENT_BASIC_FLAGS);
            Dumper.hexDumpOut(buf);
        }
    }

    @Test
    public void testStatic() throws InterruptedException
    {
        // Configure the server.
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try
        {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
             .channel(NioServerSocketChannel.class)
             .option(ChannelOption.SO_BACKLOG, 100)
             .handler(new LoggingHandler(LogLevel.INFO))
             .childHandler(new ChannelInitializer<SocketChannel>()
             {
                 @Override
                 public void initChannel(SocketChannel ch) throws Exception
                 {
                     ChannelPipeline p = ch.pipeline();
                     p.addLast(new LoggingHandler(LogLevel.INFO));
                     p.addLast(new PacketDecoder());
                     p.addLast(new PacketEncoder());
                     p.addLast(new PacketHandler());
                 }
             });
            // Start the server.
            ChannelFuture f = b.bind(8788).sync();
//            ChannelFuture f = b.bind(8788);

            Channel channel = f.channel();
//            channel.read()


            // Wait until the server socket is closed.
            channel.closeFuture().sync();
        } finally
        {
            // Shut down all event loops to terminate all threads.
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    @Test
    public void testHandshake()
    {
        ByteBuf buf = fromDumpBytes(handshakeDump);
        PacketData data = PacketCodec.readPacket(buf, new PacketData(), 0);
        HandshakeV10 handshake = PacketCodec.readPacket(data.payload, new HandshakeV10(), 0);
        System.out.println(handshake);
        ByteBuf byteBuf = PacketCodec.writePacket(Buffers.buffer(), handshake, handshake.capabilityFlags);

        data.payload.resetReaderIndex();
        System.out.println(Dumper
                .hexDumpReadable(PacketCodec.writePacket(Buffers.buffer().order(ByteOrder.LITTLE_ENDIAN), data, 0)));
    }
}
