package jss.proto.packet.text;

import com.github.mpjct.jmpjct.mysql.proto.define.Flags;
import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import java.util.HashMap;
import java.util.Map;
import jss.proto.define.Command;
import jss.proto.packet.ProtocolText;
import jss.proto.packet.replication.COM_BINLOG_DUMP;
import jss.proto.packet.replication.COM_BINLOG_DUMP_GTID;
import jss.proto.packet.replication.COM_CONNECT_OUT;
import jss.proto.packet.replication.COM_REGISTER_SLAVE;
import jss.util.Values;
import lombok.extern.slf4j.Slf4j;

/**
 * 一些单实例的命令类
 */
@Slf4j
public class Commands
{
    public static final COM_SLEEP COM_SLEEP = new COM_SLEEP();
    public static final COM_QUIT COM_QUIT = new COM_QUIT();
    public static final COM_STATISTICS COM_STATISTICS = new COM_STATISTICS();
    public static final COM_PROCESS_INFO COM_PROCESS_INFO = new COM_PROCESS_INFO();
    public static final COM_CONNECT COM_CONNECT = new COM_CONNECT();
    public static final COM_DEBUG COM_DEBUG = new COM_DEBUG();
    public static final COM_PING COM_PING = new COM_PING();
    public static final COM_TIME COM_TIME = new COM_TIME();
    public static final COM_DELAYED_INSERT COM_DELAYED_INSERT = new COM_DELAYED_INSERT();
    public static final jss.proto.packet.replication.COM_BINLOG_DUMP COM_BINLOG_DUMP = new COM_BINLOG_DUMP();
    public static final COM_TABLE_DUMP COM_TABLE_DUMP = new COM_TABLE_DUMP();
    public static final jss.proto.packet.replication.COM_CONNECT_OUT COM_CONNECT_OUT = new COM_CONNECT_OUT();
    public static final jss.proto.packet.replication.COM_REGISTER_SLAVE COM_REGISTER_SLAVE = new COM_REGISTER_SLAVE();
    public static final COM_STMT_FETCH COM_STMT_FETCH = new COM_STMT_FETCH();
    public static final COM_DAEMON COM_DAEMON = new COM_DAEMON();
    public static final jss.proto.packet.replication.COM_BINLOG_DUMP_GTID COM_BINLOG_DUMP_GTID = new COM_BINLOG_DUMP_GTID();
    public static final COM_RESET_CONNECTION COM_RESET_CONNECTION = new COM_RESET_CONNECTION();
    private static final Map<Integer, ProtocolText> COMMAND_TO_INSTANCE;
    private static final Map<Byte, Class<? extends ProtocolText>> COMMAND_TO_TYPE;

    static
    {
        {
            Map<Integer, ProtocolText> map = Maps.newHashMap();
            map.put(COM_SLEEP.command, COM_SLEEP);
            map.put(COM_QUIT.command, COM_QUIT);
            map.put(COM_STATISTICS.command, COM_STATISTICS);
            map.put(COM_PROCESS_INFO.command, COM_PROCESS_INFO);
            map.put(COM_CONNECT.command, COM_CONNECT);
            map.put(COM_DEBUG.command, COM_DEBUG);
            map.put(COM_PING.command, COM_PING);
            map.put(COM_TIME.command, COM_TIME);
            map.put(COM_DELAYED_INSERT.command, COM_DELAYED_INSERT);
            map.put(COM_BINLOG_DUMP.command, COM_BINLOG_DUMP);
            map.put(COM_TABLE_DUMP.command, COM_TABLE_DUMP);
            map.put(COM_CONNECT_OUT.command, COM_CONNECT_OUT);
            map.put(COM_REGISTER_SLAVE.command, COM_REGISTER_SLAVE);
            map.put(COM_STMT_FETCH.command, COM_STMT_FETCH);
            map.put(COM_DAEMON.command, COM_DAEMON);
            map.put(COM_BINLOG_DUMP_GTID.command, COM_BINLOG_DUMP_GTID);
            map.put(COM_RESET_CONNECTION.command, COM_RESET_CONNECTION);
            COMMAND_TO_INSTANCE = ImmutableMap.copyOf(map);
        }
        {
            Map<Byte, Class<? extends ProtocolText>> map = new HashMap<>();
            map.put(Flags.COM_SLEEP, COM_SLEEP.class);
            map.put(Flags.COM_QUIT, COM_QUIT.class);
            map.put(Flags.COM_INIT_DB, COM_INIT_DB.class);
            map.put(Flags.COM_QUERY, COM_QUERY.class);
            map.put(Flags.COM_FIELD_LIST, COM_FIELD_LIST.class);
            map.put(Flags.COM_CREATE_DB, COM_CREATE_DB.class);
            map.put(Flags.COM_DROP_DB, COM_DROP_DB.class);
            map.put(Flags.COM_REFRESH, COM_REFRESH.class);
            map.put(Flags.COM_SHUTDOWN, COM_SHUTDOWN.class);
            map.put(Flags.COM_STATISTICS, COM_STATISTICS.class);
            map.put(Flags.COM_PROCESS_INFO, COM_PROCESS_INFO.class);
            map.put(Flags.COM_CONNECT, COM_CONNECT.class);
            map.put(Flags.COM_PROCESS_KILL, COM_PROCESS_KILL.class);
            map.put(Flags.COM_DEBUG, COM_DEBUG.class);
            map.put(Flags.COM_PING, COM_PING.class);
            map.put(Flags.COM_TIME, COM_TIME.class);
            map.put(Flags.COM_DELAYED_INSERT, COM_DELAYED_INSERT.class);
            map.put(Flags.COM_CHANGE_USER, COM_CHANGE_USER.class);
            map.put(Flags.COM_BINLOG_DUMP, COM_BINLOG_DUMP.class);
            map.put(Flags.COM_TABLE_DUMP, COM_TABLE_DUMP.class);
            map.put(Flags.COM_CONNECT_OUT, COM_CONNECT_OUT.class);
            map.put(Flags.COM_REGISTER_SLAVE, COM_REGISTER_SLAVE.class);
            map.put(Flags.COM_STMT_PREPARE, COM_STMT_PREPARE.class);
            map.put(Flags.COM_STMT_EXECUTE, COM_STMT_EXECUTE.class);
            map.put(Flags.COM_STMT_SEND_LONG_DATA, COM_STMT_SEND_LONG_DATA.class);
            map.put(Flags.COM_STMT_CLOSE, COM_STMT_CLOSE.class);
            map.put(Flags.COM_STMT_RESET, COM_STMT_RESET.class);
            map.put(Flags.COM_SET_OPTION, COM_SET_OPTION.class);
            map.put(Flags.COM_STMT_FETCH, COM_STMT_FETCH.class);
            map.put(Flags.COM_DAEMON, COM_DAEMON.class);
            map.put(Flags.COM_BINLOG_DUMP_GTID, COM_BINLOG_DUMP_GTID.class);
            map.put(Flags.COM_RESET_CONNECTION, COM_RESET_CONNECTION.class);
            COMMAND_TO_TYPE = ImmutableMap.copyOf(map);
        }
    }

    public static boolean isSingleton(int command)
    {
        return COMMAND_TO_INSTANCE.containsKey(command);
    }

    @SuppressWarnings("unchecked")
    public static <T extends ProtocolText> T getSingleton(int command)
    {
        return (T) COMMAND_TO_INSTANCE.get(command);
    }

    /**
     * @return return a command, if the command is not singleton, will creat one.
     */
    @SuppressWarnings("unchecked")
    public static <T extends ProtocolText> T tryCreate(int command)
    {
        checkCommand(command);

        ProtocolText instance = COMMAND_TO_INSTANCE.get(command);
        return (T) (instance == null ? create(command) : instance);
    }

    private static boolean checkCommand(int command)
    {
        Command com = Values.fromValue(Command.class, command);
        if (log.isDebugEnabled())
            Preconditions.checkNotNull(com, "没有找到命令 %s", command);
        return com != null;
    }

    public static <T extends ProtocolText> T createAndCache(int command)
    {
        return create(command);
    }

    @SuppressWarnings("unchecked")
    public static <T extends ProtocolText> T create(int command)
    {
        checkCommand(command);
        Class<? extends ProtocolText> type = COMMAND_TO_TYPE.get((byte) command);
        try
        {
            return (T) type.newInstance();
        } catch (Exception e)
        {
            throw Throwables.propagate(e);
        }
    }
}
