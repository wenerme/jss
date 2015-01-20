package jss.proto.define;

public enum PacketType
{
    /**
     * @see jss.proto.codec.ResultsetCodec
     */
    ResultsetCodec,
    /**
     * @see jss.proto.packet.EOF_Packet
     */
    EOF_Packet,
    /**
     * @see jss.proto.packet.ERR_Packet
     */
    ERR_Packet,
    /**
     * @see jss.proto.packet.OK_Packet
     */
    OK_Packet,
    /**
     * @see jss.proto.packet.PacketData
     */
    PacketData,
    /**
     * @see jss.proto.packet.ProtocolBinary
     */
    ProtocolBinary,
    /**
     * @see jss.proto.packet.ProtocolText
     */
    ProtocolText,
    /**
     * @see jss.proto.packet.binary.BinaryValue
     */
    BinaryValue,
    /**
     * @see jss.proto.packet.binary.DateValue
     */
    DateValue,
    /**
     * @see jss.proto.packet.binary.Field
     */
    Field,
    /**
     * @see jss.proto.packet.binary.FloatValue
     */
    FloatValue,
    /**
     * @see jss.proto.packet.binary.IntegerValue
     */
    IntegerValue,
    /**
     * @see jss.proto.packet.binary.MYSQL_TYPE_BIT
     */
    MYSQL_TYPE_BIT,
    /**
     * @see jss.proto.packet.binary.MYSQL_TYPE_BLOB
     */
    MYSQL_TYPE_BLOB,
    /**
     * @see jss.proto.packet.binary.MYSQL_TYPE_DATE
     */
    MYSQL_TYPE_DATE,
    /**
     * @see jss.proto.packet.binary.MYSQL_TYPE_DATETIME
     */
    MYSQL_TYPE_DATETIME,
    /**
     * @see jss.proto.packet.binary.MYSQL_TYPE_DATETIME2
     */
    MYSQL_TYPE_DATETIME2,
    /**
     * @see jss.proto.packet.binary.MYSQL_TYPE_DECIMAL
     */
    MYSQL_TYPE_DECIMAL,
    /**
     * @see jss.proto.packet.binary.MYSQL_TYPE_DOUBLE
     */
    MYSQL_TYPE_DOUBLE,
    /**
     * @see jss.proto.packet.binary.MYSQL_TYPE_ENUM
     */
    MYSQL_TYPE_ENUM,
    /**
     * @see jss.proto.packet.binary.MYSQL_TYPE_FLOAT
     */
    MYSQL_TYPE_FLOAT,
    /**
     * @see jss.proto.packet.binary.MYSQL_TYPE_GEOMETRY
     */
    MYSQL_TYPE_GEOMETRY,
    /**
     * @see jss.proto.packet.binary.MYSQL_TYPE_INT24
     */
    MYSQL_TYPE_INT24,
    /**
     * @see jss.proto.packet.binary.MYSQL_TYPE_LONG
     */
    MYSQL_TYPE_LONG,
    /**
     * @see jss.proto.packet.binary.MYSQL_TYPE_LONGLONG
     */
    MYSQL_TYPE_LONGLONG,
    /**
     * @see jss.proto.packet.binary.MYSQL_TYPE_LONG_BLOB
     */
    MYSQL_TYPE_LONG_BLOB,
    /**
     * @see jss.proto.packet.binary.MYSQL_TYPE_MEDIUM_BLOB
     */
    MYSQL_TYPE_MEDIUM_BLOB,
    /**
     * @see jss.proto.packet.binary.MYSQL_TYPE_NEWDATE
     */
    MYSQL_TYPE_NEWDATE,
    /**
     * @see jss.proto.packet.binary.MYSQL_TYPE_NEWDECIMAL
     */
    MYSQL_TYPE_NEWDECIMAL,
    /**
     * @see jss.proto.packet.binary.MYSQL_TYPE_NULL
     */
    MYSQL_TYPE_NULL,
    /**
     * @see jss.proto.packet.binary.MYSQL_TYPE_SET
     */
    MYSQL_TYPE_SET,
    /**
     * @see jss.proto.packet.binary.MYSQL_TYPE_SHORT
     */
    MYSQL_TYPE_SHORT,
    /**
     * @see jss.proto.packet.binary.MYSQL_TYPE_STRING
     */
    MYSQL_TYPE_STRING,
    /**
     * @see jss.proto.packet.binary.MYSQL_TYPE_TIME
     */
    MYSQL_TYPE_TIME,
    /**
     * @see jss.proto.packet.binary.MYSQL_TYPE_TIME2
     */
    MYSQL_TYPE_TIME2,
    /**
     * @see jss.proto.packet.binary.MYSQL_TYPE_TIMESTAMP
     */
    MYSQL_TYPE_TIMESTAMP,
    /**
     * @see jss.proto.packet.binary.MYSQL_TYPE_TIMESTAMP2
     */
    MYSQL_TYPE_TIMESTAMP2,
    /**
     * @see jss.proto.packet.binary.MYSQL_TYPE_TINY
     */
    MYSQL_TYPE_TINY,
    /**
     * @see jss.proto.packet.binary.MYSQL_TYPE_TINY_BLOB
     */
    MYSQL_TYPE_TINY_BLOB,
    /**
     * @see jss.proto.packet.binary.MYSQL_TYPE_VARCHAR
     */
    MYSQL_TYPE_VARCHAR,
    /**
     * @see jss.proto.packet.binary.MYSQL_TYPE_VAR_STRING
     */
    MYSQL_TYPE_VAR_STRING,
    /**
     * @see jss.proto.packet.binary.MYSQL_TYPE_YEAR
     */
    MYSQL_TYPE_YEAR,
    /**
     * @see jss.proto.packet.binary.TimeValue
     */
    TimeValue,
    /**
     * @see jss.proto.packet.compress.CompressedPacketHeader
     */
    CompressedPacketHeader,
    /**
     * @see jss.proto.packet.connection.AuthSwitchRequest
     */
    AuthSwitchRequest,
    /**
     * @see jss.proto.packet.connection.AuthSwitchResponse
     */
    AuthSwitchResponse,
    /**
     * @see jss.proto.packet.connection.HandshakeResponse320
     */
    HandshakeResponse320,
    /**
     * @see jss.proto.packet.connection.HandshakeResponse41
     */
    HandshakeResponse41,
    /**
     * @see jss.proto.packet.connection.HandshakeV10
     */
    HandshakeV10,
    /**
     * @see jss.proto.packet.connection.HandshakeV9
     */
    HandshakeV9,
    /**
     * @see jss.proto.packet.connection.OldAuthSwitchRequest
     */
    OldAuthSwitchRequest,
    /**
     * @see jss.proto.packet.connection.SSLRequest
     */
    SSLRequest,
    /**
     * @see jss.proto.packet.replication.COM_BINLOG_DUMP
     */
    COM_BINLOG_DUMP,
    /**
     * @see jss.proto.packet.replication.COM_BINLOG_DUMP_GTID
     */
    COM_BINLOG_DUMP_GTID,
    /**
     * @see jss.proto.packet.replication.COM_CONNECT_OUT
     */
    COM_CONNECT_OUT,
    /**
     * @see jss.proto.packet.replication.COM_REGISTER_SLAVE
     */
    COM_REGISTER_SLAVE,
    /**
     * @see jss.proto.packet.text.COM_CHANGE_USER
     */
    COM_CHANGE_USER,
    /**
     * @see jss.proto.packet.text.COM_CONNECT
     */
    COM_CONNECT,
    /**
     * @see jss.proto.packet.text.COM_CREATE_DB
     */
    COM_CREATE_DB,
    /**
     * @see jss.proto.packet.text.COM_DAEMON
     */
    COM_DAEMON,
    /**
     * @see jss.proto.packet.text.COM_DEBUG
     */
    COM_DEBUG,
    /**
     * @see jss.proto.packet.text.COM_DELAYED_INSERT
     */
    COM_DELAYED_INSERT,
    /**
     * @see jss.proto.packet.text.COM_DROP_DB
     */
    COM_DROP_DB,
    /**
     * @see jss.proto.packet.text.COM_FIELD_LIST
     */
    COM_FIELD_LIST,
    /**
     * @see jss.proto.packet.text.COM_INIT_DB
     */
    COM_INIT_DB,
    /**
     * @see jss.proto.packet.text.COM_PING
     */
    COM_PING,
    /**
     * @see jss.proto.packet.text.COM_PROCESS_INFO
     */
    COM_PROCESS_INFO,
    /**
     * @see jss.proto.packet.text.COM_PROCESS_KILL
     */
    COM_PROCESS_KILL,
    /**
     * @see jss.proto.packet.text.COM_QUERY
     */
    COM_QUERY,
    /**
     * @see jss.proto.packet.text.COM_QUIT
     */
    COM_QUIT,
    /**
     * @see jss.proto.packet.text.COM_REFRESH
     */
    COM_REFRESH,
    /**
     * @see jss.proto.packet.text.COM_RESET_CONNECTION
     */
    COM_RESET_CONNECTION,
    /**
     * @see jss.proto.packet.text.COM_SET_OPTION
     */
    COM_SET_OPTION,
    /**
     * @see jss.proto.packet.text.COM_SHUTDOWN
     */
    COM_SHUTDOWN,
    /**
     * @see jss.proto.packet.text.COM_SLEEP
     */
    COM_SLEEP,
    /**
     * @see jss.proto.packet.text.COM_STATISTICS
     */
    COM_STATISTICS,
    /**
     * @see jss.proto.packet.text.COM_STMT_CLOSE
     */
    COM_STMT_CLOSE,
    /**
     * @see jss.proto.packet.text.COM_STMT_EXECUTE
     */
    COM_STMT_EXECUTE,
    /**
     * @see jss.proto.packet.text.COM_STMT_FETCH
     */
    COM_STMT_FETCH,
    /**
     * @see jss.proto.packet.text.COM_STMT_PREPARE
     */
    COM_STMT_PREPARE,
    /**
     * @see jss.proto.packet.text.COM_STMT_PREPARE_OK
     */
    COM_STMT_PREPARE_OK,
    /**
     * @see jss.proto.packet.text.COM_STMT_RESET
     */
    COM_STMT_RESET,
    /**
     * @see jss.proto.packet.text.COM_STMT_SEND_LONG_DATA
     */
    COM_STMT_SEND_LONG_DATA,
    /**
     * @see jss.proto.packet.text.COM_TABLE_DUMP
     */
    COM_TABLE_DUMP,
    /**
     * @see jss.proto.packet.text.COM_TIME
     */
    COM_TIME,
    /**
     * @see jss.proto.packet.text.ColumnDefinition320
     */
    ColumnDefinition320,
    /**
     * @see jss.proto.packet.text.ColumnDefinition41
     */
    ColumnDefinition41,
    /**
     * @see jss.proto.packet.text.CommandPacket
     */
    CommandPacket,
    /**
     * @see jss.proto.packet.text.LOCAL_INFILE_Data
     */
    LOCAL_INFILE_Data,
    /**
     * @see jss.proto.packet.text.LOCAL_INFILE_Request
     */
    LOCAL_INFILE_Request,
    /**
     * @see jss.proto.packet.text.ResultsetRow
     */
    ResultsetRow,;

    public static PacketType typeOfClass(Class<?> clazz)
    {
        return null;
    }
}
