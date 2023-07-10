package de.schnittie.model.businesscode.board;

import de.schnittie.model.businesscode.tile.tileObjects.Tile;
import de.schnittie.model.businesscode.tile.tileObjects.TileInProgress;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private final ArrayList<ArrayList<Tile>> board;
    private int width; //x
    private int height;//y
    //x=0 y=0 is the upper left corner

    public Board(int width, int height) {
        this.width = width;
        this.height = height;
        board = new ArrayList<>(this.width);
        for (int y = 0; y < this.height; y++) {
            board.add(new ArrayList<Tile>(this.height));
            for (int x = 0; x < this.width; x++) {
                board.get(y).add(new TileInProgress());
            }
        }
    }

    public BoardTO getBoardTO() {
        return BoardTOFactory.getBoardTO(this);
    }

    public ArrayList<Tile> getRow(int y) {
        return board.get(y);
    }

    public ArrayList<ArrayList<Tile>> getBoardAsListofArrayList() {
        return board;
    }

    public void addRows(List<ArrayList<Tile>> rows) {
        board.addAll(rows);
        height = board.size();
    }

    public void extendRow(int y, List<Tile> row) {
        board.get(y).addAll(row);
        width = board.get(y).size();
    }

    public Tile getTile(PairOfCoordinates coordinates) {
        return getTile(coordinates.x(), coordinates.y());
    }

    public Tile getTile(int x, int y) {
        return board.get(y).get(x);
    }

    public Tile setTile(PairOfCoordinates coordinates, Tile tile) {
        return setTile(coordinates.x(), coordinates.y(), tile);
    }

    public Tile setTile(int x, int y, Tile tile) {
        return board.get(y).set(x, tile);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    // unused?
    public void newTileAt(int x, int y) {
        board.get(x).set(y, new TileInProgress());
    }
}
