package de.schnittie.model.businesscode;

import de.schnittie.model.businesscode.board.Pair;
import de.schnittie.model.database.DBinteractions;
import de.schnittie.model.database.RuleTO;

import java.util.ArrayList;
import java.util.HashMap;

public class Configuration {
    private static final DBinteractions dBinteractions = DBinteractions.getInstance();
    private static final HashMap<Integer, Pair> directionChangeMap = dBinteractions.getDirectionChanges();
    private static final HashMap<Integer, String> filePathMap = dBinteractions.getFilePathMap();
    private static final ArrayList<RuleTO> rules = dBinteractions.getAllRules();
    private static final ArrayList<Integer> possibleTileIDs = dBinteractions.getPossibleTileIDs();
    private static final HashMap<Integer, Integer> probabilityMap = dBinteractions.getProbabilityMap();
    private static final String dbFolder = DBinteractions.getDbFolder();


    public static ArrayList<RuleTO> getRules(){
        return rules;
    }
    public static HashMap<Integer, Integer> getProbabilityMap() {
        return probabilityMap;
    }
    public static ArrayList<Integer> getPossibleTileIDs() {
        return possibleTileIDs;
    }
    public static HashMap<Integer, Pair> getDirectionChanges() {
        return directionChangeMap;
    }
    public static HashMap<Integer, String> getFilePathMap() {
        return filePathMap;
    }
    public static String getDbFolder() {
        return dbFolder;
    }
}
