package org.compx556.function;

import org.compx556.Box;
import org.compx556.BoxList;
import org.javatuples.Pair;

import java.util.List;
import java.util.function.BiFunction;

/**
 * <p>
 * An interface extending <code>BiFunction&lt;Pair&lt;BoxList, List&lt;Box&gt;&gt;, Integer, BoxList&gt;</code>.
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
public interface RepairFunction extends BiFunction<Pair<BoxList, List<Box>>, Integer, BoxList> {
}