package jss.proto.define;

import jss.util.IsInteger;

public enum Command implements IsInteger
{
    /**
     * internal server command
     * <p/>
     * Payload
     * 1              [00] COM_SLEEP
     * Returns
     * ERR_Packet
     */
    COM_SLEEP(0),
    /**
     * Tells the server that the client wants to close the connection
     * <p/>
     * response: either a connection close or a OK_Packet
     * <p/>
     * Payload
     * 1              [01] COM_QUIT
     * Fields
     * command (1) -- 0x01 COM_QUIT
     * <p/>
     * Example
     * 01 00 00 00 01
     */
    COM_QUIT(1),
    /**
     * change the default schema of the connection
     * <p/>
     * Returns
     * OK_Packet or ERR_Packet
     * <p/>
     * Payload
     * 1              [02] COM_INIT_DB
     * string[EOF]    schema name
     * Fields
     * command (1) -- 0x02 COM_INIT_DB
     * <p/>
     * schema_name (string.EOF) -- name of the schema to change to
     * <p/>
     * Example
     * 05 00 00 00 02 74 65 73    74                         .....test
     */
    COM_INIT_DB(2),
    /**
     * A COM_QUERY is used to send the server a text-based query that is executed immediately.
     * <p/>
     * The server replies to a COM_QUERY packet with a COM_QUERY Response.
     * <p/>
     * The length of the query-string is a taken from the packet length - 1.
     * <p/>
     * Payload
     * 1              [03] COM_QUERY
     * string[EOF]    the query the server shall execute
     * Fields
     * command_id (1) -- 0x03 COM_QUERY
     * <p/>
     * query (string.EOF) -- query_text
     * <p/>
     * Implemented By
     * mysql_query()
     * <p/>
     * Returns
     * COM_QUERY_Response
     * <p/>
     * Example
     * 21 00 00 00 03 73 65 6c    65 63 74 20 40 40 76 65    !....select @@ve
     * 72 73 69 6f 6e 5f 63 6f    6d 6d 65 6e 74 20 6c 69    rsion_comment li
     * 6d 69 74 20 31                                        mit 1
     */
    COM_QUERY(3),
    /**
     * get the column definitions of a table
     * <p/>
     * Payload
     * 1              [04] COM_FIELD_LIST
     * string[NUL]    table
     * string[EOF]    field wildcard
     * Returns
     * COM_FIELD_LIST response
     * <p/>
     * Implemented By
     * mysql_list_fields()
     * <p/>
     * Response
     * The response to a COM_FIELD_LIST can either be a
     * <p/>
     * a ERR_Packet or
     * <p/>
     * one or more Column Definition packets and a closing EOF_Packet
     * <p/>
     * 31 00 00 01 03 64 65 66    04 74 65 73 74 09 66 69    1....def.test.fi
     * 65 6c 64 6c 69 73 74 09    66 69 65 6c 64 6c 69 73    eldlist.fieldlis
     * 74 02 69 64 02 69 64 0c    3f 00 0b 00 00 00 03 00    t.id.id.?.......
     * 00 00 00 00 fb 05 00 00    02 fe 00 00 02 00          ..............
     */
    COM_FIELD_LIST(4),
    /**
     * create a schema
     * <p/>
     * Payload
     * 1              [05] COM_CREATE_DB
     * string[EOF]    schema name
     * Returns
     * OK_Packet or ERR_Packet
     * <p/>
     * Example
     * 05 00 00 00 05 74 65 73    74                         .....test
     */
    COM_CREATE_DB(5),
    /**
     * drop a schema
     * <p/>
     * Payload
     * 1              [06] COM_DROP_DB
     * string[EOF]    schema name
     * Returns
     * OK_Packet or ERR_Packet
     * <p/>
     * Example
     * 05 00 00 00 06 74 65 73    74                         .....test
     */
    COM_DROP_DB(6),
    /**
     * A low-level version of several FLUSH ... and RESET ... statements.
     * <p/>
     * COM_REFRESH:
     * Call REFRESH or FLUSH statements
     * <p/>
     * Payload
     * 1              [07] COM_REFRESH
     * 1              sub_command
     * Fields
     * command (1) -- 0x07 COM_REFRESH
     * <p/>
     * sub_command (1) -- a bitmask of sub-systems to refresh
     * <p/>
     * Returns
     * OK_Packet or ERR_Packet
     */
    COM_REFRESH(7),
    /**
     * COM_SHUTDOWN is used to shut down the MySQL server.
     * <p/>
     * The SHUTDOWN privilege is required for this operation.
     * <p/>
     * COM_SHUTDOWN:
     * shut down the server
     * <p/>
     * Payload
     * 1              [08] COM_SHUTDOWN
     * if more data {
     * 1              shutdown type
     * }
     * Fields
     * command (1) -- 0x08 COM_SHUTDOWN
     * <p/>
     * sub_command (1) -- optional if sub_command is 0x00
     * <p/>
     * Returns
     * EOF_Packet or ERR_Packet
     * <p/>
     * Note
     * Even if several shutdown types are defined, right now only one is in use: SHUTDOWN_WAIT_ALL_BUFFERS
     */
    COM_SHUTDOWN(8),
    /**
     * Get a human readable string of internal statistics.
     * <p/>
     * Returns
     * string.EOF
     * <p/>
     * Payload
     * 1              [09] COM_STATISTICS
     */
    COM_STATISTICS(9),
    /**
     * get a list of active threads
     * <p/>
     * Returns
     * a ProtocolText::Resultset or ERR_Packet
     * <p/>
     * Payload
     * 1              [0a] COM_PROCCESS_INFO
     */
    COM_PROCESS_INFO(10),
    /**
     * an internal command in the server
     * <p/>
     * Payload
     * 1              [0b] COM_CONNECT
     * Returns
     * ERR_Packet
     */
    COM_CONNECT(11),
    /**
     * Same as KILL &lt;id>.
     * <p/>
     * ask the server to terminate a connection
     * <p/>
     * Returns
     * OK_Packet or ERR_Packet
     * <p/>
     * Payload
     * 1              [0c] COM_PROCCESS_KILL
     * 4              connection id
     */
    COM_PROCESS_KILL(12),
    /**
     * COM_DEBUG triggers a dump on internal debug info to stdout of the mysql-server.
     * <p/>
     * The SUPER privilege is required for this operation.
     * <p/>
     * dump debug info to stdout
     * <p/>
     * Returns
     * EOF_Packet or ERR_Packet on error
     * <p/>
     * Payload
     * 1              [0d] COM_DEBUG
     */
    COM_DEBUG(13),
    /**
     * check if the server is alive
     * <p/>
     * Returns
     * OK_Packet
     * <p/>
     * Payload
     * 1              [0e] COM_PING
     */
    COM_PING(14),
    /**
     * an internal command in the server
     * <p/>
     * Payload
     * 1              [0f] COM_TIME
     * Returns
     * ERR_Packet
     */
    COM_TIME(15),
    /**
     * an internal command in the server
     * <p/>
     * Payload
     * 1              [10] COM_DELAYED_INSERT
     * Returns
     * ERR_Packet
     */
    COM_DELAYED_INSERT(16),
    /**
     * COM_CHANGE_USER changes the user of the current connection and reset the connection state.
     * <p/>
     * user variables
     * <p/>
     * temp tables
     * <p/>
     * prepared statemants
     * <p/>
     * ... and others
     * <p/>
     * It is followed by the same states as the initial handshake.
     * <p/>
     * COM_CHANGE_USER:
     * change the user of the current connection
     * <p/>
     * Returns
     * Authentication Method Switch Request Packet or ERR_Packet
     * <p/>
     * Payload
     * 1              [11] COM_CHANGE_USER
     * string[NUL]    user
     * if capabilities & SECURE_CONNECTION {
     * 1              auth-response-len
     * string[$len]   auth-response
     * } else {
     * string[NUL]    auth-response
     * }
     * string[NUL]    schema-name
     * if more data {
     * 2              character-set
     * if capabilities & CLIENT_PLUGIN_AUTH {
     * string[NUL]    auth plugin name
     * }
     * if capabilities & CLIENT_CONNECT_ATTRS) {
     * lenenc-int     length of all key-values
     * lenenc-str     key
     * lenenc-str     value
     * if-more data in 'length of all key-values', more keys and value pairs
     * }
     * }
     * Fields
     * command (1) -- command byte
     * <p/>
     * username (string.NUL) -- user name
     * <p/>
     * auth_plugin_data_len (1) -- length of the auth_plugin_data filed
     * <p/>
     * auth_plugin_data (string.var_len) -- auth data
     * <p/>
     * schema (string.NUL) -- default schema
     * <p/>
     * character_set (2) -- new connection character set (see Protocol::CharacterSet)
     * <p/>
     * auth_plugin_name (string.NUL) -- name of the auth plugin that auth_plugin_data corresponds to
     * <p/>
     * connect_attrs_len (lenenc_int) -- length in bytes of the following block of key-value pairs
     * <p/>
     * Implemented By
     * parse_com_change_user_packet()
     * <p/>
     * character set is the connection character set as defined in Protocol::CharacterSet and is also the encoding of user and schema-name.
     */
    COM_CHANGE_USER(17),
    COM_BINLOG_DUMP(18),
    COM_TABLE_DUMP(19),
    COM_CONNECT_OUT(20),
    COM_REGISTER_SLAVE(21),
    COM_STMT_PREPARE(22),
    COM_STMT_EXECUTE(23),
    COM_STMT_SEND_LONG_DATA(24),
    COM_STMT_CLOSE(25),
    COM_STMT_RESET(26),
    COM_SET_OPTION(27),
    COM_STMT_FETCH(28),
    /**
     * an internal command in the server
     * <p/>
     * Payload
     * 1              [1d] COM_DAEMON
     * Returns
     * ERR_Packet
     */
    COM_DAEMON(29),
    COM_BINLOG_DUMP_GTID(30),
    COM_END(31),
    /**
     * Resets the session state; more lightweight than COM_CHANGE_USER because it does not close and reopen the connection, and does not re-authenticate
     * <p/>
     * Payload
     * 1              [1f] COM_RESET_CONNECTION
     * Returns
     * a ERR_Packet
     * <p/>
     * a OK_Packet
     */
    COM_RESET_CONNECTION(0x1F),;


    private final int value;

    Command(int value) {this.value = value;}


    @Override
    public Integer get()
    {
        return value;
    }
}
