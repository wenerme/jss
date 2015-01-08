package jss.proto.packet.text;


import jss.proto.packet.Packet;

public class COM_STMT_PREPARE_OK implements Packet
{
    /*
If the COM_STMT_PREPARE succeeded, it sends a COM_STMT_PREPARE_OK

COM_STMT_PREPARE_OK:
Fields
First packet:

status (1) -- [00] OK

statement_id (4) -- statement-id

num_columns (2) -- number of columns

num_params (2) -- number of params

reserved_1 (1) -- [00] filler

warning_count (2) -- number of warnings

If num_params > 0 more packets will follow:

Parameter Definition Block

num_params * Protocol::ColumnDefinition

EOF_Packet

If num_columns > 0 more packets will follow:

Column Definition Block

num_colums * Protocol::ColumnDefinition

EOF_Packet

Example
for a prepared query like SELECT CONCAT(?, ?) AS col1:

0c 00 00 01 00 01 00 00    00 01 00 02 00 00 00 00|   ................
17 00 00 02 03 64 65 66    00 00 00 01 3f 00 0c 3f    .....def....?..?
00 00 00 00 00 fd 80 00    00 00 00|17 00 00 03 03    ................
64 65 66 00 00 00 01 3f    00 0c 3f 00 00 00 00 00    def....?..?.....
fd 80 00 00 00 00|05 00    00 04 fe 00 00 02 00|1a    ................
00 00 05 03 64 65 66 00    00 00 04 63 6f 6c 31 00    ....def....col1.
0c 3f 00 00 00 00 00 fd    80 00 1f 00 00|05 00 00    .?..............
06 fe 00 00 02 00                                     ......
Example
for a query without parameters and resultset like DO 1 it is:

0c 00 00 01 00 01 00 00    00 00 00 00 00 00 00 00
Implemented By
send_prep_stmt()
     */
    public int status = 0;
    public int statementId = 0;
    public int numColumns = 0;
    public int numParams = 0;
    public int reserved_1 = 0;
    public int warningCount = 0;
}
