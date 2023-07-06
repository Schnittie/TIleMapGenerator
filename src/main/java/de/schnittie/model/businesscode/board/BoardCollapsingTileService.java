package de.schnittie.model.businesscode.board;

import de.schnittie.model.businesscode.board.propagation.BoardPropagationService;

import java.util.Collection;

public class BoardCollapsingTileService {
    public static void collapseATile(PairOfCoordinates pairOfCoordinates, Board board)  {
        board.setTile(pairOfCoordinates.x(), pairOfCoordinates.y(),
                board.getTile(pairOfCoordinates.x(), pairOfCoordinates.y()).collapse());
        BoardPropagationService.propagate(pairOfCoordinates.x(), pairOfCoordinates.y(), board);
    }
    public static void collapseAll(Collection<PairOfCoordinates> coordinatesCollection, Board board){
        for (PairOfCoordinates coordinates: coordinatesCollection) {
            collapseATile(coordinates, board);
        }
    }
}
