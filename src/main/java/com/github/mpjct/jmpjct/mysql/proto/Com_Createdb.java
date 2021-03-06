package com.github.mpjct.jmpjct.mysql.proto;

import java.util.ArrayList;
import jss.proto.define.Flags;

public class Com_Createdb extends Packet {
    public String schema = "";
    
    public static Com_Createdb loadFromPacket(byte[] packet) {
        Com_Createdb obj = new Com_Createdb();
        Proto proto = new Proto(packet, 3);

        obj.sequenceId = proto.get_fixed_int(1);
        proto.get_filler(1);
        obj.schema = proto.get_eop_str();

        return obj;
    }

    public ArrayList<byte[]> getPayload()
    {
        ArrayList<byte[]> payload = new ArrayList<byte[]>();

        payload.add(Proto.build_byte(Flags.COM_CREATE_DB));
        payload.add(Proto.build_fixed_str(this.schema.length(), this.schema));

        return payload;
    }
}
