package jss.proto.packet.binary;

/**
 * <pre>
 * ProtocolBinary::MYSQL_TYPE_DATE,
 * ProtocolBinary::MYSQL_TYPE_DATETIME,
 * ProtocolBinary::MYSQL_TYPE_TIMESTAMP:
 * type to store a DATE, DATETIME and TIMESTAMP fields in the binary protocol.
 *
 * to save space the packet can be compressed:
 *
 * if year, month, day, hour, minutes, seconds and micro_seconds are all 0, length is 0 and no other field is sent
 *
 * if hour, minutes, seconds and micro_seconds are all 0, length is 4 and no other field is sent
 *
 * if micro_seconds is 0, length is 7 and micro_seconds is not sent
 *
 * otherwise length is 11
 *
 * Fields
 * length (1) -- number of bytes following (valid values: 0, 4, 7, 11)
 *
 * year (2) -- year
 *
 * month (1) -- month
 *
 * day (1) -- day
 *
 * hour (1) -- hour
 *
 * minute (1) -- minutes
 *
 * second (1) -- seconds
 *
 * micro_second (4) -- micro-seconds
 *
 * Example
 * 0b da 07 0a 11 13 1b 1e 01 00 00 00 -- datetime 2010-10-17 19:27:30.000 001
 * 04 da 07 0a 11                      -- date = 2010-10-17
 * 0b da 07 0a 11 13 1b 1e 01 00 00 00 -- timestamp
 * </pre>
 */
public class DateValue extends Field
{
    public int year;
    public int month;
    public int day;
    public int hour;
    public int minute;
    public int second;
    public int microSecond;

    public DateValue(int expectedType)
    {
        super(expectedType);
    }

    /**
     * @return 该包长度
     */
    public int length()
    {
        if (microSecond == 0)
        {
            if (hour == 0 && minute == 0 && second == 0)
            {
                if (year == 0 && month == 0 && day == 0)
                    return 0;
                else
                    return 4;
            } else
                return 7;
        } else
            return 11;
    }
}
