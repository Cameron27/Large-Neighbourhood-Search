package org.compx556.function;

import org.compx556.Box;
import org.compx556.BoxList;
import org.compx556.util.GlobalRandom;
import org.javatuples.Pair;

import java.util.ArrayList;
import java.util.List;

public class DestructionFunctions {
    /**
     * Removes a number of <code>Box</code> objects randomly from the solution.
     */
    public static final DestructionFunction randomNRemove = new DestructionFunction() {
        @Override
        public String getName() {
            return "Random n Remove";
        }

        @Override
        public Pair<BoxList, List<Box>> apply(BoxList solution, int n) {
            // create new lists
            BoxList remaining = solution.clone();
            List<Box> removed = new ArrayList<>();

            // randomly remove n boxes
            for (int i = 0; i < n; i++) {
                removed.add(remaining.remove(GlobalRandom.nextInt(remaining.size())));
            }

            return new Pair<>(remaining, removed);
        }
    };

    /**
     * Removes a contiguous set of <code>Box</code> objects from the solution.
     */
    public static final DestructionFunction blockNRemove = new DestructionFunction() {
        @Override
        public String getName() {
            return "Block n Remove";
        }

        @Override
        public Pair<BoxList, List<Box>> apply(BoxList solution, int n) {
            // create new lists
            BoxList remaining = solution.clone();
            List<Box> removed = new ArrayList<>();

            int removeIndex = GlobalRandom.nextInt(remaining.size() - n + 1);

            // randomly remove n boxes
            for (int i = 0; i < n; i++) {
                removed.add(remaining.remove(removeIndex));
            }

            return new Pair<>(remaining, removed);
        }
    };
}
