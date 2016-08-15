package moe.gcd.jce.field;

public class StringField extends JceField {
    private String data;

    StringField(String data, int tag) {
        super(tag);
        this.data = data;
    }

    public String get() {
        return this.data;
    }

    public void set(String data) {
        this.data = data;
    }
}