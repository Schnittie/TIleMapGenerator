package de.schnittie.model.database.fillingDB;

import de.schnittie.model.logic.tile.PossibleAdjacencyProvider;
import de.schnittie.model.logic.tile.TileDataProvider;
import de.schnittie.model.database.DBinteractions;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class EasyTileCreationUtility {
    public static void createEasyTiles(HashMap<Integer, ArrayList<Integer>> tileSocketList, ArrayList<Socket> socketSet, HashMap<Integer, String> tilePathMap) {
        ArrayList<ArrayList<Integer>> easyTileSets = EasyTileCreationUtility.findEasyTiles(tileSocketList,socketSet,tilePathMap);
        DBinteractions.getInstance().putEasyTilesIntoDB(easyTileSets);
    }

    public static ArrayList<ArrayList<Integer>> findEasyTiles(HashMap<Integer, ArrayList<Integer>> tileSocketList, ArrayList<Socket> socketSet, HashMap<Integer, String> tilePathMap) {
        ArrayList<Integer> easyTiles = new ArrayList<>();
        HashMap<Integer, String> tileNameMap = getTileNameMap(tilePathMap);
        for (Integer tileId : tileSocketList.keySet()) {
            if (tileNameMap.get(tileId).contains("Neighbour")) continue;
            if (tileIsEasy(tileId, tileSocketList, socketSet)){
                easyTiles.add(tileId);
            }
        }
        return findEasyTileSets(easyTiles);

    }

    private static boolean tileIsEasy(int tileId, HashMap<Integer, ArrayList<Integer>> tileSocketList, ArrayList<Socket> socketSet) {
        if(!isEverySocketSame(tileId, tileSocketList)){
            return false;
        }
        return isEveryColourInSocketSame(socketSet.get(tileSocketList.get(tileId).get(0)));
    }

    private static boolean isEveryColourInSocketSame(Socket socket) {
        return socket.colours()[0] == socket.colours()[1] && socket.colours()[1] == socket.colours()[2];
    }

    private static boolean isEverySocketSame(int tileId, HashMap<Integer, ArrayList<Integer>> tileSocketList) {
        int firstSocketID = tileSocketList.get(tileId).get(0);
        for (Integer socketID : tileSocketList.get(tileId)) {
            if (socketID != firstSocketID) return false;
        }
        return true;
    }

    public static ArrayList<ArrayList<Integer>> findEasyTileSets(ArrayList<Integer> easyTileIds) {
        PossibleAdjacencyProvider possibleAdjacencyProvider = TileDataProvider.getInstance().getPossibleAdjacencyProvider();
        ArrayList<ArrayList<Integer>> easyTileLists = new ArrayList<>();
        HashMap<Integer, Boolean> hasTileBeenAddedToASet = new HashMap<>(easyTileIds.size());
        for (Integer tileId : easyTileIds) {
            hasTileBeenAddedToASet.put(tileId, false);
        }
        for (Integer tileId : easyTileIds) {
            if (hasTileBeenAddedToASet.get(tileId)) {
                continue;
            }
            for (ArrayList<Integer> easyTileList : easyTileLists) {
                if (easyTileList.isEmpty()) {
                    continue;
                }
                if (possibleAdjacencyProvider.doTheseTilesMatchInEveryDirection(tileId, easyTileList.get(0))) {
                    hasTileBeenAddedToASet.replace(tileId, true);
                    easyTileList.add(tileId);
                    break;
                }
            }
            if (!hasTileBeenAddedToASet.get(tileId)){
                ArrayList<Integer> tileList = new ArrayList<>();
                tileList.add(tileId);
                easyTileLists.add(tileList);
            }
        }
        //run check that no TileList is empty
        for (ArrayList<Integer> tileList: easyTileLists){
            if (tileList.isEmpty()) throw new RuntimeException("Easy tile list is empty");
        }
        return easyTileLists;
    }

    private static HashMap<Integer, String> getTileNameMap(HashMap<Integer, String> tilePathMap) {
        HashMap<Integer, String> tileNameMap = new HashMap<>(tilePathMap.size());
        for (Integer tileId :
                tilePathMap.keySet()) {
            tileNameMap.put(tileId, getFileName(tileId,tilePathMap));
        }
        return tileNameMap;
    }
    private static String getFileName(int tileId, HashMap<Integer, String> tilePathMap) {
        File tileFile = new File(tilePathMap.get(tileId));
        return tileFile.getName();
    }
}
