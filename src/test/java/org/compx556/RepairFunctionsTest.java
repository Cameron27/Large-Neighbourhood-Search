package org.compx556;

import org.compx556.function.DestructionFunctions;
import org.compx556.function.InitialisationFunctions;
import org.compx556.function.RepairFunctions;
import org.compx556.util.GlobalRandom;
import org.javatuples.Pair;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.zip.DataFormatException;

import static org.junit.Assert.assertEquals;

public class RepairFunctionsTest {
    BoxList initialList;

    @Before
    public void setup() throws IOException, DataFormatException {
        initialList = RectanglePacker.parseDataFile(new File(getClass().getResource("/m1a.csv").getFile()));
        initialList = InitialisationFunctions.random.apply(initialList);
    }

    @Test
    public void randomLocationOptimumXTest() {
        Pair<BoxList, List<Box>> destroyedList = DestructionFunctions.randomNRemove.apply(initialList, 15);

        BoxList destroyedListBackup = destroyedList.getValue0().deepClone();

        GlobalRandom.setSeed(50L);
        BoxList repairedList = RepairFunctions.randomLocationOptimumX.apply(destroyedList, 1);

        // check immutable
        assertEquals(destroyedList.getValue0(), destroyedListBackup);

        // check size
        assertEquals(repairedList.size(), initialList.size());

        // check deterministic based on seed
        for (int i = 0; i < 10; i++) {
            GlobalRandom.setSeed(50L);
            BoxList repairedList2 = RepairFunctions.randomLocationOptimumX.apply(destroyedList, 1);
            assertEquals(repairedList2, repairedList);
        }

        // check multi threaded mode gives same result
        for (int i = 0; i < 10; i++) {
            GlobalRandom.setSeed(50L);
            BoxList repairedList3 = RepairFunctions.randomLocationOptimumX.apply(destroyedList, 12);
            assertEquals(repairedList3, repairedList);
        }
    }

    @Test
    public void optimumLocationOptimumXTest() {
        Pair<BoxList, List<Box>> destroyedList = DestructionFunctions.randomNRemove.apply(initialList, 15);

        BoxList destroyedListBackup = destroyedList.getValue0().deepClone();

        GlobalRandom.setSeed(50L);
        BoxList repairedList = RepairFunctions.optimumLocationOptimumX.apply(destroyedList, 1);

        // check immutable
        assertEquals(destroyedList.getValue0(), destroyedListBackup);

        // check size
        assertEquals(repairedList.size(), initialList.size());

        // check deterministic based on seed
        for (int i = 0; i < 10; i++) {
            GlobalRandom.setSeed(50L);
            BoxList repairedList2 = RepairFunctions.optimumLocationOptimumX.apply(destroyedList, 1);
            assertEquals(repairedList2, repairedList);
        }

        // check multi threaded mode gives same result
        for (int i = 0; i < 10; i++) {
            GlobalRandom.setSeed(50L);
            BoxList repairedList3 = RepairFunctions.optimumLocationOptimumX.apply(destroyedList, 12);
            assertEquals(repairedList3, repairedList);
        }
    }
}
