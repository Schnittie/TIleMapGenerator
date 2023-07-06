package de.schnittie.model.businesscode.tile;

import de.schnittie.model.businesscode.Configuration;
import de.schnittie.model.businesscode.MapGenerationException;
import de.schnittie.model.database.RuleTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class PossibleAdjacencyProvider {
    private final HashMap<Integer, PossibleAdjacencyHolderForOneTile> adjacencyByID = new HashMap<>();

    PossibleAdjacencyProvider() {
        int that_tile = -1;
        int next_to = -1;
        HashMap<Integer, HashSet<Integer>> possibleAdjacencyWIP = new HashMap<>();
        HashSet<Integer> adjacentIDListForOneDirection = new HashSet<>();

        for (RuleTO ruleTO : Configuration.getInstance().getRules()) {
            if (ruleTO.next_to() != next_to){

                possibleAdjacencyWIP.put(next_to,adjacentIDListForOneDirection);
                adjacentIDListForOneDirection = new HashSet<>();
                next_to = ruleTO.next_to();

                if (ruleTO.that_tile() != that_tile){
                    adjacencyByID.put(that_tile, new PossibleAdjacencyHolderForOneTile(possibleAdjacencyWIP));
                    possibleAdjacencyWIP = new HashMap<>();
                    that_tile = ruleTO.that_tile();
                }
            }
            adjacentIDListForOneDirection.add(ruleTO.this_tile());
        }
        possibleAdjacencyWIP.put(next_to,adjacentIDListForOneDirection);
        adjacencyByID.put(that_tile, new PossibleAdjacencyHolderForOneTile(possibleAdjacencyWIP));
        adjacencyByID.remove(-1);
    }

    public List<Integer> canThisBeHere(
            List<Integer> tileInQuestion, int whereIamRelativeToCaller, List<Integer> listOfPossibilities)
            throws MapGenerationException {
        //listOfPossibilities is the List of the possibilities the TileInProgress from where the propagation is coming from can be
        //tileInQuestion is the tile this tile could be

        if (listOfPossibilities.isEmpty()) {
            throw new MapGenerationException();
        }

        HashSet<Integer> possibilities = new HashSet<>();
        for (int possibleIDofCaller: listOfPossibilities) {
            possibilities.addAll(adjacencyByID.get(possibleIDofCaller).getPossibleTilesByDirection(whereIamRelativeToCaller));
        }
        possibilities.retainAll(tileInQuestion);
        if (possibilities.isEmpty()) {
            throw new MapGenerationException();
        }
        return new ArrayList<>(possibilities);
    }

}
