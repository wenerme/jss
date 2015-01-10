package jss.server;

import com.google.common.reflect.Reflection;
import io.netty.buffer.ByteBufUtil;
import io.netty.util.ResourceLeakDetector;
import io.netty.util.internal.PlatformDependent;
import jss.proto.codec.Codec;
import jss.proto.define.CapabilityFlag;
import jss.proto.define.Command;
import jss.proto.define.StatusFlag;
import jss.util.Values;
import lombok.extern.slf4j.Slf4j;

/**
 * 进行必要的系统初始化
 */
@Slf4j
public class JSSInitializer
{
    static
    {
        Values.cache(CapabilityFlag.class, StatusFlag.class);
        Values.cache(Command.class);
        Values.cache(Codec.Status.class);

        // 预先初始
        Reflection.initialize(PlatformDependent.class, ByteBufUtil.class, ResourceLeakDetector.class);
        PlatformDependent.getSystemClassLoader();
        log.debug("JSS Initialize complete");
    }

    public static void initialize()
    {
    }
}
