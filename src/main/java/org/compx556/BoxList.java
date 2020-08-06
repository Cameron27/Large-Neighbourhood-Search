package org.compx556;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class BoxList extends ArrayList<Box> implements Cloneable {
    private int objectSize;
    private int minHeight;

    public BoxList(int objectSize, int minHeight) {
        super();
        this.objectSize = objectSize;
        this.minHeight = minHeight;
    }

    public BoxList(Collection<? extends Box> c, int objectSize, int minHeight) {
        super(c);
        this.objectSize = objectSize;
        this.minHeight = minHeight;
    }

    public int getObjectSize() {
        return objectSize;
    }

    public double calculateHeight(boolean optimiseForComparison) {
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
        double highestPoint = 0;
        for (int height : heights) {
            highestPoint = Math.max(highestPoint, height);
        }

        if (optimiseForComparison) {
            int count = 0;
            for (int height : heights) {
                count += height == highestPoint ? 1 : 0;
            }
            highestPoint += (double) count / objectSize;
        }

        return highestPoint - (optimiseForComparison ? minHeight : 0);
    }

    public void saveResult(File outFile, double height) throws IOException {
        final int scale = 8;
        final int background = 0x000000;
        final int foreground = 0xAAAAAA;

        int imageWidth = objectSize * scale;
        int imageHeight = (int) (height * scale);
        BufferedImage image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_BGR);
        int[] pixels = image.getRGB(0, 0, imageWidth, imageHeight, null, 0, imageWidth);
        int[] heights = new int[objectSize];

        // set all pixels to white
        Arrays.fill(pixels, background);

        // for each box
        for (Box b : this) {
            // find height box will be stopped at
            int yLocation = 0;
            for (int x = b.getXStart(); x < b.getXFinish(); x++) {
                yLocation = Math.max(yLocation, heights[x]);
            }

            // calculate new height in location box falls in and set the values
            int boxHeight = yLocation + b.getHeight();
            for (int x = b.getXStart(); x < b.getXFinish(); x++) {
                heights[x] = boxHeight;
            }

            // fill in all the pixels
            // x and y represent units in solution space
            for (int y = yLocation; y < boxHeight; y++) {
                for (int x = b.getXStart(); x < b.getXFinish(); x++) {
                    // subX and subY represent the multiple pixels per unit
                    for (int ySub = 0; ySub < scale; ySub++) {
                        for (int xSub = 0; xSub < scale; xSub++) {
                            int pixelIndex = (imageHeight - (y * scale + ySub) - 1) * imageWidth + x * scale + xSub;
                            // if it is an edge
                            if (y == yLocation && ySub == 0 ||
                                    x == b.getXStart() && xSub == 0 ||
                                    y == boxHeight - 1 && ySub == scale - 1 ||
                                    x == b.getXFinish() - 1 && xSub == scale - 1)
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

    @Override
    public BoxList clone() {
        BoxList clone = (BoxList) super.clone();
        clone.objectSize = objectSize;
        clone.minHeight = minHeight;

        return clone;
    }


    public BoxList deepClone() {
        BoxList output = new BoxList(objectSize, minHeight);

        for (Box box : this) {
            output.add(box.clone());
        }

        return output;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof BoxList)) return false;
        else {
            BoxList bl = (BoxList) o;

            if (objectSize != bl.objectSize) return false;
            if (size() != bl.size()) return false;

            for (int i = 0; i < size(); i++) {
                if (!get(i).equals(bl.get(i))) return false;
            }

            return true;
        }
    }
}