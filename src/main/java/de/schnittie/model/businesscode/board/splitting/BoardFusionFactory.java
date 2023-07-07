package de.schnittie.model.businesscode.board.splitting;

import de.schnittie.model.businesscode.board.Board;
import de.schnittie.model.businesscode.board.PairOfCoordinates;
import de.schnittie.model.businesscode.tile.tileObjects.Tile;

import java.util.ArrayList;
import java.util.HashMap;

public class BoardFusionFactory {

    public static Board fuseMapOfBoardsIntoOneBoard(HashMap<PairOfCoordinates, Board> coordinatesBoardMap) {
        try {
            ArrayList<Board> horizontalSlices = createHorizontalSliceList(coordinatesBoardMap);
            return verticallyFuzeHorizontalSlices(horizontalSlices);
        } catch (InvalidDimensionException | BoardFusionException e) {
            throw new RuntimeException(e);
        }

    }

    private static Board verticallyFuzeHorizontalSlices(ArrayList<Board> horizontalSlices) throws InvalidDimensionException, BoardFusionException {
        if (horizontalSlices.isEmpty()) {
            throw new RuntimeException("Trying to VerticallyFuze empty list");
        }
        Board fuzedBoard = horizontalSlices.get(0);
        for (int i = 1; i < horizontalSlices.size(); i++) {
            fuseBoardsAlongXAxis(fuzedBoard, horizontalSlices.get(i));
        }
        return fuzedBoard;
    }

    private static ArrayList<Board> createHorizontalSliceList(HashMap<PairOfCoordinates, Board> coordinatesBoardMap) throws InvalidDimensionException, BoardFusionException {
        int x = 1;
        int y = 0;
        ArrayList<Board> listOfVerticalSlices = new ArrayList<>();
        while (coordinatesBoardMap.containsKey(new PairOfCoordinates(0, y))) {
            //TODO maybe this fails because of pointer
            while (coordinatesBoardMap.containsKey(new PairOfCoordinates(x, y))) {
                fuseBoardsAlongYAxis(coordinatesBoardMap.get(new PairOfCoordinates(0, y)),
                        coordinatesBoardMap.get(new PairOfCoordinates(x, y)));
                x++;
            }
            listOfVerticalSlices.add(coordinatesBoardMap.get(new PairOfCoordinates(0, y)));
            y++;
            x = 1;
        }
        return listOfVerticalSlices;
    }

    public static Board fuseBoardsAlongXAxis(Board innerBoard, Board outerBoard) throws BoardFusionException, InvalidDimensionException {
        //taking two already formed Boards and fusing them
        if (innerBoard.getWidth() != outerBoard.getWidth()) {
            throw new InvalidDimensionException("These boards don't have the right Dimensions to fit");
        }
        for (int x = 0; x < getLowerXEdge(innerBoard).size(); x++) {
            if (getLowerXEdge(innerBoard).get(x) != getUpperXEdge(outerBoard).get(x)) {
                throw new BoardFusionException("These Boards don't match on the X axis");
            }
        }
        ArrayList<ArrayList<Tile>> outerBoardAsArrayList = outerBoard.getBoardAsListofArrayList();
        outerBoardAsArrayList.remove(0);
        innerBoard.addRows(outerBoard.getBoardAsListofArrayList());
        return innerBoard;
    }

    public static Board fuseBoardsAlongYAxis(Board innerBoard, Board outerBoard) throws BoardFusionException, InvalidDimensionException {
        //taking two already formed Boards and fusing them
        if (innerBoard.getHeight() != outerBoard.getHeight()) {
            throw new InvalidDimensionException("These boards don't have the right Dimensions to fit");
        }
        for (int y = 0; y < getRightYEdge(innerBoard).size(); y++) {
            if (getRightYEdge(innerBoard).get(y) != getLeftYEdge(outerBoard).get(y)) {
                throw new BoardFusionException("These Boards don't match on the Y axis");
            }
        }
        for (int y = 0; y < outerBoard.getHeight(); y++) {
            ArrayList<Tile> outerBoardRow = outerBoard.getRow(y);
            outerBoardRow.remove(0);
            innerBoard.extendRow(y, outerBoardRow);
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
        ArrayList<Tile> lowerXEdge = new ArrayList<>(board.getWidth());
        for (int i = 0; i < board.getWidth(); i++) {
            lowerXEdge.add(board.getTile(i, board.getHeight() - 1));
        }
        return lowerXEdge;
    }

    private static ArrayList<Tile> getUpperXEdge(Board board) {
        ArrayList<Tile> upperXEdge = new ArrayList<>(board.getWidth());
        for (int i = 0; i < board.getWidth(); i++) {
            upperXEdge.add(board.getTile(i, 0));
        }
        return upperXEdge;
    }
}
