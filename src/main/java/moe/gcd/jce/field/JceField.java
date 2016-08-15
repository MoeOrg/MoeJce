package moe.gcd.jce.field;

import moe.gcd.jce.exception.JceDecodeException;

public class JceField {
    private static ZeroField[] ZERO_FIELD = new ZeroField[256];
    private int tag;

    static {
        int i = 0;
        while (i < ZERO_FIELD.length) {
            ZERO_FIELD[i] = new ZeroField(i);
            i += 1;
        }
    }

    JceField(int tag) {
        this.tag = tag;
    }

    public static JceField create(byte b, int tag) {
        return new ByteField(b, tag);
    }

    public static JceField create(double d, int tag) {
        return new DoubleField(d, tag);
    }

    public static JceField create(float f, int tag) {
        return new FloatField(f, tag);
    }

    public static JceField create(int i, int tag) {
        return new IntField(i, tag);
    }

    public static JceField create(long l, int tag) {
        return new LongField(l, tag);
    }

    public static JceField create(String str, int tag) {
        return new StringField(str, tag);
    }

    public static JceField create(short s, int tag) {
        return new ShortField(s, tag);
    }

    public static JceField create(byte[] bytes, int tag) {
        return new ByteArrayField(bytes, tag);
    }

    public static JceField createList(JceField[] list, int tag) {
        return new ListField(list, tag);
    }

    public static JceField createMap(JceField[] keys, JceField[] values, int tag) {
        return new MapField(keys, values, tag);
    }

    public static JceField createStruct(JceField[] struct, int tag) {
        return new StructField(struct, tag);
    }

    public static JceField createZero(int tag) {
        if ((tag < 0) || (tag >= 255))
            throw new JceDecodeException("invalid tag: " + tag);
        return ZERO_FIELD[tag];
    }

    public int getTag() {
        return this.tag;
    }
}