package com.github.mpjct.jmpjct.mysql.proto;

import com.github.mpjct.jmpjct.mysql.proto.define.Flags;
import java.util.ArrayList;

public class OldAuthSwitchRequest extends Packet {
    
    public static OldAuthSwitchRequest loadFromPacket(byte[] packet) {
        OldAuthSwitchRequest obj = new OldAuthSwitchRequest();
        Proto proto = new Proto(packet, 3);

        obj.sequenceId = proto.get_fixed_int(1);
        proto.get_filler(1);

        return obj;
    }

    public ArrayList<byte[]> getPayload()
    {
        ArrayList<byte[]> payload = new ArrayList<byte[]>();

        payload.add(Proto.build_byte(Flags.EOF));

        return payload;
    }
}
