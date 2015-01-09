package jss.server;

import jss.proto.define.CapabilityFlag;
import jss.proto.define.Command;
import jss.proto.define.StatusFlag;
import jss.util.Values;

/**
 * 进行必要的系统初始化
 */
public class JSSInitializer
{
    static
    {
        Values.cache(CapabilityFlag.class);
        Values.cache(StatusFlag.class);
        Values.cache(Command.class);
    }

    public static void initialize()
    {
    }
}
