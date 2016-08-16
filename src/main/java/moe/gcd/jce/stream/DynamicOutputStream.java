package moe.gcd.jce.stream;

import moe.gcd.jce.JceStruct;
import moe.gcd.jce.exception.JceDecodeException;
import moe.gcd.jce.field.*;

import java.nio.ByteBuffer;

public final class DynamicOutputStream extends JceOutputStream {
    public DynamicOutputStream() {
    }

    public DynamicOutputStream(int capacity) {
        super(capacity);
    }

    public DynamicOutputStream(ByteBuffer buffer) {
        super(buffer);
    }

    public void write(JceField field) {
        int tag = field.getTag();
        if ((field instanceof ZeroField)) {
            write(0, tag);
            return;
        }

        if (field instanceof IntField) {
            write(((IntField) field).get(), tag);
            return;
        }

        if (field instanceof ShortField) {
            write(((ShortField) field).get(), tag);
            return;
        }

        if (field instanceof ByteField) {
            write(((ByteField) field).get(), tag);
            return;
        }

        if (field instanceof StringField) {
            write(((StringField) field).get(), tag);
            return;
        }

        if (field instanceof ByteArrayField) {
            write(((ByteArrayField) field).get(), tag);
            return;
        }

        if (field instanceof ListField) {
            ListField listField = (ListField) field;
            reserve(8);
            writeHead(JceStruct.LIST, tag);
            write(listField.size(), 0);
            JceField[] fields = listField.get();
            for (JceField f : fields) {
                write(f);
            }
            return;
        }

        if (field instanceof MapField) {
            MapField mapField = (MapField) field;
            reserve(8);
            writeHead(JceStruct.MAP, tag);
            write(mapField.size(), 0);
            for (int i = 0; i < mapField.size(); i++) {
                write(mapField.getKey(i));
                write(mapField.getValue(i));
            }
        }

        if ((field instanceof StructField)) {
            StructField structField = (StructField) field;
            reserve(2);
            writeHead(JceStruct.STRUCT_BEGIN, tag);
            JceField[] fields = structField.get();
            for (JceField f : fields) {
                write(f);
            }
            reserve(2);
            writeHead(JceStruct.STRUCT_END, 0);
            return;
        }

        if ((field instanceof LongField)) {
            write(((LongField) field).get(), tag);
            return;
        }

        if ((field instanceof FloatField)) {
            write(((FloatField) field).get(), tag);
            return;
        }

        if ((field instanceof DoubleField)) {
            write(((DoubleField) field).get(), tag);
            return;
        }

        throw new JceDecodeException("Unknown JceField type: " + field.getClass().getName());
    }
}