// CameronSalisbury_1293897_SivaramManoharan_1299026

package org.compx556;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.converters.FileConverter;
import org.compx556.function.AcceptanceFunction;
import org.compx556.function.DestructionFunction;
import org.compx556.function.InitialisationFunction;
import org.compx556.function.RepairFunction;

import java.io.File;

public class Config implements Cloneable {
    /**
     * Acceptance function to use in algorithm.
     */
    public AcceptanceFunction acceptanceFunction;

    /**
     * Initialisation function to use in algorithm.
     */
    public InitialisationFunction initialisationFunction;

    /**
     * Destruction functions available to use in algorithm.
     */
    public DestructionFunction[] destructionFunctions;

    /**
     * Repair functions available to use in algorithm.
     */
    public RepairFunction[] repairFunctions;

    /**
     * Pairs of indices for valid destroy and repair function combinations.
     */
    public int[][] destroyRepairPairs;

    /**
     * The proportion of elements that should be removed during each destruction stage, 0 is remove none, 1 is remove
     * everything.
     */
    public double destructionProportion;

    /**
     * The parameter used to generate the initial temperature.
     */
    public double initialTemperatureParameter;

    /**
     * File to read data from.
     */
    @Parameter(description = "data", required = true, converter = FileConverter.class)
    public File dataFile;

    /**
     * File to save result image to.
     */
    @Parameter(names = {"-o", "-output"}, description = "Name of file to save result image as, image will not be " +
            "saved if not set", converter = FileConverter.class)
    public File outFile;

    /**
     * Seed to use for randomisation.
     */
    @Parameter(names = {"-s", "-seed"}, description = "Seed to use for randomness")
    public Long seed;

    /**
     * Number of threads to use.
     */
    @Parameter(names = {"-threads"}, description = "Number of threads to use")
    public Integer threadCount = Runtime.getRuntime().availableProcessors();

    /**
     * Time to run algorithm for.
     */
    @Parameter(names = {"-t", "-time"}, description = "Max runtime in milliseconds")
    public long runtime = 10000;

    @Parameter(names = {"-stats"}, hidden = true, description = "Whether or not adaptive stats should be printed")
    public boolean printStats = false;

    /**
     * Whether or not help be displayed.
     */
    @Parameter(names = {"-h", "-help"}, help = true, hidden = true)
    public boolean help;

    /**
     * Created a <code>Config</code> with the specified functions.
     *
     * @param initialisationFunction the initialisation function
     * @param destructionFunctions   the destruction functions available
     * @param repairFunctions        the repairs functions available
     * @param acceptanceFunction     the acceptance function
     * @param destroyRepairPairs     the indices of valid destroy and repair function combinations
     */
    public Config(AcceptanceFunction acceptanceFunction, InitialisationFunction initialisationFunction,
                  DestructionFunction[] destructionFunctions, RepairFunction[] repairFunctions,
                  int[][] destroyRepairPairs, double destructionProportion, double initialTemperatureParameter) {
        this.acceptanceFunction = acceptanceFunction;
        this.initialisationFunction = initialisationFunction;
        this.destructionFunctions = destructionFunctions;
        this.repairFunctions = repairFunctions;
        this.destroyRepairPairs = destroyRepairPairs;
        this.destructionProportion = destructionProportion;
        this.initialTemperatureParameter = initialTemperatureParameter;
    }

    @Override
    public Config clone() {
        Config clone = new Config(acceptanceFunction,
                initialisationFunction,
                destructionFunctions,
                repairFunctions,
                destroyRepairPairs,
                destructionProportion,
                initialTemperatureParameter);
        clone.dataFile = dataFile;
        clone.outFile = outFile;
        clone.seed = seed;
        clone.threadCount = threadCount;
        clone.runtime = runtime;
        clone.printStats = printStats;
        clone.help = help;

        return clone;
    }
}
