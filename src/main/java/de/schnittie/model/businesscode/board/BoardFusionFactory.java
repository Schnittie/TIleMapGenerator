package de.schnittie.model.businesscode.board;

import de.schnittie.model.businesscode.tile.Tile;

import java.util.ArrayList;

public class BoardFusionFactory {
    private static Board fuseBoardsAlongXAxis(Board innerBoard, Board outerBoard) {
        //taking two already formed Boards and fusing them
        if (innerBoard.getWIDTH() != outerBoard.getWIDTH()) {
            throw new RuntimeException("These boards don't have the right Dimensions to fit");
        }

    }

    public ArrayList<Tile> getLeftYEdge(Board board) {
        ArrayList<Tile> leftYEdge = new ArrayList<>(board.getHEIGHT());
        for (int i = 0; i < board.getHEIGHT(); i++) {
            leftYEdge.add(board.getTile(0, i));
        }
        return leftYEdge;
    }

    public ArrayList<Tile> getRightYEdge(Board board) {
        ArrayList<Tile> rightYEdge = new ArrayList<>(board.getHEIGHT());
        for (int i = 0; i < board.getHEIGHT(); i++) {
            rightYEdge.add(board.getTile(board.getWIDTH() - 1, i));
        }
        return rightYEdge;
    }

    public ArrayList<Tile> getLowerXEdge(Board board) {
        ArrayList<Tile> lowerXEdge = new ArrayList<>(board.getWIDTH());
        for (int i = 0; i < board.getWIDTH(); i++) {
            lowerXEdge.add(board.getTile(i, board.getHEIGHT() - 1));
        }
        return lowerXEdge;
    }

    public ArrayList<Tile> getUpperXEdge(Board board) {
        ArrayList<Tile> upperXEdge = new ArrayList<>(board.getWIDTH());
        for (int i = 0; i < board.getWIDTH(); i++) {
            upperXEdge.add(board.getTile(i, 0));
        }
        return upperXEdge;
    }
}
