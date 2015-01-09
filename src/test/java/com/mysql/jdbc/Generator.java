package com.mysql.jdbc;

import com.github.mpjct.jmpjct.mysql.proto.define.MySQLTypes;
import java.lang.reflect.Field;
import jss.proto.define.Command;
import org.junit.Test;

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

    @Test
    public void generateType()
    {

        String classText = "/**\n" +
                " * @see <a href=http://dev.mysql.com/doc/internals/en/binary-protocol-value.html>binary-protocol-value</a>\n" +
                " */\n" +
                "public class MYSQL_TYPE_DECIMAL extends DataField\n" +
                "{\n" +
                "    public MYSQL_TYPE_DECIMAL()\n" +
                "    {\n" +
                "        super(Flags.MYSQL_TYPE_DECIMAL);\n" +
                "    }\n" +
                "}";
        for (Field field : MySQLTypes.class.getDeclaredFields())
        {
//            System.out.println(classText.replace("MYSQL_TYPE_DECIMAL", field.getName()));
            System.out.printf("case %s: return new %1$s();%n", field.getName());
        }
    }
}
