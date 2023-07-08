package de.schnittie.model.businesscode.board;

import de.schnittie.model.businesscode.board.propagation.BoardPropagationService;

import java.util.Collection;

public class BoardCollapsingTileService {
    public static void collapseATile(PairOfCoordinates pairOfCoordinates, Board board)  {
        if (!board.getTile(pairOfCoordinates).isCollapsed()){
            board.setTile(pairOfCoordinates,
                    board.getTile(pairOfCoordinates).collapse());
            BoardPropagationService.propagate(pairOfCoordinates, board);
        }
    }
    public static void collapseAll(Collection<PairOfCoordinates> coordinatesCollection, Board board){
        for (PairOfCoordinates coordinates: coordinatesCollection) {
            collapseATile(coordinates, board);
        }
    }
}
