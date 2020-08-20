package org.compx556.function;

import org.compx556.Rectangle;
import org.compx556.Solution;
import org.javatuples.Pair;

import java.util.List;

/**
 * <p>
 * A function to add missing <code>Rectangle</code> elements to a partial solution.
 * </p>
 * <p>
 * The expected parameters for a <code>RepairFunction</code> include a <code>Pair</code> containing a
 * <code>Solution</code> representing the a partial solution and a <code>List&lt;Rectangle&gt;</code> containing the missing
 * <code>Rectangle</code> objects from the partial solution. The other parameter should be an <code>Integer</code>
 * representing the number of threads to use if the function supports multi threading.
 * </p>
 * <p>
 * The output should be a <code>Solution</code> representing the new complete solution from having the missing
 * <code>Rectangle</code> objects inserted into the solution.
 * </p>
 */
public abstract class RepairFunction {
    /**
     * Gets the name of the repair function.
     *
     * @return the name of the repair function
     */
    public abstract String getName();

    /**
     * Apply the function.
     *
     * @param rectanglesPair a <code>Pair</code> containing:
     *                       <ul><li>the new partial solution</li><li>the removed <code>Rectangle</code> elements</li></ul>
     * @param threadCount    number of thread to use to run the function
     * @return the new solution
     */
    protected abstract Solution applyPartial(Pair<Solution, List<Rectangle>> rectanglesPair, int threadCount);

    /**
     * Apply the function.
     *
     * @param rectanglesPair a <code>Pair</code> containing:
     *                       <ul><li>the new partial solution</li><li>the removed <code>Rectangle</code> elements</li></ul>
     * @param threadCount    number of thread to use to run the function
     * @return the new solution
     */
    public Solution apply(Pair<Solution, List<Rectangle>> rectanglesPair, int threadCount) {
        // start timing
        threadCount = startThreadOverriding(threadCount);

        // run function
        Solution result = applyPartial(rectanglesPair, threadCount);

        // end timing
        endThreadOverriding();

        return result;
    }

    /**
     * Time taken to run repair function in single threaded mode.
     */
    private long singleThreadScore = -1;

    /**
     * Time taken to run repair function in multithreaded mode.
     */
    private long multiThreadScore = -1;

    /**
     * Used by <code>startThreadOverriding()</code> to store the original thread count for
     * <code>endThreadOverriding()</code>
     */
    private int originalThreadCount;

    /**
     * Used by <code>startThreadOverriding()</code> to store the overridden thread count for
     * <code>endThreadOverriding()</code>
     */
    private int overriddenThreadCount;

    /**
     * Used by <code>startThreadOverriding()</code> to store the start time for <code>endThreadOverriding()</code>
     */
    private long startTime;

    /**
     * Potentially overrides the given thread count by setting it to 1 if it is more than 1.
     * This function will suggest both single threaded and multithreaded the first two times it is called with a value
     * over 1, timing how long it takes for <code>endThreadOverriding()</code> to be called.
     * After these initial tests 1 will be returned every time if it turned out the repair function performed better on
     * a single thread.
     *
     * @param threadCount the given thread count
     * @return the thread count that should be used
     */
    protected int startThreadOverriding(int threadCount) {
        originalThreadCount = threadCount;

        // do nothing if thread count is 1
        if (threadCount == 1) return threadCount;

        // use one thread if it has not been tested or if it has been proven to be better
        if (singleThreadScore == -1 || singleThreadScore < multiThreadScore) overriddenThreadCount = 1;
        else overriddenThreadCount = threadCount;

        startTime = System.nanoTime();
        return overriddenThreadCount;
    }

    /**
     * Records the time the repair function took to run either single threaded or multithreaded if it was the first
     * time.
     */
    protected void endThreadOverriding() {
        // calculate score i.e. time taken
        long score = System.nanoTime() - startTime;

        // do nothing if original thread count is 1
        if (originalThreadCount == 1) return;

        // set single thread score if overridden count is 1 and it is not set
        if (overriddenThreadCount == 1 && singleThreadScore == -1) {
            singleThreadScore = score;
        }
        // set multi thread score if overridden count is not 1 and it is not set
        else if (overriddenThreadCount != 1 && multiThreadScore == -1) {
            multiThreadScore = score;
        }
    }

    /**
     * Resets the scores for single threaded and multithreaded modes.
     */
    public void resetThreadOverriding() {
        singleThreadScore = -1;
        multiThreadScore = -1;
    }
}
