package com.github.mpjct.jmpjct.mysql.proto.define;

public interface PacketTypes
{

    // Packet types
    public static final byte COM_SLEEP = (byte) 0x00; // deprecated
    public static final byte COM_QUIT = (byte) 0x01;
    public static final byte COM_INIT_DB = (byte) 0x02;
    public static final byte COM_QUERY = (byte) 0x03;
    public static final byte COM_FIELD_LIST = (byte) 0x04;
    public static final byte COM_CREATE_DB = (byte) 0x05;
    public static final byte COM_DROP_DB = (byte) 0x06;
    public static final byte COM_REFRESH = (byte) 0x07;
    public static final byte COM_SHUTDOWN = (byte) 0x08;
    public static final byte COM_STATISTICS = (byte) 0x09;
    public static final byte COM_PROCESS_INFO = (byte) 0x0a; // deprecated
    public static final byte COM_CONNECT = (byte) 0x0b; // deprecated
    public static final byte COM_PROCESS_KILL = (byte) 0x0c;
    public static final byte COM_DEBUG = (byte) 0x0d;
    public static final byte COM_PING = (byte) 0x0e;
    public static final byte COM_TIME = (byte) 0x0f; // deprecated
    public static final byte COM_DELAYED_INSERT = (byte) 0x10; // deprecated
    public static final byte COM_CHANGE_USER = (byte) 0x11;
    public static final byte COM_BINLOG_DUMP = (byte) 0x12;
    public static final byte COM_TABLE_DUMP = (byte) 0x13;
    public static final byte COM_CONNECT_OUT = (byte) 0x14;
    public static final byte COM_REGISTER_SLAVE = (byte) 0x15;
    public static final byte COM_STMT_PREPARE = (byte) 0x16;
    public static final byte COM_STMT_EXECUTE = (byte) 0x17;
    public static final byte COM_STMT_SEND_LONG_DATA = (byte) 0x18;
    public static final byte COM_STMT_CLOSE = (byte) 0x19;
    public static final byte COM_STMT_RESET = (byte) 0x1a;
    public static final byte COM_SET_OPTION = (byte) 0x1b;
    public static final byte COM_STMT_FETCH = (byte) 0x1c;
    public static final byte COM_DAEMON = (byte) 0x1d; // deprecated
    public static final byte COM_BINLOG_DUMP_GTID = (byte) 0x1e;
    public static final byte COM_END = (byte) 0x1f; // Must be last
    public static final byte COM_RESET_CONNECTION = (byte) 0x1f; // Must be last

    public static final byte OK = (byte) 0x00;
    public static final byte ERR = (byte) 0xff;
    public static final byte EOF = (byte) 0xfe;
    public static final byte LOCAL_INFILE = (byte) 0xfb;

}
