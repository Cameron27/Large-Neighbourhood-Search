// CameronSalisbury_1293897_SivaramManoharan_1299026

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
public abstract class AcceptanceFunction {
    /**
     * Indicates next solution is better than the best solution.
     */
    public static int BEST = 3;

    /**
     * Indicates next solution is better than the current solution.
     */
    public static int BETTER = 2;

    /**
     * Indicates next solution should be accepted.
     */
    public static int ACCEPTED = 1;

    /**
     * Indicates that next solution should be rejected.
     */
    public static int REJECTED = 0;

    /**
     * Gets the name of the acceptance function.
     *
     * @return the name of the acceptance function
     */
    public abstract String getName();

    /**
     * Apply the function.
     *
     * @param next        next solution's height
     * @param current     current solution's height
     * @param best        best solution's height
     * @param temperature current temperature
     * @return acceptance level
     */
    public abstract int apply(double next, double current, double best, double temperature);

    /**
     * Calculate the starting temperature for this acceptance function.
     *
     * @param initialScore the score of the initial randomly generated solution
     * @param h            base threshold, the meaning of this depends on the acceptance function.
     * @return initial temperature
     */
    public abstract double initialTemperature(double initialScore, double h);
}
