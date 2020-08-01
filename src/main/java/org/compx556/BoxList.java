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
            int yLocation = 0;
            for (int x = b.getXStart(); x < b.getXFinish(); x++) {
                yLocation = Math.max(yLocation, heights[x]);
            }

            int boxHeight = yLocation + b.getHeight();
            for (int x = b.getXStart(); x < b.getXFinish(); x++) {
                heights[x] = boxHeight;
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