package moe.gcd.jce;

import java.nio.ByteBuffer;
import java.util.List;

public final class JceUtil {
    private static final char[] HIGH_DIGITS;
    private static final char[] LOW_DIGITS;

    static {
        char[] digits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        HIGH_DIGITS = new char[256];
        LOW_DIGITS = new char[256];
        for (int i = 0; i < 256; i++) {
            HIGH_DIGITS[i] = digits[(i >>> 4)];
            LOW_DIGITS[i] = digits[(i & 0xF)];
        }
    }

    public static int compareTo(byte b1, byte b2) {
        if (b1 < b2)
            return -1;
        if (b1 > b2)
            return 1;
        return 0;
    }

    public static int compareTo(char c1, char c2) {
        if (c1 < c2)
            return -1;
        if (c1 > c2)
            return 1;
        return 0;
    }

    public static int compareTo(double d1, double d2) {
        if (d1 < d2)
            return -1;
        if (d1 > d2)
            return 1;
        return 0;
    }

    public static int compareTo(float f1, float f2) {
        if (f1 < f2)
            return -1;
        if (f1 > f2)
            return 1;
        return 0;
    }

    public static int compareTo(int i1, int i2) {
        if (i1 < i2)
            return -1;
        if (i1 > i2)
            return 1;
        return 0;
    }

    public static int compareTo(long l1, long l2) {
        if (l1 < l2)
            return -1;
        if (l1 > l2)
            return 1;
        return 0;
    }

    public static <T extends Comparable<T>> int compareTo(T o1, T o2) {
        return o1.compareTo(o2);
    }

    public static <T extends Comparable<T>> int compareTo(List<T> list1, List<T> list2) {
        int r = compareTo(list1.size(), list2.size());
        if (r != 0) {
            return r;
        }

        for (int i = 0; i < list1.size(); i++) {
            r = list1.get(i).compareTo(list2.get(i));
            if (r != 0) {
                return r;
            }
        }
        return 0;
    }

    public static int compareTo(short s1, short s2) {
        if (s1 < s2)
            return -1;
        if (s1 > s2)
            return 1;
        return 0;
    }

    public static int compareTo(boolean b1, boolean b2) {
        return (b1 == b2) ? 0 : (b1 ? 1 : -1);
    }

    public static int compareTo(byte[] bytes1, byte[] bytes2) {
        int r = compareTo(bytes1.length, bytes2.length);
        if (r != 0) {
            return r;
        }

        for (int i = 0; i < bytes1.length; i++) {
            r = compareTo(bytes1[i], bytes2[i]);
            if (r != 0) {
                return r;
            }
        }
        return 0;
    }

    public static int compareTo(char[] chars1, char[] chars2) {
        int r = compareTo(chars1.length, chars2.length);
        if (r != 0) {
            return r;
        }

        for (int i = 0; i < chars1.length; i++) {
            r = compareTo(chars1[i], chars2[i]);
            if (r != 0) {
                return r;
            }
        }
        return 0;
    }

    public static int compareTo(double[] doubles1, double[] doubles2) {
        int r = compareTo(doubles1.length, doubles2.length);
        if (r != 0) {
            return r;
        }

        for (int i = 0; i < doubles1.length; i++) {
            r = compareTo(doubles1[i], doubles2[i]);
            if (r != 0) {
                return r;
            }
        }
        return 0;
    }

    public static int compareTo(float[] floats1, float[] floats2) {
        int r = compareTo(floats1.length, floats2.length);
        if (r != 0) {
            return r;
        }

        for (int i = 0; i < floats1.length; i++) {
            r = compareTo(floats1[i], floats2[i]);
            if (r != 0) {
                return r;
            }
        }
        return 0;
    }

    public static int compareTo(int[] ints1, int[] ints2) {
        int r = compareTo(ints1.length, ints2.length);
        if (r != 0) {
            return r;
        }

        for (int i = 0; i < ints1.length; i++) {
            r = compareTo(ints1[i], ints2[i]);
            if (r != 0) {
                return r;
            }
        }
        return 0;
    }

    public static int compareTo(long[] longs1, long[] longs2) {
        int r = compareTo(longs1.length, longs2.length);
        if (r != 0) {
            return r;
        }

        for (int i = 0; i < longs1.length; i++) {
            r = compareTo(longs1[i], longs2[i]);
            if (r != 0) {
                return r;
            }
        }
        return 0;
    }

    public static <T extends Comparable<T>> int compareTo(T[] array1, T[] array2) {
        int r = compareTo(array1.length, array2.length);
        if (r != 0) {
            return r;
        }

        for (int i = 0; i < array1.length; i++) {
            r = compareTo(array1[i], array2[i]);
            if (r != 0) {
                return r;
            }
        }
        return 0;
    }

    public static int compareTo(short[] shorts1, short[] shorts2) {
        int r = compareTo(shorts1.length, shorts2.length);
        if (r != 0) {
            return r;
        }

        for (int i = 0; i < shorts1.length; i++) {
            r = compareTo(shorts1[i], shorts2[i]);
            if (r != 0) {
                return r;
            }
        }
        return 0;
    }

    public static int compareTo(boolean[] booleans1, boolean[] booleans2) {
        int r = compareTo(booleans1.length, booleans2.length);
        if (r != 0) {
            return r;
        }

        for (int i = 0; i < booleans1.length; i++) {
            r = compareTo(booleans1[i], booleans2[i]);
            if (r != 0) {
                return r;
            }
        }
        return 0;
    }

    public static boolean equals(byte b1, byte b2) {
        return b1 == b2;
    }

    public static boolean equals(char c1, char c2) {
        return c1 == c2;
    }

    public static boolean equals(double d1, double d2) {
        return d1 == d2;
    }

    public static boolean equals(float f1, float f2) {
        return f1 == f2;
    }

    public static boolean equals(int i1, int i2) {
        return i1 == i2;
    }

    public static boolean equals(long l1, long l2) {
        return l1 == l2;
    }

    public static boolean equals(Object o1, Object o2) {
        return o1.equals(o2);
    }

    public static boolean equals(short s1, short s2) {
        return s1 == s2;
    }

    public static boolean equals(boolean b1, boolean b2) {
        return b1 == b2;
    }

    public static String getHexdump(ByteBuffer buffer) {
        int remaining = buffer.remaining();
        if (remaining == 0) {
            return "empty";
        }
        StringBuilder stringBuffer = new StringBuilder(buffer.remaining() * 3 - 1);
        int position = buffer.position();
        int v = buffer.get() & 0xFF;
        stringBuffer.append(HIGH_DIGITS[v]);
        stringBuffer.append(LOW_DIGITS[v]);

        while (buffer.remaining() > 0) {
            stringBuffer.append(' ');
            v = buffer.get() & 0xFF;
            stringBuffer.append(HIGH_DIGITS[v]);
            stringBuffer.append(LOW_DIGITS[v]);
        }
        buffer.position(position);
        return stringBuffer.toString();
    }

    public static String getHexdump(byte[] bytes) {
        return getHexdump(ByteBuffer.wrap(bytes));
    }

    public static byte[] getJceBufArray(ByteBuffer buffer) {
        byte[] arrayOfByte = new byte[buffer.position()];
        System.arraycopy(buffer.array(), 0, arrayOfByte, 0, arrayOfByte.length);
        return arrayOfByte;
    }

    public static int hashCode(byte b) {
        return b + 629;
    }

    public static int hashCode(char c) {
        return c + 'ɵ';
    }

    public static int hashCode(double d) {
        return hashCode(Double.doubleToLongBits(d));
    }

    public static int hashCode(float f) {
        return Float.floatToIntBits(f) + 629;
    }

    public static int hashCode(int i) {
        return i + 629;
    }

    public static int hashCode(long l) {
        return (int) (l >> 32 ^ l) + 629;
    }

    public static int hashCode(Object o) {
        if (o == null) {
            return 629;
        }

        if (o.getClass().isArray()) {
            if ((o instanceof long[])) {
                return hashCode((long[]) o);
            }

            if ((o instanceof int[])) {
                return hashCode((int[]) o);
            }

            if ((o instanceof short[])) {
                return hashCode((short[]) o);
            }

            if ((o instanceof char[])) {
                return hashCode((char[]) o);
            }

            if ((o instanceof byte[])) {
                return hashCode((byte[]) o);
            }

            if ((o instanceof double[])) {
                return hashCode((double[]) o);
            }

            if ((o instanceof float[])) {
                return hashCode((float[]) o);
            }

            if ((o instanceof boolean[])) {
                return hashCode((boolean[]) o);
            }

            if ((o instanceof JceStruct[])) {
                return hashCode((JceStruct[]) o);
            }
        }

        if ((o instanceof JceStruct)) {
            return o.hashCode();
        }

        return o.hashCode() + 629;
    }

    public static int hashCode(short s) {
        return s + 629;
    }

    public static int hashCode(boolean b) {
        return (b ? 1 : 0) + 629;
    }

    public static int hashCode(byte[] bytes) {
        int hashcode;
        if (bytes == null) {
            hashcode = 629;
        } else {
            hashcode = 17;
            for (int i = 0; i < bytes.length; i++) {
                hashcode = hashcode * 37 + bytes[i];
            }
        }
        return hashcode;
    }

    public static int hashCode(char[] chars) {
        int hashcode;
        if (chars == null) {
            hashcode = 629;
        } else {
            hashcode = 17;
            for (int i = 0; i < chars.length; i++) {
                hashcode = hashcode * 37 + chars[i];
            }
        }
        return hashcode;
    }

    public static int hashCode(double[] doubles) {
        int hashcode;
        if (doubles == null) {
            hashcode = 629;
        } else {
            hashcode = 17;
            for (int i = 0; i < doubles.length; i++) {
                hashcode = hashcode * 37 + (int) (Double.doubleToLongBits(doubles[i]) ^ Double.doubleToLongBits(doubles[i]) >> 32);
            }
        }
        return hashcode;
    }

    public static int hashCode(float[] floats) {
        int hashcode;
        if (floats == null) {
            hashcode = 629;
        } else {
            hashcode = 17;
            for (int i = 0; i < floats.length; i++) {
                hashcode = hashcode * 37 + Float.floatToIntBits(floats[i]);
            }
        }
        return hashcode;
    }

    public static int hashCode(int[] ints) {
        int hashcode;
        if (ints == null) {
            hashcode = 629;
        } else {
            hashcode = 17;
            for (int i = 0; i < ints.length; i++) {
                hashcode = hashcode * 37 + ints[i];
            }
        }
        return hashcode;
    }

    public static int hashCode(long[] longs) {
        int hashcode;
        if (longs == null) {
            hashcode = 629;
        } else {
            hashcode = 17;
            for (int i = 0; i < longs.length; i++) {
                hashcode = hashcode * 37 + (int) (longs[i] ^ longs[i] >> 32);
            }
        }
        return hashcode;
    }

    public static int hashCode(JceStruct[] structs) {
        int hashcode;
        if (structs == null) {
            hashcode = 629;
        } else {
            hashcode = 17;
            for (int i = 0; i < structs.length; i++) {
                hashcode = hashcode * 37 + structs[i].hashCode();
            }
        }
        return hashcode;
    }

    public static int hashCode(short[] shorts) {
        int hashcode;
        if (shorts == null) {
            hashcode = 629;
        } else {
            hashcode = 17;
            for (int i = 0; i < shorts.length; i++) {
                hashcode = hashcode * 37 + shorts[i];
            }
        }
        return hashcode;
    }

    public static int hashCode(boolean[] booleans) {
        int hashcode;
        if (booleans == null) {
            hashcode = 629;
        } else {
            hashcode = 17;
            for (int i = 0; i < booleans.length; i++) {
                hashcode = hashcode * 37 + (booleans[i] ? 1 : 0);
            }
        }
        return hashcode;
    }
}