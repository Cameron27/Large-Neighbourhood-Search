package org.compx556;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import org.compx556.function.DestructionFunctions;
import org.compx556.function.InitialisationFunctions;
import org.compx556.function.RepairFunctions;

public class RectanglePacker {
    public static void main(String[] args) {
        Config config = new Config(InitialisationFunctions.random, DestructionFunctions.randomNRemove, RepairFunctions.randomLocationOptimumX);

        JCommander builder = JCommander.newBuilder()
                .addObject(config)
                .build();
        try {
            builder.parse(args);
        } catch (ParameterException e) {
            System.out.println(e.getMessage());
            e.usage();
            return;
        }
        if (config.help) {
            builder.usage();
            return;
        }
    }
}
