package moe.gcd.jce.stream;

import moe.gcd.jce.HexUtil;
import moe.gcd.jce.JceStruct;
import moe.gcd.jce.exception.IllegalArgumentExceptionHandler;
import moe.gcd.jce.exception.JceEncodeException;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class JceOutputStream {
    private ByteBuffer buffer;
    private IllegalArgumentExceptionHandler exceptionHandler;
    protected String SERVER_ENCODING = "GBK";

    public JceOutputStream() {
        this(128);
    }

    public JceOutputStream(int capacity) {
        this.buffer = ByteBuffer.allocate(capacity);
    }

    public JceOutputStream(ByteBuffer buffer) {
        this.buffer = buffer;
    }

    private void writeArray(Object[] objects, int tag) {
        reserve(8);
        writeHead(JceStruct.LIST, tag);
        write(objects.length, 0);
        for (Object object : objects) {
            write(object, 0);
        }
    }

    public ByteBuffer getByteBuffer() {
        return this.buffer;
    }

    public IllegalArgumentExceptionHandler getExceptionHandler() {
        return this.exceptionHandler;
    }

    public void reserve(int capacity) {
        if (this.buffer.remaining() < capacity) {
            int newCapacity = (this.buffer.capacity() + capacity) * 2;
            try {
                ByteBuffer newBuffer = ByteBuffer.allocate(newCapacity);
                newBuffer.put(this.buffer.array(), 0, this.buffer.position());
                this.buffer = newBuffer;
            } catch (IllegalArgumentException e) {
                if (this.exceptionHandler != null) {
                    this.exceptionHandler.onException(e, this.buffer, capacity, newCapacity);
                }

                throw e;
            }
        }
    }

    public void setExceptionHandler(IllegalArgumentExceptionHandler exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
    }

    public void setServerEncoding(String encoding) {
        this.SERVER_ENCODING = encoding;
    }

    public byte[] toByteArray() {
        byte[] bytes = new byte[this.buffer.position()];
        System.arraycopy(this.buffer.array(), 0, bytes, 0, this.buffer.position());
        return bytes;
    }

    public void write(byte b, int tag) {
        reserve(3);
        if (b == 0) {
            writeHead(JceStruct.ZERO_TAG, tag);
            return;
        }

        writeHead(JceStruct.BYTE, tag);
        this.buffer.put(b);
    }

    public void write(double d, int tag) {
        reserve(10);
        writeHead(JceStruct.DOUBLE, tag);
        this.buffer.putDouble(d);
    }

    public void write(float f, int tag) {
        reserve(6);
        writeHead(JceStruct.FLOAT, tag);
        this.buffer.putFloat(f);
    }

    public void write(int i, int tag) {
        reserve(6);
        if ((i >= -32768) && (i <= 32767)) {
            write((short) i, tag);
            return;
        }

        writeHead(JceStruct.INT, tag);
        this.buffer.putInt(i);
    }

    public void write(long l, int tag) {
        reserve(10);
        if ((l >= -2147483648L) && (l <= 2147483647L)) {
            write((int) l, tag);
            return;
        }
        writeHead(JceStruct.LONG, tag);
        this.buffer.putLong(l);
    }

    public void write(JceStruct struct, int tag) {
        reserve(2);
        writeHead(JceStruct.STRUCT_BEGIN, tag);
        struct.writeTo(this);
        reserve(2);
        writeHead(JceStruct.STRUCT_END, 0);
    }

    public void write(Boolean b, int tag) {
        write(b.booleanValue(), tag);
    }

    public void write(Byte b, int tag) {
        write(b.byteValue(), tag);
    }

    public void write(Double d, int tag) {
        write(d.doubleValue(), tag);
    }

    public void write(Float f, int tag) {
        write(f.floatValue(), tag);
    }

    public void write(Integer i, int tag) {
        write(i.intValue(), tag);
    }

    public void write(Long parlmLong, int tag) {
        write(parlmLong.longValue(), tag);
    }

    public void write(Object object, int tag) {
        if ((object instanceof Byte)) {
            write(((Byte) object).byteValue(), tag);
            return;
        }
        if ((object instanceof Boolean)) {
            write(((Boolean) object).booleanValue(), tag);
            return;
        }
        if ((object instanceof Short)) {
            write(((Short) object).shortValue(), tag);
            return;
        }
        if ((object instanceof Integer)) {
            write(((Integer) object).intValue(), tag);
            return;
        }
        if ((object instanceof Long)) {
            write(((Long) object).longValue(), tag);
            return;
        }
        if ((object instanceof Float)) {
            write(((Float) object).floatValue(), tag);
            return;
        }
        if ((object instanceof Double)) {
            write(((Double) object).doubleValue(), tag);
            return;
        }
        if ((object instanceof String)) {
            write((String) object, tag);
            return;
        }
        if ((object instanceof Map)) {
            write((Map) object, tag);
            return;
        }
        if ((object instanceof List)) {
            write((List) object, tag);
            return;
        }
        if ((object instanceof JceStruct)) {
            write((JceStruct) object, tag);
            return;
        }
        if ((object instanceof byte[])) {
            write((byte[]) object, tag);
            return;
        }
        if ((object instanceof boolean[])) {
            write((boolean[]) object, tag);
            return;
        }
        if ((object instanceof short[])) {
            write((short[]) object, tag);
            return;
        }
        if ((object instanceof int[])) {
            write((int[]) object, tag);
            return;
        }
        if ((object instanceof long[])) {
            write((long[]) object, tag);
            return;
        }
        if ((object instanceof float[])) {
            write((float[]) object, tag);
            return;
        }
        if ((object instanceof double[])) {
            write((double[]) object, tag);
            return;
        }
        if (object.getClass().isArray()) {
            writeArray((Object[]) object, tag);
            return;
        }

        if ((object instanceof Collection)) {
            write((Collection) object, tag);
            return;
        }

        throw new JceEncodeException("Unsupported type: " + object.getClass());
    }

    public void write(Short s, int tag) {
        write(s.shortValue(), tag);
    }

    public void write(String str, int tag) {
        byte[] strBytes;

        try {
            strBytes = str.getBytes(this.SERVER_ENCODING);
        } catch (UnsupportedEncodingException e) {
            strBytes = str.getBytes();
        }

        reserve(strBytes.length + 10);

        if (strBytes.length > 0xff) {
            writeHead(JceStruct.STRING4, tag);
            this.buffer.putInt(strBytes.length);
            this.buffer.put(strBytes);
        } else {
            writeHead(JceStruct.STRING1, tag);
            this.buffer.put((byte) strBytes.length);
            this.buffer.put(strBytes);
        }
    }

    public <T> void write(Collection<T> collection, int tag) {
        reserve(8);
        writeHead(JceStruct.LIST, tag);

        if (collection == null) {
            write(0, 0);
            return;
        } else {
            write(collection.size(), 0);
        }

        for (T object : collection) {
            write(object, 0);
        }
    }

    public <K, V> void write(Map<K, V> map, int tag) {
        reserve(8);
        writeHead(JceStruct.MAP, tag);
        if (map == null) {
            write(0, 0);
            return;
        } else {
            write(map.size(), 0);
        }

        for (Entry<K, V> entry : map.entrySet()) {
            write(entry.getKey(), 0);
            write(entry.getValue(), 1);
        }
    }

    public void write(short s, int tag) {
        reserve(4);
        if ((s >= -128) && (s <= 127)) {
            write((byte) s, tag);
            return;
        }
        writeHead(JceStruct.SHORT, tag);
        this.buffer.putShort(s);
    }

    public void write(boolean b, int tag) {
        write((byte) (b ? 1 : 0), tag);
    }

    public void write(byte[] bytes, int tag) {
        reserve(bytes.length + 8);
        writeHead(JceStruct.SIMPLE_LIST, tag);
        writeHead(JceStruct.BYTE, 0);
        write(bytes.length, 0);
        this.buffer.put(bytes);
    }

    public void write(double[] doubles, int tag) {
        reserve(8);
        writeHead(JceStruct.LIST, tag);
        write(doubles.length, 0);
        for (double d : doubles) {
            write(d, 0);
        }
    }

    public void write(float[] floats, int tag) {
        reserve(8);
        writeHead(JceStruct.LIST, tag);
        write(floats.length, 0);
        for (float f : floats) {
            write(f, 0);
        }
    }

    public void write(int[] ints, int tag) {
        reserve(8);
        writeHead(JceStruct.LIST, tag);
        write(ints.length, 0);
        for (int i : ints) {
            write(i, 0);
        }
    }

    public void write(long[] longs, int tag) {
        reserve(8);
        writeHead(JceStruct.LIST, tag);
        write(longs.length, 0);
        for (long l : longs) {
            write(l, 0);
        }
    }

    public <T> void write(T[] array, int tag) {
        writeArray(array, tag);
    }

    public void write(short[] shorts, int tag) {
        reserve(8);
        writeHead(JceStruct.LIST, tag);
        write(shorts.length, 0);
        for (short s : shorts) {
            write(s, 0);
        }
    }

    public void write(boolean[] booleans, int tag) {
        reserve(8);
        writeHead(JceStruct.LIST, tag);
        write(booleans.length, 0);
        for (boolean b : booleans) {
            write(b, 0);
        }
    }

    public void writeHexString(String hexString, int tag) {
        reserve(hexString.length() + 10);

        byte[] bytes = HexUtil.hexStr2Bytes(hexString);

        if (bytes.length > 0xff) {
            writeHead(JceStruct.STRING4, tag);
            this.buffer.putInt(bytes.length);
            this.buffer.put(bytes);
        } else {
            writeHead(JceStruct.STRING1, tag);
            this.buffer.put((byte) bytes.length);
            this.buffer.put(bytes);
        }

    }

    public void writeByteString(String hexString, int tag) {
        writeHexString(hexString, tag);
    }

    public void writeHead(byte head, int tag) {
        if (tag < 0xf) {
            this.buffer.put((byte) (tag << 4 | head));
            return;
        }

        if (tag <= 0xff) {
            this.buffer.put((byte) (head | 0xF0));
            this.buffer.put((byte) tag);
            return;
        }

        throw new JceEncodeException("tag is too large: " + tag);
    }

    public void writeStringByte(String hexStr, int tag) {
        writeByteString(hexStr, tag);
    }
}