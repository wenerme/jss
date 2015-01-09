package jss.proto.define;

import jss.util.IsInteger;

public enum MySQLType implements IsInteger
{
    ;

    private final int value;

    MySQLType(int value) {this.value = value;}

    @Override
    public Integer get()
    {
        return value;
    }
}
