package org.compx556;

public class Box implements Cloneable {
    private int width;
    private int height;
    private int xLocation;

    public Box(int width, int height, int xLocation) {
        this.width = width;
        this.height = height;
        this.xLocation = xLocation;
    }

    public void rotate() {
        int oldWidth = width;
        width = height;
        height = oldWidth;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public int getXLocation() {
        return this.xLocation;
    }

    public void setXLocation(int xLocation) {
        this.xLocation = xLocation;
    }

    @Override
    public String toString() {
        return String.format("[w:%d,h:%d,x:%d]", width, height, xLocation);
    }

    @Override
    public Box clone() {
        return new Box(width, height, xLocation);
    }
}