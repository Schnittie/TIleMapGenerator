package de.schnittie.model.businesscode.board;

import de.schnittie.model.businesscode.tile.tileObjects.Tile;
import de.schnittie.model.businesscode.tile.tileObjects.TileInProgress;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private final ArrayList<ArrayList<Tile>> board;
    private int width = 150; //x
    private int height = 150;//y
    //x=0 y=0 is the upper left corner

    public Board() {
        board = new ArrayList<>(this.width);
        for (int x = 0; x < this.width; x++) {
            board.add(new ArrayList<Tile>(this.height));
            for (int y = 0; y < this.height; y++) {
                board.get(x).add(new TileInProgress());
            }
        }
    }

    public BoardTO getBoardTO() {
        return BoardTOFactory.getBoardTO(this);
    }
    public List<Tile> getRow(int y){
        return board.get(y);
    }
    public List<ArrayList<Tile>> getBoardAsListofArrayList(){
        return board;
    }
    public void addRows(List<ArrayList<Tile>> rows){
        board.addAll(rows);
        height = board.size();
    }

    public void extendRow(int y, List<Tile> row) {
        board.get(y).addAll(row);
        width = board.get(y).size();
    }

    public Tile getTile(int x, int y) {
        return board.get(x).get(y);
    }

    public Tile setTile(int x, int y, Tile tile) {
        return board.get(x).set(y,tile);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void newTileAt(int x, int y) {
        board.get(x).set(y, new TileInProgress());
    }
}
