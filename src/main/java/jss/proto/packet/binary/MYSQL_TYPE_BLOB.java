package jss.proto.packet.binary;

import com.github.mpjct.jmpjct.mysql.proto.define.Flags;

/**
 * @see <a href=http://dev.mysql.com/doc/internals/en/binary-protocol-value.html>binary-protocol-value</a>
 */
public class MYSQL_TYPE_BLOB extends BinaryValue
{
    public MYSQL_TYPE_BLOB()
    {
        super(Flags.MYSQL_TYPE_BLOB);
    }
}