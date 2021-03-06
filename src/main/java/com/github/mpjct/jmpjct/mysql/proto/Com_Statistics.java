package com.github.mpjct.jmpjct.mysql.proto;

import java.util.ArrayList;
import jss.proto.define.Flags;

public class Com_Statistics extends Packet {
    
    public static Com_Statistics loadFromPacket(byte[] packet) {
        Com_Statistics obj = new Com_Statistics();
        Proto proto = new Proto(packet, 3);

        obj.sequenceId = proto.get_fixed_int(1);

        return obj;
    }

    public ArrayList<byte[]> getPayload()
    {
        ArrayList<byte[]> payload = new ArrayList<byte[]>();

        payload.add(Proto.build_byte(Flags.COM_STATISTICS));

        return payload;
    }
}
