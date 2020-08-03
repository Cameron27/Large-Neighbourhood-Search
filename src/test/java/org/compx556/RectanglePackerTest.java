package org.compx556;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.zip.DataFormatException;

import static org.junit.Assert.assertEquals;

public class RectanglePackerTest {
    @Test
    public void mainTest() {
        String m1aSmall = this.getClass().getResource("/m1a_small.csv").getFile();

        RectanglePacker.main(new String[]{});
        RectanglePacker.main(new String[]{"-help"});
        RectanglePacker.main(new String[]{"-t", "500", m1aSmall});
        RectanglePacker.main(new String[]{"-s", "23", "-t", "500", m1aSmall});
    }

    @Test
    public void parseDataFileTest() throws IOException, DataFormatException {
        String m1aVerySmall = this.getClass().getResource("/m1a_very_small.csv").getFile();
        BoxList m1aVerySmallList = RectanglePacker.parseDataFile(new File(m1aVerySmall));

        BoxList m1aVerySmallListExplicit = new BoxList(15, 144);
        m1aVerySmallListExplicit.add(new Box(1, 6, 0));
        m1aVerySmallListExplicit.add(new Box(2, 8, 0));
        m1aVerySmallListExplicit.add(new Box(6, 5, 0));
        m1aVerySmallListExplicit.add(new Box(4, 9, 0));
        m1aVerySmallListExplicit.add(new Box(8, 7, 0));

        assertEquals(m1aVerySmallList, m1aVerySmallListExplicit);

        String m1a = this.getClass().getResource("/m1a.csv").getFile();
        String m2c = this.getClass().getResource("/m2c.csv").getFile();
        String m3d = this.getClass().getResource("/m3d.csv").getFile();

        RectanglePacker.parseDataFile(new File(m1a));
        RectanglePacker.parseDataFile(new File(m2c));
        RectanglePacker.parseDataFile(new File(m3d));
    }
}