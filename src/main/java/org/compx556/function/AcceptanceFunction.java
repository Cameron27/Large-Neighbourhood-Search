package org.compx556.function;

/**
 * <p>
 * A function to decide if the next solution should be accepted.
 * </p>
 * <p>
 * The expected parameters for a <code>AcceptanceFunction</code> are the height of the next solution, the height of the
 * current solution, the height of the best solution and a temperature which may be used.
 * </p>
 * <p>
 * The output should be an <code>int</code> representing the level of acceptance.
 * </p>
 */
@FunctionalInterface
public interface AcceptanceFunction {
    /**
     * Indicates next solution is better than the best solution.
     */
    int BEST = 3;

    /**
     * Indicates next solution is better than the current solution.
     */
    int BETTER = 2;

    /**
     * Indicates next solution should be accepted.
     */
    int ACCEPTED = 1;

    /**
     * Indicates that next solution should be rejected.
     */
    int REJECTED = 0;

    /**
     * Apply the function.
     *
     * @param next        next solution's height
     * @param current     current solution's height
     * @param best        best solution's height
     * @param temperature current temperature
     * @return acceptance level
     */
    int apply(double next, double current, double best, double temperature);
}
