package org.compx556;

import java.util.Arrays;

public class PerformanceTest {
    public static void runTest(RectanglePacker rectanglePacker, String name) {
        int count = 10;
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
