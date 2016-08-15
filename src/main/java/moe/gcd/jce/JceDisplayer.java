package moe.gcd.jce;

import moe.gcd.jce.exception.JceEncodeException;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public final class JceDisplayer {
    private int space = 0;
    private StringBuilder stringBuilder;

    public JceDisplayer(StringBuilder stringBuilder) {
        this.stringBuilder = stringBuilder;
    }

    public JceDisplayer(StringBuilder stringBuilder, int space) {
        this.stringBuilder = stringBuilder;
        this.space = space;
    }

    private void ps(String label) {
        for (int i = 0; i < this.space; i++) {
            this.stringBuilder.append('\t');
        }

        if (label != null) {
            this.stringBuilder.append(label).append(": ");
        }
    }

    public JceDisplayer display(byte b, String label) {
        ps(label);
        this.stringBuilder.append(b).append('\n');
        return this;
    }

    public JceDisplayer display(char c, String label) {
        ps(label);
        this.stringBuilder.append(c).append('\n');
        return this;
    }

    public JceDisplayer display(double d, String label) {
        ps(label);
        this.stringBuilder.append(d).append('\n');
        return this;
    }

    public JceDisplayer display(float f, String label) {
        ps(label);
        this.stringBuilder.append(f).append('\n');
        return this;
    }

    public JceDisplayer display(int i, String label) {
        ps(label);
        this.stringBuilder.append(i).append('\n');
        return this;
    }

    public JceDisplayer display(long l, String label) {
        ps(label);
        this.stringBuilder.append(l).append('\n');
        return this;
    }

    public JceDisplayer display(JceStruct struct, String label) {
        display('{', label);
        if (struct == null) {
            this.stringBuilder.append('\t').append("null");
        } else {
            struct.display(this.stringBuilder, this.space + 1);
        }

        display('}', null);
        return this;
    }

    public <T> JceDisplayer display(T o, String label) {
        if (o == null) {
            this.stringBuilder.append("null").append('\n');
            return this;
        }
        if ((o instanceof Byte)) {
            display(((Byte) o).byteValue(), label);
            return this;
        }
        if ((o instanceof Boolean)) {
            display(((Boolean) o).booleanValue(), label);
            return this;
        }
        if ((o instanceof Short)) {
            display(((Short) o).shortValue(), label);
            return this;
        }
        if ((o instanceof Integer)) {
            display(((Integer) o).intValue(), label);
            return this;
        }
        if ((o instanceof Long)) {
            display(((Long) o).longValue(), label);
            return this;
        }
        if ((o instanceof Float)) {
            display(((Float) o).floatValue(), label);
            return this;
        }
        if ((o instanceof Double)) {
            display(((Double) o).doubleValue(), label);
            return this;
        }
        if ((o instanceof String)) {
            display((String) o, label);
            return this;
        }
        if ((o instanceof Map)) {
            display((Map) o, label);
            return this;
        }
        if ((o instanceof List)) {
            display((List) o, label);
            return this;
        }
        if ((o instanceof JceStruct)) {
            display((JceStruct) o, label);
            return this;
        }
        if ((o instanceof byte[])) {
            display((byte[]) o, label);
            return this;
        }
        if ((o instanceof boolean[])) {
            display((boolean[]) o, label);
            return this;
        }
        if ((o instanceof short[])) {
            display((short[]) o, label);
            return this;
        }
        if ((o instanceof int[])) {
            display((int[]) o, label);
            return this;
        }
        if ((o instanceof long[])) {
            display((long[]) o, label);
            return this;
        }
        if ((o instanceof float[])) {
            display((float[]) o, label);
            return this;
        }
        if ((o instanceof double[])) {
            display((double[]) o, label);
            return this;
        }
        if (o.getClass().isArray()) {
            display((Object[]) o, label);
            return this;
        }
        throw new JceEncodeException("write object error: unsupport type.");
    }

    public JceDisplayer display(String label1, String label2) {
        ps(label2);
        if (label1 == null) {
            this.stringBuilder.append("null").append('\n');
            return this;
        }
        this.stringBuilder.append(label1).append('\n');
        return this;
    }

    public <T> JceDisplayer display(Collection<T> collection, String label) {
        if (collection == null) {
            ps(label);
            this.stringBuilder.append("null").append('\t');
            return this;
        }
        return display(collection.toArray(), label);
    }

    public <K, V> JceDisplayer display(Map<K, V> map, String label) {
        ps(label);
        if (map == null) {
            this.stringBuilder.append("null").append('\n');
            return this;
        }
        if (map.isEmpty()) {
            this.stringBuilder.append(map.size()).append(", {}").append('\n');
            return this;
        }
        this.stringBuilder.append(map.size()).append(", {").append('\n');
        JceDisplayer displayer1 = new JceDisplayer(this.stringBuilder, this.space + 1);
        JceDisplayer displayer2 = new JceDisplayer(this.stringBuilder, this.space + 2);
        for (Entry<K, V> entry : map.entrySet()) {
            displayer1.display('(', null);
            displayer2.display(entry.getKey(), null);
            displayer2.display(entry.getValue(), null);
            displayer1.display(')', null);
        }
        display('}', null);
        return this;
    }

    public JceDisplayer display(short s, String label) {
        ps(label);
        this.stringBuilder.append(s).append('\n');
        return this;
    }

    public JceDisplayer display(boolean b, String label) {
        ps(label);
        this.stringBuilder.append(b ? "T" : "F").append('\n');
        return this;
    }

    public JceDisplayer display(byte[] bytes, String label) {
        ps(label);
        if (bytes == null) {
            this.stringBuilder.append("null").append('\n');
            return this;
        }
        if (bytes.length == 0) {
            this.stringBuilder.append(bytes.length).append(", []").append('\n');
            return this;
        }
        this.stringBuilder.append(bytes.length).append(", [").append('\n');
        JceDisplayer displayer = new JceDisplayer(this.stringBuilder, this.space + 1);
        for (byte b : bytes) {
            displayer.display(b, null);
        }
        display(']', null);
        return this;
    }

    public JceDisplayer display(char[] chars, String label) {
        ps(label);
        if (chars == null) {
            this.stringBuilder.append("null").append('\n');
            return this;
        }
        if (chars.length == 0) {
            this.stringBuilder.append(chars.length).append(", []").append('\n');
            return this;
        }
        this.stringBuilder.append(chars.length).append(", [").append('\n');
        JceDisplayer displayer = new JceDisplayer(this.stringBuilder, this.space + 1);
        for (char c : chars) {
            displayer.display(c, null);
        }
        display(']', null);
        return this;
    }

    public JceDisplayer display(double[] doubles, String label) {
        ps(label);
        if (doubles == null) {
            this.stringBuilder.append("null").append('\n');
            return this;
        }
        if (doubles.length == 0) {
            this.stringBuilder.append(doubles.length).append(", []").append('\n');
            return this;
        }
        this.stringBuilder.append(doubles.length).append(", [").append('\n');
        JceDisplayer displayer = new JceDisplayer(this.stringBuilder, this.space + 1);
        for (double d : doubles) {
            displayer.display(d, null);
        }
        return this;
    }

    public JceDisplayer display(float[] floats, String label) {
        ps(label);
        if (floats == null) {
            this.stringBuilder.append("null").append('\n');
            return this;
        }
        if (floats.length == 0) {
            this.stringBuilder.append(floats.length).append(", []").append('\n');
            return this;
        }
        this.stringBuilder.append(floats.length).append(", [").append('\n');
        JceDisplayer displayer = new JceDisplayer(this.stringBuilder, this.space + 1);
        for (float f : floats) {
            displayer.display(f, null);
        }
        display(']', null);
        return this;
    }

    public JceDisplayer display(int[] ints, String label) {
        ps(label);
        if (ints == null) {
            this.stringBuilder.append("null").append('\n');
            return this;
        }
        if (ints.length == 0) {
            this.stringBuilder.append(ints.length).append(", []").append('\n');
            return this;
        }
        this.stringBuilder.append(ints.length).append(", [").append('\n');
        JceDisplayer displayer = new JceDisplayer(this.stringBuilder, this.space + 1);
        for (int i : ints) {
            displayer.display(i, null);
        }
        display(']', null);
        return this;
    }

    public JceDisplayer display(long[] longs, String label) {
        ps(label);
        if (longs == null) {
            this.stringBuilder.append("null").append('\n');
            return this;
        }
        if (longs.length == 0) {
            this.stringBuilder.append(longs.length).append(", []").append('\n');
            return this;
        }
        this.stringBuilder.append(longs.length).append(", [").append('\n');
        JceDisplayer displayer = new JceDisplayer(this.stringBuilder, this.space + 1);
        for (long l : longs) {
            displayer.display(l, null);
        }
        display(']', null);
        return this;
    }

    public <T> JceDisplayer display(T[] objects, String label) {
        ps(label);
        if (objects == null) {
            this.stringBuilder.append("null").append('\n');
            return this;
        }
        if (objects.length == 0) {
            this.stringBuilder.append(objects.length).append(", []").append('\n');
            return this;
        }
        this.stringBuilder.append(objects.length).append(", [").append('\n');
        JceDisplayer displayer = new JceDisplayer(this.stringBuilder, this.space + 1);
        for (T object : objects) {
            displayer.display(object, null);
        }
        display(']', null);
        return this;
    }

    public JceDisplayer display(short[] shorts, String label) {
        ps(label);
        if (shorts == null) {
            this.stringBuilder.append("null").append('\n');
            return this;
        }
        if (shorts.length == 0) {
            this.stringBuilder.append(shorts.length).append(", []").append('\n');
            return this;
        }
        this.stringBuilder.append(shorts.length).append(", [").append('\n');
        JceDisplayer displayer = new JceDisplayer(this.stringBuilder, this.space + 1);
        for (short s : shorts) {
            displayer.display(s, null);
        }
        display(']', null);
        return this;
    }

    public JceDisplayer display(boolean[] booleans, String label) {
        ps(label);
        if (booleans == null) {
            this.stringBuilder.append("null").append('\n');
            return this;
        }
        if (booleans.length == 0) {
            this.stringBuilder.append(booleans.length).append(", []").append('\n');
            return this;
        }
        this.stringBuilder.append(booleans.length).append(", [").append('\n');
        JceDisplayer displayer = new JceDisplayer(this.stringBuilder, this.space + 1);
        for (boolean b : booleans) {
            displayer.display(b, null);
        }
        display(']', null);
        return this;
    }

    public JceDisplayer displaySimple(byte b, boolean split) {
        this.stringBuilder.append(b);
        if (split) {
            this.stringBuilder.append("|");
        }
        return this;
    }

    public JceDisplayer displaySimple(char c, boolean split) {
        this.stringBuilder.append(c);
        if (split) {
            this.stringBuilder.append("|");
        }
        return this;
    }

    public JceDisplayer displaySimple(double d, boolean split) {
        this.stringBuilder.append(d);
        if (split) {
            this.stringBuilder.append("|");
        }
        return this;
    }

    public JceDisplayer displaySimple(float f, boolean split) {
        this.stringBuilder.append(f);
        if (split) {
            this.stringBuilder.append("|");
        }
        return this;
    }

    public JceDisplayer displaySimple(int i, boolean split) {
        this.stringBuilder.append(i);
        if (split) {
            this.stringBuilder.append("|");
        }
        return this;
    }

    public JceDisplayer displaySimple(long l, boolean split) {
        this.stringBuilder.append(l);
        if (split) {
            this.stringBuilder.append("|");
        }
        return this;
    }

    public JceDisplayer displaySimple(JceStruct struct, boolean split) {
        this.stringBuilder.append("{");
        if (struct == null) {
            this.stringBuilder.append('\t').append("null");
        } else {
            struct.displaySimple(this.stringBuilder, this.space + 1);
        }

        this.stringBuilder.append("}");
        if (split) {
            this.stringBuilder.append("|");
        }
        return this;
    }

    public <T> JceDisplayer displaySimple(T o, boolean split) {
        if (o == null) {
            this.stringBuilder.append("null").append('\n');
            return this;
        }
        if ((o instanceof Byte)) {
            displaySimple(((Byte) o).byteValue(), split);
            return this;
        }
        if ((o instanceof Boolean)) {
            displaySimple(((Boolean) o).booleanValue(), split);
            return this;
        }
        if ((o instanceof Short)) {
            displaySimple(((Short) o).shortValue(), split);
            return this;
        }
        if ((o instanceof Integer)) {
            displaySimple(((Integer) o).intValue(), split);
            return this;
        }
        if ((o instanceof Long)) {
            displaySimple(((Long) o).longValue(), split);
            return this;
        }
        if ((o instanceof Float)) {
            displaySimple(((Float) o).floatValue(), split);
            return this;
        }
        if ((o instanceof Double)) {
            displaySimple(((Double) o).doubleValue(), split);
            return this;
        }
        if ((o instanceof String)) {
            displaySimple((String) o, split);
            return this;
        }
        if ((o instanceof Map)) {
            displaySimple((Map) o, split);
            return this;
        }
        if ((o instanceof List)) {
            displaySimple((List) o, split);
            return this;
        }
        if ((o instanceof JceStruct)) {
            displaySimple((JceStruct) o, split);
            return this;
        }
        if ((o instanceof byte[])) {
            displaySimple((byte[]) o, split);
            return this;
        }
        if ((o instanceof boolean[])) {
            displaySimple((boolean[]) o, split);
            return this;
        }
        if ((o instanceof short[])) {
            displaySimple((short[]) o, split);
            return this;
        }
        if ((o instanceof int[])) {
            displaySimple((int[]) o, split);
            return this;
        }
        if ((o instanceof long[])) {
            displaySimple((long[]) o, split);
            return this;
        }
        if ((o instanceof float[])) {
            displaySimple((float[]) o, split);
            return this;
        }
        if ((o instanceof double[])) {
            displaySimple((double[]) o, split);
            return this;
        }
        if (o.getClass().isArray()) {
            displaySimple((Object[]) o, split);
            return this;
        }
        throw new JceEncodeException("Unsupported type.");
    }

    public JceDisplayer displaySimple(String label, boolean split) {
        if (label == null) {
            this.stringBuilder.append("null");
        } else {
            this.stringBuilder.append(label);
        }

        if (split) {
            this.stringBuilder.append("|");
        }
        return this;
    }

    public <T> JceDisplayer displaySimple(Collection<T> collection, boolean split) {
        if (collection == null) {
            this.stringBuilder.append("[]");
            if (split) {
                this.stringBuilder.append("|");
            }
            return this;
        }
        return displaySimple(collection.toArray(), split);
    }

    public <K, V> JceDisplayer displaySimple(Map<K, V> map, boolean split) {
        if ((map == null) || (map.isEmpty())) {
            this.stringBuilder.append("{}");
            if (split) {
                this.stringBuilder.append("|");
            }
            return this;
        }
        this.stringBuilder.append("{");
        JceDisplayer displayer = new JceDisplayer(this.stringBuilder, this.space + 2);
        boolean first = true;
        for (Entry<K, V> entry : map.entrySet()) {
            if (!first) {
                this.stringBuilder.append(",");
            }
            displayer.displaySimple(entry.getKey(), true);
            displayer.displaySimple(entry.getValue(), false);
            first = false;
        }
        this.stringBuilder.append("}");

        if (split) {
            this.stringBuilder.append("|");
        }
        return this;
    }

    public JceDisplayer displaySimple(short s, boolean split) {
        this.stringBuilder.append(s);
        if (split) {
            this.stringBuilder.append("|");
        }
        return this;
    }

    public JceDisplayer displaySimple(boolean b, boolean split) {
        this.stringBuilder.append(b ? "T" : "F");
        if (split) {
            this.stringBuilder.append("|");
        }
        return this;
    }

    public JceDisplayer displaySimple(byte[] bytes, boolean split) {
        if ((bytes == null) || (bytes.length == 0)) {
            if (split) {
                this.stringBuilder.append("|");
            }
            return this;
        }

        this.stringBuilder.append(HexUtil.bytes2HexStr(bytes));

        if (split) {
            this.stringBuilder.append("|");
        }
        return this;
    }

    public JceDisplayer displaySimple(char[] chars, boolean split) {
        if ((chars == null) || (chars.length == 0)) {
            if (split) {
                this.stringBuilder.append("|");
            }
            return this;
        }

        this.stringBuilder.append(new String(chars));

        if (split) {
            this.stringBuilder.append("|");
        }
        return this;
    }

    public JceDisplayer displaySimple(double[] doubles, boolean split) {
        if ((doubles == null) || (doubles.length == 0)) {
            this.stringBuilder.append("[]");
            if (split) {
                this.stringBuilder.append("|");
            }

            return this;
        }

        this.stringBuilder.append("[");
        JceDisplayer displayer = new JceDisplayer(this.stringBuilder, this.space + 1);
        for (int i = 0; i < doubles.length; i++) {
            if (i != 0)
                this.stringBuilder.append("|");
            displayer.displaySimple(doubles[i], false);
        }
        this.stringBuilder.append("]");

        if (split) {
            this.stringBuilder.append("|");
        }
        return this;
    }

    public JceDisplayer displaySimple(float[] floats, boolean split) {
        if ((floats == null) || (floats.length == 0)) {
            this.stringBuilder.append("[]");
            if (split) {
                this.stringBuilder.append("|");
            }

            return this;
        }

        this.stringBuilder.append("[");
        JceDisplayer displayer = new JceDisplayer(this.stringBuilder, this.space + 1);
        for (int i = 0; i < floats.length; i++) {
            if (i != 0)
                this.stringBuilder.append("|");
            displayer.displaySimple(floats[i], false);
        }
        this.stringBuilder.append("]");

        if (split) {
            this.stringBuilder.append("|");
        }
        return this;
    }

    public JceDisplayer displaySimple(int[] ints, boolean split) {
        if ((ints == null) || (ints.length == 0)) {
            this.stringBuilder.append("[]");
            if (split) {
                this.stringBuilder.append("|");
            }

            return this;
        }

        this.stringBuilder.append("[");
        JceDisplayer displayer = new JceDisplayer(this.stringBuilder, this.space + 1);
        for (int i = 0; i < ints.length; i++) {
            if (i != 0)
                this.stringBuilder.append("|");
            displayer.displaySimple(ints[i], false);
        }
        this.stringBuilder.append("]");

        if (split) {
            this.stringBuilder.append("|");
        }
        return this;
    }

    public JceDisplayer displaySimple(long[] longs, boolean split) {
        if ((longs == null) || (longs.length == 0)) {
            this.stringBuilder.append("[]");
            if (split) {
                this.stringBuilder.append("|");
            }

            return this;
        }

        this.stringBuilder.append("[");
        JceDisplayer displayer = new JceDisplayer(this.stringBuilder, this.space + 1);
        for (int i = 0; i < longs.length; i++) {
            if (i != 0)
                this.stringBuilder.append("|");
            displayer.displaySimple(longs[i], false);
        }
        this.stringBuilder.append("]");

        if (split) {
            this.stringBuilder.append("|");
        }
        return this;
    }

    public <T> JceDisplayer displaySimple(T[] objects, boolean split) {
        if ((objects == null) || (objects.length == 0)) {
            this.stringBuilder.append("[]");
            if (split) {
                this.stringBuilder.append("|");
            }

            return this;
        }

        this.stringBuilder.append("[");
        JceDisplayer displayer = new JceDisplayer(this.stringBuilder, this.space + 1);
        for (int i = 0; i < objects.length; i++) {
            if (i != 0)
                this.stringBuilder.append("|");
            displayer.displaySimple(objects[i], false);
        }
        this.stringBuilder.append("]");

        if (split) {
            this.stringBuilder.append("|");
        }
        return this;
    }

    public JceDisplayer displaySimple(short[] shorts, boolean split) {
        if ((shorts == null) || (shorts.length == 0)) {
            this.stringBuilder.append("[]");
            if (split) {
                this.stringBuilder.append("|");
            }

            return this;
        }

        this.stringBuilder.append("[");
        JceDisplayer displayer = new JceDisplayer(this.stringBuilder, this.space + 1);
        for (int i = 0; i < shorts.length; i++) {
            if (i != 0)
                this.stringBuilder.append("|");
            displayer.displaySimple(shorts[i], false);
        }
        this.stringBuilder.append("]");

        if (split) {
            this.stringBuilder.append("|");
        }
        return this;
    }

    public JceDisplayer displaySimple(boolean[] booleans, boolean split) {
        if ((booleans == null) || (booleans.length == 0)) {
            this.stringBuilder.append("[]");
            if (split) {
                this.stringBuilder.append("|");
            }

            return this;
        }

        this.stringBuilder.append("[");
        JceDisplayer displayer = new JceDisplayer(this.stringBuilder, this.space + 1);
        for (int i = 0; i < booleans.length; i++) {
            if (i != 0)
                this.stringBuilder.append("|");
            displayer.displaySimple(booleans[i], false);
        }
        this.stringBuilder.append("]");

        if (split) {
            this.stringBuilder.append("|");
        }
        return this;
    }
}