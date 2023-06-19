package de.schnittie.model.businesscode.board;

import de.schnittie.model.businesscode.tile.Tile;

public class Board {
    private final Tile[][] board;
    private final int WIDTH; //x
    private final int HEIGHT;//y
    public Board(int width, int height){
        WIDTH = width;
        HEIGHT = height;
        board = new Tile[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                board[x][y] = new Tile();
            }
        }
    }
    public BoardTO getBoardTO(){
        return BoardTOFactory.getBoardTO(this);
    }
    public Tile getTile(int x, int y){
        return board[x][y];
    }
    public int getWIDTH() {
        return WIDTH;
    }

    public int getHEIGHT() {
        return HEIGHT;
    }
    public void newTileAt(int x, int y){
        board[x][y] = new Tile();
    }
}
