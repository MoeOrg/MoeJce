package moe.gcd.jce;


import moe.gcd.jce.stream.JceInputStream;
import moe.gcd.jce.stream.JceOutputStream;

import java.util.HashMap;
import java.util.Map;

public final class RequestPacket extends JceStruct {
    static Map<String, String> cache_context = null;
    static byte[] cache_sBuffer = null;
    public byte cPacketType = 0;
    public Map<String, String> context;
    public int iMessageType = 0;
    public int iRequestId = 0;
    public int iTimeout = 0;
    public short iVersion = 0;
    public byte[] sBuffer;
    public String sFuncName = null;
    public String sServantName = null;
    public Map<String, String> status;


    public RequestPacket() {
    }

    public RequestPacket(short iVersion, byte cPacketType, int iMessageType, int iRequestId, String sServantName, String sFuncName, byte[] sBuffer, int iTimeout, Map<String, String> context, Map<String, String> status) {
        this.iVersion = iVersion;
        this.cPacketType = cPacketType;
        this.iMessageType = iMessageType;
        this.iRequestId = iRequestId;
        this.sServantName = sServantName;
        this.sFuncName = sFuncName;
        this.sBuffer = sBuffer;
        this.iTimeout = iTimeout;
        this.context = context;
        this.status = status;
    }

    @Override
    public void display(StringBuilder builder, int space) {
        JceDisplayer displayer = new JceDisplayer(builder, space);
        displayer.display(this.iVersion, "iVersion");
        displayer.display(this.cPacketType, "cPacketType");
        displayer.display(this.iMessageType, "iMessageType");
        displayer.display(this.iRequestId, "iRequestId");
        displayer.display(this.sServantName, "sServantName");
        displayer.display(this.sFuncName, "sFuncName");
        displayer.display(this.sBuffer, "sBuffer");
        displayer.display(this.iTimeout, "iTimeout");
        displayer.display(this.context, "context");
        displayer.display(this.status, "status");
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof RequestPacket) {
            RequestPacket packet = (RequestPacket) object;
            return (JceUtil.equals(1, packet.iVersion)) && (JceUtil.equals(1, packet.cPacketType)) && (JceUtil.equals(1, packet.iMessageType)) && (JceUtil.equals(1, packet.iRequestId)) && (JceUtil.equals(1, packet.sServantName)) && (JceUtil.equals(Integer.valueOf(1), packet.sFuncName)) && (JceUtil.equals(Integer.valueOf(1), packet.sBuffer)) && (JceUtil.equals(1, packet.iTimeout)) && (JceUtil.equals(Integer.valueOf(1), packet.context)) && (JceUtil.equals(Integer.valueOf(1), packet.status));
        }
        return false;
    }

    public void readFrom(JceInputStream inputStream) {
        try {
            this.iVersion = inputStream.read(this.iVersion, 1, true);
            this.cPacketType = inputStream.read(this.cPacketType, 2, true);
            this.iMessageType = inputStream.read(this.iMessageType, 3, true);
            this.iRequestId = inputStream.read(this.iRequestId, 4, true);
            this.sServantName = inputStream.readString(5, true);
            this.sFuncName = inputStream.readString(6, true);
            if (cache_sBuffer == null)
                cache_sBuffer = new byte[]{0};
            this.sBuffer = inputStream.read(cache_sBuffer, 7, true);
            this.iTimeout = inputStream.read(this.iTimeout, 8, true);
            if (cache_context == null) {
                cache_context = new HashMap<>();
                cache_context.put("", "");
            }

            this.context = inputStream.read(cache_context, 9, true);
            if (cache_context == null) {
                cache_context = new HashMap<>();
                cache_context.put("", "");
            }
            this.status = inputStream.read(cache_context, 10, true);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("RequestPacket decode error " + HexUtil.bytes2HexStr(this.sBuffer));
            throw new RuntimeException(e);
        }
    }

    public void writeTo(JceOutputStream outputStream) {
        outputStream.write(this.iVersion, 1);
        outputStream.write(this.cPacketType, 2);
        outputStream.write(this.iMessageType, 3);
        outputStream.write(this.iRequestId, 4);
        outputStream.write(this.sServantName, 5);
        outputStream.write(this.sFuncName, 6);
        outputStream.write(this.sBuffer, 7);
        outputStream.write(this.iTimeout, 8);
        outputStream.write(this.context, 9);
        outputStream.write(this.status, 10);
    }
}