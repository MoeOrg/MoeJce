package moe.gcd.jce;

import moe.gcd.jce.stream.JceInputStream;
import moe.gcd.jce.stream.JceOutputStream;

import java.io.Serializable;

public abstract class JceStruct implements Serializable {
    public static final byte BYTE = 0;
    public static final byte SHORT = 1;
    public static final byte INT = 2;
    public static final byte LONG = 3;
    public static final byte FLOAT = 4;
    public static final byte DOUBLE = 5;
    public static final byte STRING1 = 6;
    public static final byte STRING4 = 7;
    public static final byte MAP = 8;
    public static final byte LIST = 9;
    public static final byte STRUCT_BEGIN = 10;
    public static final byte STRUCT_END = 11;
    public static final byte ZERO_TAG = 12;
    public static final byte SIMPLE_LIST = 13;

    public static final int JCE_MAX_STRING_LENGTH = 104857600;

    private Object tag;

    public static String toDisplaySimpleString(JceStruct struct) {
        if (struct == null)
            return null;
        StringBuilder builder = new StringBuilder();
        struct.displaySimple(builder, 0);
        return builder.toString();
    }

    public boolean containField(String name) {
        return false;
    }

    public void display(StringBuilder builder, int space) {
    }

    public void displaySimple(StringBuilder builder, int space) {
    }

    public Object getFieldByName(String name) {
        return null;
    }

    public Object getTag() {
        return this.tag;
    }

    public JceStruct newInit() {
        return null;
    }

    public abstract void readFrom(JceInputStream stream);

    public void recyle() {
    }

    public void setFieldByName(String name, Object tag) {
    }

    public void setTag(Object tag) {
        this.tag = tag;
    }

    public byte[] toByteArray() {
        JceOutputStream outputStream = new JceOutputStream();
        writeTo(outputStream);
        return outputStream.toByteArray();
    }

    public byte[] toByteArray(String encoding) {
        JceOutputStream outputStream = new JceOutputStream();
        outputStream.setServerEncoding(encoding);
        writeTo(outputStream);
        return outputStream.toByteArray();
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        display(builder, 0);
        return builder.toString();
    }

    public abstract void writeTo(JceOutputStream stream);
}