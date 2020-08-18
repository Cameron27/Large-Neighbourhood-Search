package org.compx556;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import org.compx556.function.*;
import org.compx556.util.GlobalRandom;

import java.io.*;
import java.util.zip.DataFormatException;

public class RectanglePacker {
    public static Config defaultConfig = new Config(
            AcceptanceFunctions.recordToRecord,
            InitialisationFunctions.greedy,
            new DestructionFunction[]{
                    DestructionFunctions.randomNRemove,
                    DestructionFunctions.blockNRemove},
            new RepairFunction[]{
                    RepairFunctions.randomLocationOptimumX,
                    RepairFunctions.optimumLocationOptimumX,
                    RepairFunctions.optimumBlockLocation},
            new int[][]{
                    new int[]{0, 0},
                    new int[]{0, 1},
                    new int[]{1, 0},
                    new int[]{1, 1},
                    new int[]{1, 2}},
            0.04,
            0.1
    );

    private final BoxList initialState;
    private final AcceptanceFunction acceptanceFunction;
    private final InitialisationFunction initialisationFunction;
    private final DestructionFunction[] destructionFunctions;
    private final RepairFunction[] repairFunctions;
    private final int[][] destroyRepairPairs;
    private final long maxTime;
    private final int threadCount;
    private final File outFile;
    private final boolean printStats;
    private final int n;
    private final double initialTemperatureParameter;

    private DestroyRepairSampler destroyRepairSampler;

    public RectanglePacker(Config config) throws IOException, DataFormatException {
        // load data
        initialState = parseDataFile(config.dataFile);

        // get data from config
        acceptanceFunction = config.acceptanceFunction;
        initialisationFunction = config.initialisationFunction;
        destructionFunctions = config.destructionFunctions;
        repairFunctions = config.repairFunctions;
        destroyRepairPairs = config.destroyRepairPairs;

        maxTime = config.runtime;
        threadCount = config.threadCount;
        outFile = config.outFile;
        printStats = config.printStats;
        n = (int) (config.destructionProportion * initialState.size());
        initialTemperatureParameter = config.initialTemperatureParameter;
    }

    public int solve() {
        // reset destroy repair sampler
        destroyRepairSampler = new DestroyRepairSampler(destructionFunctions, repairFunctions, destroyRepairPairs);

        BoxList current = initialisationFunction.apply(initialState);
        BoxList best = current;
        double currentHeight = current.calculateHeight(true);
        double bestHeight = currentHeight;

        double startTemperature = acceptanceFunction.initialTemperature(currentHeight, initialTemperatureParameter);
        double endTemperature = 0;
        double temp = startTemperature;

        long startTime = System.currentTimeMillis();
        // while there is time remaining
        while (System.currentTimeMillis() - startTime < maxTime) {
            // get neighbour
            int index = destroyRepairSampler.sampleRandomIndex();
            BoxList next = destroyRepairSampler.apply(index, current, n, threadCount);
            double nextHeight = next.calculateHeight(true);

            // check acceptance
            int acceptanceLevel = acceptanceFunction.apply(nextHeight, currentHeight, bestHeight, temp);

            // update current if accepted
            if (acceptanceLevel >= AcceptanceFunction.ACCEPTED) {
                current = next;
                currentHeight = nextHeight;
            }
            // update best if new best
            if (acceptanceLevel >= AcceptanceFunction.BEST) {
                best = current;
                bestHeight = currentHeight;
            }

            destroyRepairSampler.updateWeighting(index, acceptanceLevel);

            // update temperature
            double remainingTimeFraction = (maxTime - (System.currentTimeMillis() - startTime)) / (double) maxTime;
            temp = (startTemperature - endTemperature) * remainingTimeFraction + endTemperature;
        }

        int finalHeight = (int) best.calculateHeight(false);
        // save file if an output file was provided
        if (outFile != null) {
            try {
                best.saveResult(outFile, finalHeight);
            } catch (IOException e) {
                System.err.println("Failed to save output as image.");
            }
        }

        // print stats if required
        if (printStats)
            System.out.println(destroyRepairSampler.statsString());

        return finalHeight;
    }

    public static BoxList parseDataFile(File file) throws IOException, DataFormatException {
        BoxList initialList = null;

        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);

        String line = br.readLine();
        boolean isBoxData = false;
        int size = 0;
        int itemArea = 0;

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
                        int objectWidth = Integer.parseInt(data[2]);
                        initialList = new BoxList(objectWidth, (int) Math.ceil((double) itemArea / objectWidth));
                    } else if (line.contains("item area")) {
                        itemArea = Integer.parseInt(data[2]);
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
        // create config
        Config config = defaultConfig.clone();

        // parse args
        JCommander builder = JCommander.newBuilder()
                .addObject(config)
                .build();
        try {
            builder.parse(args);
        } catch (
                ParameterException e) {
            System.out.println(e.getMessage());
            e.usage();
            return;
        }
        if (config.help) {
            builder.usage();
            return;
        }

        // set seed if provided
        if (config.seed != null) GlobalRandom.setSeed(config.seed);

        // create packer
        RectanglePacker rectanglePacker;
        try {
            rectanglePacker = new RectanglePacker(config);
        } catch (FileNotFoundException e) {
            System.err.println("File not found.");
            return;
        } catch (IOException e) {
            System.err.println("Failed to load data from file.");
            return;
        } catch (DataFormatException e) {
            System.err.println("Data is formatted incorrectly.");
            return;
        }

        // calculate
        int bestHeight = rectanglePacker.solve();

        System.out.println("Best height: " + bestHeight);
    }
}
