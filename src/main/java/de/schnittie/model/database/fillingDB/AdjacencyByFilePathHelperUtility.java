package de.schnittie.model.database.fillingDB;

import de.schnittie.model.logic.board.PairOfCoordinates;
import de.schnittie.model.database.DBinteractions;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class AdjacencyByFilePathHelperUtility {
    private final static HashMap<Integer, PairOfCoordinates> directionChanges = DBinteractions.getInstance().getDirectionChanges();
    private final static HashMap<Integer, String> directionNames = DBinteractions.getInstance().getDirectionNameMap();
    private final static HashMap<Integer, Integer> directionOpposites = DBinteractions.getInstance().getReverseDirection();

    public static int extraRuleValidly(int thisTileId, int thatTileId, int direction, HashMap<Integer, String> tilePathMap) {

        String thisTileFileName = getFileName(thisTileId, tilePathMap);
        String thatTileFileName = getFileName(thatTileId, tilePathMap);
        ArrayList<Integer> thatTileNumbers = ExtractingNumberUtility.extractNumbers(getFileName(thatTileId, tilePathMap));
        ArrayList<Integer> thisTileNumbers = ExtractingNumberUtility.extractNumbers(getFileName(thisTileId, tilePathMap));
        thisTileNumbers.remove(0);
        thatTileNumbers.remove(0);
        PairOfCoordinates pairOfCoordinates = directionChanges.get(direction);
        if (thisTileFileName.contains("NeighbourRules") && !thatTileFileName.contains("NeighbourRules")) {
            if ((thisTileFileName.contains(directionNames.get((direction))))) {
                return 0;
            }
            return -1;
        }
        if (!thisTileFileName.contains("NeighbourRules") && thatTileFileName.contains("NeighbourRules")) {
            if ((thatTileFileName.contains(directionNames.get(directionOpposites.get(direction))))) {
                return 0;
            }
            return -1;
        }
        if ((thisTileFileName.contains("NeighbourRules") && thatTileFileName.contains("NeighbourRules"))) {
            if (twoFileNamesAreFromTheSameTile(thisTileFileName, thatTileFileName)) {
                if (thisTileNumbers.get(0) + pairOfCoordinates.x() == thatTileNumbers.get(0) && thisTileNumbers.get(1) +
                        pairOfCoordinates.y() == thatTileNumbers.get(1)) {
                    return 1;
                }
            }
            return -1;
        }
        return 0;
    }

    private static boolean twoFileNamesAreFromTheSameTile(String thisTileFileName, String thatTileFileName) {
        int i = 0;
        while (thisTileFileName.charAt(i) != '.') {
            if (thisTileFileName.charAt(i) != thatTileFileName.charAt(i)) {
                return false;
            }
            i++;
        }
        return true;
    }

    private static String getFileName(int tileId, HashMap<Integer, String> tilePathMap) {
        File tileFile = new File(tilePathMap.get(tileId));
        return tileFile.getName();
    }
}




