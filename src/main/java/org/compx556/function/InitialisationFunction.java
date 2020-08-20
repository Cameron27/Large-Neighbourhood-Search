package org.compx556.function;

import org.compx556.Solution;

/**
 * <p>
 * A function to generate an initial solution.
 * </p>
 * <p>
 * The expected parameter for a <code>InitialisationFunction</code> is a <code>Solution</code>> representing a complete solution.</p>
 * <p>
 * The output should be a <code>Solution</code> representing the new complete solution after reshuffling it in some way.
 * </p>
 */
public abstract class InitialisationFunction {
    /**
     * Gets the name of the initialisation function.
     *
     * @return the name of the initialisation function
     */
    public abstract String getName();

    /**
     * Apply the function.
     *
     * @param solution a <code>Solution</code> containing the <code>Rectangle</code> elements to generate the initial solution
     *                 from
     * @return the initial solution
     */
    public abstract Solution apply(Solution solution);
}
