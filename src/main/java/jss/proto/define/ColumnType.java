package jss.proto.define;

import jss.proto.Protocol;

/**
 * <h2>Note</h2>
 * Not all Table Column Types have a representation in the Text or Binary protocol. While a TIMESTAMP field may be a MYSQL_TYPE_TIMESTAMP or MYSQL_TYPE_TIMESTAMP2 within the MySQL Server depending on the version, in the protocol it always is MYSQL_TYPE_TIMESTAMP.
 *
 * @see <a href=http://dev.mysql.com/doc/internals/en/com-query-response.html#packet-Protocol::ColumnType>Protocol::ColumnType</a>
 */
public enum ColumnType implements Protocol
{
}
