package jss.proto.packet.binary;

import com.github.mpjct.jmpjct.mysql.proto.define.Flags;

/**
 * @see <a href=http://dev.mysql.com/doc/internals/en/binary-protocol-value.html>binary-protocol-value</a>
 */
public class MYSQL_TYPE_VAR_STRING extends BinaryValue
{
    public MYSQL_TYPE_VAR_STRING()
    {
        super(Flags.MYSQL_TYPE_VAR_STRING);
    }
}
