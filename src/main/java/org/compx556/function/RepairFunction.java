package org.compx556.function;

import org.compx556.Box;
import org.compx556.BoxList;
import org.compx556.util.GlobalRandom;
import org.javatuples.Pair;

import java.util.List;
import java.util.function.Function;

public interface RepairFunction extends Function<Pair<BoxList, List<Box>>, BoxList> {
    RepairFunction randomLocationOptimumX = input -> {
        BoxList list = input.getValue0().clone();
        List<Box> missing = input.getValue1();

        for (Box box : missing) {
            box = box.clone();

            // pick random location to insert
            int insertionIndex = GlobalRandom.nextInt(list.size() + 1);

            int bestScore = Integer.MAX_VALUE;
            int bestX = 0;
            int bestRotation = 0;
            list.add(insertionIndex, box);

            // for both rotations
            for (int rotation = 0; rotation < 2; rotation++) {
                //for each valid x location
                for (int x = 0; x <= list.getObjectSize() - box.getWidth(); x++) {
                    // move x location
                    list.get(insertionIndex).setXStart(x);

                    int score = list.calculateHeight();
                    if (score < bestScore) {
                        bestScore = score;
                        bestX = x;
                        bestRotation = rotation;
                    }
                }

                // rotate
                list.get(insertionIndex).rotate();
            }

            // set to best x
            list.get(insertionIndex).setXStart(bestX);
            // if second rotation is best, rotate
            if (bestRotation == 1) list.get(insertionIndex).rotate();
        }

        return list;
    };
}
