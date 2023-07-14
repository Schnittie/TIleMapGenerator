package de.schnittie.model.businesscode.board;

public class BoardFillingServiceThread implements Runnable{
    private final Board board;
    public BoardFillingServiceThread(Board board){
        this.board = board;
    }
    public void fill(Board board) {
        while (true) {
            PairOfCoordinates nextTile = BoardNextTileToCollapseService.getNextTile(board);
            if (nextTile.x() == -10) {
                //this means all Tiles are collapsed
                return;
            }
            BoardCollapsingTileUtility.collapseATile(nextTile, board);
        }
    }

    @Override
    public void run() {
        fill(this.board);
    }
}


