package com.github.mpjct.jmpjct.mysql.proto;

import java.util.ArrayList;
import jss.proto.define.Flags;

public class Com_Debug extends Packet {
    public static Com_Debug loadFromPacket(byte[] packet) {
        Com_Debug obj = new Com_Debug();
        Proto proto = new Proto(packet, 3);

        obj.sequenceId = proto.get_fixed_int(1);

        return obj;
    }

    public ArrayList<byte[]> getPayload()
    {
        ArrayList<byte[]> payload = new ArrayList<byte[]>();

        payload.add(Proto.build_byte(Flags.COM_DEBUG));

        return payload;
    }
}
