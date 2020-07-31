package org.compx556.function;

import org.compx556.Box;
import org.compx556.BoxList;
import org.javatuples.Pair;

import java.util.List;
import java.util.function.BiFunction;

public interface RepairFunction extends BiFunction<Pair<BoxList, List<Box>>, Integer, BoxList> {
}