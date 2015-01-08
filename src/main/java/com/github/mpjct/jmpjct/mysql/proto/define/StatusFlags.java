package com.github.mpjct.jmpjct.mysql.proto.define;

/**
 * @see <a href=http://dev.mysql.com/doc/internals/en/status-flags.html>status-flags</a>
 */
public interface StatusFlags
{
    /**
     * connection state information has changed
     */
    public static final int SERVER_SESSION_STATE_CHANGED = 0x4000;
    public static final int SERVER_STATUS_IN_TRANS = 1;
    public static final int SERVER_STATUS_AUTOCOMMIT = 2;
    public static final int SERVER_MORE_RESULTS_EXISTS = 8;
    public static final int SERVER_QUERY_NO_GOOD_INDEX_USED = 16;
    public static final int SERVER_STATUS_NO_GOOD_INDEX_USED = SERVER_QUERY_NO_GOOD_INDEX_USED;
    public static final int SERVER_QUERY_NO_INDEX_USED = 32;
    public static final int SERVER_STATUS_NO_INDEX_USED = SERVER_QUERY_NO_INDEX_USED;
    public static final int SERVER_STATUS_CURSOR_EXISTS = 64;
    public static final int SERVER_STATUS_LAST_ROW_SENT = 128;
    public static final int SERVER_STATUS_DB_DROPPED = 256;
    public static final int SERVER_STATUS_NO_BACKSLASH_ESCAPES = 512;
    public static final int SERVER_STATUS_METADATA_CHANGED = 1024;
    public static final int SERVER_QUERY_WAS_SLOW = 2048;
    public static final int SERVER_PS_OUT_PARAMS = 4096;
    public static final int SERVER_STATUS_IN_TRANS_READONLY = 8192;

    public static final int SERVER_STATUS_CLEAR_SET = (
            SERVER_QUERY_NO_GOOD_INDEX_USED
                    | SERVER_QUERY_NO_INDEX_USED
                    | SERVER_MORE_RESULTS_EXISTS
                    | SERVER_STATUS_METADATA_CHANGED
                    | SERVER_QUERY_WAS_SLOW
                    | SERVER_STATUS_DB_DROPPED
                    | SERVER_STATUS_CURSOR_EXISTS
                    | SERVER_STATUS_LAST_ROW_SENT
    );

}
