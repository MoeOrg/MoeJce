package moe.gcd.jce;

import moe.gcd.jce.stream.JceInputStream;
import moe.gcd.jce.stream.JceOutputStream;

/**
 * Nukkit Project
 * Author: MagicDroidX
 */
public class Main {
    public static void main(String[] args) {
        RequestPacket packet = new RequestPacket();
        packet.readFrom(new JceInputStream(HexUtil.hexStr2Bytes("10022C3C4201B2CD43560353534F6613537663526571436865636B41707049444E65777D0000320800010603726573180001061A4B5151436F6E6669672E4772617955696E436865636B526573701D0000070A1C2C3D000C0B8C980CA80C")));
        StringBuilder builder = new StringBuilder();
        packet.display(builder, 1);
        System.out.println(builder.toString());

        JceOutputStream outputStream = new JceOutputStream();
        packet.writeTo(outputStream);
        System.out.println(HexUtil.bytes2HexStr(outputStream.getByteBuffer().array()));
    }
}
