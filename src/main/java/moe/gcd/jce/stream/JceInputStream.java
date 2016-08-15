package moe.gcd.jce.stream;

import moe.gcd.jce.HexUtil;
import moe.gcd.jce.exception.JceDecodeException;
import moe.gcd.jce.JceStruct;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.util.*;

public final class JceInputStream {
    private ByteBuffer buffer;
    protected String SERVER_ENCODING = "GBK";

    public JceInputStream() {
    }

    public JceInputStream(ByteBuffer buffer) {
        this.buffer = buffer;
    }

    public JceInputStream(byte[] data) {
        this.buffer = ByteBuffer.wrap(data);
    }

    public JceInputStream(byte[] data, int position) {
        this.buffer = ByteBuffer.wrap(data);
        this.buffer.position(position);
    }

    private int peakHead(HeadData head) {
        return readHead(head, this.buffer.duplicate());
    }

    @SuppressWarnings("unchecked")
    private <T> T[] readArrayImpl(T defaultValue, int tag, boolean force) {
        if (skipToTag(tag)) {
            HeadData head = new HeadData();
            readHead(head);
            if (head.type != JceStruct.LIST) {
                throw new JceDecodeException("Type mismatch.");
            }

            int size = read(0, 0, true);
            if (size < 0) {
                throw new JceDecodeException("Invalid size: " + size);
            }

            T[] array = (T[]) Array.newInstance(defaultValue.getClass(), size);
            for (int i = 0; i < size; i++) {
                array[i] = read(defaultValue, 0, true);
            }
        } else if (force) {
            throw new JceDecodeException("Require field not exist.");
        }
        return null;
    }

    public static int readHead(HeadData head, ByteBuffer buffer) {
        byte b = buffer.get();
        head.type = (byte) (b & 0xF);
        head.tag = ((b & 0xF0) >> 4);
        if (head.tag == 0xF) {
            head.tag = (buffer.get() & 0xFF);
            return 2;
        }
        return 1;
    }

    private <K, V> Map<K, V> readMap(Map<K, V> defaultValue, Map<K, V> map, int tag, boolean force) {
        if ((map == null) || (map.isEmpty())) {
            return new HashMap<>();
        }

        Map.Entry<K, V> entry = map.entrySet().iterator().next();
        K key = entry.getKey();
        V value = entry.getValue();
        if (skipToTag(tag)) {
            HeadData head = new HeadData();
            readHead(head);
            switch (head.type) {
                default:
                    throw new JceDecodeException("type mismatch.");
                case JceStruct.MAP: {
                    HashMap<K, V> newMap = new HashMap<>();
                    int size = read(0, 0, true);
                    if (size < 0) {
                        throw new JceDecodeException("size invalid: " + size);
                    }

                    for (int i = 0; i < size; i++) {
                        newMap.put(read(key, 0, true), read(value, 0, true));
                    }
                    return newMap;
                }
            }
        }

        if (force) {
            throw new JceDecodeException("require field not exist.");
        }

        return defaultValue;
    }

    private void skip(int shift) {
        this.buffer.position(this.buffer.position() + shift);
    }

    private void skipField() {
        HeadData headData = new HeadData();
        readHead(headData);
        skipField(headData.type);
    }

    private void skipField(byte tag) {
        switch (tag) {
            default:
                throw new JceDecodeException("invalid type.");
            case JceStruct.BYTE:
                skip(1);
            case JceStruct.STRUCT_END:
            case JceStruct.ZERO_TAG:
                return;
            case JceStruct.SHORT:
                skip(2);
                return;
            case JceStruct.INT:
                skip(4);
                return;
            case JceStruct.LONG:
                skip(8);
                return;
            case JceStruct.FLOAT:
                skip(4);
                return;
            case JceStruct.DOUBLE:
                skip(8);
                return;
            case JceStruct.STRING1:
                skip(this.buffer.get() & 0xff);
                return;
            case JceStruct.STRING4:
                skip(this.buffer.getInt());
                return;
            case JceStruct.MAP: {
                int length = read(0, 0, true);
                for (int j = 0; j < length * 2; j++) {
                    skipField();
                }
            }
            case JceStruct.LIST: {
                int length = read(0, 0, true);
                for (int j = 0; j < length; j++) {
                    skipField();
                }
            }
            case JceStruct.SIMPLE_LIST:
                HeadData head = new HeadData();
                readHead(head);
                if (head.type != JceStruct.BYTE) {
                    throw new JceDecodeException("skipField with invalid type, type value: " + tag + ", " + head.type);
                }

                skip(read(0, 0, true));
                return;
            case 10:
        }
        skipToStructEnd();
    }

    public JceStruct directRead(JceStruct struct, int tag, boolean force) {
        HeadData head;
        if (skipToTag(tag)) {
            try {
                struct = struct.newInit();
                head = new HeadData();
                readHead(head);
                if (head.type != 10)
                    throw new JceDecodeException("type mismatch.");
            } catch (Exception e) {
                throw new JceDecodeException(e.getMessage());
            }
            struct.readFrom(this);
            skipToStructEnd();
        } else if (force) {
            throw new JceDecodeException("Require field not exist.");
        }
        return struct;
    }

    public ByteBuffer getBuffer() {
        return this.buffer;
    }

    public byte read(byte defaultValue, int tag, boolean force) {
        if (skipToTag(tag)) {
            HeadData head = new HeadData();
            readHead(head);
            switch (head.type) {
                default:
                    throw new JceDecodeException("type mismatch.");
                case JceStruct.ZERO_TAG:
                    return 0;
                case JceStruct.BYTE:
                    return this.buffer.get();
            }
        } else if (force) {
            throw new JceDecodeException("require field not exist.");
        }

        return defaultValue;
    }

    public double read(double defaultValue, int tag, boolean force) {
        if (skipToTag(tag)) {
            HeadData head = new HeadData();
            readHead(head);
            switch (head.type) {
                default:
                    throw new JceDecodeException("type mismatch.");
                case JceStruct.ZERO_TAG:
                    return 0;
                case JceStruct.FLOAT:
                    return this.buffer.getFloat();
                case 5:
                    return this.buffer.getDouble();
            }
        } else if (force) {
            throw new JceDecodeException("Require field not exist.");
        }

        return defaultValue;
    }

    public float read(float defaultValue, int tag, boolean force) {
        if (skipToTag(tag)) {
            HeadData head = new HeadData();
            readHead(head);
            switch (head.type) {
                default:
                    throw new JceDecodeException("type mismatch.");
                case JceStruct.ZERO_TAG:
                    return 0;
                case JceStruct.FLOAT:
                    return this.buffer.getFloat();
            }
        } else if (force) {
            throw new JceDecodeException("Require field not exist.");
        }

        return defaultValue;
    }

    public int read(int defaultValue, int tag, boolean force) {
        if (skipToTag(tag)) {
            HeadData head = new HeadData();
            readHead(head);
            switch (head.type) {
                default:
                    throw new JceDecodeException("type mismatch.");
                case 12:
                    return 0;
                case JceStruct.BYTE:
                    return this.buffer.get();
                case JceStruct.SHORT:
                    return this.buffer.getShort();
                case JceStruct.INT:
                    return this.buffer.getInt();
            }
        } else if (force) {
            throw new JceDecodeException("Require field not exist.");
        }

        return defaultValue;
    }

    public long read(long defaultValue, int tag, boolean force) {
        if (skipToTag(tag)) {
            HeadData head = new HeadData();
            readHead(head);
            switch (head.type) {
                default:
                    throw new JceDecodeException("type mismatch.");
                case 12:
                    return 0;
                case JceStruct.BYTE:
                    return this.buffer.get();
                case JceStruct.SHORT:
                    return this.buffer.getShort();
                case JceStruct.INT:
                    return this.buffer.getInt();
                case JceStruct.LONG:
                    return this.buffer.getLong();
            }
        } else if (force) {
            throw new JceDecodeException("Require field not exist.");
        }

        return defaultValue;
    }

    public JceStruct read(JceStruct defaultValue, int tag, boolean force) {
        if (skipToTag(tag)) {
            try {
                defaultValue = defaultValue.getClass().newInstance();
                HeadData head = new HeadData();
                readHead(head);
                if (head.type != JceStruct.STRUCT_BEGIN) {
                    throw new JceDecodeException("Type mismatch.");
                }
            } catch (Exception e) {
                throw new JceDecodeException(e.getMessage());
            }
            defaultValue.readFrom(this);
            skipToStructEnd();
        } else if (force) {
            throw new JceDecodeException("Require field not exist.");
        }

        return defaultValue;
    }

    @SuppressWarnings("unchecked")
    public <T> T read(T defaultValue, int tag, boolean force) {
        if ((defaultValue instanceof Byte)) {
            return (T) Byte.valueOf(read((byte) 0, tag, force));
        }
        if ((defaultValue instanceof Boolean)) {
            return (T) Boolean.valueOf(read(false, tag, force));
        }
        if ((defaultValue instanceof Short)) {
            return (T) Short.valueOf(read((short) 0, tag, force));
        }
        if ((defaultValue instanceof Integer)) {
            return (T) Integer.valueOf(read(0, tag, force));
        }
        if ((defaultValue instanceof Long)) {
            return (T) Long.valueOf(read(0L, tag, force));
        }
        if ((defaultValue instanceof Float)) {
            return (T) Float.valueOf(read(0.0F, tag, force));
        }
        if ((defaultValue instanceof Double)) {
            return (T) Double.valueOf(read(0.0D, tag, force));
        }
        if ((defaultValue instanceof String)) {
            return (T) readString(tag, force);
        }
        if ((defaultValue instanceof Map)) {
            return (T) readMap((Map) defaultValue, tag, force);
        }
        if ((defaultValue instanceof List)) {
            return (T) readArray((List) defaultValue, tag, force);
        }
        if ((defaultValue instanceof JceStruct)) {
            return (T) read((JceStruct) defaultValue, tag, force);
        }

        if (defaultValue.getClass().isArray()) {
            if ((defaultValue instanceof byte[]) || (defaultValue instanceof Byte[])) {
                return (T) read((byte[]) null, tag, force);
            }
            if ((defaultValue instanceof boolean[])) {
                return (T) read((boolean[]) null, tag, force);
            }
            if ((defaultValue instanceof short[])) {
                return (T) read((short[]) null, tag, force);
            }
            if ((defaultValue instanceof int[])) {
                return (T) read((int[]) null, tag, force);
            }
            if ((defaultValue instanceof long[])) {
                return (T) read((long[]) null, tag, force);
            }
            if ((defaultValue instanceof float[])) {
                return (T) read((float[]) null, tag, force);
            }
            if ((defaultValue instanceof double[])) {
                return (T) read((double[]) null, tag, force);
            }
            return (T) readArray((T[]) defaultValue, tag, force);
        }
        throw new JceDecodeException("Unsupported type.");
    }

    public String read(String defaultValue, int tag, boolean force) {
        if (skipToTag(tag)) {
            HeadData head = new HeadData();
            readHead(head);
            switch (head.type) {
                default:
                    throw new JceDecodeException("type mismatch.");
                case JceStruct.STRING1: {
                    byte[] data = new byte[this.buffer.get() & 0xff];
                    this.buffer.get(data);
                    try {
                        return new String(data, this.SERVER_ENCODING);
                    } catch (UnsupportedEncodingException e) {
                        return new String(data);
                    }
                }
                case JceStruct.STRING4: {
                    int length = this.buffer.getInt();
                    if ((length > JceStruct.JCE_MAX_STRING_LENGTH) || (length < 0) || (length > this.buffer.capacity())) {
                        throw new JceDecodeException("String too long: " + tag);
                    }
                    byte[] data = new byte[length];
                    this.buffer.get(data);
                    try {
                        return new String(data, this.SERVER_ENCODING);
                    } catch (UnsupportedEncodingException e) {
                        return new String(data);
                    }
                }
            }
        } else if (force) {
            throw new JceDecodeException("Require field not exist.");
        }

        return defaultValue;
    }

    public short read(short defaultValue, int tag, boolean force) {
        if (skipToTag(tag)) {
            HeadData head = new HeadData();
            readHead(head);
            switch (head.type) {
                default:
                    throw new JceDecodeException("type mismatch.");
                case 12:
                    return 0;
                case JceStruct.BYTE:
                    return (short) (this.buffer.get() & 0xff);
                case JceStruct.SHORT:
                    return this.buffer.getShort();
            }
        } else if (force) {
            throw new JceDecodeException("Require field not exist.");
        }

        return defaultValue;
    }

    public boolean read(boolean defaultValue, int tag, boolean force) {
        return read(0, tag, force) != 0;
    }

    public byte[] read(byte[] defaultValue, int tag, boolean force) {
        if (skipToTag(tag)) {
            HeadData head = new HeadData();
            readHead(head);
            switch (head.type) {
                default:
                    throw new JceDecodeException("Type mismatch.");
                case JceStruct.SIMPLE_LIST: {
                    HeadData headData = new HeadData();
                    readHead(headData);
                    if (headData.type != JceStruct.BYTE) {
                        throw new JceDecodeException("Type mismatch, tag: " + tag + ", type: " + head.type + ", " + headData.type);
                    }
                    int length = read(0, 0, true);
                    if ((length < 0) || (length > this.buffer.capacity())) {
                        throw new JceDecodeException("Invalid size, tag: " + tag + ", type: " + head.type + ", " + headData.type + ", size: " + length);
                    }
                    byte[] bytes = new byte[length];
                    this.buffer.get(bytes);
                    return bytes;
                }
                case JceStruct.LIST: {
                    int length = read(0, 0, true);
                    if ((length < 0) || (length > this.buffer.capacity())) {
                        throw new JceDecodeException("size invalid: " + length);
                    }
                    byte[] bytes = new byte[length];
                    for (int i = 0; i < length; i++) {
                        bytes[i] = read((byte) 0, 0, true);
                    }
                    return bytes;
                }
            }
        } else if (force) {
            throw new JceDecodeException("require field not exist.");
        }

        return defaultValue;
    }

    public double[] read(double[] defaultValue, int tag, boolean force) {
        if (skipToTag(tag)) {
            HeadData head = new HeadData();
            readHead(head);
            switch (head.type) {
                default:
                    throw new JceDecodeException("Type mismatch.");
                case JceStruct.LIST:
                    int length = read(0, 0, true);
                    if ((length < 0) || (length > this.buffer.capacity())) {
                        throw new JceDecodeException("size invalid: " + length);
                    }
                    double[] doubles = new double[length];
                    for (int i = 0; i < length; i++) {
                        doubles[i] = read(0d, 0, true);
                    }
                    return doubles;
            }
        } else if (force) {
            throw new JceDecodeException("require field not exist.");
        }

        return defaultValue;
    }

    public float[] read(float[] defaultValue, int tag, boolean force) {
        if (skipToTag(tag)) {
            HeadData head = new HeadData();
            readHead(head);
            switch (head.type) {
                default:
                    throw new JceDecodeException("Type mismatch.");
                case JceStruct.LIST:
                    int length = read(0, 0, true);
                    if ((length < 0) || (length > this.buffer.capacity())) {
                        throw new JceDecodeException("size invalid: " + length);
                    }
                    float[] floats = new float[length];
                    for (int i = 0; i < length; i++) {
                        floats[i] = read(0f, 0, true);
                    }
                    return floats;
            }
        } else if (force) {
            throw new JceDecodeException("require field not exist.");
        }

        return defaultValue;
    }

    public int[] read(int[] defaultValue, int tag, boolean force) {
        if (skipToTag(tag)) {
            HeadData head = new HeadData();
            readHead(head);
            switch (head.type) {
                default:
                    throw new JceDecodeException("Type mismatch.");
                case JceStruct.LIST:
                    int length = read(0, 0, true);
                    if ((length < 0) || (length > this.buffer.capacity())) {
                        throw new JceDecodeException("size invalid: " + length);
                    }
                    int[] ints = new int[length];
                    for (int i = 0; i < length; i++) {
                        ints[i] = read(0, 0, true);
                    }
                    return ints;
            }
        } else if (force) {
            throw new JceDecodeException("require field not exist.");
        }

        return defaultValue;
    }

    public long[] read(long[] defaultValue, int tag, boolean force) {
        if (skipToTag(tag)) {
            HeadData head = new HeadData();
            readHead(head);
            switch (head.type) {
                default:
                    throw new JceDecodeException("Type mismatch.");
                case JceStruct.LIST:
                    int length = read(0, 0, true);
                    if ((length < 0) || (length > this.buffer.capacity())) {
                        throw new JceDecodeException("size invalid: " + length);
                    }
                    long[] longs = new long[length];
                    for (int i = 0; i < length; i++) {
                        longs[i] = read(0L, 0, true);
                    }
                    return longs;
            }
        } else if (force) {
            throw new JceDecodeException("require field not exist.");
        }

        return defaultValue;
    }

    public JceStruct[] read(JceStruct[] defaultValue, int tag, boolean force) {
        return readArray(defaultValue, tag, force);
    }

    public String[] read(String[] defaultValue, int tag, boolean force) {
        return readArray(defaultValue, tag, force);
    }

    public short[] read(short[] defaultValue, int tag, boolean force) {
        if (skipToTag(tag)) {
            HeadData head = new HeadData();
            readHead(head);
            switch (head.type) {
                default:
                    throw new JceDecodeException("Type mismatch.");
                case JceStruct.LIST:
                    int length = read(0, 0, true);
                    if ((length < 0) || (length > this.buffer.capacity())) {
                        throw new JceDecodeException("size invalid: " + length);
                    }
                    short[] shorts = new short[length];
                    for (int i = 0; i < length; i++) {
                        shorts[i] = read((short) 0, 0, true);
                    }
                    return shorts;
            }
        } else if (force) {
            throw new JceDecodeException("require field not exist.");
        }

        return defaultValue;
    }

    public boolean[] read(boolean[] defaultValue, int tag, boolean force) {
        if (skipToTag(tag)) {
            HeadData head = new HeadData();
            readHead(head);
            switch (head.type) {
                default:
                    throw new JceDecodeException("Type mismatch.");
                case JceStruct.LIST:
                    int length = read(0, 0, true);
                    if ((length < 0) || (length > this.buffer.capacity())) {
                        throw new JceDecodeException("size invalid: " + length);
                    }
                    boolean[] booleans = new boolean[length];
                    for (int i = 0; i < length; i++) {
                        booleans[i] = read(false, 0, true);
                    }
                    return booleans;
            }
        } else if (force) {
            throw new JceDecodeException("require field not exist.");
        }

        return defaultValue;
    }

    public <T> List<T> readArray(List<T> defaultValue, int tag, boolean force) {
        if ((defaultValue == null) || (defaultValue.isEmpty())) {
            return new ArrayList<>();
        }

        T[] array = readArrayImpl(defaultValue.get(0), tag, force);
        if (array == null) {
            return null;
        }
        List<T> list = new ArrayList<>();
        Collections.addAll(list, array);
        return list;
    }

    public <T> T[] readArray(T[] defaultValue, int tag, boolean force) {
        if ((defaultValue == null) || (defaultValue.length == 0)) {
            throw new JceDecodeException("unable to get type of key and value.");
        }

        return readArrayImpl(defaultValue[0], tag, force);
    }

    public String readByteString(String defaultValue, int tag, boolean force) {
        if (skipToTag(tag)) {
            HeadData head = new HeadData();
            readHead(head);
            switch (head.type) {
                default:
                    throw new JceDecodeException("Type mismatch.");
                case JceStruct.STRING1: {
                    byte[] bytes = new byte[this.buffer.get() & 0xff];
                    this.buffer.get(bytes);
                    return HexUtil.bytes2HexStr(bytes);
                }
                case JceStruct.STRING4: {
                    int length = this.buffer.getInt();
                    if ((length > JceStruct.JCE_MAX_STRING_LENGTH) || (length < 0) || (length > this.buffer.capacity())) {
                        throw new JceDecodeException("String too long: " + tag);
                    }
                    byte[] bytes = new byte[length];
                    this.buffer.get(bytes);
                    return HexUtil.bytes2HexStr(bytes);
                }
            }
        } else if (force) {
            throw new JceDecodeException("Require field not exist.");
        }

        return defaultValue;
    }

    public void readHead(HeadData headData) {
        readHead(headData, this.buffer);
    }

    public List readList(int tag, boolean force) {
        List<Object> list = new ArrayList<>();

        if (skipToTag(tag)) {
            HeadData head = new HeadData();
            readHead(head);
            switch (head.type) {
                default:
                    throw new JceDecodeException("type mismatch.");
                case 9: {
                    int size = read(0, 0, true);
                    if (size < 0) {
                        throw new JceDecodeException("size invalid: " + size);
                    }

                    for (int i = 0; i < size; i++) {
                        HeadData headData = new HeadData();
                        readHead(headData);
                        switch (headData.type) {
                            case 0:
                                skip(1);
                                break;
                            case JceStruct.MAP:
                            case JceStruct.LIST:
                                break;
                            case JceStruct.SHORT:
                                skip(2);
                                break;
                            case JceStruct.INT:
                                skip(4);
                                break;
                            case JceStruct.LONG:
                                skip(8);
                                break;
                            case JceStruct.FLOAT:
                                skip(4);
                                break;
                            case JceStruct.DOUBLE:
                                skip(8);
                                break;
                            case JceStruct.STRING1:
                                skip(this.buffer.get() & 0xff);
                                break;
                            case JceStruct.STRING4:
                                skip(this.buffer.getInt());
                                break;
                            case JceStruct.STRUCT_BEGIN:
                                try {
                                    JceStruct struct = (JceStruct) Class.forName(JceStruct.class.getName()).getConstructor().newInstance();
                                    struct.readFrom(this);
                                    skipToStructEnd();
                                    list.add(struct);
                                    break;
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    throw new JceDecodeException("type mismatch." + e);
                                }
                            case JceStruct.ZERO_TAG:
                                list.add(0);
                                break;
                            case JceStruct.STRUCT_END:
                            default:
                                throw new JceDecodeException("Type mismatch.");
                        }
                    }
                }
            }
        } else if (force) {
            throw new JceDecodeException("Require field not exist.");
        }
        return list;
    }

    public <K, V> HashMap<K, V> readMap(Map<K, V> map, int tag, boolean force) {
        return (HashMap<K, V>) readMap(new HashMap<>(), map, tag, force);
    }

    public String readString(int tag, boolean force) {
        if (skipToTag(tag)) {
            HeadData head = new HeadData();
            readHead(head);
            switch (head.type) {
                case JceStruct.STRING1: {
                    byte[] bytes = new byte[this.buffer.get() & 0xff];
                    this.buffer.get(bytes);

                    try {
                        return new String(bytes, this.SERVER_ENCODING);
                    } catch (UnsupportedEncodingException e) {
                        return new String(bytes);
                    }
                }
                case JceStruct.STRING4:
                    int length = this.buffer.getInt();
                    if ((length > JceStruct.JCE_MAX_STRING_LENGTH) || (length < 0) || (length > this.buffer.capacity())) {
                        throw new JceDecodeException("String too long: " + length);
                    }

                    byte[] bytes = new byte[length];
                    this.buffer.get(bytes);
                    try {
                        return new String(bytes, this.SERVER_ENCODING);
                    } catch (UnsupportedEncodingException e) {
                        return new String(bytes);
                    }
                default:
                    throw new JceDecodeException("Type mismatch.");
            }
        } else if (force) {
            throw new JceDecodeException("Require field not exist.");
        }

        return null;
    }

    public Map<String, String> readStringMap(int tag, boolean force) {
        HashMap<String, String> map = new HashMap<>();
        if (skipToTag(tag)) {
            HeadData head = new HeadData();
            readHead(head);
            switch (head.type) {
                case JceStruct.MAP:
                    int size = read(0, 0, true);
                    if (size < 0) {
                        throw new JceDecodeException("size invalid: " + size);
                    }

                    for (int i = 0; i < size; i++) {
                        map.put(readString(0, true), readString(1, true));
                    }
                    break;
                default:
                    throw new JceDecodeException("type mismatch.");
            }

        } else if (force) {
            throw new JceDecodeException("Require field not exist.");
        }

        return map;
    }

    public int setServerEncoding(String encoding) {
        this.SERVER_ENCODING = encoding;
        return 0;
    }

    public void skipToStructEnd() {
        HeadData head = new HeadData();

        while (head.type != JceStruct.STRUCT_END) {
            readHead(head);
            skipField(head.type);
        }
    }

    public boolean skipToTag(int tag) {
        try {
            HeadData head = new HeadData();
            while (true) {
                int peak = peakHead(head);
                if (head.type == JceStruct.STRUCT_END) {
                    return false;
                }
                if (tag <= head.tag) {
                    if (tag != head.tag) {
                        break;
                    }
                    return true;
                }
                skip(peak);
                skipField(head.type);
            }
        } catch (JceDecodeException e) {
            return false;
        } catch (BufferUnderflowException ignored) {
        }
        return false;
    }

    public void warp(byte[] bytes) {
        wrap(bytes);
    }

    public void wrap(byte[] bytes) {
        this.buffer = ByteBuffer.wrap(bytes);
    }

    public static class HeadData {
        public int tag;
        public byte type;

        public void clear() {
            this.type = 0;
            this.tag = 0;
        }
    }
}