package org.compx556;

import org.compx556.function.*;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.zip.DataFormatException;

public class PerformanceTest {
    @Test
    public void RepairFunctionTests() throws IOException, DataFormatException {
        Config config = new Config(AcceptanceFunctions.hillClimb, InitialisationFunctions.random,
                DestructionFunctions.randomNRemove, RepairFunctions.randomLocationOptimumX);
        config.seed = 10L;

        String[] dataFiles = new String[]{"/m1a.csv", "/m2c.csv", "/m3d.csv"};
        int[] runtimes = new int[]{2000, 5000, 10000};
        RepairFunction[] repairFunctions = new RepairFunction[]{RepairFunctions.randomLocationOptimumX,
                RepairFunctions.optimumLocationOptimumX};
        String[] repairFunctionNames = new String[]{"random location, optimum x", "optimum location, optimum x"};

        for (int i = 0; i < dataFiles.length; i++) {
            config.dataFile = new File(getClass().getResource(dataFiles[i]).getFile());
            config.runtime = runtimes[i];

            for (int j = 0; j < repairFunctions.length; j++) {
                config.repairFunction = repairFunctions[j];
                RectanglePacker rectanglePacker = new RectanglePacker(config);
                runTest(rectanglePacker, dataFiles[i] + " " + repairFunctionNames[j]);
            }
        }
    }

    @Test
    public void AcceptanceFunctionTests() throws IOException, DataFormatException {
        Config config = new Config(AcceptanceFunctions.hillClimb, InitialisationFunctions.random,
                DestructionFunctions.randomNRemove, RepairFunctions.randomLocationOptimumX);
        config.seed = 10L;

        String[] dataFiles = new String[]{"/m1a.csv", "/m2c.csv", "/m3d.csv"};
        int[] runtimes = new int[]{2000, 5000, 10000};
        AcceptanceFunction[] acceptanceFunctions = new AcceptanceFunction[]{AcceptanceFunctions.hillClimb,
                AcceptanceFunctions.recordToRecord};
        String[] acceptanceFunctionNames = new String[]{"hill climb", "record-to-record"};

        for (int i = 0; i < dataFiles.length; i++) {
            config.dataFile = new File(getClass().getResource(dataFiles[i]).getFile());
            config.runtime = runtimes[i];

            for (int j = 0; j < acceptanceFunctions.length; j++) {
                config.acceptanceFunction = acceptanceFunctions[j];
                RectanglePacker rectanglePacker = new RectanglePacker(config);
                runTest(rectanglePacker, dataFiles[i] + " " + acceptanceFunctionNames[j]);
            }
        }
    }

    private void runTest(RectanglePacker rectanglePacker, String name) {
        int count = 10;
        int[] results = new int[count];

        for (int i = 0; i < count; i++) {
            results[i] = rectanglePacker.solve();
        }

        double average = Arrays.stream(results).average().getAsDouble();
        int max = Arrays.stream(results).max().getAsInt();
        int min = Arrays.stream(results).min().getAsInt();

        System.out.printf("%s:\n\taverage: %.2f\n\tmin: %d\n\tmax: %d\n", name, average, min, max);
    }
}
