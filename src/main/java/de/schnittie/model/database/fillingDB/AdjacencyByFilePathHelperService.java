package de.schnittie.model.database.fillingDB;

import de.schnittie.model.businesscode.board.PairOfCoordinates;
import de.schnittie.model.database.DBinteractions;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class AdjacencyByFilePathHelperService {
    private final static HashMap<Integer, PairOfCoordinates> directionChanges = DBinteractions.getInstance().getDirectionChanges();
    private final static HashMap<Integer, String> directionNames = DBinteractions.getInstance().getDirectionNameMap();
    private final static HashMap<Integer, Integer> directionOpposites = DBinteractions.getInstance().getReverseDirection();

    public static int extraRuleValidly(int thisTileId, int thatTileId, int direction, HashMap<Integer, String> tilePathMap) {


        ArrayList<Integer> thatTileNumbers = ExtractingNumberService.extractNumbers(getFileName(thatTileId, tilePathMap));
        ArrayList<Integer> thisTileNumbers = ExtractingNumberService.extractNumbers(getFileName(thisTileId, tilePathMap));
        thisTileNumbers.remove(0);
        thatTileNumbers.remove(0);
        PairOfCoordinates pairOfCoordinates = directionChanges.get(direction);
        if (tilePathMap.get(thisTileId).contains("NeighbourRules") && !tilePathMap.get(thatTileId).contains("NeighbourRules")){
            if ((tilePathMap.get(thisTileId).contains(directionNames.get((direction))))){
                return 0;
            }
            return -1;
        }
        if (!tilePathMap.get(thisTileId).contains("NeighbourRules") && tilePathMap.get(thatTileId).contains("NeighbourRules")){
            if ((tilePathMap.get(thatTileId).contains(directionNames.get(directionOpposites.get(direction))))){
                return 0;
            }
            return -1;
        }
        if ((tilePathMap.get(thisTileId).contains("NeighbourRules") && tilePathMap.get(thatTileId).contains("NeighbourRules"))){
            if (thisTileNumbers.get(0) + pairOfCoordinates.x() == thatTileNumbers.get(0) && thisTileNumbers.get(1) + pairOfCoordinates.y() == thatTileNumbers.get(1)) {
                return 1;
            }
            return -1;
        }
        return 0;
    }

    private static String getFileName(int thatTileId, HashMap<Integer, String> tilePathMap) {
        File thatTile = new File(tilePathMap.get(thatTileId));
        String thatTileFileName =  thatTile.getName();
        return thatTileFileName;
    }
}




