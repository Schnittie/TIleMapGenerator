package de.schnittie.model.businesscode.Tile;

import java.util.HashMap;
import java.util.HashSet;

public record PossibleAdjacencyHolderForOneTile(HashMap<Integer, HashSet<Integer>> TileIDbyDirection) {
    public HashSet<Integer> getPossibleTilesByDirection(int direction){
        return TileIDbyDirection.get(direction);
    }
}
