package jss.proto.packet.text;

import jss.proto.define.Command;

/**
 * @see jss.proto.define.Command#COM_FIELD_LIST
 */
public class COM_FIELD_LIST extends AbstractProtocolText
{
    public String table = "";
    public String fields = "";

    public COM_FIELD_LIST() {super(Command.COM_FIELD_LIST);}

}
