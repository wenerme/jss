package jss.proto.packet.text;

import jss.proto.define.Command;
import jss.proto.packet.AbstractProtocolText;

/**
 * @see jss.proto.define.Command#COM_TIME
 */
public class COM_TIME extends AbstractProtocolText
{
    public COM_TIME() {super(Command.COM_TIME);}
}