package jss.proto.packet;

import io.netty.buffer.ByteBuf;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * <pre>
 * If a MySQL client or server wants to send data, it:
 *
 * Splits the data into packets of size (224–1) bytes
 *
 * Prepends to each chunk a packet header
 *
 * Protocol::Packet
 * Data between client and server is exchanged in packets of max 16MByte size.
 *
 * Payload
 * Type	Name	Description
 * int&lt;3>	payload_length	Length of the payload. The number of bytes in the packet beyond the initial 4 bytes that make up the packet header.
 * int&lt;1>	sequence_id	Sequence ID
 * string&lt;var>	payload	[len=payload_length] payload of the packet
 * Example
 * A COM_QUIT looks like this:
 *
 * 01 00 00 00 01
 * length: 1
 * sequence_id: x00
 * payload: 0x01
 * </pre>
 *
 * @see <a href=http://dev.mysql.com/doc/internals/en/mysql-packet.html>packet</a>
 */
@ToString
@EqualsAndHashCode
public class PacketData implements Packet
{
    public int payloadLength;
    public int sequenceId = 0;
    public ByteBuf payload;

    public int payloadLength()
    {
        return payload == null ? -1 : payload.readableBytes();
    }
}
