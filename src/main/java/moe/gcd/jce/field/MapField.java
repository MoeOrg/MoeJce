package moe.gcd.jce.field;

public final class MapField extends JceField {
    private JceField[] keys;
    private JceField[] values;

    MapField(JceField[] keys, JceField[] values, int tag) {
        super(tag);
        this.keys = keys;
        this.values = values;
    }

    public JceField getKey(int tag) {
        return this.keys[tag];
    }

    public JceField[] getKeys() {
        return this.keys;
    }

    public JceField getValue(int tag) {
        return this.values[tag];
    }

    public JceField[] getValues() {
        return this.values;
    }

    public void setKey(int tag, JceField key) {
        this.keys[tag] = key;
    }

    public void setValue(int tag, JceField key) {
        this.values[tag] = key;
    }

    public int size() {
        return this.keys.length;
    }
}