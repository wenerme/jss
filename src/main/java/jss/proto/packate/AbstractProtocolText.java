package jss.proto.packate;

import jss.proto.define.Command;

public class AbstractProtocolText implements ProtocolText
{
    public final int command;

    public AbstractProtocolText(int command) {this.command = command;}

    public AbstractProtocolText(Command command)
    {
        this(command.get());
    }

    @Override
    public int command()
    {
        return command;
    }
}
