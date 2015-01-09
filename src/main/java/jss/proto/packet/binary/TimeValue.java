package jss.proto.packet.binary;


/**
 * <pre>
 * ProtocolBinary::MYSQL_TYPE_TIME:
 * type to store a TIME field in the binary protocol.
 *
 * to save space the packet can be compressed:
 *
 * if days, hours, minutes, seconds and micro_seconds are all 0, length is 0 and no other field is sent
 *
 * if micro_seconds is 0, length is 8 and micro_seconds is not sent
 *
 * otherwise length is 12
 *
 * Fields
 * length (1) -- number of bytes following (valid values: 0, 8, 12)
 *
 * is_negative (1) -- (1 if minus, 0 for plus)
 *
 * days (4) -- days
 *
 * hours (1) -- hours
 *
 * minutes (1) -- minutes
 *
 * seconds (1) -- seconds
 *
 * micro_seconds (4) -- micro-seconds
 *
 * Example
 * 0c 01 78 00 00 00 13 1b 1e 01 00 00 00 -- time  -120d 19:27:30.000 001
 * 08 01 78 00 00 00 13 1b 1e             -- time  -120d 19:27:30
 * 01                                     -- time     0d 00:00:00
 * </pre>
 *
 * @see <a href=http://dev.mysql.com/doc/internals/en/binary-protocol-value.html>binary-protocol-value</a>
 */
public class TimeValue extends Field
{
    public boolean isNegative;
    public int days;
    public int minutes;
    public int seconds;
    public int microSeconds;
    public int hours;

    public TimeValue(int expectedType)
    {
        super(expectedType);
    }
}
