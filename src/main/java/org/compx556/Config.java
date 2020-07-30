package org.compx556;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.converters.FileConverter;
import org.compx556.function.DestructionFunction;
import org.compx556.function.InitialisationFunction;
import org.compx556.function.RepairFunction;

import java.io.File;

public class Config {
    public InitialisationFunction initialisationFunction;
    public DestructionFunction destructionFunction;
    public RepairFunction repairFunction;

    @Parameter(description = "data", required = true, converter = FileConverter.class)
    public File dataFile;

    @Parameter(names = {"-o", "-output"}, description = "Name of file to save result image as", converter = FileConverter.class)
    public File outFile;

    @Parameter(names = {"-s", "-seed"}, description = "Seed to use for randomness")
    public Integer seed;

    @Parameter(names = {"-h", "-help"}, help = true, hidden = true)
    public boolean help;

    public Config(InitialisationFunction initialisationFunction, DestructionFunction destructionFunction, RepairFunction repairFunction) {
        this.initialisationFunction = initialisationFunction;
        this.destructionFunction = destructionFunction;
        this.repairFunction = repairFunction;
    }
}
