package moe.gcd.jce.field;

public class ByteArrayField extends JceField {
    private byte[] data;

    ByteArrayField(byte[] data, int tagId) {
        super(tagId);
        this.data = data;
    }

    public byte[] get() {
        return this.data;
    }

    public void set(byte[] data) {
        this.data = data;
    }
}