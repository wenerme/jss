package jss.proto.packet.compress;

import jss.proto.packet.Packet;

/**
 * <pre>
 * The header looks like:
 *
 * 3              length of compressed payload
 * 1              compressed sequence id
 * 3              length of payload before compression
 * <b>length of compressed payload</b>
 * raw packet length minus the size of the compressed packet header (7 bytes) itself.
 *
 * <b>compressed sequence id</b>
 * sequence id of the compressed packets, reset in the same way as the MySQL Packet, but incremented independently
 *
 * <b>length of payload before compression</b>
 * size of payload before it was compressed.
 * </pre>
 */
public class CompressedPacketHeader implements Packet
{
    public int compressedPayloadLength;
    public int compressedSequenceId;
    public int payloadBeforeCompressionLength;
}
