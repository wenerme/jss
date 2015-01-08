package jss.proto.packet.text;

import jss.proto.define.Command;

public class Generator
{
    public static void main(String[] args)
    {
        String classText = "/**\n" +
                " * @see jss.proto.define.ServerCommand#COM_SLEEP\n" +
                " */\n" +
                "public class COM_SLEEP extends AbstractProtocolText\n" +
                "{\n" +
                "    public COM_SLEEP() {super(ServerCommand.COM_SLEEP);}\n" +
                "}";
        for (Command command : Command.values())
        {
//            System.out.println(classText.replace("COM_SLEEP", command.toString()));
//            System.out.printf("public static final %1$s %s = new %1$s();%n", command);
//            System.out.printf("map.put(%1$s.command, %1$s);%n", command);
            System.out.printf("map.put(Flags.%1$s, %1$s.class);%n", command);
        }
    }
}
