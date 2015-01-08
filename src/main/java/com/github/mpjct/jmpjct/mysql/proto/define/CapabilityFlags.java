package com.github.mpjct.jmpjct.mysql.proto.define;

/**
 * @see <a href=http://dev.mysql.com/doc/internals/en/capability-flags.html>capability-flags</a>
 */
public interface CapabilityFlags
{
    public static final int CLIENT_LONG_PASSWORD = 1;
    public static final int CLIENT_FOUND_ROWS = 2;
    public static final int CLIENT_LONG_FLAG = 4;
    public static final int CLIENT_CONNECT_WITH_DB = 8;
    public static final int CLIENT_NO_SCHEMA = 16;
    public static final int CLIENT_COMPRESS = 32;
    public static final int CLIENT_ODBC = 64;
    public static final int CLIENT_LOCAL_FILES = 128;
    public static final int CLIENT_IGNORE_SPACE = 256;
    public static final int CLIENT_PROTOCOL_41 = 512;
    public static final int CLIENT_INTERACTIVE = 1024;
    public static final int CLIENT_SSL = 2048;
    public static final int CLIENT_IGNORE_SIGPIPE = 4096;
    public static final int CLIENT_TRANSACTIONS = 8192;
    public static final int CLIENT_RESERVED = 16384;
    public static final int CLIENT_SECURE_CONNECTION = 32768;
    public static final int CLIENT_MULTI_STATEMENTS = 1 << 16;
    public static final int CLIENT_MULTI_RESULTS = 1 << 17;
    public static final int CLIENT_PS_MULTI_RESULTS = 1 << 18;
    public static final int CLIENT_PLUGIN_AUTH = 1 << 19;
    public static final int CLIENT_CONNECT_ATTRS = 1 << 20;
    public static final int CLIENT_PLUGIN_AUTH_LENENC_CLIENT_DATA = 1 << 21;
    public static final int CLIENT_CAN_HANDLE_EXPIRED_PASSWORDS = 1 << 22;
    public static final int CLIENT_SSL_VERIFY_SERVER_CERT = 1 << 30;
    public static final int CLIENT_REMEMBER_OPTIONS = 1 << 31;

    public static final int CLIENT_ALL_FLAGS = (
            CLIENT_LONG_PASSWORD
                    | CLIENT_FOUND_ROWS
                    | CLIENT_LONG_FLAG
                    | CLIENT_CONNECT_WITH_DB
                    | CLIENT_NO_SCHEMA
                    | CLIENT_COMPRESS
                    | CLIENT_ODBC
                    | CLIENT_LOCAL_FILES
                    | CLIENT_IGNORE_SPACE
                    | CLIENT_PROTOCOL_41
                    | CLIENT_INTERACTIVE
                    | CLIENT_SSL
                    | CLIENT_IGNORE_SIGPIPE
                    | CLIENT_TRANSACTIONS
                    | CLIENT_RESERVED
                    | CLIENT_SECURE_CONNECTION
                    | CLIENT_MULTI_STATEMENTS
                    | CLIENT_MULTI_RESULTS
                    | CLIENT_PS_MULTI_RESULTS
                    | CLIENT_SSL_VERIFY_SERVER_CERT
                    | CLIENT_REMEMBER_OPTIONS
                    | CLIENT_PLUGIN_AUTH
                    | CLIENT_CONNECT_ATTRS
                    | CLIENT_PLUGIN_AUTH_LENENC_CLIENT_DATA
                    | CLIENT_CAN_HANDLE_EXPIRED_PASSWORDS
    );

    public static final int CLIENT_BASIC_FLAGS =
            (((CLIENT_ALL_FLAGS & ~CLIENT_SSL)
                    & ~CLIENT_COMPRESS)
                    & ~CLIENT_SSL_VERIFY_SERVER_CERT);


    public static final int CLIENT_SESSION_TRACK = 0x00800000;
}
