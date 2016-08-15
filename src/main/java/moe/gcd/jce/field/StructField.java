package moe.gcd.jce.field;

import java.util.Arrays;
import java.util.Comparator;

public class StructField extends JceField {
    private static final Comparator<JceField> FIELD_COMPARATOR = new Comparator<JceField>() {
        public int compare(JceField f1, JceField f2) {
            return f1.getTag() - f2.getTag();
        }
    };
    private JceField[] data;

    StructField(JceField[] data, int tag) {
        super(tag);
        this.data = data;
    }

    public JceField[] get() {
        return this.data;
    }

    public JceField getFieldByTag(int tag) {
        tag = Arrays.binarySearch(this.data, JceField.createZero(tag), FIELD_COMPARATOR);
        if (tag >= 0) {
            return this.data[tag];
        }

        return null;
    }

    public boolean setByTag(int tag, JceField value) {
        tag = Arrays.binarySearch(this.data, JceField.createZero(tag), FIELD_COMPARATOR);
        if (tag >= 0) {
            this.data[tag] = value;
            return true;
        }

        int insertionPoint = -tag - 1;
        JceField[] newDataArray = new JceField[this.data.length + 1];
        int i;
        for (i = 0; i < insertionPoint; i++) {
            newDataArray[i] = this.data[tag];
        }
        newDataArray[insertionPoint] = value;
        for (i = insertionPoint; i < this.data.length; i++) {
            newDataArray[(i + 1)] = this.data[i];
        }

        this.data = newDataArray;
        return false;
    }
}