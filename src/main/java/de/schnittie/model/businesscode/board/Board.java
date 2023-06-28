package de.schnittie.model.businesscode.board;

import de.schnittie.model.businesscode.tile.TileInProgress;
import de.schnittie.model.businesscode.tile.Tile;

import java.util.ArrayList;

public class Board {
    private final ArrayList<ArrayList<Tile>> board;
    private final int WIDTH; //x
    private final int HEIGHT;//y
    //x=0 y=0 is the upper left corner

    public Board(int width, int height) {
        WIDTH = width;
        HEIGHT = height;
        board = new ArrayList<>(WIDTH);
        for (int x = 0; x < WIDTH; x++) {
            board.add(new ArrayList<Tile>(HEIGHT));
            for (int y = 0; y < HEIGHT; y++) {
                board.get(x).add(new TileInProgress());
            }
        }
    }

    public BoardTO getBoardTO() {
        return BoardTOFactory.getBoardTO(this);
    }
    public Tile getTile(int x, int y) {
        return board.get(x).get(y);
    }
    public Tile setTile(int x, int y, Tile tile) {
        return board.get(x).set(y,tile);
    }

    public int getWIDTH() {
        return WIDTH;
    }

    public int getHEIGHT() {
        return HEIGHT;
    }

    public void newTileAt(int x, int y) {
        board.get(x).set(y, new TileInProgress());
    }
}
