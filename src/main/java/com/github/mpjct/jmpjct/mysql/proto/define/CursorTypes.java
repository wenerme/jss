package com.github.mpjct.jmpjct.mysql.proto.define;

public interface CursorTypes
{
    public static final int CURSOR_TYPE_NO_CURSOR = 0;
    public static final int CURSOR_TYPE_READ_ONLY = 1;
    public static final int CURSOR_TYPE_FOR_UPDATE = 2;
    public static final int CURSOR_TYPE_SCROLLABLE = 4;
}
