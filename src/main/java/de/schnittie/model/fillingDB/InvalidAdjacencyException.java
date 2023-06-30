package de.schnittie.model.fillingDB;

import de.schnittie.model.database.DBinteractions;

import java.util.HashSet;
import java.util.Map;

public class InvalidAdjacencyException extends Throwable {
    private final Map<Integer, HashSet<Integer>> mapOfIDsToSetsOfDirections;
    private final Map<Integer, String> iDToFilePathMap = DBinteractions.getInstance().getFilePathMap();
    private final Map<Integer, String> directionNameMap = DBinteractions.getInstance().getDirectionNameMap();

    public InvalidAdjacencyException(Map<Integer, HashSet<Integer>> mapOfIDsToSetsOfDirections) {
        this.mapOfIDsToSetsOfDirections = mapOfIDsToSetsOfDirections;
    }

    public void printFaultyTiles() {
        for (Integer id : mapOfIDsToSetsOfDirections.keySet()) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Faulty Tile with ID: ");
            stringBuilder.append(id);
            stringBuilder.append(" has Filepath: ");
            stringBuilder.append(iDToFilePathMap.get(id));
            stringBuilder.append(" and cannot be ");
            StringBuilder missingDirectionsStringBuilder = new StringBuilder();
            for (Integer directionID : directionNameMap.keySet()) {
                if (!mapOfIDsToSetsOfDirections.get(id).contains(directionID)){
                    missingDirectionsStringBuilder.append(directionNameMap.get(directionID));
                    missingDirectionsStringBuilder.append(", ");
                }
            }
            if (missingDirectionsStringBuilder.isEmpty()){
                continue;
            }
            stringBuilder.append(missingDirectionsStringBuilder);
            stringBuilder.append("any other Tile");
            System.out.println(stringBuilder);
        }
    }


}
