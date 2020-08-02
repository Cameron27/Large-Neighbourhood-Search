package org.compx556.function;

import org.compx556.BoxList;

import java.util.function.Function;

/**
 * <p>
 * An interface extending <code>Function&lt;BoxList, BoxList&gt;</code>.
 * </p>
 * <p>
 * The expected parameter for a <code>InitialisationFunction</code> is a BoxList representing a complete solution.</p>
 * <p>
 * The output should be a <code>BoxList</code> representing the new complete solution after reshuffling it in some way.
 * </p>
 */
public interface InitialisationFunction extends Function<BoxList, BoxList> {
    @Override
    BoxList apply(BoxList boxList);
}
