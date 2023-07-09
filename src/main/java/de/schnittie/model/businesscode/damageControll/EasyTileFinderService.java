package de.schnittie.model.businesscode.damageControll;

import de.schnittie.model.businesscode.tile.PossibleAdjacencyProvider;
import de.schnittie.model.businesscode.tile.TileDataProvider;
import de.schnittie.model.database.DBinteractions;

import java.util.ArrayList;
import java.util.HashMap;

public class EasyTileFinderService {
    private static TileDataProvider tileDataProvider = TileDataProvider.getInstance();

    private static ArrayList<Integer> findEasyTiles() {
        PossibleAdjacencyProvider possibleAdjacencyProvider = tileDataProvider.getPossibleAdjacencyProvider();
        ArrayList<Integer> listOfEasyTiles = new ArrayList<>();
        for (Integer tileID : DBinteractions.getInstance().getPossibleTileIDs()) {
            if (possibleAdjacencyProvider.doTheseTilesMatchInEveryDirection(tileID, tileID)) {
                listOfEasyTiles.add(tileID);
            }
        }
        return listOfEasyTiles;
    }

    public static ArrayList<ArrayList<Integer>> findEasyTileSets() {
        ArrayList<Integer> easyTileIds = findEasyTiles();
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
                if (tileDataProvider.getPossibleAdjacencyProvider().doTheseTilesMatchInEveryDirection(tileId, easyTileList.get(0))) {
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
}
