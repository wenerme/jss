package jss.proto.packet.text;

import jss.proto.define.Command;
import jss.proto.packet.ProtocolText;
import jss.util.IsInteger;
import jss.util.Values;

public class AbstractProtocolText implements ProtocolText
{
    public final int expected;
    public int command;

    public AbstractProtocolText(int command) {expected = this.command = command;}

    public AbstractProtocolText(IsInteger command)
    {
        this(command.get());
    }

    public boolean isValid()
    {
        return expected == command;
    }

    @Override
    public int command()
    {
        return command;
    }

    @Override
    public String toString()
    {
        return "ProtocolText{" +
                "expected=" + expected + "->" + Values.fromValue(Command.class, expected) +
                ", command=" + command +
                '}';
    }
}
