package org.compx556;

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
        Config config = new Config(InitialisationFunctions.random, DestructionFunctions.randomNRemove, RepairFunctions.randomLocationOptimumX);
        config.seed = 10L;
        config.threadCount = 12;

        //m1a
        {
            config.dataFile = new File(getClass().getResource("/m1a.csv").getFile());
            config.runtime = 2000;

            // randomLocationOptimumX test
            config.repairFunction = RepairFunctions.randomLocationOptimumX;
            RectanglePacker rectanglePacker = new RectanglePacker(config);
            runTest(rectanglePacker, 10, "m1a randomLocationOptimumX");

            // optimumLocationOptimumX test
            config.repairFunction = RepairFunctions.optimumLocationOptimumX;
            rectanglePacker = new RectanglePacker(config);
            runTest(rectanglePacker, 10, "m1a optimumLocationOptimumX");
        }
        //m2c
        {
            config.dataFile = new File(getClass().getResource("/m2c.csv").getFile());
            config.runtime = 5000;

            // randomLocationOptimumX test
            config.repairFunction = RepairFunctions.randomLocationOptimumX;
            RectanglePacker rectanglePacker = new RectanglePacker(config);
            runTest(rectanglePacker, 10, "m2c randomLocationOptimumX");

            // optimumLocationOptimumX test
            config.repairFunction = RepairFunctions.optimumLocationOptimumX;
            rectanglePacker = new RectanglePacker(config);
            runTest(rectanglePacker, 10, "m2c optimumLocationOptimumX");
        }
        //m3d
        {
            config.dataFile = new File(getClass().getResource("/m3d.csv").getFile());
            config.runtime = 10000;

            // randomLocationOptimumX test
            config.repairFunction = RepairFunctions.randomLocationOptimumX;
            RectanglePacker rectanglePacker = new RectanglePacker(config);
            runTest(rectanglePacker, 10, "m3d randomLocationOptimumX");

            // optimumLocationOptimumX test
            config.repairFunction = RepairFunctions.optimumLocationOptimumX;
            rectanglePacker = new RectanglePacker(config);
            runTest(rectanglePacker, 10, "m3d optimumLocationOptimumX");
        }

    }

    private void runTest(RectanglePacker rectanglePacker, int count, String name) {
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
