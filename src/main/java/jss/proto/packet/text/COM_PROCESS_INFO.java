package jss.proto.packet.text;

import jss.proto.define.Command;
import jss.proto.packet.AbstractProtocolText;

/**
 * @see jss.proto.define.Command#COM_PROCESS_INFO
 */
public class COM_PROCESS_INFO extends AbstractProtocolText
{
    public COM_PROCESS_INFO() {super(Command.COM_PROCESS_INFO);}
}