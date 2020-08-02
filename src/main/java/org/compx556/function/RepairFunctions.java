package org.compx556.function;

import org.compx556.Box;
import org.compx556.BoxList;
import org.compx556.util.GlobalRandom;
import org.javatuples.Triplet;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RepairFunctions {
    /**
     * Inserts each <code>Box</code> object into a random location in the solution but will find the x value and
     * rotation for the mox that minimises the height of the solution. Insertions are performed in a random order in a
     * greedy fashion.
     */
    public static RepairFunction randomLocationOptimumX = (input, threadCount) -> {
        ExecutorService executor = null;

        BoxList list = input.getValue0().clone();
        List<Box> missing = new ArrayList<>(input.getValue1());
        Collections.shuffle(missing, GlobalRandom.getRnd());

        for (Box box : missing) {
            box = box.clone();

            // pick random location to insert
            int insertionIndex = GlobalRandom.nextInt(list.size() + 1);
            int bestX = 0;
            int bestRotation = 0;

            // multi thread mode
            if (threadCount > 1) {
                // setup executor
                if (executor == null) executor = Executors.newFixedThreadPool(threadCount);

                List<Callable<Triplet<Integer, Integer, Integer>>> threadList = new ArrayList<>();
                int count = list.getObjectSize() - Math.min(box.getWidth(), box.getHeight()) + 1;
                for (int i = 0; i < threadCount; i++) {
                    threadList.add(randomLocationOptimumXThread(list, box, (int) (((float) i / threadCount) * count),
                            (int) (((float) (i + 1) / threadCount) * count), insertionIndex));
                }

                Triplet<Integer, Integer, Integer> best;
                try {
                    // run threads
                    best = executor.invokeAll(threadList)
                            .stream()
                            // get items
                            .map(future -> {
                                try {
                                    return future.get();
                                } catch (InterruptedException | ExecutionException e) {
                                    throw new NoSuchElementException();
                                }
                            })
                            // select best
                            .min(Comparator.comparing(Triplet::getValue0))
                            .orElseThrow(NoSuchElementException::new);
                } catch (InterruptedException | NoSuchElementException e) {
                    throw new RuntimeException("Error in randomLocationOptimumX thread.");
                }
                // extract values
                bestX = best.getValue1();
                bestRotation = best.getValue2();
                list.add(insertionIndex, box);
            }
            // single thread mode
            else {
                int bestScore = Integer.MAX_VALUE;
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
            }
            // set to best x
            list.get(insertionIndex).setXStart(bestX);
            // if second rotation is best, rotate
            if (bestRotation == 1) list.get(insertionIndex).rotate();
        }

        if (executor != null) executor.shutdown();

        return list;
    };

    private static Callable<Triplet<Integer, Integer, Integer>> randomLocationOptimumXThread(BoxList boxList, Box boxToAdd, int lowerBound, int upperBound, int insertionIndex) {
        // clones objects so each thread has a copy
        final BoxList list = boxList.clone();
        final Box box = boxToAdd.clone();

        return () -> {
            int bestScore = Integer.MAX_VALUE;
            int bestX = 0;
            int bestRotation = 0;
            list.add(insertionIndex, box);

            // for both rotations
            for (int rotation = 0; rotation < 2; rotation++) {
                //for each valid x location
                for (int x = lowerBound; x < Math.min(upperBound, list.getObjectSize() - box.getWidth() + 1); x++) {
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

            return new Triplet<>(bestScore, bestX, bestRotation);
        };
    }
}

