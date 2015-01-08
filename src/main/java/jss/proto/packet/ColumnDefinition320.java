package jss.proto.packet;

import io.netty.buffer.ByteBuf;

/**
 * 
 * Column Definition
 * 
 * <pre>
 * 	Payload
 * 	lenenc-str     table
 * 	lenenc-str     name
 * 	lenenc_int     [03] length of the column_length field
 * 	3              column_length
 * 	lenenc_int     [01] length of type field
 * 	1              type
 * 	  if capabilities & CLIENT_LONG_FLAG {
 * 	lenenc_int     [03] length of flags+decimals fields
 * 	2              flags
 * 	1              decimals
 * 	  } else {
 * 	1              [02] length of flags+decimals fields
 * 	1              flags
 * 	1              decimals
 * 	  }
 * 	  if command was COM_FIELD_LIST {
 * 	lenenc_int     length of default-values
 * 	string[$len]   default values
 * 	  }
 * 	Implemented By
 * 	Protocol::send_result_set_metadata()
 * </pre>
 * 
 * @see <a
 *      href=http://dev.mysql.com/doc/internals/en/com-query-response.html>com
 *      -query-response</a>
 */
public class ColumnDefinition320 implements Packet
{
	public ByteBuf table;
	public ByteBuf name;
	public int columnLengthFieldLength;
	public int columnLength;
	public int typeFieldLength;
	public int type;
	public int flagsDecimalsFieldsLength;
	public int flags;
	public int decimals;
	public int defaultValuesLength;
	public ByteBuf defaultValues;
}
