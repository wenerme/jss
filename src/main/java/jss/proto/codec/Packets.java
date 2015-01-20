package jss.proto.codec;

import static jss.proto.codec.Codec.int1;
import static jss.proto.codec.Codec.string_eof;
import static jss.proto.codec.PacketCodec.readPacket;

import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import com.google.common.collect.Maps;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.ByteOrder;
import java.util.Map;
import jss.proto.define.Command;
import jss.proto.define.Flags;
import jss.proto.define.PacketTypes;
import jss.proto.packet.EOF_Packet;
import jss.proto.packet.ERR_Packet;
import jss.proto.packet.OK_Packet;
import jss.proto.packet.Packet;
import jss.proto.packet.PacketData;
import jss.proto.packet.ProtocolText;
import jss.proto.packet.connection.AuthSwitchRequest;
import jss.proto.packet.text.COM_QUERY;
import jss.proto.packet.text.CommandPacket;
import jss.proto.packet.text.Commands;
import jss.proto.util.Buffers;
import jss.util.Values;
import lombok.extern.slf4j.Slf4j;

/**
 * 读取包的辅助操作
 */
@Slf4j
public class Packets
{
    private static final Map<Class<?>, Reader<?>> CLASS_TO_READER_MAP = Maps.newHashMap();
    private static final Map<Class<?>, Writer<?>> CLASS_TO_WRITER_MAP = Maps.newHashMap();

    static
    {
        for (Method method : PacketCodec.class.getDeclaredMethods())
        {
            if (method.getName().equals("readPacket"))
            {
                CLASS_TO_READER_MAP.put(method.getReturnType(), new MethodWrapper<Packet>(method));
                log.debug("Add reader for {}", method.getReturnType());
            }
            if (method.getName().equals("writePacket"))
            {
                CLASS_TO_WRITER_MAP.put(method.getParameterTypes()[1], new MethodWrapper<Packet>(method));
                log.debug("Add writer for {}", method.getReturnType());
            }
        }
        // command packet
        try
        {
            Method read = Packets.class
                    .getDeclaredMethod("readTextPacketForReader", ByteBuf.class, Packet.class, int.class);
            Method write = Packets.class
                    .getDeclaredMethod("writeTextPacketForWriter", ByteBuf.class, Packet.class, int.class);
            MethodWrapper<Packet> reader = new MethodWrapper<>(read);
            MethodWrapper<Packet> writer = new MethodWrapper<>(write);
            for (Field field : PacketTypes.class.getDeclaredFields())
            {
                String name = field.getName();
                if (name.startsWith("COM_"))
                {
                    try
                    {
                        Class<?> type = Class.forName("jss.proto.packet.text." + name);
                        CLASS_TO_READER_MAP.put(type, reader);
                        CLASS_TO_WRITER_MAP.put(type, writer);
                        log.debug("Add reader for {}", type);
                    } catch (ClassNotFoundException ignored) { }
                }
            }
        } catch (NoSuchMethodException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * @see <a href=http://dev.mysql.com/doc/internals/en/com-query-response.html#packet-COM_QUERY_Response>COM_QUERY_Response</a>
     */
    public static Packet readQueryResponse(ByteBuf buf, int flags)
    {
        Packet packet;
        switch (buf.getByte(buf.readerIndex()))
        {
            case Flags.ERR:
                packet = new ERR_Packet();
                break;
            case Flags.OK:
                packet = new OK_Packet();
                break;
            case Flags.LOCAL_INFILE:
                throw new AssertionError("不支持 LOCAL_INFILE");
            default:
//                return readResultset(buf, flags);
                return null;
        }
        return Packets.readGenericPacket(buf, packet, flags);
    }

    private static ResultsetCodec readResultset(ByteBuf buf, int flags)
    {
        return new ResultsetCodec().read(buf, flags);
    }

    /**
     * 用于通过反射生成 reader
     */
    @SuppressWarnings("unused")
    static ProtocolText readTextPacketForReader(ByteBuf buf, Packet packet, int flags)
    {
        return readCommandPacket(buf, flags);
    }

    @SuppressWarnings("unused")
    static ByteBuf writeTextPacketForWriter(ByteBuf buf, Packet packet, int flags)
    {
        return writeCommandPacket(buf, (ProtocolText) packet, flags);
    }

    public static ProtocolText readCommandPacket(ByteBuf buf, int flags)
    {
        short command = int1(buf);
        CommandPacket packet = Commands.tryCreate(command);
        packet.command = command;
        switch (command)
        {
            case Flags.COM_SLEEP:
            case Flags.COM_QUIT:
            case Flags.COM_STATISTICS:
            case Flags.COM_PROCESS_INFO:
            case Flags.COM_CONNECT:
            case Flags.COM_DEBUG:
            case Flags.COM_PING:
            case Flags.COM_TIME:
            case Flags.COM_DELAYED_INSERT:
            case Flags.COM_BINLOG_DUMP:
            case Flags.COM_TABLE_DUMP:
            case Flags.COM_CONNECT_OUT:
            case Flags.COM_REGISTER_SLAVE:
            case Flags.COM_STMT_FETCH:
            case Flags.COM_DAEMON:
            case Flags.COM_BINLOG_DUMP_GTID:
            case Flags.COM_RESET_CONNECTION:
                break;
            case Flags.COM_QUERY:
                ((COM_QUERY) packet).query = string_eof(buf);
                break;
            default:
                throw new AssertionError("不支持的类型 " + Values.fromValue(Command.class, (int) command));
        }

        return packet;
    }

    public static ByteBuf writeCommandPacket(ByteBuf buf, ProtocolText packet, int flags)
    {
        int command = ((CommandPacket) packet).command;
        switch (command)
        {
            case Flags.COM_SLEEP:
            case Flags.COM_QUIT:
            case Flags.COM_STATISTICS:
            case Flags.COM_PROCESS_INFO:
            case Flags.COM_CONNECT:
            case Flags.COM_DEBUG:
            case Flags.COM_PING:
            case Flags.COM_TIME:
            case Flags.COM_DELAYED_INSERT:
            case Flags.COM_BINLOG_DUMP:
            case Flags.COM_TABLE_DUMP:
            case Flags.COM_CONNECT_OUT:
            case Flags.COM_REGISTER_SLAVE:
            case Flags.COM_STMT_FETCH:
            case Flags.COM_DAEMON:
            case Flags.COM_BINLOG_DUMP_GTID:
            case Flags.COM_RESET_CONNECTION:
                int1(buf, command);
                break;
            case Flags.COM_QUERY:
                string_eof(buf, ((COM_QUERY) packet).query);
                break;
            default:
                throw new AssertionError("不支持的类型 " + Values.fromValue(Command.class, (int) command));
        }

        return buf;
    }

    /**
     * @return OK ERR EOF null
     */
    public static Packet readResultPacket(ByteBuf buf, int flags)
    {
        Packet packetObject;
        byte type = buf.getByte(buf.readerIndex());
        switch (type)
        {
            case Flags.ERR:
                packetObject = new ERR_Packet();
                break;
            case Flags.OK:
                packetObject = new OK_Packet();
                break;
            case Flags.EOF:
                packetObject = new EOF_Packet();
                break;
            default:
                throw new AssertionError("不支持的返回包结果 " + type);
        }
        return Packets.readGenericPacket(buf, packetObject, flags);
    }

    @SuppressWarnings("unchecked")
    public static <T extends Packet> T readGenericPacket(ByteBuf buf, T packet, int flags)
    {
        Reader<T> reader = getReaderByPacketClass((Class<T>) packet.getClass());
        Preconditions.checkNotNull(reader, "No reader for " + packet.getClass());
        return reader.readPacket(buf, packet, flags);
    }

    @SuppressWarnings("unchecked")
    public static <T extends Packet> ByteBuf writeGenericPacket(ByteBuf buf, T packet, int flags)
    {
        Writer<T> reader = getWriterByPacketClass((Class<T>) packet.getClass());
        Preconditions.checkNotNull(reader, "No writer for " + packet.getClass());
        return reader.writePacket(buf, packet, flags);
    }

    @SuppressWarnings("unchecked")
    static <T extends Packet> Reader<T> getReaderByPacketClass(Class<T> type)
    {
        return (Reader<T>) CLASS_TO_READER_MAP.get(type);
    }

    @SuppressWarnings("unchecked")
    static <T extends Packet> Writer<T> getWriterByPacketClass(Class<T> type)
    {
        return (Writer<T>) CLASS_TO_WRITER_MAP.get(type);
    }

    public static <T extends Packet> T readPacketPayload(byte[] buf, T packet, int flags)
    {
        return readPacketPayload(Unpooled.wrappedBuffer(buf).order(ByteOrder.LITTLE_ENDIAN), packet, flags);
    }

    public static <T extends Packet> T readPacketPayload(ByteBuf buf, T packet, int flags)
    {
        PacketData data = readPacket(buf, new PacketData(), flags);
        try
        {
            return readGenericPacket(data.payload, packet, flags);
        } finally
        {
            data.payload.release();
        }
    }

    public static AuthSwitchRequest createPacket(Class<? extends Packet> clazz)
    {
        return null;
    }

    public static PacketData wrapPacket(Packet packet, int sequenceId, int flags)
    {
        PacketData data = new PacketData();
        data.sequenceId = sequenceId;
        data.payload = writeGenericPacket(Buffers.buffer(), packet, flags);
        return data;
    }


    interface Reader<P extends Packet>
    {
        P readPacket(ByteBuf buf, P packet, int flags);
    }

    interface Writer<T extends Packet>
    {
        ByteBuf writePacket(ByteBuf buf, T packet, int flags);
    }

    private static class MethodWrapper<P extends Packet> implements Reader<P>, Writer<P>
    {
        private final Method method;
        private final Object target;


        private MethodWrapper(Method method)
        {
            this(method, null);
        }

        private MethodWrapper(Method method, Object target)
        {
            this.method = method;
            this.target = target;
        }

        @SuppressWarnings("unchecked")
        @Override
        public P readPacket(ByteBuf buf, P packet, int flags)
        {
            try
            {
                return (P) method.invoke(target, buf, packet, flags);
            } catch (InvocationTargetException e)
            {
                Throwables.propagate(e.getCause());
            } catch (Exception e)
            {
                Throwables.propagate(e);
            }
            return null;
        }

        @Override
        public ByteBuf writePacket(ByteBuf buf, P packet, int flags)
        {
            try
            {
                return (ByteBuf) method.invoke(target, buf, packet, flags);
            } catch (InvocationTargetException e)
            {
                Throwables.propagate(e.getCause());
            } catch (Exception e)
            {
                Throwables.propagate(e);
            }
            return null;
        }
    }

}
