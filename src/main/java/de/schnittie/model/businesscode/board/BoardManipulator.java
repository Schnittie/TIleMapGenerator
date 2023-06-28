package de.schnittie.model.businesscode.board;

import de.schnittie.model.businesscode.MapGenerationException;
import de.schnittie.model.businesscode.board.propagation.BoardPropagationService;

public class BoardManipulator {
    private final Board board;
    public BoardManipulator(int width, int height) throws MapGenerationException {
        board = new Board(width, height);
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
            collapseATile(nextTile.x(), nextTile.y());
        }
    }
    private void collapseATile(int x, int y) throws MapGenerationException {
        board.setTile(x,y, board.getTile(x, y).collapse()) ;
        BoardPropagationService.propagate(x, y, board);
    }
    public BoardTO getBoardTO() {
        return board.getBoardTO();
    }
}


