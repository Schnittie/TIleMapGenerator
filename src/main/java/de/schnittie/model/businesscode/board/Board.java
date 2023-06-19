package de.schnittie.model.businesscode.board;

import de.schnittie.model.businesscode.tile.Tile;
import de.schnittie.model.businesscode.tile.TileDataProvider;

public class Board {
    private final Tile[][] board;
    private final int WIDTH; //x
    private final int HEIGHT;//y
    private final TileDataProvider tileDataProvider = TileDataProvider.getInstance();
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
        int[][] ids = new int[getWIDTH()][getHEIGHT()];
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                ids[x][y] = getTile(x,y).getContent();
            }
        }
        return new BoardTO(ids,getWIDTH(),getHEIGHT(),null);
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
    public void setTile(int x, int y, Tile tile){
        board[x][y] = tile;
    }
    public void newTileAt(int x, int y){
        board[x][y] = new Tile();
    }
}
