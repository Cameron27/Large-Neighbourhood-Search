// CameronSalisbury_1293897_SivaramManoharan_1299026

package org.compx556.function;

import org.compx556.Rectangle;
import org.compx556.RectanglePacker;
import org.compx556.Solution;
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
    Solution initialList;

    @Before
    public void setup() throws IOException, DataFormatException {
        initialList = RectanglePacker.parseDataFile(new File(getClass().getResource("/m1a.csv").getFile()));
        initialList = InitialisationFunctions.random.apply(initialList);
    }

    @Test
    public void randomLocationOptimumXTest() {
        Pair<Solution, List<Rectangle>> destroyedList = DestructionFunctions.randomNRemove.apply(initialList, 15);

        Solution destroyedListBackup = destroyedList.getValue0().deepClone();

        GlobalRandom.setSeed(50L);
        Solution repairedList = RepairFunctions.randomLocationOptimumX.applyPartial(destroyedList, 1);

        // check immutable
        assertEquals(destroyedList.getValue0(), destroyedListBackup);

        // check size
        assertEquals(repairedList.size(), initialList.size());

        // check deterministic based on seed
        for (int i = 0; i < 10; i++) {
            GlobalRandom.setSeed(50L);
            Solution repairedList2 = RepairFunctions.randomLocationOptimumX.applyPartial(destroyedList, 1);
            assertEquals(repairedList2, repairedList);
        }

        // check multi threaded mode gives same result
        for (int i = 0; i < 10; i++) {
            GlobalRandom.setSeed(50L);
            Solution repairedList3 = RepairFunctions.randomLocationOptimumX.applyPartial(destroyedList, 12);
            assertEquals(repairedList3, repairedList);
        }
    }

    @Test
    public void optimumLocationOptimumXTest() {
        Pair<Solution, List<Rectangle>> destroyedList = DestructionFunctions.randomNRemove.apply(initialList, 15);

        Solution destroyedListBackup = destroyedList.getValue0().deepClone();

        GlobalRandom.setSeed(50L);
        Solution repairedList = RepairFunctions.optimumLocationOptimumX.applyPartial(destroyedList, 1);

        // check immutable
        assertEquals(destroyedList.getValue0(), destroyedListBackup);

        // check size
        assertEquals(repairedList.size(), initialList.size());

        // check deterministic based on seed
        for (int i = 0; i < 10; i++) {
            GlobalRandom.setSeed(50L);
            Solution repairedList2 = RepairFunctions.optimumLocationOptimumX.applyPartial(destroyedList, 1);
            assertEquals(repairedList2, repairedList);
        }

        // check multi threaded mode gives same result
        for (int i = 0; i < 10; i++) {
            GlobalRandom.setSeed(50L);
            Solution repairedList3 = RepairFunctions.optimumLocationOptimumX.applyPartial(destroyedList, 12);
            assertEquals(repairedList3, repairedList);
        }
    }
}
