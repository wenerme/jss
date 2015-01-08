package com.github.mpjct.jmpjct.mysql.proto;

import com.github.mpjct.jmpjct.mysql.proto.define.Flags;
import java.util.ArrayList;

public class Com_Sleep extends Packet {
    public static Com_Sleep loadFromPacket(byte[] packet) {
        Com_Sleep obj = new Com_Sleep();
        Proto proto = new Proto(packet, 3);

        obj.sequenceId = proto.get_fixed_int(1);
        proto.get_filler(1);

        return obj;
    }

    public ArrayList<byte[]> getPayload()
    {
        ArrayList<byte[]> payload = new ArrayList<byte[]>();

        payload.add(Proto.build_byte(Flags.COM_SLEEP));

        return payload;
    }
}
