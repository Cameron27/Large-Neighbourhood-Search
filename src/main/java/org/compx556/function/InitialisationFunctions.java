package org.compx556.function;

import org.compx556.Box;
import org.compx556.BoxList;
import org.compx556.util.GlobalRandom;

import java.util.Collections;

/**
 * Contains static <code>InitialisationFunction(s)</code> to use.
 */
public class InitialisationFunctions {
    /**
     * Randomises the order of the <code>Box</code> objects and sets every <code>Box</code> to have a random x value and
     * rotation.
     */
    public static final InitialisationFunction random = list -> {
        // create new list with random order
        BoxList output = list.clone();
        Collections.shuffle(output);

        // rotate boxes randomly
        for (int i = 0; i < output.size(); i++) {
            Box b = list.get(i).clone();
            if (GlobalRandom.nextBoolean()) {
                // clone box if it is going to be rotated
                b.rotate();
            }
            b.setXStart(GlobalRandom.nextInt(list.getObjectSize() - b.getWidth() + 1));

            output.set(i, b);
        }

        return output;
    };
}
