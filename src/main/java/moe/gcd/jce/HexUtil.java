package moe.gcd.jce;

public class HexUtil {
    private static final char[] DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
    public static final byte[] EMPTY_BYTES = new byte[0];

    public static String byte2HexStr(byte b) {
        char i = DIGITS[(b & 0xF)];
        b = (byte) (b >>> 4);
        return new String(new char[]{DIGITS[(b & 0xF)], i});
    }

    public static String bytes2HexStr(byte[] bytes) {
        if ((bytes == null) || (bytes.length == 0)) {
            return null;
        }

        char[] chars = new char[bytes.length * 2];
        for (int i = 0; i < bytes.length; i++) {
            byte b = bytes[i];
            chars[(i * 2 + 1)] = DIGITS[(b & 0xF)];
            b = (byte) (b >>> 4);
            chars[(i * 2)] = DIGITS[(b & 0xF)];
        }
        return new String(chars);
    }

    public static byte char2Byte(char c) {
        if ((c >= '0') && (c <= '9')) {
            return (byte) (c - '0');
        }

        if ((c >= 'a') && (c <= 'f')) {
            return (byte) (c - 'a' + 10);
        }

        if ((c >= 'A') && (c <= 'F')) {
            return (byte) (c - 'A' + 10);
        }

        return 0;
    }

    public static byte hexStr2Byte(String str) {
        byte b = 0;

        if (str != null) {
            if (str.length() == 1) {
                b = char2Byte(str.charAt(0));
            }
        }

        return b;
    }

    public static byte[] hexStr2Bytes(String string) {
        byte[] bytes;

        if ((string == null) || (string.isEmpty())) {
            bytes = EMPTY_BYTES;
            return bytes;
        }

        string = string.trim();

        bytes = new byte[string.length() / 2];
        for (int i = 0; i < bytes.length; i++) {
            char c1 = string.charAt(i * 2);
            char c2 = string.charAt(i * 2 + 1);
            bytes[i] = (byte) ((char2Byte(c1) << 4) + char2Byte(c2));
        }

        return bytes;
    }
}