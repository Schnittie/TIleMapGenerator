package de.schnittie.model.businesscode.board;

public class BoardFillingServiceThread implements Runnable{
    private final Board board;
    public BoardFillingServiceThread(Board board){
        this.board = board;
    }
    public void fill(Board board) {
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

    @Override
    public void run() {
        fill(this.board);
    }
}


