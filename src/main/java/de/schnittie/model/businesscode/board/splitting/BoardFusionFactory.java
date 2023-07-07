package de.schnittie.model.businesscode.board.splitting;

import de.schnittie.model.businesscode.board.Board;
import de.schnittie.model.businesscode.tile.tileObjects.Tile;

import java.util.ArrayList;

public class BoardFusionFactory {

//    public static Board fuseMapOfBoardsIntoOneBoard(HashMap<PairOfCoordinates, Board> coordinatesBoardMap){
//        Board HorizontalSlice = createHorizontalSlice(coordinatesBoardMap);
//    }
//
//    private static Board createHorizontalSlice(HashMap<PairOfCoordinates, Board> coordinatesBoardMap) {
//
//    }

    public static Board fuseBoardsAlongXAxis(Board innerBoard, Board outerBoard) throws BoardFusionException, InvalidDimensionException {
        //taking two already formed Boards and fusing them
        if (innerBoard.getWidth() != outerBoard.getWidth()) {
            throw new InvalidDimensionException("These boards don't have the right Dimensions to fit");
        }
        for (int x = 0; x < getLowerXEdge(innerBoard).size(); x++) {
            if (getLowerXEdge(innerBoard).get(x) != getUpperXEdge(outerBoard).get(x)){
                throw new BoardFusionException("These Boards don't match on the X axis");
            }
        }
        for (int y = 1; y < outerBoard.getHeight(); y++) {
            innerBoard.addRows(outerBoard.getBoardAsListofArrayList());
        }
        return innerBoard;
    }

    public static Board fuseBoardsAlongYAxis(Board innerBoard, Board outerBoard) throws BoardFusionException, InvalidDimensionException {
        //taking two already formed Boards and fusing them
        if (innerBoard.getHeight() != outerBoard.getHeight()) {
            throw new InvalidDimensionException("These boards don't have the right Dimensions to fit");
        }
        for (int y = 0; y < getRightYEdge(innerBoard).size(); y++) {
            if (getRightYEdge(innerBoard).get(y) != getLeftYEdge(outerBoard).get(y)){
                throw new BoardFusionException("These Boards don't match on the Y axis");
            }
        }
        for (int y = 1; y < outerBoard.getHeight(); y++) {
            innerBoard.extendRow(y, outerBoard.getRow(y));
        }
        return innerBoard;
    }

    private static ArrayList<Tile> getLeftYEdge(Board board) {
        ArrayList<Tile> leftYEdge = new ArrayList<>(board.getHeight());
        for (int i = 0; i < board.getHeight(); i++) {
            leftYEdge.add(board.getTile(0, i));
        }
        return leftYEdge;
    }

    private static ArrayList<Tile> getRightYEdge(Board board) {
        ArrayList<Tile> rightYEdge = new ArrayList<>(board.getHeight());
        for (int i = 0; i < board.getHeight(); i++) {
            rightYEdge.add(board.getTile(board.getWidth() - 1, i));
        }
        return rightYEdge;
    }

    private static ArrayList<Tile> getLowerXEdge(Board board) {
        ArrayList<Tile> LowerXEdge = new ArrayList<>(board.getHeight());
        for (int i = 0; i < board.getHeight(); i++) {
            rightYEdge.add(board.getTile(board.getWidth() - 1, i));
        }
        return rightYEdge;
    }

    private static ArrayList<Tile> getUpperXEdge(Board board) {
        return (ArrayList<Tile>) board.getRow(0);
    }
}
