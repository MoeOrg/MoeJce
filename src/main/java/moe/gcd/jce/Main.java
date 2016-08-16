package moe.gcd.jce;

import moe.gcd.jce.stream.JceInputStream;

import java.util.HashMap;

/**
 * Nukkit Project
 * Author: MagicDroidX
 */
public class Main {
    public static void main(String[] args) {
        //Decoding Request Packet
        RequestPacket packet = new RequestPacket();
        packet.readFrom(new JceInputStream(HexUtil.hexStr2Bytes("10022C3C4201B2CD43560353534F6613537663526571436865636B41707049444E65777D0000320800010603726573180001061A4B5151436F6E6669672E4772617955696E436865636B526573701D0000070A1C2C3D000C0B8C980CA80C")));
        StringBuilder builder = new StringBuilder();
        packet.display(builder, 1);
        System.out.println(builder.toString());

        //Decoding sBuffer
        HashMap<String, HashMap<String, byte[]>> cache__tempdata = new HashMap<>();
        HashMap<String, byte[]> hashMap = new HashMap<>();
        hashMap.put("", new byte[0]);
        cache__tempdata.put("", hashMap);
        JceInputStream inputStream = new JceInputStream(packet.sBuffer);
        JceDisplayer displayer = new JceDisplayer(builder = new StringBuilder());
        displayer.display(inputStream.readMap(cache__tempdata, 0, false), null);
        System.out.println(builder.toString());
    }
}
