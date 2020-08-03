package org.compx556.function;

import org.compx556.Box;
import org.compx556.BoxList;
import org.javatuples.Pair;

import java.util.List;

/**
 * <p>
 * A function to add missing <code>Box</code> elements to a partial solution.
 * </p>
 * <p>
 * The expected parameters for a <code>RepairFunction</code> include a <code>Pair</code> containing a
 * <code>BoxList</code> representing the a partial solution and a <code>List&lt;Box&gt;</code> containing the missing
 * <code>Box</code> objects from the partial solution. The other parameter should be an <code>Integer</code>
 * representing the number of threads to use if the function supports multi threading.
 * </p>
 * <p>
 * The output should be a <code>BoxList</code> representing the new complete solution from having the missing
 * <code>Box</code> objects inserted into the solution.
 * </p>
 */
@FunctionalInterface
public interface RepairFunction {
    /**
     * Apply the function.
     *
     * @param boxesPair a <code>Pair</code> containing:
     *                  <ul><li>the new partial solution</li><li>the removed <code>Box</code> elements</li></ul>
     * @param threads   number of thread to use to run the function
     * @return the new solution
     */
    BoxList apply(Pair<BoxList, List<Box>> boxesPair, int threads);
}
