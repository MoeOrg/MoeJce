package moe.gcd.jce.stream;

import moe.gcd.jce.exception.JceDecodeException;
import moe.gcd.jce.field.JceField;
import moe.gcd.jce.field.NumberField;

import java.io.UnsupportedEncodingException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import static moe.gcd.jce.JceStruct.*;

public final class DynamicInputStream {
    private ByteBuffer buffer;
    private String SERVER_ENCODING = "GBK";

    public DynamicInputStream(ByteBuffer buffer) {
        this.buffer = buffer;
    }

    public DynamicInputStream(byte[] bytes) {
        this.buffer = ByteBuffer.wrap(bytes);
    }

    private JceField readString(JceInputStream.HeadData head, int length) {
        byte[] bytes = new byte[length];
        this.buffer.get(bytes);
        try {
            return JceField.create(new String(bytes, this.SERVER_ENCODING), head.tag);
        } catch (UnsupportedEncodingException e) {
            return JceField.create(new String(bytes), head.tag);
        }
    }

    public JceField read() {
        try {
            JceInputStream.HeadData head = new JceInputStream.HeadData();
            JceInputStream.readHead(head, this.buffer);
            switch (head.type) {
                case BYTE:
                    return JceField.create(this.buffer.get(), head.tag);
                case SHORT:
                    return JceField.create(this.buffer.getShort(), head.tag);
                case INT:
                    return JceField.create(this.buffer.getInt(), head.tag);
                case LONG:
                    return JceField.create(this.buffer.getLong(), head.tag);
                case FLOAT:
                    return JceField.create(this.buffer.getFloat(), head.tag);
                case DOUBLE:
                    return JceField.create(this.buffer.getDouble(), head.tag);
                case STRING1:
                    return readString(head, this.buffer.get() & 0xff);
                case STRING4:
                    return readString(head, this.buffer.getInt());
                case LIST: {
                    JceField field = read();
                    if (field instanceof NumberField) {
                        int size = ((NumberField) field).intValue();
                        JceField[] fields = new JceField[size];
                        for (int i = 0; i < size; i++) {
                            fields[i] = read();
                        }

                        return JceField.createList(fields, head.tag);
                    }
                    break;
                }
                case MAP: {
                    JceField field = read();
                    if (field instanceof NumberField) {
                        int size = ((NumberField) field).intValue();
                        JceField[] keys = new JceField[size];
                        JceField[] values = new JceField[size];
                        for (int i = 0; i < size; i++) {
                            keys[i] = read();
                            values[i] = read();
                        }

                        return JceField.createMap(keys, values, head.tag);
                    }
                    break;
                }
                case STRUCT_BEGIN: {
                    JceField field;
                    List<JceField> list = new ArrayList<>();
                    while ((field = read()) != null) {
                        list.add(field);
                    }

                    return JceField.createStruct((list).toArray(new JceField[0]), head.tag);
                }
                case ZERO_TAG:
                    return JceField.createZero(head.tag);
                case SIMPLE_LIST: {
                    int oldTag = head.tag;
                    JceInputStream.readHead(head, this.buffer);
                    if (head.type != BYTE) {
                        throw new JceDecodeException("type mismatch, simple_list only support byte, tag: " + oldTag + ", type: " + head.type);
                    }
                    JceField field = read();
                    if (field instanceof NumberField) {
                        byte[] bytes = new byte[((NumberField) field).intValue()];
                        this.buffer.get(bytes);
                        return JceField.create(bytes, oldTag);
                    }
                    break;
                }
                case 11:
                default:
                    break;
            }
        } catch (BufferUnderflowException e) {
            return null;
        }
        return null;
    }

    public int setServerEncoding(String encoding) {
        this.SERVER_ENCODING = encoding;
        return 0;
    }
}