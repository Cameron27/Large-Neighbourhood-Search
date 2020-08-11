package org.compx556.function;

import org.compx556.util.GlobalRandom;

public class AcceptanceFunctions {
    /**
     * Accepts the next solution only if it is better than the best solution.
     */
    public static final AcceptanceFunction hillClimb = new AcceptanceFunction() {
        @Override
        public String getName() {
            return "Hill Climb";
        }

        @Override
        public int apply(double next, double current, double best, double temperature) {
            if (next < current) return BEST;
            return REJECTED;
        }

        @Override
        public double initialTemperature(double initialScore, double h) {
            return 0;
        }
    };

    /**
     * Uses simulated annealing to accept some solutions that are worse than the current solution.
     */
    public static final AcceptanceFunction simulatedAnnealing = new AcceptanceFunction() {
        @Override
        public String getName() {
            return "Simulated Annealing";
        }

        @Override
        public int apply(double next, double current, double best, double temperature) {
            if (next < best)
                return BEST;
            if (next < current)
                return BETTER;
            if (temperature != 0 && GlobalRandom.nextDouble() <= Math.exp((current - next) / temperature))
                return ACCEPTED;
            return REJECTED;
        }

        @Override
        public double initialTemperature(double initialScore, double h) {
            return initialScore * -h / Math.log(0.5);
        }
    };

    /**
     * Will accept solutions worse than the current if they are within a certain distance of the best solution.
     */
    public static final AcceptanceFunction recordToRecord = new AcceptanceFunction() {

        @Override
        public String getName() {
            return "Record-to-Record";
        }

        @Override
        public int apply(double next, double current, double best, double temperature) {
            if (next < best)
                return BEST;
            if (next < current)
                return BETTER;
            if ((next - best) / next < temperature)
                return ACCEPTED;
            return REJECTED;
        }

        @Override
        public double initialTemperature(double initialScore, double h) {
            return h;
        }
    };
}
