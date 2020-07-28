package org.compx556;

import java.util.ArrayList;
import java.util.Collection;

public class BoxList extends ArrayList<Box> implements Cloneable {
    private int objectSize;

    public BoxList(int objectSize) {
        super();
        this.objectSize = objectSize;
    }

    public BoxList(Collection<? extends Box> c, int objectSize) {
        super(c);
        this.objectSize = objectSize;
    }

    public int getObjectSize() {
        return objectSize;
    }

    public int calculateHeight() {
        int Height = 0;
        for (Box b : this) {
            Height = Height + b.getHeight();
        }
        return Height;
    }

    @Override
    public BoxList clone() {
        BoxList output = new BoxList(objectSize);

        output.addAll(this);

        return output;
    }
}