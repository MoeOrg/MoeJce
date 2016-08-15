package moe.gcd.jce.field;

public final class ByteField extends NumberField {
    private byte data;

    ByteField(byte data, int tag) {
        super(tag);
        this.data = data;
    }

    public byte get() {
        return this.data;
    }

    public Number getNumber() {
        return this.data;
    }

    public void set(byte data) {
        this.data = data;
    }
}