package org.compx556.function;

import org.javatuples.Triplet;

import java.util.function.BiFunction;

public interface AcceptanceFunction extends BiFunction<Triplet<Integer, Integer, Integer>, Double, Integer> {
    int BEST = 4;
    int BETTER = 3;
    int ACCEPTED = 2;
    int REJECTED = 1;

    @Override
    Integer apply(Triplet<Integer, Integer, Integer> nextCurrentBest, Double temperature);
}
