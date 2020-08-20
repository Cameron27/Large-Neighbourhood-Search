// CameronSalisbury_1293897_SivaramManoharan_1299026

package org.compx556.function;

import org.compx556.Rectangle;
import org.compx556.Solution;
import org.compx556.util.GlobalRandom;
import org.javatuples.Pair;
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
     * Inserts each <code>Rectangle</code> object into a random location in the solution but will find the x value and
     * rotation for the mox that minimises the height of the solution. Insertions are performed in a random order in a
     * greedy fashion.
     */
    public static final RepairFunction randomLocationOptimumX = new RepairFunction() {
        @Override
        public String getName() {
            return "Random Location, Optimum X";
        }

        @Override
        public Solution applyPartial(Pair<Solution, List<Rectangle>> rectanglesPair, int threadCount) {
            ExecutorService executor = null;

            Solution list = rectanglesPair.getValue0().clone();
            List<Rectangle> missing = new ArrayList<>(rectanglesPair.getValue1());
            Collections.shuffle(missing, GlobalRandom.getRnd());

            for (Rectangle rectangle : missing) {
                rectangle = rectangle.clone();

                // pick random location to insert
                int insertionIndex = GlobalRandom.nextInt(list.size() + 1);

                Triplet<Double, Integer, Integer> best;
                int globalUpperBound = list.getObjectSize() - Math.min(rectangle.getWidth(), rectangle.getHeight()) + 1;

                // multi thread mode
                if (threadCount > 1) {
                    // setup executor
                    if (executor == null) executor = Executors.newFixedThreadPool(threadCount);

                    List<Callable<Triplet<Double, Integer, Integer>>> threadList = new ArrayList<>();
                    for (int i = 0; i < threadCount; i++) {
                        threadList.add(randomLocationOptimumXThread(list, rectangle, (int) (((float) i / threadCount) * globalUpperBound),
                                (int) (((float) (i + 1) / threadCount) * globalUpperBound), insertionIndex));
                    }

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
                }
                // single thread mode
                else {
                    try {
                        best = randomLocationOptimumXThread(list, rectangle, 0, globalUpperBound, insertionIndex).call();
                    } catch (Exception e) {
                        throw new RuntimeException("Error in randomLocationOptimumX single thread.");
                    }
                }
                // extract values
                int bestX = best.getValue2();
                int bestRotation = best.getValue1();

                // set to best x
                list.add(insertionIndex, rectangle);
                list.get(insertionIndex).setXStart(bestX);
                // if second rotation is best, rotate
                if (bestRotation == 1) list.get(insertionIndex).rotate();
            }

            if (executor != null) executor.shutdown();

            return list;
        }

        /**
         * Created a <code>Callable</code> to use as a thread that finds the best location for a <code>Rectangle</code> to be
         * inserted into a partial solution at a specific index for a subset of valid x locations.
         *
         * @param solution        current partial solution
         * @param rectangleToAdd       <code>Rectangle</code> to insert
         * @param lowerBound     lower bound of x values to check (inclusive)
         * @param upperBound     upper bound of x values to check (exclusive)
         * @param insertionIndex index to insert rectangle into
         * @return a <code>Quartet</code> containing:
         * <ul>
         * <li>the height of the best solution found</li>
         * <li>index of the <code>Rectangle</code> for the best solution found</li>
         * <li>the rotation of the <code>Rectangle</code> for the best solution found</li>
         * <li>the x location of the <code>Rectangle</code> for the best solution found</li>
         * </ul>
         */
        private Callable<Triplet<Double, Integer, Integer>> randomLocationOptimumXThread(Solution solution, Rectangle rectangleToAdd, int lowerBound, int upperBound, int insertionIndex) {
            // clones objects so each thread has a copy
            final Solution list = solution.clone();
            final Rectangle rectangle = rectangleToAdd.clone();

            return () -> {
                double bestScore = Double.POSITIVE_INFINITY;
                int bestX = 0;
                int bestRotation = 0;
                list.add(insertionIndex, rectangle);

                // for both rotations
                for (int rotation = 0; rotation < 2; rotation++) {
                    //for each valid x location
                    for (int x = lowerBound; x < Math.min(upperBound, list.getObjectSize() - rectangle.getWidth() + 1); x++) {
                        // move x location
                        list.get(insertionIndex).setXStart(x);

                        double score = list.calculateHeight(true);
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
    };


    /**
     * Inserts each <code>Box</code> object into the best position to minimises the height of the solution. Insertions
     * are performed in a random order in a greedy fashion.
     */
    public static final RepairFunction optimumLocationOptimumX = new RepairFunction() {
        @Override
        public String getName() {
            return "Optimum Location, Optimum X";
        }

        @Override
        public Solution applyPartial(Pair<Solution, List<Rectangle>> rectanglesPair, int threadCount) {
            ExecutorService executor = null;

            Solution list = rectanglesPair.getValue0().clone();
            List<Rectangle> missing = new ArrayList<>(rectanglesPair.getValue1());
            Collections.shuffle(missing, GlobalRandom.getRnd());

            for (Rectangle rectangle : missing) {
                rectangle = rectangle.clone();

                Quartet<Double, Integer, Integer, Integer> best;
                int globalUpperBound = list.size() + 1;

                // multi thread mode
                if (threadCount > 1) {
                    // setup executor
                    if (executor == null) executor = Executors.newFixedThreadPool(threadCount);

                    List<Callable<Quartet<Double, Integer, Integer, Integer>>> threadList = new ArrayList<>();
                    for (int i = 0; i < threadCount; i++) {
                        threadList.add(optimumLocationOptimumXThread(list, rectangle, (int) (((float) i / threadCount) * globalUpperBound),
                                (int) (((float) (i + 1) / threadCount) * globalUpperBound)));
                    }

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
                }
                // single thread mode
                else {
                    // create single callable for whole range and run it
                    try {
                        best = optimumLocationOptimumXThread(list, rectangle, 0, globalUpperBound).call();
                    } catch (Exception e) {
                        throw new RuntimeException("Error in randomLocationOptimumX single thread.");
                    }
                }
                // extract values
                int bestX = best.getValue3();
                int bestRotation = best.getValue2();
                int bestIndex = best.getValue1();

                // set to best x
                list.add(bestIndex, rectangle);
                list.get(bestIndex).setXStart(bestX);
                // if second rotation is best, rotate
                if (bestRotation == 1) list.get(bestIndex).rotate();
            }

            if (executor != null) executor.shutdown();

            return list;
        }

        /**
         * Created a <code>Callable</code> to use as a thread that finds the best location for a <code>Rectangle</code> to be
         * inserted into for a subset of indices.
         *
         * @param solution    current partial solution
         * @param rectangleToAdd   <code>Rectangle</code> to insert
         * @param lowerBound lower bound of indices to check (inclusive)
         * @param upperBound upper bound of indices to check (exclusive)
         * @return a <code>Quartet</code> containing:
         * <ul>
         * <li>the height of the best solution found</li>
         * <li>index of the <code>Rectangle</code> for the best solution found</li>
         * <li>the rotation of the <code>Rectangle</code> for the best solution found</li>
         * <li>the x location of the <code>Rectangle</code> for the best solution found</li>
         * </ul>
         */
        private Callable<Quartet<Double, Integer, Integer, Integer>> optimumLocationOptimumXThread(Solution solution, Rectangle rectangleToAdd, int lowerBound, int upperBound) {
            // clones objects so each thread has a copy
            final Solution list = solution.clone();
            final Rectangle rectangle = rectangleToAdd.clone();

            return () -> {
                double bestScore = Double.POSITIVE_INFINITY;
                int bestX = 0;
                int bestRotation = 0;
                int bestIndex = 0;

                // for every location
                for (int insertionIndex = lowerBound; insertionIndex < upperBound; insertionIndex++) {
                    list.add(insertionIndex, rectangle);

                    // for both rotations
                    for (int rotation = 0; rotation < 2; rotation++) {
                        //for each valid x location
                        for (int x = 0; x <= list.getObjectSize() - rectangle.getWidth(); x++) {
                            // move x location
                            list.get(insertionIndex).setXStart(x);

                            double score = list.calculateHeight(true);
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
    };

    /**
     * Inserts all <code>Rectangle</code> objects as a contiguous set into the best position to minimises the height of the
     * solution. The x values, rotation and order of the <code>Rectangle</code> objects will not be changed
     */
    public static RepairFunction optimumBlockLocation = new RepairFunction() {
        @Override
        public String getName() {
            return "Optimum Block Location";
        }

        @Override
        protected Solution applyPartial(Pair<Solution, List<Rectangle>> rectanglesPair, int threadCount) {
            Solution list = rectanglesPair.getValue0().clone();
            List<Rectangle> missing = new ArrayList<>(rectanglesPair.getValue1());

            double bestHeight = Double.POSITIVE_INFINITY;
            int bestIndex = 0;
            for (int startIndex = 0; startIndex <= list.size(); startIndex++) {
                for (int i = 0; i < missing.size(); i++) {
                    Rectangle rectangle = missing.get(i);

                    list.add(startIndex + i, rectangle);
                }

                double height = list.calculateHeight(true);
                if (height < bestHeight) {
                    bestHeight = height;
                    bestIndex = startIndex;
                }

                for (int i = 0; i < missing.size(); i++) {
                    list.remove(startIndex);
                }
            }

            for (int i = 0; i < missing.size(); i++) {
                Rectangle rectangle = missing.get(i);

                list.add(bestIndex + i, rectangle);
            }

            return list;
        }
    };
}

