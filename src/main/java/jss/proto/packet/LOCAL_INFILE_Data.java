package jss.proto.packet;

/**
 * If the client has data to send, it sends in one or more non-empty packets AS IS followed by a empty packet.
 * <p/>
 * If the file is empty or there is a error while reading the file only the empty packet is sent.
 * <pre>
 * Protocol::LOCAL_INFILE_Data:
 * <b>Payload</b>
 * string[EOF]             the filedata
 * Fields
 * data (string.EOF) -- the raw file data
 * </pre>
 *
 * @see <a href=http://dev.mysql.com/doc/internals/en/com-query-response.html>com-query-response</a>
 */
public class LOCAL_INFILE_Data implements Packet
{
}
