// CameronSalisbury_1293897_SivaramManoharan_1299026

package org.compx556.function;

import org.compx556.Rectangle;
import org.compx556.Solution;
import org.javatuples.Pair;

import java.util.List;

/**
 * <p>
 * A function to remove some <code>Rectangle</code> elements from the solution to create a partial solution.
 * </p>
 * <p>
 * The expected parameters for a <code>DestructionFunction</code> are a <code>Solution</code> representing a complete
 * solution and an <code>int</code> representing how many <code>Rectangle</code> objects should be removed.
 * </p>
 * <p>
 * The output should be a <code>Pair</code> containing a <code>Solution</code> representing the new partial solution
 * after having some <code>Rectangle</code> objects removed and a <code>List&lt;Rectangle&gt;</code> containing the removed
 * <code>Rectangle</code> objects (in order if relevant).
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
     * @param n        number of <code>Rectangle</code> elements to remove
     * @return a <code>Pair</code> containing:
     * <ul><li>the new partial solution</li><li>the removed <code>Rectangle</code> elements</li></ul>
     */
    public abstract Pair<Solution, List<Rectangle>> apply(Solution solution, int n);
}
