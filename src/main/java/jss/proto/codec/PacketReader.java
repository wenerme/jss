package jss.proto.codec;

import static jss.proto.codec.Codec.*;
import static jss.proto.codec.PacketCodec.hasFlag;
import static jss.proto.codec.PacketCodec.readPacket;

import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.ByteOrder;
import java.util.List;
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
import jss.proto.packet.text.COM_QUERY;
import jss.proto.packet.text.ColumnDefinition41;
import jss.proto.packet.text.CommandPacket;
import jss.proto.packet.text.Commands;
import jss.proto.packet.text.Resultset;
import jss.proto.packet.text.ResultsetRow;
import jss.util.Values;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PacketReader
{
    private static final Map<Class<?>, Reader<?>> CLASS_TO_READER_MAP = Maps.newHashMap();

    static
    {
        for (Method method : PacketCodec.class.getDeclaredMethods())
        {
            if (method.getName().equals("readPacket"))
            {
                CLASS_TO_READER_MAP.put(method.getReturnType(), new MethodReader<Packet>(method));
                log.debug("Add reader for {}", method.getReturnType());
            }
        }
        // command packet
        try
        {
            Method method = PacketReader.class
                    .getDeclaredMethod("readTextPacketForReader", ByteBuf.class, Packet.class, int.class);
            MethodReader<Packet> reader = new MethodReader<>(method);
            for (Field field : PacketTypes.class.getDeclaredFields())
            {
                String name = field.getName();
                if (name.startsWith("COM_"))
                {
                    try
                    {
                        Class<?> type = Class.forName("jss.proto.packet.text." + name);
                        CLASS_TO_READER_MAP.put(type, reader);
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

            default:
                return readResultset(buf, flags);

        }
        return PacketReader.readGenericPacket(buf, packet, flags);
    }

    private static Resultset readResultset(ByteBuf buf, int flags)
    {
        // field count
        // field *
        // eof
        // row *
        // eof | err
        long fieldCount = int_lenenc(buf);
        List<ColumnDefinition41> columns = Lists.newArrayList();
        for (long i = 0; i < fieldCount; i++)
        {
            ColumnDefinition41 column = PacketCodec.readPacket(buf, new ColumnDefinition41(), flags);
            columns.add(column);
        }
        if (!hasFlag(flags, Flags.CLIENT_DEPRECATE_EOF))
        {
            readPacket(buf, new EOF_Packet(), flags);
        }

        fieldCount = int_lenenc(buf);
        for (long i = 0; i < fieldCount; i++)
        {
            readPacket(buf, new ResultsetRow(), flags);
        }

        if (!hasFlag(flags, Flags.CLIENT_DEPRECATE_EOF))
        {
            readPacket(buf, new EOF_Packet(), flags);
        }

        return null;
    }


    static ProtocolText readTextPacketForReader(ByteBuf buf, Packet packet, int flags)
    {
        return readCommandPacket(buf, flags);
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
        return PacketReader.readGenericPacket(buf, packetObject, flags);
    }

    @SuppressWarnings("unchecked")
    public static <T extends Packet> T readGenericPacket(ByteBuf buf, T packet, int flags)
    {
        Reader<T> reader = getReaderByPacketClass((Class<T>) packet.getClass());
        Preconditions.checkNotNull(reader, "No reader for " + packet.getClass());
        return reader.readPacket(buf, packet, flags);
    }

    @SuppressWarnings("unchecked")
    static <T extends Packet> Reader<T> getReaderByPacketClass(Class<T> type)
    {
        return (Reader<T>) CLASS_TO_READER_MAP.get(type);
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

    interface Reader<P extends Packet>
    {
        P readPacket(ByteBuf buf, P packet, int flags);
    }


    private static class MethodReader<P extends Packet> implements Reader<P>
    {
        private final Method method;
        private final Object target;


        private MethodReader(Method method)
        {
            this(method, null);
        }

        private MethodReader(Method method, Object target)
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
    }

}
