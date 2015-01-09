package jss.proto.packet.text;

import jss.proto.define.Command;
import jss.proto.packet.ProtocolText;
import jss.util.IsInteger;
import jss.util.Values;

public class CommandPacket implements ProtocolText
{
    public final int expected;
    public int command;

    public CommandPacket(int command) {expected = this.command = command;}

    public CommandPacket(IsInteger command)
    {
        this(command.get());
    }

    public boolean isValid()
    {
        return expected == command;
    }

    @Override
    public String toString()
    {
        return "CommandPacket{" +
                "expected=" + expected + "->" + Values.fromValue(Command.class, expected) +
                ", command=" + expected + "->" + Values.fromValue(Command.class, expected) +
                '}';
    }
}
