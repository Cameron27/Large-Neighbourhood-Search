package org.compx556.function;

import org.compx556.Box;
import org.compx556.BoxList;
import org.javatuples.Pair;

import java.util.List;
import java.util.function.Function;

public interface RepairFunction extends Function<Pair<BoxList, List<Box>>, BoxList> {
}
