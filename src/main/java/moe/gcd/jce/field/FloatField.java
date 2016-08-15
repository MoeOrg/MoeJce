package moe.gcd.jce.field;

public class FloatField extends NumberField {
    private float data;

    FloatField(float data, int tag) {
        super(tag);
        this.data = data;
    }

    public float get() {
        return this.data;
    }

    public Number getNumber() {
        return this.data;
    }

    public void set(float data) {
        this.data = data;
    }
}