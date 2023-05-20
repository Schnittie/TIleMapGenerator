package de.schnittie.model.businesscode;

import java.util.ArrayList;
import java.util.HashMap;

public record PropagationResultLists(ArrayList<Pair> toCollapse, HashMap<Pair, ArrayList<Integer>> toPropagate) {
}
