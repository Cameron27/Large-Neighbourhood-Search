// CameronSalisbury_1293897_SivaramManoharan_1299026

package org.compx556.function;

import org.compx556.Rectangle;
import org.compx556.Solution;
import org.compx556.util.GlobalRandom;
import org.javatuples.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InitialisationFunctions {
    /**
     * Randomises the order of the <code>Rectangle</code> objects and sets every <code>Rectangle</code> to have a random x value and
     * Randomises the order of the <code>Rectangle</code> objects and sets every <code>Rectangle</code> to have a random x value and
     * rotation.
     */
    public static final InitialisationFunction random = new InitialisationFunction() {
        @Override
        public String getName() {
            return "Random";
        }

        @Override
        public Solution apply(Solution solution) {
            // create new list with random order
            Solution output = solution.clone();
            Collections.shuffle(output);

            // rotate rectangle randomly
            for (int i = 0; i < output.size(); i++) {
                Rectangle b = solution.get(i).clone();
                if (GlobalRandom.nextBoolean()) {
                    // clone rectangle if it is going to be rotated
                    b.rotate();
                }
                b.setXStart(GlobalRandom.nextInt(solution.getObjectSize() - b.getWidth() + 1));

                output.set(i, b);
            }

            return output;
        }
    };

    /**
     * Inserts each <code>Rectangle</code> element into the solution using the <code>optimumLocationOptimumX</code> repair
     * function.
     */
    public static final InitialisationFunction greedy = new InitialisationFunction() {
        @Override
        public String getName() {
            return "Greedy";
        }

        @Override
        public Solution apply(Solution solution) {
            Solution output = solution.clone();
            output.clear();
            List<Rectangle> missing = new ArrayList<>(solution);

            return RepairFunctions.optimumLocationOptimumX.apply(new Pair<>(output, missing), 1);
        }
    };
}
