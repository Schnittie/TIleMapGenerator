package de.schnittie.model.businesscode.tile;

import java.util.HashMap;
import java.util.HashSet;

public record PossibleAdjacencyHolderForOneTile(HashMap<Integer, HashSet<Integer>> tileIDbyDirection) {
    public HashSet<Integer> getPossibleTilesByDirection(int direction){
        return tileIDbyDirection.get(direction);
    }
}
