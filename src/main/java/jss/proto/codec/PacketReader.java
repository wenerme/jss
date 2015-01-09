package jss.proto.codec;

import static jss.proto.codec.PacketCodec.readPacket;

import com.google.common.base.Throwables;
import com.google.common.collect.Maps;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.ByteOrder;
import java.util.Map;
import jss.proto.packet.Packet;
import jss.proto.packet.PacketData;
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
            }
        }
    }


    // ok,err,eof


    @SuppressWarnings("unchecked")
    static <T extends Packet> T readGenericPacket(ByteBuf buf, T packet, int flags)
    {
        Reader<T> reader = getReaderByPacketClass((Class<T>) packet.getClass());
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
