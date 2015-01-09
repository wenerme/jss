package jss.proto.packet.binary;

import static com.github.mpjct.jmpjct.mysql.proto.define.MySQLTypes.MYSQL_TYPE_BIT;
import static com.github.mpjct.jmpjct.mysql.proto.define.MySQLTypes.MYSQL_TYPE_BLOB;
import static com.github.mpjct.jmpjct.mysql.proto.define.MySQLTypes.MYSQL_TYPE_DATE;
import static com.github.mpjct.jmpjct.mysql.proto.define.MySQLTypes.MYSQL_TYPE_DATETIME;
import static com.github.mpjct.jmpjct.mysql.proto.define.MySQLTypes.MYSQL_TYPE_DATETIME2;
import static com.github.mpjct.jmpjct.mysql.proto.define.MySQLTypes.MYSQL_TYPE_DECIMAL;
import static com.github.mpjct.jmpjct.mysql.proto.define.MySQLTypes.MYSQL_TYPE_DOUBLE;
import static com.github.mpjct.jmpjct.mysql.proto.define.MySQLTypes.MYSQL_TYPE_ENUM;
import static com.github.mpjct.jmpjct.mysql.proto.define.MySQLTypes.MYSQL_TYPE_FLOAT;
import static com.github.mpjct.jmpjct.mysql.proto.define.MySQLTypes.MYSQL_TYPE_GEOMETRY;
import static com.github.mpjct.jmpjct.mysql.proto.define.MySQLTypes.MYSQL_TYPE_INT24;
import static com.github.mpjct.jmpjct.mysql.proto.define.MySQLTypes.MYSQL_TYPE_LONG;
import static com.github.mpjct.jmpjct.mysql.proto.define.MySQLTypes.MYSQL_TYPE_LONGLONG;
import static com.github.mpjct.jmpjct.mysql.proto.define.MySQLTypes.MYSQL_TYPE_LONG_BLOB;
import static com.github.mpjct.jmpjct.mysql.proto.define.MySQLTypes.MYSQL_TYPE_MEDIUM_BLOB;
import static com.github.mpjct.jmpjct.mysql.proto.define.MySQLTypes.MYSQL_TYPE_NEWDATE;
import static com.github.mpjct.jmpjct.mysql.proto.define.MySQLTypes.MYSQL_TYPE_NEWDECIMAL;
import static com.github.mpjct.jmpjct.mysql.proto.define.MySQLTypes.MYSQL_TYPE_NULL;
import static com.github.mpjct.jmpjct.mysql.proto.define.MySQLTypes.MYSQL_TYPE_SET;
import static com.github.mpjct.jmpjct.mysql.proto.define.MySQLTypes.MYSQL_TYPE_SHORT;
import static com.github.mpjct.jmpjct.mysql.proto.define.MySQLTypes.MYSQL_TYPE_STRING;
import static com.github.mpjct.jmpjct.mysql.proto.define.MySQLTypes.MYSQL_TYPE_TIME;
import static com.github.mpjct.jmpjct.mysql.proto.define.MySQLTypes.MYSQL_TYPE_TIME2;
import static com.github.mpjct.jmpjct.mysql.proto.define.MySQLTypes.MYSQL_TYPE_TIMESTAMP;
import static com.github.mpjct.jmpjct.mysql.proto.define.MySQLTypes.MYSQL_TYPE_TIMESTAMP2;
import static com.github.mpjct.jmpjct.mysql.proto.define.MySQLTypes.MYSQL_TYPE_TINY;
import static com.github.mpjct.jmpjct.mysql.proto.define.MySQLTypes.MYSQL_TYPE_TINY_BLOB;
import static com.github.mpjct.jmpjct.mysql.proto.define.MySQLTypes.MYSQL_TYPE_VARCHAR;
import static com.github.mpjct.jmpjct.mysql.proto.define.MySQLTypes.MYSQL_TYPE_VAR_STRING;
import static com.github.mpjct.jmpjct.mysql.proto.define.MySQLTypes.MYSQL_TYPE_YEAR;

public class Fields
{
    public static Field createField(int type)
    {
        switch (type)
        {
            case MYSQL_TYPE_DECIMAL:
                return new MYSQL_TYPE_DECIMAL();
            case MYSQL_TYPE_TINY:
                return new MYSQL_TYPE_TINY();
            case MYSQL_TYPE_SHORT:
                return new MYSQL_TYPE_SHORT();
            case MYSQL_TYPE_LONG:
                return new MYSQL_TYPE_LONG();
            case MYSQL_TYPE_FLOAT:
                return new MYSQL_TYPE_FLOAT();
            case MYSQL_TYPE_DOUBLE:
                return new MYSQL_TYPE_DOUBLE();
            case MYSQL_TYPE_NULL:
                return new MYSQL_TYPE_NULL();
            case MYSQL_TYPE_TIMESTAMP:
                return new MYSQL_TYPE_TIMESTAMP();
            case MYSQL_TYPE_LONGLONG:
                return new MYSQL_TYPE_LONGLONG();
            case MYSQL_TYPE_INT24:
                return new MYSQL_TYPE_INT24();
            case MYSQL_TYPE_DATE:
                return new MYSQL_TYPE_DATE();
            case MYSQL_TYPE_TIME:
                return new MYSQL_TYPE_TIME();
            case MYSQL_TYPE_DATETIME:
                return new MYSQL_TYPE_DATETIME();
            case MYSQL_TYPE_YEAR:
                return new MYSQL_TYPE_YEAR();
            case MYSQL_TYPE_NEWDATE:
                return new MYSQL_TYPE_NEWDATE();
            case MYSQL_TYPE_VARCHAR:
                return new MYSQL_TYPE_VARCHAR();
            case MYSQL_TYPE_BIT:
                return new MYSQL_TYPE_BIT();
            case MYSQL_TYPE_TIMESTAMP2:
                return new MYSQL_TYPE_TIMESTAMP2();
            case MYSQL_TYPE_DATETIME2:
                return new MYSQL_TYPE_DATETIME2();
            case MYSQL_TYPE_TIME2:
                return new MYSQL_TYPE_TIME2();
            case MYSQL_TYPE_NEWDECIMAL:
                return new MYSQL_TYPE_NEWDECIMAL();
            case MYSQL_TYPE_ENUM:
                return new MYSQL_TYPE_ENUM();
            case MYSQL_TYPE_SET:
                return new MYSQL_TYPE_SET();
            case MYSQL_TYPE_TINY_BLOB:
                return new MYSQL_TYPE_TINY_BLOB();
            case MYSQL_TYPE_MEDIUM_BLOB:
                return new MYSQL_TYPE_MEDIUM_BLOB();
            case MYSQL_TYPE_LONG_BLOB:
                return new MYSQL_TYPE_LONG_BLOB();
            case MYSQL_TYPE_BLOB:
                return new MYSQL_TYPE_BLOB();
            case MYSQL_TYPE_VAR_STRING:
                return new MYSQL_TYPE_VAR_STRING();
            case MYSQL_TYPE_STRING:
                return new MYSQL_TYPE_STRING();
            case MYSQL_TYPE_GEOMETRY:
                return new MYSQL_TYPE_GEOMETRY();
            default:
                return null;
        }
    }

    public enum FieldValueType
    {
        DATE, BINARY, INTEGER, FLOAT
    }
}
