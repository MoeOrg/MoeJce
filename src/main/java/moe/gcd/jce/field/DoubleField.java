package moe.gcd.jce.field;

public class DoubleField extends NumberField {
    private double data;

    DoubleField(double data, int tag) {
        super(tag);
        this.data = data;
    }

    public double get() {
        return this.data;
    }

    public Number getNumber() {
        return this.data;
    }

    public void set(double data) {
        this.data = data;
    }
}