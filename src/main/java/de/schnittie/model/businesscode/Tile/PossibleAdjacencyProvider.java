package de.schnittie.model.businesscode.Tile;

import de.schnittie.model.businesscode.MapGenerationException;
import de.schnittie.model.database.DBinteractions;
import de.schnittie.model.database.RuleTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class PossibleAdjacencyProvider {
    private HashMap<Integer, PossibleAdjacencyHolderForOneTile> adjacencyByID = new HashMap<>();

    PossibleAdjacencyProvider(DBinteractions dBinteractions) {
        int that_tile = -1;
        int next_to = -1;
        HashMap<Integer, HashSet<Integer>> possibleAdjacencyWIP = new HashMap<>();
        HashSet<Integer> adjacentIDListForOneDirection = new HashSet<>();

        for (RuleTO ruleTO : dBinteractions.getAllRules()) {
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
            adjacentIDListForOneDirection.add(ruleTO.this_tile());//this tile
        }
        possibleAdjacencyWIP.put(next_to,adjacentIDListForOneDirection);
        adjacencyByID.put(that_tile, new PossibleAdjacencyHolderForOneTile(possibleAdjacencyWIP));
        adjacencyByID.remove(-1);
    }

    public List<Integer> canThisBeHere(List<Integer> tileInQuestion, int whereIamRelativeToCaller, List<Integer> listOfPossibilities) throws MapGenerationException {
        //listOfPossibilities is the List of the possibilities the Tile from where the propagation is coming from can be
        //tileInQuestion is the tile this tile could be

//        "SELECT DISTINCT this_tile FROM rule " +
//                "WHERE this_tile IN (" + getParameterPlaceholders(tileInQuestion.size()) + ") " +
//                "AND next_to = ? " +
//                "AND that_tile IN (" + getParameterPlaceholders(listOfPossibilities.size()) + ")");

        HashSet<Integer> possibilities = new HashSet<>();
        for (int possibleIDofCaller: listOfPossibilities) {
            possibilities.addAll(adjacencyByID.get(possibleIDofCaller).getPossibleTilesByDirection(whereIamRelativeToCaller));
        }
        possibilities.retainAll(tileInQuestion);
        return new ArrayList<Integer>(possibilities);
    }

}
