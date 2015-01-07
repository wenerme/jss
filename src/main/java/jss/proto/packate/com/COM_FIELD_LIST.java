package jss.proto.packate.com;

import jss.proto.define.Command;
import jss.proto.packate.AbstractProtocolText;

/**
 * @see jss.proto.define.Command#COM_FIELD_LIST
 */
public class COM_FIELD_LIST extends AbstractProtocolText
{
    public String table = "";
    public String fields = "";

    public COM_FIELD_LIST() {super(Command.COM_FIELD_LIST);}

}
