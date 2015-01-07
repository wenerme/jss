package jss.proto.define;

import jss.util.IsInteger;

/**
 * @see Command#COM_SHUTDOWN
 * @see <a href=http://dev.mysql.com/doc/internals/en/com-shutdown.html>com-shutdown</a>
 */
public enum ShutdownCommand implements IsInteger
{

    /**
     * defaults to SHUTDOWN_WAIT_ALL_BUFFERS
     */
    SHUTDOWN_DEFAULT(0),
    /**
     * wait for existing connections to finish
     */
    SHUTDOWN_WAIT_CONNECTIONS(0x01),
    /**
     * wait for existing trans to finish
     */
    SHUTDOWN_WAIT_TRANSACTIONS(0x02),
    /**
     * wait for existing updates to finish (=> no partial MyISAM update)
     */
    SHUTDOWN_WAIT_UPDATES(0x08),
    /**
     * flush InnoDB buffers and other storage engines' buffers
     */
    SHUTDOWN_WAIT_ALL_BUFFERS(0x10),
    /**
     * don't flush InnoDB buffers, flush other storage engines' buffers
     */
    SHUTDOWN_WAIT_CRITICAL_BUFFERS(0x11),
    KILL_QUERY(0xfe),
    KILL_CONNECTION(0xff),;

    private final int value;

    ShutdownCommand(int value) {this.value = value;}


    @Override
    public Integer get()
    {
        return value;
    }
}
