package org.compx556;

public class Box implements Cloneable {
    private int width;
    private int height;
    private int xStart;
    private int xFinish;

    public Box(int width, int height, int xStart) {
        this.width = width;
        this.height = height;
        this.xStart = xStart;
        this.xFinish = this.xStart + this.width;
    }

    public void rotate() {
        int oldWidth = width;
        this.width = height;
        this.height = oldWidth;
        this.xFinish = this.xStart + this.width;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public int getXStart() {
        return xStart;
    }

    public void setXStart(int xStart) {
        this.xStart = xStart;
        this.xFinish = this.xStart + this.width;
    }

    public int getXFinish() {
        return xFinish;
    }

    @Override
    public String toString() {
        return String.format("[w:%d,h:%d,x:%d]", width, height, xStart);
    }

    @Override
    public Box clone() {
        return new Box(width, height, xStart);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Box)) return false;
        else {
            Box b = (Box) o;
            return width == b.width && height == b.height && xStart == b.xStart;
        }
    }
}