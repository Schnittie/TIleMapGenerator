package de.schnittie.model.businesscode.board;

import de.schnittie.model.businesscode.board.propagation.BoardPropagationService;

import java.util.Collection;

public class BoardCollapsingTileService {
    // This class sounds more like an Uitl-Class, not a service
    // while Reading "service", I'd expect this class to be instantiated instead of calling#
    // some static utility-(*cough*)Methods.
    public static void collapseATile(PairOfCoordinates pairOfCoordinates, Board board)  {
        if (!board.getTile(pairOfCoordinates).isCollapsed()){
            board.setTile(pairOfCoordinates,
                    board.getTile(pairOfCoordinates).collapse());
            BoardPropagationService.startPropagation(pairOfCoordinates, board);
        }
    }
    public static void collapseAll(Collection<PairOfCoordinates> coordinatesCollection, Board board){
        for (PairOfCoordinates coordinates: coordinatesCollection) {
            collapseATile(coordinates, board);
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
