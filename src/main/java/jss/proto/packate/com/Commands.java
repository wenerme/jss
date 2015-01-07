package jss.proto.packate.com;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import java.util.Map;
import jss.proto.packate.Packet;
import jss.proto.packate.ProtocolText;

/**
 * 一些单实例的命令类
 */
public class Commands
{
    public static final COM_SLEEP COM_SLEEP = new COM_SLEEP();
    public static final COM_QUIT COM_QUIT = new COM_QUIT();
    public static final COM_INIT_DB COM_INIT_DB = new COM_INIT_DB();
    public static final COM_QUERY COM_QUERY = new COM_QUERY();
    public static final COM_FIELD_LIST COM_FIELD_LIST = new COM_FIELD_LIST();
    public static final COM_CREATE_DB COM_CREATE_DB = new COM_CREATE_DB();
    public static final COM_DROP_DB COM_DROP_DB = new COM_DROP_DB();
    public static final COM_REFRESH COM_REFRESH = new COM_REFRESH();
    public static final COM_SHUTDOWN COM_SHUTDOWN = new COM_SHUTDOWN();
    public static final COM_STATISTICS COM_STATISTICS = new COM_STATISTICS();
    public static final COM_PROCESS_INFO COM_PROCESS_INFO = new COM_PROCESS_INFO();
    public static final COM_CONNECT COM_CONNECT = new COM_CONNECT();
    public static final COM_PROCESS_KILL COM_PROCESS_KILL = new COM_PROCESS_KILL();
    public static final COM_DEBUG COM_DEBUG = new COM_DEBUG();
    public static final COM_PING COM_PING = new COM_PING();
    public static final COM_TIME COM_TIME = new COM_TIME();
    public static final COM_DELAYED_INSERT COM_DELAYED_INSERT = new COM_DELAYED_INSERT();
    public static final COM_CHANGE_USER COM_CHANGE_USER = new COM_CHANGE_USER();
    public static final COM_BINLOG_DUMP COM_BINLOG_DUMP = new COM_BINLOG_DUMP();
    public static final COM_TABLE_DUMP COM_TABLE_DUMP = new COM_TABLE_DUMP();
    public static final COM_CONNECT_OUT COM_CONNECT_OUT = new COM_CONNECT_OUT();
    public static final COM_REGISTER_SLAVE COM_REGISTER_SLAVE = new COM_REGISTER_SLAVE();
    public static final COM_STMT_PREPARE COM_STMT_PREPARE = new COM_STMT_PREPARE();
    public static final COM_STMT_EXECUTE COM_STMT_EXECUTE = new COM_STMT_EXECUTE();
    public static final COM_STMT_SEND_LONG_DATA COM_STMT_SEND_LONG_DATA = new COM_STMT_SEND_LONG_DATA();
    public static final COM_STMT_CLOSE COM_STMT_CLOSE = new COM_STMT_CLOSE();
    public static final COM_STMT_RESET COM_STMT_RESET = new COM_STMT_RESET();
    public static final COM_SET_OPTION COM_SET_OPTION = new COM_SET_OPTION();
    public static final COM_STMT_FETCH COM_STMT_FETCH = new COM_STMT_FETCH();
    public static final COM_DAEMON COM_DAEMON = new COM_DAEMON();
    public static final COM_BINLOG_DUMP_GTID COM_BINLOG_DUMP_GTID = new COM_BINLOG_DUMP_GTID();
    public static final COM_END COM_END = new COM_END();
    public static final COM_RESET_CONNECTION COM_RESET_CONNECTION = new COM_RESET_CONNECTION();
    private static final Map<Integer, ProtocolText> COMMAND_TO_INSTANCE;

    static
    {
        Map<Integer, ProtocolText> map = Maps.newHashMap();
        map.put(COM_SLEEP.command, COM_SLEEP);
        map.put(COM_QUIT.command, COM_QUIT);
        map.put(COM_INIT_DB.command, COM_INIT_DB);
        map.put(COM_QUERY.command, COM_QUERY);
        map.put(COM_FIELD_LIST.command, COM_FIELD_LIST);
        map.put(COM_CREATE_DB.command, COM_CREATE_DB);
        map.put(COM_DROP_DB.command, COM_DROP_DB);
        map.put(COM_REFRESH.command, COM_REFRESH);
        map.put(COM_SHUTDOWN.command, COM_SHUTDOWN);
        map.put(COM_STATISTICS.command, COM_STATISTICS);
        map.put(COM_PROCESS_INFO.command, COM_PROCESS_INFO);
        map.put(COM_CONNECT.command, COM_CONNECT);
        map.put(COM_PROCESS_KILL.command, COM_PROCESS_KILL);
        map.put(COM_DEBUG.command, COM_DEBUG);
        map.put(COM_PING.command, COM_PING);
        map.put(COM_TIME.command, COM_TIME);
        map.put(COM_DELAYED_INSERT.command, COM_DELAYED_INSERT);
        map.put(COM_CHANGE_USER.command, COM_CHANGE_USER);
        map.put(COM_BINLOG_DUMP.command, COM_BINLOG_DUMP);
        map.put(COM_TABLE_DUMP.command, COM_TABLE_DUMP);
        map.put(COM_CONNECT_OUT.command, COM_CONNECT_OUT);
        map.put(COM_REGISTER_SLAVE.command, COM_REGISTER_SLAVE);
        map.put(COM_STMT_PREPARE.command, COM_STMT_PREPARE);
        map.put(COM_STMT_EXECUTE.command, COM_STMT_EXECUTE);
        map.put(COM_STMT_SEND_LONG_DATA.command, COM_STMT_SEND_LONG_DATA);
        map.put(COM_STMT_CLOSE.command, COM_STMT_CLOSE);
        map.put(COM_STMT_RESET.command, COM_STMT_RESET);
        map.put(COM_SET_OPTION.command, COM_SET_OPTION);
        map.put(COM_STMT_FETCH.command, COM_STMT_FETCH);
        map.put(COM_DAEMON.command, COM_DAEMON);
        map.put(COM_BINLOG_DUMP_GTID.command, COM_BINLOG_DUMP_GTID);
        map.put(COM_END.command, COM_END);
        map.put(COM_RESET_CONNECTION.command, COM_RESET_CONNECTION);
        COMMAND_TO_INSTANCE = ImmutableMap.copyOf(map);
    }

    public static boolean isSingleton(int command)
    {
        return COMMAND_TO_INSTANCE.containsKey(command);
    }

    /**
     * @return return a command, if the command is not singleton, will creat one.
     */
    public static <T extends Packet> T tryCreate(int command)
    {

        return null;
    }
}
