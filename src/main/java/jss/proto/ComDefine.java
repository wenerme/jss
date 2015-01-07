package jss.proto;

/**
 * include/mysql_com.h
 */
public class ComDefine
{

    /**
     * Length of random string sent by server on handshake; this is also length of
     * obfuscated password, recieved from client
     */
    public static final int SCRAMBLE_LENGTH = 20;

    ;
    public static final int SCRAMBLE_LENGTH_323 = 8;
    /**
     * length of password stored in the db: new passwords are preceeded with '*'
     */
    public static final int SCRAMBLED_PASSWORD_CHAR_LENGTH = (SCRAMBLE_LENGTH * 2 + 1);
    public static final int SCRAMBLED_PASSWORD_CHAR_LENGTH_323 = (SCRAMBLE_LENGTH_323 * 2);

    public static enum ServerCommand
    {
        COM_SLEEP, COM_QUIT, COM_INIT_DB, COM_QUERY, COM_FIELD_LIST,
        COM_CREATE_DB, COM_DROP_DB, COM_REFRESH, COM_SHUTDOWN, COM_STATISTICS,
        COM_PROCESS_INFO, COM_CONNECT, COM_PROCESS_KILL, COM_DEBUG, COM_PING,
        COM_TIME, COM_DELAYED_INSERT, COM_CHANGE_USER, COM_BINLOG_DUMP,
        COM_TABLE_DUMP, COM_CONNECT_OUT, COM_REGISTER_SLAVE,
        COM_STMT_PREPARE, COM_STMT_EXECUTE, COM_STMT_SEND_LONG_DATA, COM_STMT_CLOSE,
        COM_STMT_RESET, COM_SET_OPTION, COM_STMT_FETCH, COM_DAEMON,
        COM_BINLOG_DUMP_GTID,
        /* don't forget to update const char *command_name[] in sql_parse.cc */

        /* Must be last */
        COM_END
    }
}
