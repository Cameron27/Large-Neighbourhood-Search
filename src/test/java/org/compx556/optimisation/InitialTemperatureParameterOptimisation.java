// CameronSalisbury_1293897_SivaramManoharan_1299026

package org.compx556.optimisation;

import org.compx556.Config;
import org.compx556.PerformanceTest;
import org.compx556.RectanglePacker;
import org.compx556.function.AcceptanceFunction;
import org.compx556.function.AcceptanceFunctions;
import org.compx556.util.GlobalRandom;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.zip.DataFormatException;

public class InitialTemperatureParameterOptimisation {
    /**
     * Test to find the optimum acceptance function and initial temperature parameter.
     */
    @Test
    public void InitialTemperatureParameterOptimisationTest() throws IOException, DataFormatException {
        // values to test
        double[] testValues = new double[]{0.005, 0.01, 0.025, 0.05, 0.1, 0.2};
        // functions to test
        AcceptanceFunction[] testFunctions = new AcceptanceFunction[]{
                AcceptanceFunctions.hillClimb,
                AcceptanceFunctions.recordToRecord,
                AcceptanceFunctions.simulatedAnnealing};
        // datasets to test on
        String[] testFiles = new String[]{"/m1a.csv", "/m2c.csv", "/m3d.csv"};
        // runtime for each dataset
        int[] runtimes = new int[]{60000, 90000, 120000};

        // for each file
        for (int i = 0; i < testFiles.length; i++) {
            String testFile = testFiles[i];

            // for each test function
            for (AcceptanceFunction testFunction : testFunctions) {
                // for each test value
                if (testFunction != AcceptanceFunctions.hillClimb)
                    for (double testValue : testValues) {
                        // set seed
                        GlobalRandom.setSeed((long) (testFile.hashCode() ^ Double.hashCode(testValue)));

                        // set config
                        Config config = RectanglePacker.defaultConfig.clone();
                        config.initialTemperatureParameter = testValue;
                        config.acceptanceFunction = testFunction;
                        config.dataFile = new File(getClass().getResource(testFile).getFile());
                        config.runtime = runtimes[i];

                        // run tests
                        RectanglePacker packer = new RectanglePacker(config);
                        PerformanceTest.runTest(packer, testFile + " " + testFunction.getName() + " " + testValue);
                    }
                else {
                    // set seed
                    GlobalRandom.setSeed((long) (testFile.hashCode()));

                    // set config
                    Config config = RectanglePacker.defaultConfig.clone();
                    config.acceptanceFunction = testFunction;
                    config.dataFile = new File(getClass().getResource(testFile).getFile());
                    config.runtime = runtimes[i];

                    // run tests
                    RectanglePacker packer = new RectanglePacker(config);
                    PerformanceTest.runTest(packer, testFile + " " + testFunction.getName());
                }
            }
        }
    }
}
