package org.compx556;

import org.compx556.function.DestructionFunction;
import org.compx556.function.RepairFunction;
import org.compx556.util.GlobalRandom;

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
     * @param current     the solution to apply the destroy and repair functions to
     * @param n           the <code>n</code> parameter value for the destroy function
     * @param threadCount the <code>threads</code> parameter for the repair function
     * @return the solution generated after applying the destroy and repair functions randomly selected.
     */
    public BoxList sampleAndApply(BoxList current, int n, int threadCount) {
        // sample randomly
        int index = GlobalRandom.nextInt(destroyRepairPairs.length);

        // get functions
        int[] pair = destroyRepairPairs[index];
        DestructionFunction destructionFunction = destructionFunctions[pair[0]];
        RepairFunction repairFunction = repairFunctions[pair[1]];

        // apply functions and return results
        return repairFunction.apply(destructionFunction.apply(current, n), threadCount);
    }
}
