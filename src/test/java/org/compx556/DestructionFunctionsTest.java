package org.compx556;

import org.compx556.function.DestructionFunctions;
import org.compx556.function.InitialisationFunctions;
import org.javatuples.Pair;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.zip.DataFormatException;

import static org.junit.Assert.assertEquals;

public class DestructionFunctionsTest {
    BoxList initialList;

    @Before
    public void setup() throws IOException, DataFormatException {
        initialList = RectanglePacker.parseDataFile(new File(getClass().getResource("/m1a.csv").getFile()));
        initialList = InitialisationFunctions.random.apply(initialList);
    }

    @Test
    public void randomLocationOptimumXTest() {
        BoxList initialListBackup = initialList.deepClone();

        Pair<BoxList, List<Box>> destroyedList = DestructionFunctions.randomNRemove.apply(initialList, 15);

        // check immutable
        assertEquals(initialList, initialListBackup);

        // check counts are correct
        assertEquals(destroyedList.getValue0().size(), initialList.size() - 15);
        assertEquals(15, destroyedList.getValue1().size());
    }
}
