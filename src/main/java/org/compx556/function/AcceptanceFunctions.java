package org.compx556.function;

import org.compx556.util.GlobalRandom;

import static org.compx556.function.AcceptanceFunction.*;

public class AcceptanceFunctions {
    /**
     * Accepts the next solution only if it is better than the best solution.
     */
    public static final AcceptanceFunction hillClimb = (next, current, best, temp) -> {
        if (next < current) return BEST;
        return REJECTED;
    };

    /**
     * Uses simulated annealing to accept some solutions that are worse than the current solution.
     */
    public static final AcceptanceFunction simulatedAnnealing = (next, current, best, temp) -> {
        if (next < best)
            return BEST;
        if (next < current)
            return BETTER;
        if (temp != 0 && GlobalRandom.nextDouble() <= Math.exp((current - next) / temp))
            return ACCEPTED;
        return REJECTED;
    };
}
