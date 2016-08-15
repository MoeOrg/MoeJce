package moe.gcd.jce.field;

public final class ShortField extends NumberField {
    private short data;

    ShortField(short data, int tag) {
        super(tag);
        this.data = data;
    }

    public short get() {
        return this.data;
    }

    public Number getNumber() {
        return this.data;
    }

    public void set(short data) {
        this.data = data;
    }
}