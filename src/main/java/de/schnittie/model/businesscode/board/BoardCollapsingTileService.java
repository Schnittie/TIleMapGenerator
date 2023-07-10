package de.schnittie.model.businesscode.board;

import de.schnittie.model.businesscode.Configuration;
import de.schnittie.model.businesscode.board.propagation.BoardPropagationService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

public class BoardCollapsingTileService {
    public static void collapseATile(PairOfCoordinates pairOfCoordinates, Board board)  {
        if (!board.getTile(pairOfCoordinates).isCollapsed()){
            board.setTile(pairOfCoordinates, board.getTile(pairOfCoordinates).collapse());
            BoardPropagationService.startPropagation(pairOfCoordinates, board);
        }
    }
    private static void collapseATile(PairOfCoordinates pairOfCoordinates, Board board, int tileId)  {
        if (!board.getTile(pairOfCoordinates).isCollapsed()){
            board.setTile(pairOfCoordinates, board.getTile(pairOfCoordinates).collapse(tileId));
            BoardPropagationService.startPropagation(pairOfCoordinates, board);
        }
    }
    public static void collapseAllEasy(Collection<PairOfCoordinates> coordinatesCollection, Board board){
        Random random = new Random();
        ArrayList<ArrayList<Integer>> easyTiles= Configuration.getInstance().getEasyTiles();
        ArrayList<Integer> easyTileSet = easyTiles.get(random.nextInt(easyTiles.size()));
        for (PairOfCoordinates coordinates: coordinatesCollection) {
            collapseATile(coordinates, board,easyTileSet.get(random.nextInt(easyTileSet.size())));
        }
    }
    public static void forceCollapse(Collection<PairOfCoordinates> coordinatesCollection, Board board, int tileID){
        for (PairOfCoordinates coordinates : coordinatesCollection) {
            board.setTile(coordinates, board.getTile(coordinates).collapse(tileID));
        }
        for (PairOfCoordinates coordinates : coordinatesCollection) {
            BoardPropagationService.startPropagation(coordinates, board);
        }
    }
}
