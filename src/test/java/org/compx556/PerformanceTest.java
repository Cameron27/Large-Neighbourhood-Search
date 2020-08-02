package org.compx556;

import org.compx556.function.DestructionFunctions;
import org.compx556.function.InitialisationFunctions;
import org.compx556.function.RepairFunctions;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.zip.DataFormatException;

public class PerformanceTest {
    @Test
    public void timingTest() throws IOException, DataFormatException {
        Config config = new Config(InitialisationFunctions.random, DestructionFunctions.randomNRemove, RepairFunctions.randomLocationOptimumX);
        config.seed = 10L;
        config.runtime = 10000;
        config.threadCount = 12;
        config.dataFile = new File(getClass().getResource("/m3d.csv").getFile());
        config.outFile = new File("test.png");

        RectanglePacker rectanglePacker = new RectanglePacker(config);
        int result = rectanglePacker.solve();

        System.out.println(result);
    }
}
