package moe.gcd.jce.field;

public class IntField extends NumberField {
    private int data;

    IntField(int data, int tag) {
        super(tag);
        this.data = data;
    }

    public int get() {
        return this.data;
    }

    public Number getNumber() {
        return this.data;
    }

    public void set(int data) {
        this.data = data;
    }
}