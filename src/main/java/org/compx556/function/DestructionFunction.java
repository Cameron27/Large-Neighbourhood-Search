package org.compx556.function;

import org.compx556.Box;
import org.compx556.BoxList;
import org.javatuples.Pair;

import java.util.List;
import java.util.function.BiFunction;

/**
 * <p>
 * An interface extending <code>BiFunction&lt;BoxList, Integer, Pair&lt;BoxList, List&lt;Box&gt;&gt&gt;</code>.
 * </p>
 * <p>
 * The expected parameters for a <code>DestructionFunction</code> are a <code>BoxList</code> representing a complete
 * solution and an <code>Integer</code> representing how many <code>Box</code> objects should be removed.
 * </p>
 * <p>
 * The output should be a <code>Pair</code> containing a <code>BoxList</code> representing the new partial solution
 * after having some <code>Box</code> objects removed and a <code>List&lt;Box&gt;</code> containing the removed
 * <code>Box</code> objects (in order if relevant).
 * </p>
 */
public interface DestructionFunction extends BiFunction<BoxList, Integer, Pair<BoxList, List<Box>>> {
}
