package org.compx556.optimisation;

import org.compx556.Config;
import org.compx556.PerformanceTest;
import org.compx556.RectanglePacker;
import org.compx556.util.GlobalRandom;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.zip.DataFormatException;

public class NValueOptimisation {
    /**
     * Test to find the optimum proportion of elements to remove from the solution in each destruction step.
     */
    @Test
    public void NValueOptimisationTest() throws IOException, DataFormatException {
        // values to test
        double[] testValues = new double[]{0.02, 0.03, 0.04, 0.05, 0.1, 0.15};
        // datasets to test on
        String[] testFiles = new String[]{"/m1a.csv", "/m2c.csv", "/m3d.csv"};
        // runtime for each dataset
        int[] runtimes = new int[]{60000, 90000, 120000};

        // for each file
        for (int i = 0; i < testFiles.length; i++) {
            String testFile = testFiles[i];

            // for each test value
            for (double testValue : testValues) {
                // set seed
                GlobalRandom.setSeed((long) (testFile.hashCode() ^ Double.hashCode(testValue)));

                // set config
                Config config = RectanglePacker.defaultConfig.clone();
                config.destructionProportion = testValue;
                config.dataFile = new File(getClass().getResource(testFile).getFile());
                config.runtime = runtimes[i];

                // run tests
                RectanglePacker packer = new RectanglePacker(config);
                PerformanceTest.runTest(packer, testFile + " " + testValue);

            }
        }
    }
}
