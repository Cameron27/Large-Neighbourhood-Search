package org.compx556;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.converters.FileConverter;
import org.compx556.function.DestructionFunction;
import org.compx556.function.InitialisationFunction;
import org.compx556.function.RepairFunction;

import java.io.File;

public class Config {
    /**
     * Initialisation function to use in algorithm.
     */
    public InitialisationFunction initialisationFunction;

    /**
     * Destruction function to use in algorithm.
     */
    public DestructionFunction destructionFunction;

    /**
     * Repair function to use in algorithm.
     */
    public RepairFunction repairFunction;

    /**
     * File to read data from.
     */
    @Parameter(description = "data", required = true, converter = FileConverter.class)
    public File dataFile;

    /**
     * File to save result image to.
     */
    @Parameter(names = {"-o", "-output"}, description = "Name of file to save result image as", converter = FileConverter.class)
    public File outFile;

    /**
     * Seed to use for randomisation.
     */
    @Parameter(names = {"-s", "-seed"}, description = "Seed to use for randomness")
    public Integer seed;

    /**
     * Number of threads to use.
     */
    @Parameter(names = {"-t", "-threads"}, description = "Number of threads to use")
    public Integer threadCount = Runtime.getRuntime().availableProcessors();

    /**
     * Whether or not help be displayed.
     */
    @Parameter(names = {"-h", "-help"}, help = true, hidden = true)
    public boolean help;

    /**
     * Created a <code>Config</code> with the specified functions.
     *
     * @param initialisationFunction the initialisation function
     * @param destructionFunction    the destruction function
     * @param repairFunction         the repair function
     */
    public Config(InitialisationFunction initialisationFunction, DestructionFunction destructionFunction, RepairFunction repairFunction) {
        this.initialisationFunction = initialisationFunction;
        this.destructionFunction = destructionFunction;
        this.repairFunction = repairFunction;
    }
}
