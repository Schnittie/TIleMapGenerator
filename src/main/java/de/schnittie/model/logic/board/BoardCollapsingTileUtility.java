package de.schnittie.model.businesscode.board;

import de.schnittie.model.businesscode.Configuration;
import de.schnittie.model.businesscode.board.propagation.BoardPropagationUtility;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;


public class BoardCollapsingTileUtility {


    public static void collapseATile(PairOfCoordinates pairOfCoordinates, Board board) {
        if (!board.getTile(pairOfCoordinates).isCollapsed()) {
            board.setTile(pairOfCoordinates, board.getTile(pairOfCoordinates).collapse());
            BoardPropagationUtility.startPropagation(pairOfCoordinates, board);
        }
    }

    private static void collapseATile(PairOfCoordinates pairOfCoordinates, Board board, int tileId) {
        if (!board.getTile(pairOfCoordinates).isCollapsed()) {
            board.setTile(pairOfCoordinates, board.getTile(pairOfCoordinates).collapse(tileId));
            BoardPropagationUtility.startPropagation(pairOfCoordinates, board);
        }
    }

    public static void collapseAll(ArrayList<PairOfCoordinates> listOfCoordinatesForSplitLines, Board board) {
        for (PairOfCoordinates coordinates : listOfCoordinatesForSplitLines) {
            collapseATile(coordinates, board);
        }
    }

    public static void collapseAllEasy(Collection<PairOfCoordinates> coordinatesCollection, Board board) {
        Random random = new Random();
        ArrayList<ArrayList<Integer>> easyTiles = Configuration.getInstance().getEasyTiles();
        ArrayList<Integer> easyTileSet = findBiggestInnerList(easyTiles);
        for (PairOfCoordinates coordinates : coordinatesCollection) {
            collapseATile(coordinates, board, easyTileSet.get(random.nextInt(easyTileSet.size())));
        }
    }

    private static ArrayList<Integer> findBiggestInnerList(ArrayList<ArrayList<Integer>> outerList) {
        int maxSize = 0;
        ArrayList<Integer> biggestInnerList = null;
        for (ArrayList<Integer> innerList : outerList) {
            if (innerList.size() > maxSize) {
                maxSize = innerList.size();
                biggestInnerList = innerList;
            }
        }
        return biggestInnerList;
    }

    public static void forceCollapse(Collection<PairOfCoordinates> coordinatesCollection, Board board, int tileID) {
        Random random = new Random();
        ArrayList<Integer> easyTileSet = findEasyTileSetForExample(Configuration.getInstance().getEasyTiles(), tileID);
        for (PairOfCoordinates coordinates : coordinatesCollection) {
            board.setTile(coordinates, board.getTile(coordinates).collapse(easyTileSet.get(random.nextInt(easyTileSet.size()))));
        }
        for (PairOfCoordinates coordinates : coordinatesCollection) {
            BoardPropagationUtility.startPropagation(coordinates, board);
        }
    }

    private static ArrayList<Integer> findEasyTileSetForExample(ArrayList<ArrayList<Integer>> listOfEasyTileSets, int tileID) {
        for (ArrayList<Integer> tileSet : listOfEasyTileSets) {
            if (tileSet.contains(tileID)) return tileSet;
        }
        throw new RuntimeException("Given Tile ID does not occur in given Sets");
    }
}
