package org.compx556;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import org.compx556.function.*;
import org.compx556.util.GlobalRandom;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.zip.DataFormatException;

public class RectanglePacker {
    private BoxList initialState;
    private final InitialisationFunction initialisationFunction;
    private final DestructionFunction destructionFunction;
    private final RepairFunction repairFunction;
    private final long maxTime;
    private final int threadCount;
    private final File outFile;

    public RectanglePacker(Config config) throws IOException, DataFormatException {
        initialState = parseDataFile(config.dataFile);

        initialisationFunction = config.initialisationFunction;
        destructionFunction = config.destructionFunction;
        repairFunction = config.repairFunction;

        maxTime = config.runtime;
        threadCount = config.threadCount;
        outFile = config.outFile;
    }

    public int solve() {
        BoxList current = initialisationFunction.apply(initialState);
        BoxList best = current;
        int currentHeight = current.calculateHeight();
        int bestHeight = currentHeight;

        long startTime = System.currentTimeMillis();
        int iterations = 0;
        while (System.currentTimeMillis() - startTime < maxTime) {
            BoxList next = repairFunction.apply(destructionFunction.apply(current, 15), threadCount);
            int nextHeight = next.calculateHeight();

            if (nextHeight < currentHeight) {
                current = next;
                currentHeight = nextHeight;
            }
            if (currentHeight < bestHeight) {
                best = current;
                bestHeight = currentHeight;
            }

            iterations++;
        }

        // save file if an output file was provided
        if (outFile != null) {
            try {
                best.saveResult(outFile, bestHeight);
            } catch (IOException e) {
                System.err.println("Failed to save output as image.");
            }
        }

        return bestHeight;
    }

    static BoxList parseDataFile(File file) throws IOException, DataFormatException {
        BoxList initialList = null;

        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);

        String line = br.readLine();
        boolean isBoxData = false;
        int size = 0;

        try {
            while (line != null) {
                String[] data = line.split(",");
                if (isBoxData) {
                    int numBox = Integer.parseInt(data[0]);
                    int xLocation = 0; // Placeholder
                    int width = Integer.parseInt(data[1]);
                    int height = Integer.parseInt(data[2]);
                    Box inputBox = new Box(width, height, xLocation);
                    initialList.add(inputBox);

                    if (numBox == size) {
                        isBoxData = false;
                    }
                } else {
                    if (line.contains("object width")) {
                        initialList = new BoxList(Integer.parseInt(data[2]));
                    } else if (line.contains("width")) {
                        isBoxData = true;
                        if (initialList == null)
                            throw new DataFormatException("Data is formatted incorrectly.");
                    } else if (line.contains("size")) {
                        size = Integer.parseInt(data[2]);
                    }
                }
                line = br.readLine();

            }
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            throw new DataFormatException("Data is formatted incorrectly.");
        }

        return initialList;
    }

    public static void main(String[] args) {
        Config config = new Config(InitialisationFunctions.random, DestructionFunctions.randomNRemove, RepairFunctions.randomLocationOptimumX);

        JCommander builder = JCommander.newBuilder()
                .addObject(config)
                .build();
        try {
            builder.parse(args);
        } catch (ParameterException e) {
            System.out.println(e.getMessage());
            e.usage();
            return;
        }
        if (config.help) {
            builder.usage();
            return;
        }

        if (config.seed != null) GlobalRandom.setSeed(config.seed);

        RectanglePacker rectanglePacker = null;
        try {
            rectanglePacker = new RectanglePacker(config);
        } catch (IOException e) {
            System.err.println("Failed to load data from file.");
            return;
        } catch (DataFormatException e) {
            System.err.println("Data is formatted incorrectly.");
            return;
        }

        int bestHeight = rectanglePacker.solve();

        System.out.println("Best height: " + bestHeight);
    }
}
