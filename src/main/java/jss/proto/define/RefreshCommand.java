package jss.proto.define;

import jss.util.IsInteger;

/**
 * @see Command#COM_REFRESH
 */
public enum RefreshCommand implements IsInteger
{
    /**
     * Refresh grant tables FLUSH PRIVILEGES
     */
    REFRESH_GRANT(1),
    /**
     * Start on new log file FLUSH LOGS
     */
    REFRESH_LOG(2),
    /**
     * Close all tables FLUSH TABLES
     */
    REFRESH_TABLES(4),
    /**
     * Flush host cache FLUSH HOSTS
     */
    REFRESH_HOSTS(8),
    /**
     * Flush status variables FLUSH STATUS
     */
    REFRESH_STATUS(16),
    /**
     * Flush thread cache
     */
    REFRESH_THREADS(32),
    /**
     * Reset master info and restart slave thread RESET SLAVE
     */
    REFRESH_SLAVE(64),
    /**
     * Remove all binary logs in the index and truncate the index RESET MASTER
     */
    REFRESH_MASTER(128),
    REFRESH_ERROR_LOG(256),
    REFRESH_ENGINE_LOG(512),
    REFRESH_BINARY_LOG(1024),
    REFRESH_RELAY_LOG(2048),
    REFRESH_GENERAL_LOG(4096),
    REFRESH_SLOW_LOG(8192),
    REFRESH_READ_LOCK(16384),
    REFRESH_FAST(32768),
    REFRESH_QUERY_CACHE(65536),
    REFRESH_QUERY_CACHE_FREE(0x20000),
    REFRESH_DES_KEY_FILE(0x40000),
    REFRESH_USER_RESOURCES(0x80000),
    REFRESH_FOR_EXPORT(0x100000),;

    private final int value;

    RefreshCommand(int value) {this.value = value;}

    @Override
    public Integer get()
    {
        return value;
    }
}
