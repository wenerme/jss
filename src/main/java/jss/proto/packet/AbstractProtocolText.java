package jss.proto.packet;

import jss.util.IsInteger;

public class AbstractProtocolText implements ProtocolText
{
    public final int command;

    public AbstractProtocolText(int command) {this.command = command;}

    public AbstractProtocolText(IsInteger command)
    {
        this(command.get());
    }

    @Override
    public int command()
    {
        return command;
    }
}
