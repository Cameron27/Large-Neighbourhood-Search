package org.compx556.function;

import org.compx556.util.GlobalRandom;

import static org.compx556.function.AcceptanceFunction.*;

public class AcceptanceFunctions {
    public static final AcceptanceFunction hillClimb = (triplet, temp) -> {
        int next = triplet.getValue0();
        int current = triplet.getValue1();

        if (next < current) return BEST;
        return REJECTED;
    };

    public static final AcceptanceFunction simulatedAnnealing = (triplet, temp) -> {
        int next = triplet.getValue0();
        int current = triplet.getValue1();
        int best = triplet.getValue2();

        if (next < best)
            return BEST;
        if (next < current)
            return BETTER;
        if (temp != 0 && GlobalRandom.nextDouble() <= Math.exp((current - next) / temp))
            return ACCEPTED;
        return REJECTED;
    };
}
