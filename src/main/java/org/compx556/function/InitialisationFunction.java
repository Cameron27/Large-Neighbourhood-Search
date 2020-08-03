package org.compx556.function;

import org.compx556.BoxList;

/**
 * <p>
 * A function to generate an initial solution.
 * </p>
 * <p>
 * The expected parameter for a <code>InitialisationFunction</code> is a BoxList representing a complete solution.</p>
 * <p>
 * The output should be a <code>BoxList</code> representing the new complete solution after reshuffling it in some way.
 * </p>
 */
@FunctionalInterface
public interface InitialisationFunction {
    /**
     * Apply the function.
     *
     * @param boxList a <code>BoxList</code> containing the <code>Box</code>> elements to generate the initial solution
     *                from
     * @return the initial solution
     */
    BoxList apply(BoxList boxList);
}
