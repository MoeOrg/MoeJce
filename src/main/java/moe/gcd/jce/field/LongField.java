package moe.gcd.jce.field;

public class LongField extends NumberField {
    private long data;

    LongField(long data, int tag) {
        super(tag);
        this.data = data;
    }

    public long get() {
        return this.data;
    }

    public Number getNumber() {
        return Long.valueOf(this.data);
    }

    public void set(long data) {
        this.data = data;
    }
}