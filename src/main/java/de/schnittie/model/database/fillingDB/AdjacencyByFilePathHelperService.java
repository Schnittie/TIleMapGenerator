package de.schnittie.model.database.fillingDB;

import de.schnittie.model.businesscode.board.PairOfCoordinates;
import de.schnittie.model.database.DBinteractions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AdjacencyByFilePathHelperService {
    private final static HashMap<Integer, PairOfCoordinates> directionChanges = DBinteractions.getInstance().getDirectionChanges();
    private final static HashMap<Integer, String> directionNames = DBinteractions.getInstance().getDirectionNameMap();
    private final static HashMap<Integer, Integer> directionOpposites = DBinteractions.getInstance().getReverseDirection();
    private static ArrayList<Integer> extractNumbers(String input) {
        ArrayList<Integer> numbers = new ArrayList<>(4);

        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(input);

        while (matcher.find()) {
            String numberString = matcher.group();
            int number = Integer.parseInt(numberString);
            numbers.add(number);
        }
        numbers.remove(0);
        return numbers;
    }

    public static int extraRuleValidly(int thisTileId, int thatTileId, int direction, HashMap<Integer, String> tilePathMap) {

        ArrayList<Integer> thatTileNumbers = extractNumbers(tilePathMap.get(thatTileId));
        ArrayList<Integer> thisTileNumbers = extractNumbers(tilePathMap.get(thisTileId));
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
                System.out.println("true " + thisTileId + " "+ thatTileId + " "+ direction);
                return 1;
            }
            return -1;
        }
        return 0;
    }
}




