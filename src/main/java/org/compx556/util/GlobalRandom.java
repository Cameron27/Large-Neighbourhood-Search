package org.compx556.util;

import java.util.Random;

public class GlobalRandom {
    private static Random rnd = new Random();

    public static boolean nextBoolean() {
        return rnd.nextBoolean();
    }

    public static int nextInt() {
        return rnd.nextInt();
    }

    public static int nextInt(int bound) {
        return rnd.nextInt(bound);
    }


    public static double nextDouble() {
        return rnd.nextDouble();
    }

    public static Random getRnd() {
        return rnd;
    }

    public static void setSeed(Long seed) {
        rnd.setSeed(seed);
    }
}
