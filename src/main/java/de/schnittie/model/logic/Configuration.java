package de.schnittie.model.logic;

import de.schnittie.model.logic.board.PairOfCoordinates;
import de.schnittie.model.database.DBinteractions;
import de.schnittie.model.database.RuleTO;

import java.util.ArrayList;
import java.util.HashMap;

public class Configuration {
    private static Configuration configuration = new Configuration();
    public static Configuration getInstance() {
        return configuration;
    }
    public static void reloadConfiguration(){
        configuration = new Configuration();
    }

    private  final DBinteractions dBinteractions = DBinteractions.getInstance();
    private  final HashMap<Integer, PairOfCoordinates> directionChangeMap = dBinteractions.getDirectionChanges();
    private  final HashMap<Integer, String> filePathMap = dBinteractions.getFilePathMap();
    private  final HashMap<String, Integer> getFilenameToIDMap = dBinteractions.getFilenameToIDMap();
    private  final ArrayList<RuleTO> rules = dBinteractions.getAllRules();
    private  final ArrayList<Integer> possibleTileIDs = dBinteractions.getPossibleTileIDs();
    private  final HashMap<Integer, Integer> probabilityMap = dBinteractions.getProbabilityMap();
    private final ArrayList<ArrayList<Integer>> easyTiles = dBinteractions.getEasyTiles();
    private final boolean isSimpleMap = possibleTileIDs.size() < 300;
    private  final String dbFolder = DBinteractions.getInstance().getDbFolder();


    public  ArrayList<RuleTO> getRules(){
        return rules;
    }
    public  HashMap<Integer, Integer> getProbabilityMap() {
        return probabilityMap;
    }
    public  ArrayList<Integer> getPossibleTileIDs() {
        return possibleTileIDs;
    }
    public  HashMap<Integer, PairOfCoordinates> getDirectionChanges() {
        return directionChangeMap;
    }
    public  HashMap<Integer, String> getFilePathMap() {return filePathMap;}
    public  HashMap<String, Integer> getFilenameToIDMap() {return getFilenameToIDMap;}
    public  String getDbFolder() {
        return dbFolder;
    }
    public  ArrayList<ArrayList<Integer>> getEasyTiles(){
        return easyTiles;
    }
    public boolean isSimpleMap(){
        return isSimpleMap;
    }
}
