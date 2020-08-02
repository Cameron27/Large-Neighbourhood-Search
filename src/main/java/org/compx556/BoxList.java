package org.compx556;

import org.compx556.util.GlobalRandom;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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

    public void saveResult(File outFile) throws IOException {
        saveResult(outFile, calculateHeight());
    }

    public void saveResult(File outFile, int height) throws IOException {
        final int scale = 8;

        int imageWidth = objectSize * scale;
        int imageHeight = height * scale;
        BufferedImage image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_BGR);
        int[] pixels = image.getRGB(0, 0, imageWidth, imageHeight, null, 0, imageWidth);
        int[] heights = new int[objectSize];

        // set all pixels to white
        Arrays.fill(pixels, 0xFFFFFFFF);

        int red = GlobalRandom.nextInt(200);
        int green = GlobalRandom.nextInt(200);
        int blue = GlobalRandom.nextInt(200);

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

            // shuffle rgb value
            red = (red + GlobalRandom.nextInt(30)) % 200;
            green = (green + GlobalRandom.nextInt(25) + 20) % 200;
            blue = (blue + GlobalRandom.nextInt(35) + 40) % 200;
            int rgb = (red << 16) | (green << 8) | blue;
            // fill in all the pixels
            // x and y represent units in solution space
            for (int y = yLocation; y < boxHeight; y++) {
                for (int x = b.getXStart(); x < b.getXFinish(); x++) {
                    // subX and subY represent the multiple pixels per unit
                    for (int ySub = 0; ySub < 8; ySub++) {
                        for (int xSub = 0; xSub < 8; xSub++) {
                            pixels[(imageHeight - (y * scale + ySub) - 1) * imageWidth + x * scale + xSub] = rgb;
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
        if (!Arrays.stream(ImageIO.getWriterFormatNames()).anyMatch(extension::equals)) {
            System.err.println("File extension is invalid.");
            return;
        }

        // save image
        ImageIO.write(image, extension, outFile);
    }

    @Override
    public BoxList clone() {
        BoxList output = new BoxList(objectSize);

        output.addAll(this);

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