package de.schnittie.model.businesscode.board;

import de.schnittie.model.businesscode.MapGenerationException;

public class BoardFillingService {
    private final Board board;

    public BoardFillingService() throws MapGenerationException {
        board = new Board(160,160);
        System.out.println("board created");
    }

    public static void fill(Board board) throws MapGenerationException {
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


