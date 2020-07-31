package org.compx556.function;

import org.compx556.Box;
import org.compx556.BoxList;
import org.compx556.util.GlobalRandom;
import org.javatuples.Pair;

import java.util.ArrayList;
import java.util.List;

public class DestructionFunctions {
    public static DestructionFunction randomNRemove = (list, n) -> {
        // create new lists
        BoxList remaining = list.clone();
        List<Box> removed = new ArrayList<>();

        // randomly remove n boxes
        for (int i = 0; i < n; i++) {
            removed.add(remaining.remove(GlobalRandom.nextInt(remaining.size())));
        }

        return new Pair<>(remaining, removed);
    };
}
