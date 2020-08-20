// CameronSalisbury_1293897_SivaramManoharan_1299026

package org.compx556;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import org.compx556.function.*;
import org.compx556.util.GlobalRandom;

import java.io.*;
import java.util.zip.DataFormatException;

public class RectanglePacker {
    /**
     * Default configuration.
     */
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
            0.025
    );

    /**
     * Initial solution.
     */
    private final Solution initialState;

    /**
     * Acceptance function to use.
     */
    private final AcceptanceFunction acceptanceFunction;

    /**
     * Initialisation function to use.
     */
    private final InitialisationFunction initialisationFunction;

    /**
     * Destruction functions available to use.
     */
    private final DestructionFunction[] destructionFunctions;

    /**
     * Repair functions available to use.
     */
    private final RepairFunction[] repairFunctions;

    /**
     * Valid pairs of destruction and repair functions.
     */
    private final int[][] destroyRepairPairs;

    /**
     * Time to search for solutions.
     */
    private final long maxTime;

    /**
     * Thread count to use in repair functions.
     */
    private final int threadCount;

    /**
     * Name of file to save results to.
     */
    private final File outFile;

    /**
     * Whether or not stats should be printed.
     */
    private final boolean printStats;

    /**
     * Number of rectangles to be remove by destruction functions.
     */
    private final int n;

    /**
     * Parameter used to set initial temperature.
     */
    private final double initialTemperatureParameter;

    /**
     * Create a <code>RectanglePacker</code> object.
     *
     * @param config config to use
     * @throws IOException         if there is an error reading from the file
     * @throws DataFormatException if there is an error in the format of the data
     */
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
        n = Math.max((int) (config.destructionProportion * initialState.size()), 1);
        initialTemperatureParameter = config.initialTemperatureParameter;
    }

    /**
     * Find a solution for the 2D rectangle packing problem.
     *
     * @return the height of the best solution found
     */
    public int solve() {
        // reset destroy repair sampler
        DestroyRepairSampler destroyRepairSampler = new DestroyRepairSampler(destructionFunctions, repairFunctions, destroyRepairPairs);

        Solution current = initialisationFunction.apply(initialState);
        Solution best = current;
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
            Solution next = destroyRepairSampler.apply(index, current, n, threadCount);
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

    /**
     * Parse a file to generate a solution.
     *
     * @param file file to read
     * @return solution generated from file
     * @throws IOException         if there is an error reading from the file
     * @throws DataFormatException if there is an error in the format of the data
     */
    public static Solution parseDataFile(File file) throws IOException, DataFormatException {
        Solution initialList = null;

        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);

        String line = br.readLine();
        boolean isRectangleData = false;
        int size = 0;
        int itemArea = 0;

        try {
            // process one line at a time
            while (line != null) {
                String[] data = line.split(",");
                // read rectangle
                if (isRectangleData) {
                    int numRectangle = Integer.parseInt(data[0]);
                    int xLocation = 0;
                    int width = Integer.parseInt(data[1]);
                    int height = Integer.parseInt(data[2]);
                    Rectangle inputRectangle = new Rectangle(width, height, xLocation);
                    initialList.add(inputRectangle);

                    if (numRectangle == size) {
                        isRectangleData = false;
                    }
                } else {
                    // read object width
                    if (line.contains("object width")) {
                        int objectWidth = Integer.parseInt(data[2]);
                        initialList = new Solution(objectWidth, (int) Math.ceil((double) itemArea / objectWidth));
                    }
                    // read total rectangle area
                    else if (line.contains("item area")) {
                        itemArea = Integer.parseInt(data[2]);
                    }
                    // identify when to start reading rectangles
                    else if (line.contains("width")) {
                        isRectangleData = true;
                        if (initialList == null)
                            throw new DataFormatException("Data is formatted incorrectly.");
                    }
                    // read number of rectangles
                    else if (line.contains("size")) {
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
