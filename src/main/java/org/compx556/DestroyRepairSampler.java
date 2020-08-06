package org.compx556;

import org.compx556.function.DestructionFunction;
import org.compx556.function.RepairFunction;
import org.compx556.util.GlobalRandom;

import java.util.Arrays;

public class DestroyRepairSampler {
    /**
     * Destruction functions available.
     */
    private final DestructionFunction[] destructionFunctions;

    /**
     * Repair functions available.
     */
    private final RepairFunction[] repairFunctions;

    /**
     * Pairs of indices for valid destroy and repair function combinations.
     */
    private final int[][] destroyRepairPairs;

    /**
     * Bias to use for the linear combination when updating weights.
     */
    private static final double bias = 0.75;

    /**
     * Scores to use when updating for each acceptance level.
     */
    private static final double[] scores = new double[]{0, 0.33, 0.66, 1};

    /**
     * Weights for each destroy and repair function combinations.
     */
    private final double[] weights;


    /**
     * Created a <code>DestroyRepairSampler</code> with the specified functions.
     *
     * @param destructionFunctions the destruction functions available
     * @param repairFunctions      the repairs functions available
     * @param destroyRepairPairs   the indices of valid destroy and repair function combinations
     */
    public DestroyRepairSampler(DestructionFunction[] destructionFunctions, RepairFunction[] repairFunctions,
                                int[][] destroyRepairPairs) {
        this.destructionFunctions = destructionFunctions;
        this.repairFunctions = repairFunctions;
        this.destroyRepairPairs = destroyRepairPairs;

        // set all initial weights to 1
        this.weights = new double[destroyRepairPairs.length];
        Arrays.fill(weights, 1);

        // check indices are all in bounds
        for (int i = 0; i < destroyRepairPairs.length; i++) {
            int[] pair = destroyRepairPairs[i];
            if (pair.length != 2) {
                throw new IllegalArgumentException("Pair in index " + i + " does not have a length of 2.");
            } else if (pair[0] >= destructionFunctions.length) {
                throw new IllegalArgumentException("Pair in index " + i + " has a destruction function index that is " +
                        "out of bounds.");
            } else if (pair[1] >= repairFunctions.length) {
                throw new IllegalArgumentException("Pair in index " + i + " has a repair function index that is out of " +
                        "bounds.");
            }
        }
    }

    /**
     * Select a random pair of destroy and repair functions and apply them to a solution.
     *
     * @param index       index of destroy and repair function pair
     * @param current     the solution to apply the destroy and repair functions to
     * @param n           the <code>n</code> parameter value for the destroy function
     * @param threadCount the <code>threads</code> parameter for the repair function
     * @return the solution generated after applying the destroy and repair functions randomly selected.
     */
    public BoxList apply(int index, BoxList current, int n, int threadCount) {
        // get functions
        int[] pair = destroyRepairPairs[index];
        DestructionFunction destructionFunction = destructionFunctions[pair[0]];
        RepairFunction repairFunction = repairFunctions[pair[1]];

        // apply functions and return results
        return repairFunction.apply(destructionFunction.apply(current, n), threadCount);
    }

    /**
     * Select a random index for the the destroy and repair function combination.
     *
     * @return the random index selected
     */
    public int sampleRandomIndex() {
        // calculate total weight sum
        double weightSum = 0;
        for (double weight : weights) {
            weightSum += weight;
        }

        // calculates each index's probability
        double[] probabilities = new double[weights.length];
        for (int i = 0; i < weights.length; i++) {
            double weight = weights[i];
            probabilities[i] = weight / weightSum;
        }

        // random sample from 0-1
        double rnd = GlobalRandom.nextDouble();
        // find corresponding index for random sample
        double cumulativeSum = 0;
        for (int i = 0; i < probabilities.length; i++) {
            double p = probabilities[i];
            cumulativeSum += p;
            if (rnd <= cumulativeSum) return i;
        }

        // covers rounding error
        return destroyRepairPairs.length - 1;
    }

    /**
     * Update the weighting for a destroy and repair function combination.
     *
     * @param index           index of destroy and repair function combination
     * @param acceptanceLevel acceptance level obtained from using destroy and repair function combination
     */
    public void updateWeighting(int index, int acceptanceLevel) {
        weights[index] = bias * weights[index] + (1 - bias) * scores[acceptanceLevel];
    }
}
