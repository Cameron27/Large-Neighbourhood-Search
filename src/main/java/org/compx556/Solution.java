package org.compx556;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * A solution for a 2D rectangle packing problem.
 */
public class Solution extends ArrayList<Rectangle> implements Cloneable {
    /**
     * Width of the object.
     */
    private int objectSize;

    /**
     * Minimum theoretical height the rectangles could stack to.
     */
    private int minHeight;

    /**
     * Create a new <code>Solution</code> object.
     *
     * @param objectSize width of the object for the problem
     * @param minHeight  minimum theoretical height the rectangles could stack to
     */
    public Solution(int objectSize, int minHeight) {
        super();
        this.objectSize = objectSize;
        this.minHeight = minHeight;
    }

    /**
     * Create a new <code>Solution</code> object.
     *
     * @param c          rectangles to add to the solution
     * @param objectSize width of the object for the problem
     * @param minHeight  minimum theoretical height the rectangles could stack to
     */
    public Solution(Collection<? extends Rectangle> c, int objectSize, int minHeight) {
        super(c);
        this.objectSize = objectSize;
        this.minHeight = minHeight;
    }

    /**
     * Get the size of the object.
     *
     * @return size of the object
     */
    public int getObjectSize() {
        return objectSize;
    }

    /**
     * Calculate the height of the solution.
     *
     * @param optimiseForComparison if true the height will be modified to be better for comparing to other solutions
     *                              but the returned height will not be the true height
     * @return the height of the solution
     */
    public double calculateHeight(boolean optimiseForComparison) {
        // the height at each x value
        int[] heights = new int[this.objectSize];

        // for each rectangle
        for (Rectangle r : this) {
            // find height rectangle will be stopped at
            int yLocation = 0;
            for (int x = r.getXStart(); x < r.getXFinish(); x++) {
                yLocation = Math.max(yLocation, heights[x]);
            }

            // calculate new height in location rectangle falls in and set the values
            int rectangleHeight = yLocation + r.getHeight();
            for (int x = r.getXStart(); x < r.getXFinish(); x++) {
                heights[x] = rectangleHeight;
            }
        }

        // find the highest point
        double highestPoint = 0;
        for (int height : heights) {
            highestPoint = Math.max(highestPoint, height);
        }

        // if optimising for comparison
        if (optimiseForComparison) {
            // count number of x coordinates at max height
            int count = 0;
            for (int height : heights) {
                count += height == highestPoint ? 1 : 0;
            }
            // add the fraction of x values at the max height
            highestPoint += (double) count / objectSize;

            // remove min height
            highestPoint -= minHeight;
        }

        return highestPoint;
    }

    /**
     * Save the solution as an image.
     *
     * @param outFile file to save image as
     * @param height  height of the solution
     * @throws IOException if there is an error creating the file
     */
    public void saveResult(File outFile, double height) throws IOException {
        final int scale = 8;
        final int background = 0x000000;
        final int foreground = 0xAAAAAA;

        int imageWidth = objectSize * scale;
        int imageHeight = (int) (height * scale);
        BufferedImage image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_BGR);
        int[] pixels = image.getRGB(0, 0, imageWidth, imageHeight, null, 0, imageWidth);

        // the height at each x value
        int[] heights = new int[objectSize];

        // set all pixels to white
        Arrays.fill(pixels, background);

        // for each rectangle
        for (Rectangle r : this) {
            // find height rectangle will be stopped at
            int yLocation = 0;
            for (int x = r.getXStart(); x < r.getXFinish(); x++) {
                yLocation = Math.max(yLocation, heights[x]);
            }

            // calculate new height in location rectangle falls in and set the values
            int rectangleHeight = yLocation + r.getHeight();
            for (int x = r.getXStart(); x < r.getXFinish(); x++) {
                heights[x] = rectangleHeight;
            }

            // fill in all the pixels
            // x and y represent units in solution space
            for (int y = yLocation; y < rectangleHeight; y++) {
                for (int x = r.getXStart(); x < r.getXFinish(); x++) {
                    // subX and subY represent the multiple pixels per unit
                    for (int ySub = 0; ySub < scale; ySub++) {
                        for (int xSub = 0; xSub < scale; xSub++) {
                            int pixelIndex = (imageHeight - (y * scale + ySub) - 1) * imageWidth + x * scale + xSub;
                            // if it is an edge
                            if (y == yLocation && ySub == 0 ||
                                    x == r.getXStart() && xSub == 0 ||
                                    y == rectangleHeight - 1 && ySub == scale - 1 ||
                                    x == r.getXFinish() - 1 && xSub == scale - 1)
                                pixels[pixelIndex] = background;
                                // otherwise
                            else
                                pixels[pixelIndex] = foreground;
                        }

                    }
                }
            }
        }

        // set pixels
        image.setRGB(0, 0, imageWidth, imageHeight, pixels, 0, imageWidth);

        // get extension
        String[] splitFileName = outFile.toString().split("\\.");
        String extension = splitFileName[splitFileName.length - 1];

        // check file extension is valid
        if (Arrays.stream(ImageIO.getWriterFormatNames()).noneMatch(extension::equals)) {
            System.err.println("File extension is invalid.");
            return;
        }

        // save image
        ImageIO.write(image, extension, outFile);
    }

    /**
     * Creates a clone of the solution, rectangles in the solution are copied by reference.
     *
     * @return a clone of the solution
     */
    @Override
    public Solution clone() {
        Solution clone = (Solution) super.clone();
        clone.objectSize = objectSize;
        clone.minHeight = minHeight;

        return clone;
    }

    /**
     * Creates a clone of the solution and clones all the rectangles in the solution.
     *
     * @return a clone of the solution.
     */
    public Solution deepClone() {
        Solution output = new Solution(objectSize, minHeight);

        for (Rectangle rectangle : this) {
            output.add(rectangle.clone());
        }

        return output;
    }

    /**
     * Check if this solution is equal to another by checking if the object size is the same and all the rectangles in
     * the solution are the same and in the same order.
     *
     * @param o the object to compare to
     * @return true if the solutions are equal
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Solution)) return false;
        else {
            Solution bl = (Solution) o;

            if (objectSize != bl.objectSize) return false;
            if (size() != bl.size()) return false;

            for (int i = 0; i < size(); i++) {
                if (!get(i).equals(bl.get(i))) return false;
            }

            return true;
        }
    }
}