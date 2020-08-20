// CameronSalisbury_1293897_SivaramManoharan_1299026

package org.compx556;

import org.compx556.util.GlobalRandom;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.zip.DataFormatException;

public class PerformanceTest {
    /**
     * Test the performance of the final algorithm on all datasets.
     */
    @Test
    public void NValueOptimisationTest() throws IOException, DataFormatException {
        // datasets to test on
        String[] testFiles = new String[]{"/m1a.csv", "/m2c.csv", "/m3d.csv"};
        // runtime for each dataset
        int[] runtimes = new int[]{300000, 300000, 300000};

        // for each file
        for (int i = 0; i < testFiles.length; i++) {
            String testFile = testFiles[i];

            // set seed
            GlobalRandom.setSeed((long) (testFile.hashCode()));

            // set config
            Config config = RectanglePacker.defaultConfig.clone();
            config.dataFile = new File(getClass().getResource(testFile).getFile());
            config.outFile = new File(testFile + i + ".png");
            config.runtime = runtimes[i];

            // run tests
            RectanglePacker packer = new RectanglePacker(config);
            PerformanceTest.runTest(packer, testFile);
        }
    }

    public static void runTest(RectanglePacker rectanglePacker, String name) {
        int count = 10;
        int[] results = new int[count];

        System.out.println(name);

        for (int i = 0; i < count; i++) {
            results[i] = rectanglePacker.solve();
            System.out.println(i + ": " + results[i]);
        }

        double average = Arrays.stream(results).average().getAsDouble();
        int max = Arrays.stream(results).max().getAsInt();
        int min = Arrays.stream(results).min().getAsInt();

        System.out.printf("\taverage: %.2f\n\tmin: %d\n\tmax: %d\n", average, min, max);
    }
}
