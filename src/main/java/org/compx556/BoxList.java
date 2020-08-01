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
        int[] heights = new int[this.objectSize];
        for (Box b : this) {
            for (int x = b.getXLocation(); x < b.getWidth() + b.getXLocation(); x++) {
                heights[x] += b.getHeight();
            }
        }
        int maxHeight = 0;
        for (int height : heights) {
            maxHeight = Math.max(maxHeight, height);
        }
        return maxHeight;
    }

    @Override
    public BoxList clone() {
        BoxList output = new BoxList(objectSize);

        output.addAll(this);

        return output;
    }
}