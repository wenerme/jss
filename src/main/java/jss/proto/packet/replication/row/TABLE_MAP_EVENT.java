package jss.proto.packet.replication.row;

import io.netty.buffer.ByteBuf;
import jss.proto.util.Buffers;

/**
 * <pre>
 *     The first event used in Row Based Replication declares how a table that is about to be changed is defined.

 TABLE_MAP_EVENT:
 The TABLE_MAP_EVENT defines the structure if the tables that are about to be changed.

 Payload
 post-header:
 if post_header_len == 6 {
 4              table id
 } else {
 6              table id
 }
 2              flags

 payload:
 1              schema name length
 string         schema name
 1              [00]
 1              table name length
 string         table name
 1              [00]
 lenenc-int     column-count
 string.var_len [length=$column-count] column-def
 lenenc-str     column-meta-def
 n              NULL-bitmask, length: (column-count + 8) / 7
 Fields
 table_id (6) -- numeric table id

 flags (2) -- flags

 schema_name_length (1) -- length of the schema name

 schema_name (string.var_len) -- [len=schema_name_length] schema name

 table_name_length (1) -- length of the schema name

 table_name (string.var_len) -- [len=table_name_length] table name

 column_count (lenenc_int) -- number of columns in the table map

 column_type_def (string.var_len) -- [len=column_count] array of column definitions, one byte per field type

 column_meta_def (lenenc_str) -- array of metainfo per column, length is the overall length of the metainfo-array in bytes, the length of each metainfo field is dependent on the columns field type

 null_bitmap (string.var_len) -- [len=(column_count + 8) / 7]

 column_type_def
 the column definitions. It is sent as length encoded string where the length of the string is the number of columns and each byte of it is the Protocol::ColumnType of the column.

 column_meta_def
 type-specific metadata for each column

 Type

 meta-len

 Protocol::MYSQL_TYPE_STRING 2
 Protocol::MYSQL_TYPE_VAR_STRING 2
 Protocol::MYSQL_TYPE_VARCHAR 2
 Protocol::MYSQL_TYPE_BLOB 1
 Protocol::MYSQL_TYPE_DECIMAL 2
 Protocol::MYSQL_TYPE_NEWDECIMAL 2
 Protocol::MYSQL_TYPE_DOUBLE 1
 Protocol::MYSQL_TYPE_FLOAT 1
 Protocol::MYSQL_TYPE_ENUM 2
 Protocol::MYSQL_TYPE_SET see MYSQL_TYPE_ENUM
 Protocol::MYSQL_TYPE_BIT 0
 Protocol::MYSQL_TYPE_DATE 0
 Protocol::MYSQL_TYPE_DATETIME 0
 Protocol::MYSQL_TYPE_TIMESTAMP 0
 Protocol::MYSQL_TYPE_TIME --
 Protocol::MYSQL_TYPE_TINY 0
 Protocol::MYSQL_TYPE_SHORT 0
 Protocol::MYSQL_TYPE_INT24 0
 Protocol::MYSQL_TYPE_LONG 0
 Protocol::MYSQL_TYPE_LONGLONG 0
 Protocol::MYSQL_TYPE_STRING
 due to Bug37426 layout of the string metadata is a bit tightly packed:

 1              byte0
 1              byte1
 The two bytes encode type and length

 NULL-bitmap
 a bitmask contained a bit set for each column that can be NULL. The column-length is taken from the column-def
 * </pre>
 * @see <a href=http://dev.mysql.com/doc/internals/en/table-map-event.html>TABLE_MAP_EVENT</a>
 */
public class TABLE_MAP_EVENT
{
    public int tableId;
    public int flags;
    public ByteBuf schemaName;
    public int tableNameLength;
    public ByteBuf tableName;
    public ByteBuf columnTypeDef;
    public ByteBuf columnMetaDef;
    int columnCount;

    public int schemaNameLength()
    {
        return Buffers.length(schemaName);
    }
}
