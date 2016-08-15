package moe.gcd.jce.field;

public abstract class NumberField extends JceField {
    NumberField(int tag) {
        super(tag);
    }

    public byte byteValue() {
        return getNumber().byteValue();
    }

    public double doubleValue() {
        return getNumber().doubleValue();
    }

    public float floatValue() {
        return getNumber().floatValue();
    }

    public abstract Number getNumber();

    public int intValue() {
        return getNumber().intValue();
    }

    public long longValue() {
        return getNumber().longValue();
    }

    public short shortValue() {
        return getNumber().shortValue();
    }
}