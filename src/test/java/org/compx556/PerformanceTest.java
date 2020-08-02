package org.compx556;

import org.compx556.function.AcceptanceFunctions;
import org.compx556.function.DestructionFunctions;
import org.compx556.function.InitialisationFunctions;
import org.compx556.function.RepairFunctions;
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
        config.threadCount = 12;

        // m1a
        {
            config.dataFile = new File(getClass().getResource("/m1a.csv").getFile());
            config.runtime = 2000;

            // randomLocationOptimumX test
            config.repairFunction = RepairFunctions.randomLocationOptimumX;
            RectanglePacker rectanglePacker = new RectanglePacker(config);
            runTest(rectanglePacker, "m1a randomLocationOptimumX");

            // optimumLocationOptimumX test
            config.repairFunction = RepairFunctions.optimumLocationOptimumX;
            rectanglePacker = new RectanglePacker(config);
            runTest(rectanglePacker, "m1a optimumLocationOptimumX");
        }
        // m2c
        {
            config.dataFile = new File(getClass().getResource("/m2c.csv").getFile());
            config.runtime = 5000;

            // randomLocationOptimumX test
            config.repairFunction = RepairFunctions.randomLocationOptimumX;
            RectanglePacker rectanglePacker = new RectanglePacker(config);
            runTest(rectanglePacker, "m2c randomLocationOptimumX");

            // optimumLocationOptimumX test
            config.repairFunction = RepairFunctions.optimumLocationOptimumX;
            rectanglePacker = new RectanglePacker(config);
            runTest(rectanglePacker, "m2c optimumLocationOptimumX");
        }
        // m3d
        {
            config.dataFile = new File(getClass().getResource("/m3d.csv").getFile());
            config.runtime = 10000;

            // randomLocationOptimumX test
            config.repairFunction = RepairFunctions.randomLocationOptimumX;
            RectanglePacker rectanglePacker = new RectanglePacker(config);
            runTest(rectanglePacker, "m3d randomLocationOptimumX");

            // optimumLocationOptimumX test
            config.repairFunction = RepairFunctions.optimumLocationOptimumX;
            rectanglePacker = new RectanglePacker(config);
            runTest(rectanglePacker, "m3d optimumLocationOptimumX");
        }

    }

    @Test
    public void AcceptanceFunctionTests() throws IOException, DataFormatException {
        Config config = new Config(AcceptanceFunctions.hillClimb, InitialisationFunctions.random,
                DestructionFunctions.randomNRemove, RepairFunctions.randomLocationOptimumX);
        config.seed = 10L;
        config.threadCount = 12;

        // m1a
        {
            config.dataFile = new File(getClass().getResource("/m1a.csv").getFile());
            config.runtime = 2000;

            // hillClimb test
            config.acceptanceFunction = AcceptanceFunctions.hillClimb;
            RectanglePacker rectanglePacker = new RectanglePacker(config);
            runTest(rectanglePacker, "m1a hillClimb");

            // simulatedAnnealing test
            config.acceptanceFunction = AcceptanceFunctions.simulatedAnnealing;
            rectanglePacker = new RectanglePacker(config);
            runTest(rectanglePacker, "m1a simulatedAnnealing");
        }
        // m2c
        {
            config.dataFile = new File(getClass().getResource("/m2c.csv").getFile());
            config.runtime = 5000;

            // hillClimb test
            config.acceptanceFunction = AcceptanceFunctions.hillClimb;
            RectanglePacker rectanglePacker = new RectanglePacker(config);
            runTest(rectanglePacker, "m2c hillClimb");

            // simulatedAnnealing test
            config.acceptanceFunction = AcceptanceFunctions.simulatedAnnealing;
            rectanglePacker = new RectanglePacker(config);
            runTest(rectanglePacker, "m2c simulatedAnnealing");
        }
        // m3d
        {
            config.dataFile = new File(getClass().getResource("/m3d.csv").getFile());
            config.runtime = 10000;

            // hillClimb test
            config.acceptanceFunction = AcceptanceFunctions.hillClimb;
            RectanglePacker rectanglePacker = new RectanglePacker(config);
            runTest(rectanglePacker, "m3d hillClimb");

            // simulatedAnnealing test
            config.acceptanceFunction = AcceptanceFunctions.simulatedAnnealing;
            rectanglePacker = new RectanglePacker(config);
            runTest(rectanglePacker, "m3d simulatedAnnealing");
        }
    }

    private void runTest(RectanglePacker rectanglePacker, String name) {
        int count = 3;
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
