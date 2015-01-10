package jss.proto.define;

/**
 * Just a list of important flags that the Proxy code uses.
 */
public interface Flags extends CapabilityFlags, PacketTypes, MySQLTypes
        , StatusFlags, RefreshCommands, CursorTypes
{
    public static final int HOSTNAME_LENGTH = 60;
    public static final int SYSTEM_CHARSET_MBMAXLEN = 3;
    public static final int NAME_CHAR_LEN = 64;
    public static final int USERNAME_CHAR_LENGTH = 16;
    public static final int NAME_LEN = NAME_CHAR_LEN * SYSTEM_CHARSET_MBMAXLEN;
    public static final int USERNAME_LENGTH = USERNAME_CHAR_LENGTH * SYSTEM_CHARSET_MBMAXLEN;

    public static final String MYSQL_AUTODETECT_CHARSET_NAME = "auto";

    public static final int SERVER_VERSION_LENGTH = 60;
    public static final int SQLSTATE_LENGTH = 5;

    public static final int TABLE_COMMENT_INLINE_MAXLEN = 180;
    public static final int TABLE_COMMENT_MAXLEN = 2048;
    public static final int COLUMN_COMMENT_MAXLEN = 1024;
    public static final int INDEX_COMMENT_MAXLEN = 1024;
    public static final int TABLE_PARTITION_COMMENT_MAXLEN = 1024;

    public static final int USER_HOST_BUFF_SIZE = HOSTNAME_LENGTH + USERNAME_LENGTH + 2;

    public static final int MYSQL_ERRMSG_SIZE = 512;
    public static final int NET_READ_TIMEOUT = 30;
    public static final int NET_WRITE_TIMEOUT = 60;
    public static final int NET_WAIT_TIMEOUT = 8 * 60 * 60;

    public static final int ONLY_KILL_QUERY = 1;

    public static final String LOCAL_HOST = "localhost";
    public static final String LOCAL_HOST_NAMEDPIPE = ".";

    public static final int MODE_INIT = 0;  // Connection opened
    public static final int MODE_READ_HANDSHAKE = 1;  // Read the handshake from the server and process it
    public static final int MODE_SEND_HANDSHAKE = 2;  // Forward the handshake from the server
    public static final int MODE_READ_AUTH = 3;  // Read the reply from the client and process it
    public static final int MODE_SEND_AUTH = 4;  // Forward the reply from the client
    public static final int MODE_READ_AUTH_RESULT = 5;  // Read the reply from the server and process it
    public static final int MODE_SEND_AUTH_RESULT = 6;  // Forward the reply from the server
    public static final int MODE_READ_QUERY = 7;  // Read the query from the client and process it
    public static final int MODE_SEND_QUERY = 8;  // Send the query to the server
    public static final int MODE_READ_QUERY_RESULT = 9;  // Read the result set from the server and and process it
    public static final int MODE_SEND_QUERY_RESULT = 10; // Send a result set to the client
    public static final int MODE_CLEANUP = 11; // Connection closed

    public static final int CAN_CLIENT_COMPRESS = 0;


    public static final int MYSQL_OPTION_MULTI_STATEMENTS_ON = 0;
    public static final int MYSQL_OPTION_MULTI_STATEMENTS_OFF = 1;

    public static final int ROW_TYPE_TEXT = 0;
    public static final int ROW_TYPE_BINARY = 1;

    public static final int RS_OK = 0;
    public static final int RS_FULL = 1;
    public static final int RS_COL_DEF = 2;
    public static final int RS_DATA_FILE = 3;

    public static final int SCRAMBLE_LENGTH = 20;
    public static final int SCRAMBLE_LENGTH_323 = 8;
    public static final int SCRAMBLED_PASSWORD_CHAR_LENGTH = SCRAMBLE_LENGTH * 2 + 1;
    public static final int SCRAMBLED_PASSWORD_CHAR_LENGTH_323 = SCRAMBLE_LENGTH_323 * 2;

    public static final int NOT_NULL_FLAG = 1;
    public static final int PRI_KEY_FLAG = 2;
    public static final int UNIQUE_KEY_FLAG = 4;
    public static final int MULTIPLE_KEY_FLAG = 8;
    public static final int BLOB_FLAG = 16;
    public static final int UNSIGNED_FLAG = 32;
    public static final int ZEROFILL_FLAG = 64;
    public static final int BINARY_FLAG = 128;

    public static final int ENUM_FLAG = 256;
    public static final int AUTO_INCREMENT_FLAG = 512;
    public static final int TIMESTAMP_FLAG = 1024;
    public static final int SET_FLAG = 2048;
    public static final int NO_DEFAULT_VALUE_FLAG = 4096;
    public static final int ON_UPDATE_NOW_FLAG = 8192;
    public static final int NUM_FLAG = 32768;
    public static final int PART_KEY_FLAG = 16384;
    public static final int GROUP_FLAG = 32768;
    public static final int UNIQUE_FLAG = 65536;
    public static final int BINCMP_FLAG = 131072;
    public static final int GET_FIXED_FIELDS_FLAG = 1 << 18;
    public static final int FIELD_IN_PART_FUNC_FLAG = 1 << 19;

    public static final int FIELD_IN_ADD_INDEX = 1 << 20;
    public static final int FIELD_IS_RENAMED = 1 << 21;
    public static final int FIELD_FLAGS_STORAGE_MEDIA = 22;
    public static final int FIELD_FLAGS_STORAGE_MEDIA_MASK = 3 << FIELD_FLAGS_STORAGE_MEDIA;
    public static final int FIELD_FLAGS_COLUMN_FORMAT = 24;
    public static final int FIELD_FLAGS_COLUMN_FORMAT_MASK = 3 << FIELD_FLAGS_COLUMN_FORMAT;
    public static final int FIELD_IS_DROPPED = 1 << 26;

    public static final int MAX_TINYINT_WIDTH = 3;
    public static final int MAX_SMALLINT_WIDTH = 5;
    public static final int MAX_MEDIUMINT_WIDTH = 8;
    public static final int MAX_INT_WIDTH = 10;
    public static final int MAX_BIGINT_WIDTH = 20;
    public static final int MAX_CHAR_WIDTH = 255;
    public static final int MAX_BLOB_WIDTH = 16777216;

    public static final int packet_error = 0;

    public static final int MYSQL_SHUTDOWN_KILLABLE_CONNECT = (char) (1 << 0);
    public static final int MYSQL_SHUTDOWN_KILLABLE_TRANS = (char) (1 << 1);
    public static final int MYSQL_SHUTDOWN_KILLABLE_LOCK_TABLE = (char) (1 << 2);
    public static final int MYSQL_SHUTDOWN_KILLABLE_UPDATE = (char) (1 << 3);

    public static final int SHUTDOWN_DEFAULT = 0;
    public static final int SHUTDOWN_WAIT_CONNECTIONS = MYSQL_SHUTDOWN_KILLABLE_CONNECT;
    public static final int SHUTDOWN_WAIT_TRANSACTIONS = MYSQL_SHUTDOWN_KILLABLE_TRANS;
    public static final int SHUTDOWN_WAIT_UPDATES = MYSQL_SHUTDOWN_KILLABLE_UPDATE;
    public static final int SHUTDOWN_WAIT_ALL_BUFFERS = MYSQL_SHUTDOWN_KILLABLE_UPDATE << 1;
    public static final int SHUTDOWN_WAIT_CRITICAL_BUFFERS = (MYSQL_SHUTDOWN_KILLABLE_UPDATE << 1) + 1;
    public static final int KILL_QUERY = 254;
    public static final int KILL_CONNECTION = 255;

    public static final int STRING_RESULT = 0;
    public static final int REAL_RESULT = 1;
    public static final int INT_RESULT = 2;
    public static final int ROW_RESULT = 3;
    public static final int DECIMAL_RESULT = 4;

    public static final int NET_HEADER_SIZE = 4;
    public static final int COMP_HEADER_SIZE = 3;

    public static final int NULL_LENGTH = ~0;
    public static final int MYSQL_STMT_HEADER = 4;
    public static final int MYSQL_LONG_DATA_HEADER = 6;

    public static final int NOT_FIXED_DEC = 31;

    /**
     * 在 ResultsetRow 中表示 NULL 的值
     */
    public static final short NULL_CELL = 0xfb;
}
