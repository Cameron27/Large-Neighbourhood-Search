package org.compx556.function;

import org.compx556.BoxList;
import org.compx556.util.GlobalRandom;

import java.util.Collections;
import java.util.function.Function;

public interface InitialisationFunction extends Function<BoxList, BoxList> {
    InitialisationFunction random = list -> {
        // create new list with random order
        BoxList output = list.clone();
        Collections.shuffle(output);

        // rotate boxes randomly
        for (int i = 0; i < output.size(); i++) {
            if (GlobalRandom.nextBoolean()) {
                // clone box if it is going to be rotated
                output.set(i, output.get(i).clone());
                output.get(i).rotate();
            }
        }

        return output;
    };
}
