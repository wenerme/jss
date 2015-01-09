package jss.proto.packet.text;

import java.util.List;
import jss.proto.packet.Packet;

/**
 * <pre>
 * a packet containing a Protocol::LengthEncodedInteger column_count
 *
 * column_count * Protocol::ColumnDefinition packets
 *
 * if CLIENT_DEPRECATE_EOF isn't set, EOF_Packet
 *
 * one or more ProtocolText::ResultsetRow packets, each containing column_count values
 *
 * if CLIENT_DEPRECATE_EOF isn't set, EOF_Packet otherwise OK_Packet or ERR_Packet in case of error.
 *
 * If the SERVER_MORE_RESULTS_EXISTS flag is set in the last EOF_Packet another ProtocolText::Resultset will follow (see Multi-resultset).
 *
 *
 * <b>Example</b>
 * see ProtocolText::Resultset
 * </pre>
 *
 * @see <a href=http://dev.mysql.com/doc/internals/en/com-query-response.html#packet-ProtocolText::Resultset>Resultset</a>
 */
public class Resultset implements Packet
{
    public List<ColumnDefinition> columns;
    public List<ResultsetRow> rows;

}
