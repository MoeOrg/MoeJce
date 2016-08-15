package moe.gcd.jce.field;

public final class ListField extends JceField {
    private JceField[] data;

    ListField(JceField[] data, int tag) {
        super(tag);
        this.data = data;
    }

    public JceField get(int tag) {
        return this.data[tag];
    }

    public JceField[] get() {
        return this.data;
    }

    public void set(int tag, JceField value) {
        this.data[tag] = value;
    }

    public void set(JceField[] data) {
        this.data = data;
    }

    public int size() {
        return this.data.length;
    }
}