package org.compx556.function;

import org.compx556.Box;
import org.compx556.BoxList;
import org.javatuples.Pair;

import java.util.List;

/**
 * <p>
 * A function to remove some <code>Box</code> elements from the solution to create a partial solution.
 * </p>
 * <p>
 * The expected parameters for a <code>DestructionFunction</code> are a <code>BoxList</code> representing a complete
 * solution and an <code>int</code> representing how many <code>Box</code> objects should be removed.
 * </p>
 * <p>
 * The output should be a <code>Pair</code> containing a <code>BoxList</code> representing the new partial solution
 * after having some <code>Box</code> objects removed and a <code>List&lt;Box&gt;</code> containing the removed
 * <code>Box</code> objects (in order if relevant).
 * </p>
 */
public abstract class DestructionFunction {
    /**
     * Gets the name of the destruction function.
     *
     * @return the name of the destruction function
     */
    public abstract String getName();

    /**
     * Apply the function.
     *
     * @param solution solution to destroy
     * @param n        number of <code>Box</code> elements to remove
     * @return a <code>Pair</code> containing:
     * <ul><li>the new partial solution</li><li>the removed <code>Box</code> elements</li></ul>
     */
    public abstract Pair<BoxList, List<Box>> apply(BoxList solution, int n);
}
