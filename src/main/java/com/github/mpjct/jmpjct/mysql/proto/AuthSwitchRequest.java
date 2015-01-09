package com.github.mpjct.jmpjct.mysql.proto;

import java.util.ArrayList;
import jss.proto.define.Flags;

public class AuthSwitchRequest extends Packet {
    public String pluginName = "";
    public String authPluginData = "";
    
    public static AuthSwitchRequest loadFromPacket(byte[] packet) {
        AuthSwitchRequest obj = new AuthSwitchRequest();
        Proto proto = new Proto(packet, 3);

        obj.sequenceId = proto.get_fixed_int(1);
        proto.get_filler(1);
        obj.pluginName = proto.get_null_str();
        obj.authPluginData = proto.get_null_str();

        return obj;
    }

    public ArrayList<byte[]> getPayload()
    {
        ArrayList<byte[]> payload = new ArrayList<byte[]>();

        payload.add(Proto.build_byte(Flags.EOF));
        payload.add(Proto.build_null_str(this.pluginName));
        payload.add(Proto.build_null_str(this.authPluginData));

        return payload;
    }
}
