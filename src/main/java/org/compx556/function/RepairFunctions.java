package org.compx556.function;

import org.compx556.Box;
import org.compx556.BoxList;
import org.compx556.util.GlobalRandom;
import org.javatuples.Quartet;
import org.javatuples.Triplet;
import org.javatuples.Tuple;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
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
    public static final RepairFunction randomLocationOptimumX = (input, threadCount) -> {
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
                            .min(Tuple::compareTo)
                            .orElseThrow(NoSuchElementException::new);
                } catch (InterruptedException | NoSuchElementException e) {
                    throw new RuntimeException("Error in randomLocationOptimumX thread.");
                }
                // extract values
                bestX = best.getValue2();
                bestRotation = best.getValue1();
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

    /**
     * Created a <code>Callable</code> to use as a thread that finds the best location for a <code>Box</code> to be
     * inserted into a partial solution at a specific index for a subset of valid x locations.
     *
     * @param boxList        current partial solution
     * @param boxToAdd       <code>Box</code> to insert
     * @param lowerBound     lower bound of x values to check (inclusive)
     * @param upperBound     upper bound of x values to check (exclusive)
     * @param insertionIndex index to insert box into
     * @return a <code>Triplet</code> containing:
     * <ul>
     * <li>the height of the best solution found</li>
     * <li>the rotation of the <code>Box</code> for the best solution found</li>
     * <li>the x location of the <code>Box</code> for the best solution found</li>
     * </ul>
     */
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

            return new Triplet<>(bestScore, bestRotation, bestX);
        };
    }

    /**
     * Inserts each <code>Box</code> object into the best position to minimises the height of the solution. Insertions
     * are performed in a random order in a greedy fashion.
     */
    public static final RepairFunction optimumLocationOptimumX = (input, threadCount) -> {
        ExecutorService executor = null;

        BoxList list = input.getValue0().clone();
        List<Box> missing = new ArrayList<>(input.getValue1());
        Collections.shuffle(missing, GlobalRandom.getRnd());

        for (Box box : missing) {
            box = box.clone();

            int bestX = 0;
            int bestRotation = 0;
            int bestIndex = 0;


            // multi thread mode
            if (threadCount > 1) {
                // setup executor
                if (executor == null) executor = Executors.newFixedThreadPool(threadCount);

                List<Callable<Quartet<Integer, Integer, Integer, Integer>>> threadList = new ArrayList<>();
                int count = list.size() + 1;
                for (int i = 0; i < threadCount; i++) {
                    threadList.add(optimumLocationOptimumXThread(list, box, (int) (((float) i / threadCount) * count),
                            (int) (((float) (i + 1) / threadCount) * count)));
                }

                Quartet<Integer, Integer, Integer, Integer> best;
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
                            .min(Tuple::compareTo)
                            .orElseThrow(NoSuchElementException::new);
                } catch (InterruptedException | NoSuchElementException e) {
                    throw new RuntimeException("Error in randomLocationOptimumX thread.");
                }
                // extract values
                bestX = best.getValue3();
                bestRotation = best.getValue2();
                bestIndex = best.getValue1();
            }
            // single thread mode
            else {
                int bestScore = Integer.MAX_VALUE;

                // for every location
                int listSize = list.size();
                for (int insertionIndex = 0; insertionIndex < listSize; insertionIndex++) {
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
                                bestIndex = insertionIndex;
                            }
                        }

                        // rotate
                        list.get(insertionIndex).rotate();
                    }

                    list.remove(insertionIndex);
                }
            }
            // set to best x
            list.add(bestIndex, box);
            list.get(bestIndex).setXStart(bestX);
            // if second rotation is best, rotate
            if (bestRotation == 1) list.get(bestIndex).rotate();
        }

        if (executor != null) executor.shutdown();

        return list;
    };

    /**
     * Created a <code>Callable</code> to use as a thread that finds the best location for a <code>Box</code> to be
     * inserted into for a subset of indices.
     *
     * @param boxList    current partial solution
     * @param boxToAdd   <code>Box</code> to insert
     * @param lowerBound lower bound of indices to check (inclusive)
     * @param upperBound upper bound of indices to check (exclusive)
     * @return a <code>Quartet</code> containing:
     * <ul>
     * <li>the height of the best solution found</li>
     * <li>index of the <code>Box</code> for the best solution found</li>
     * <li>the rotation of the <code>Box</code> for the best solution found</li>
     * <li>the x location of the <code>Box</code> for the best solution found</li>
     * </ul>
     */
    private static Callable<Quartet<Integer, Integer, Integer, Integer>> optimumLocationOptimumXThread(BoxList boxList, Box boxToAdd, int lowerBound, int upperBound) {
        // clones objects so each thread has a copy
        final BoxList list = boxList.clone();
        final Box box = boxToAdd.clone();

        return () -> {
            int bestScore = Integer.MAX_VALUE;
            int bestX = 0;
            int bestRotation = 0;
            int bestIndex = 0;

            // for every location
            for (int insertionIndex = lowerBound; insertionIndex < upperBound; insertionIndex++) {
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
                            bestIndex = insertionIndex;
                        }
                    }

                    // rotate
                    list.get(insertionIndex).rotate();
                }

                list.remove(insertionIndex);
            }

            return new Quartet<>(bestScore, bestIndex, bestRotation, bestX);
        };
    }
}

