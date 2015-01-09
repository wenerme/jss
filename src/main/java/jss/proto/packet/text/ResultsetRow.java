package jss.proto.packet.text;

import jss.proto.packet.Packet;

/**
 * <pre>
 * ProtocolText::ResultsetRow:
 * A row with the data for each column.
 *
 * NULL is sent as 0xfb
 *
 * everything else is converted into a string and is sent as Protocol::LengthEncodedString.
 * </pre>
 *
 * @see <a href=http://dev.mysql.com/doc/internals/en/com-query-response.html>com-query-response</a>
 */
public class ResultsetRow implements Packet
{
}
