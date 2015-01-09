package jss.proto.packet.binary;

import io.netty.buffer.ByteBuf;

/**
 * ProtocolBinary::MYSQL_TYPE_STRING,
 * ProtocolBinary::MYSQL_TYPE_VARCHAR,
 * ProtocolBinary::MYSQL_TYPE_VAR_STRING,
 * ProtocolBinary::MYSQL_TYPE_ENUM,
 * ProtocolBinary::MYSQL_TYPE_SET,
 * ProtocolBinary::MYSQL_TYPE_LONG_BLOB,
 * ProtocolBinary::MYSQL_TYPE_MEDIUM_BLOB,
 * ProtocolBinary::MYSQL_TYPE_BLOB,
 * ProtocolBinary::MYSQL_TYPE_TINY_BLOB,
 * ProtocolBinary::MYSQL_TYPE_GEOMETRY,
 * ProtocolBinary::MYSQL_TYPE_BIT,
 * ProtocolBinary::MYSQL_TYPE_DECIMAL,
 * ProtocolBinary::MYSQL_TYPE_NEWDECIMAL
 *
 * @see <a href=http://dev.mysql.com/doc/internals/en/binary-protocol-value.html>binary-protocol-value</a>
 */
public class BinaryValue extends Field
{
    public ByteBuf value;

    public BinaryValue(int expectedType)
    {
        super(expectedType);
    }
}
