package org.compx556.function;

import org.compx556.Box;
import org.compx556.BoxList;
import org.compx556.util.GlobalRandom;

import java.util.Collections;

public class InitialisationFunctions {
    /**
     * Randomises the order of the <code>Box</code> objects and sets every <code>Box</code> to have a random x value and
     * rotation.
     */
    public static final InitialisationFunction random = new InitialisationFunction() {
        @Override
        public String getName() {
            return "Random";
        }

        @Override
        public BoxList apply(BoxList solution) {
            // create new list with random order
            BoxList output = solution.clone();
            Collections.shuffle(output);

            // rotate boxes randomly
            for (int i = 0; i < output.size(); i++) {
                Box b = solution.get(i).clone();
                if (GlobalRandom.nextBoolean()) {
                    // clone box if it is going to be rotated
                    b.rotate();
                }
                b.setXStart(GlobalRandom.nextInt(solution.getObjectSize() - b.getWidth() + 1));

                output.set(i, b);
            }

            return output;
        }
    };
}
