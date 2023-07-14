package de.schnittie.model.businesscode.board;

public class BoardTO {
    private final int[][] boardOfIDs;

    private final int WIDTH; //x
    private final int HEIGHT;//y
    public BoardTO(int[][] boardOfIDs, int WIDTH, int HEIGHT) {
        this.boardOfIDs = boardOfIDs;
        this.WIDTH = WIDTH;
        this.HEIGHT = HEIGHT;
    }
    public int getIDat(int x, int y){
        return boardOfIDs[x][y];
    }
    public int getWIDTH() {
        return WIDTH;
    }
    public int getHEIGHT() {
        return HEIGHT;
    }
    public void setIDat(int x, int y, int id){
        boardOfIDs[x][y]=id;
    }
}
