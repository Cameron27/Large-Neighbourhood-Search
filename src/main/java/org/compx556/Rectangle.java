package org.compx556;

/**
 * A rectangle that makes up part of a solution.
 */
public class Rectangle implements Cloneable {
    /**
     * Width of rectangle.
     */
    private int width;

    /**
     * Height of rectangle.
     */
    private int height;

    /**
     * The x value the rectangle starts at.
     */
    private int xStart;

    /**
     * The x value the rectangle ends at.
     */
    private int xFinish;

    /**
     * Create a new <code>Rectangle</code>.
     *
     * @param width  the width
     * @param height the height
     * @param xStart the x start value
     */
    public Rectangle(int width, int height, int xStart) {
        this.width = width;
        this.height = height;
        this.xStart = xStart;
        this.xFinish = this.xStart + this.width;
    }

    /**
     * Rotate the rectangle by flipping the width and height.
     */
    public void rotate() {
        int oldWidth = width;
        this.width = height;
        this.height = oldWidth;
        this.xFinish = this.xStart + this.width;
    }

    /**
     * Get width of the rectangle.
     *
     * @return width of rectangle
     */
    public int getWidth() {
        return this.width;
    }

    /**
     * Get height of the rectangle.
     *
     * @return height of rectangle
     */
    public int getHeight() {
        return this.height;
    }

    /**
     * Get the x value the rectangle starts at.
     *
     * @return x value the rectangle starts at
     */
    public int getXStart() {
        return xStart;
    }

    /**
     * Set the x value the rectangle starts at.
     *
     * @param xStart the new x value the rectangle starts at
     */
    public void setXStart(int xStart) {
        this.xStart = xStart;
        this.xFinish = this.xStart + this.width;
    }

    /**
     * Get the x value the rectangle ends at.
     *
     * @return x value the rectangle ends at
     */
    public int getXFinish() {
        return xFinish;
    }

    @Override
    public String toString() {
        return String.format("[w:%d,h:%d,x:%d]", width, height, xStart);
    }

    /**
     * Create a clone of the rectangle.
     *
     * @return a clone of the rectangle
     */
    @Override
    public Rectangle clone() {
        return new Rectangle(width, height, xStart);
    }

    /**
     * Check if this rectangle is equal to another by checking if the width, height and the x value the rectangle starts
     * at are the same.
     *
     * @param o the object to compare to
     * @return true if the rectangles are equal
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Rectangle)) return false;
        else {
            Rectangle b = (Rectangle) o;
            return width == b.width && height == b.height && xStart == b.xStart;
        }
    }
}