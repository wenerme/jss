package jss.proto.packet.binary;

import jss.proto.define.Flags;

/**
 * @see <a href=http://dev.mysql.com/doc/internals/en/binary-protocol-value.html>binary-protocol-value</a>
 */
public class MYSQL_TYPE_TIMESTAMP2 extends BinaryValue
{
    public MYSQL_TYPE_TIMESTAMP2()
    {
        super(Flags.MYSQL_TYPE_TIMESTAMP2);
    }
}
