package com.github.mpjct.jmpjct.mysql.proto.define;


/**
 * @see jss.proto.define.Command#COM_REFRESH
 */
public interface RefreshCommands
{
    public static final int REFRESH_GRANT = 1;
    public static final int REFRESH_LOG = 2;
    public static final int REFRESH_TABLES = 4;
    public static final int REFRESH_HOSTS = 8;
    public static final int REFRESH_STATUS = 16;
    public static final int REFRESH_THREADS = 32;
    public static final int REFRESH_SLAVE = 64;
    public static final int REFRESH_MASTER = 128;
    public static final int REFRESH_ERROR_LOG = 256;
    public static final int REFRESH_ENGINE_LOG = 512;
    public static final int REFRESH_BINARY_LOG = 1024;
    public static final int REFRESH_RELAY_LOG = 2048;
    public static final int REFRESH_GENERAL_LOG = 4096;
    public static final int REFRESH_SLOW_LOG = 8192;
    public static final int REFRESH_READ_LOCK = 16384;
    public static final int REFRESH_FAST = 32768;
    public static final int REFRESH_QUERY_CACHE = 65536;
    public static final int REFRESH_QUERY_CACHE_FREE = 0x20000;
    public static final int REFRESH_DES_KEY_FILE = 0x40000;
    public static final int REFRESH_USER_RESOURCES = 0x80000;
    public static final int REFRESH_FOR_EXPORT = 0x100000;
}
