package de.schnittie.model.businesscode.board;

import de.schnittie.model.businesscode.MapGenerationException;
import de.schnittie.model.businesscode.board.splitting.BoardSplittingService;

public class BoardManipulator {
    private final Board board;

    public BoardManipulator() throws MapGenerationException {
        board = new Board();
        BoardSplittingService.vorbereitingForSplitting(board);
    }

    public void fill() throws MapGenerationException {
        long timeAtStartOfTest = System.currentTimeMillis();
        while (true) {
            PairOfCoordinates nextTile = BoardNextTileToCollapseService.getNextTile(board);
            if (nextTile.x() == -10) {
                //this means all Tiles are collapsed
                System.out.println("Map creation completed in: " + (System.currentTimeMillis() - timeAtStartOfTest) + " Millisecond");
                return;
            }
            BoardCollapsingTileService.collapseATile(nextTile, board);
        }
    }

    public BoardTO getBoardTO() {
        return board.getBoardTO();
    }
}


