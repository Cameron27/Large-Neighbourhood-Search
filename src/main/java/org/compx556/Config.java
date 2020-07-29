package org.compx556;

import org.compx556.function.DestructionFunction;
import org.compx556.function.InitialisationFunction;
import org.compx556.function.RepairFunction;

public class Config {
    public InitialisationFunction initialisationFunction;

    public DestructionFunction destructionFunction;

    public RepairFunction repairFunction;

    public Config(InitialisationFunction initialisationFunction, DestructionFunction destructionFunction, RepairFunction repairFunction) {
        this.initialisationFunction = initialisationFunction;
        this.destructionFunction = destructionFunction;
        this.repairFunction = repairFunction;
    }
}
