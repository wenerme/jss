package jss.proto.packet;

/**
 * If the client wants to LOAD DATA from a LOCAL file into the server it sends:
 * <pre>
 * LOAD DATA LOCAL INFILE '&lt;filename>' INTO TABLE &lt;table>;
 * The LOCAL keyword triggers the server to send a LOCAL INFILE request packet which asks the client to send the file via a Protocol::LOCAL_INFILE_Data response.
 * The client has to set the CLIENT_LOCAL_FILES capability.
 *
 * Protocol::LOCAL_INFILE_Request:
 * Returns
 * LOCAL INFILE data
 *
 * <b>Payload</b>
 * 1              [fb] LOCAL INFILE
 * string[EOF]    filename the client shall send
 * Example
 * 0c 00 00 01 fb 2f 65 74    63 2f 70 61 73 73 77 64    ...../etc/passwd
 * </pre>
 *
 * @see <a href=http://dev.mysql.com/doc/internals/en/com-query-response.html>com-query-response</a>
 */
public class LOCAL_INFILE_Request implements Packet
{

}
