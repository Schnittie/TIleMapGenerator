package de.schnittie.model.businesscode.board.splitting;

import de.schnittie.model.businesscode.Configuration;
import de.schnittie.model.businesscode.board.Board;
import de.schnittie.model.businesscode.board.BoardCollapsingTileService;
import de.schnittie.model.businesscode.board.PairOfCoordinates;

import java.util.*;
//TODO
// Stop calling utils a "Service" :D
// conventions DO matter :P
public class BoardSplittingService {
    private static final int MINIMAL_BOARD_HEIGHT = 50;
    private static final int MINIMAL_BOARD_WIDTH = 50;

    public static HashMap<PairOfCoordinates, Board> splitBoardIntoSmallerShelledBoards(Board board) {
        //a "shelled" board is a board where all the outer edges are collapsed
        //TODO: Mulitithreaded Recursive Splitting
        System.out.println("Splitting board...");
//        if (board.getHeight() <= MINIMAL_BOARD_HEIGHT && board.getWidth() <= MINIMAL_BOARD_WIDTH) {
//            System.out.println("board is too small to split");
//            HashMap<PairOfCoordinates, Board> coordinatesSubBoardMap = new HashMap<>(1);
//            coordinatesSubBoardMap.put(new PairOfCoordinates(0, 0), board);
//            return coordinatesSubBoardMap;
//        }
        HashMap<PairOfCoordinates, BoardCornerCoordinates> boardToCornerMap = vorbereitingForSplitting(board);
        HashMap<PairOfCoordinates, Board> coordinatesSubBoardMap = new HashMap<>();
        for (PairOfCoordinates coordinates : boardToCornerMap.keySet()) {
            BoardCornerCoordinates corner = boardToCornerMap.get(coordinates);
            int minX = corner.MinXMinY().x();
            int minY = corner.MinXMinY().y();
            int maxX = corner.MaxXMaxY().x();
            int maxY = corner.MaxXMaxY().y();
            coordinatesSubBoardMap.put(coordinates, new Board(maxX - minX + 1, maxY - minY + 1));
            for (int x = minX; x <= maxX; x++) {
                for (int y = minY; y <= maxY; y++) {
                    coordinatesSubBoardMap.get(coordinates).setTile(x - minX, y - minY, board.getTile(x, y));
                }
            }
        }
        return coordinatesSubBoardMap;
    }


    private static HashMap<PairOfCoordinates, BoardCornerCoordinates> vorbereitingForSplitting(Board board) {
        //returns a map of the Corners of each SubBoard
        int numberOfBoardsAlongHeight = (board.getHeight() / MINIMAL_BOARD_HEIGHT) +
                (board.getHeight() % MINIMAL_BOARD_HEIGHT == 0 ? 0 : 1);
        int numberOfBoardsAlongWidth = (board.getWidth() / MINIMAL_BOARD_WIDTH) +
                (board.getWidth() % MINIMAL_BOARD_WIDTH == 0 ? 0 : 1);


        //the Pair of Coordinates here refers to the "Coordinates" of the Subboard within the bigger Board
        //not to be confused with the Coordinates of the Tiles within a Board
        // 0 0 is still the upper left corner
        HashMap<PairOfCoordinates, BoardCornerCoordinates> boardToCornerMap = getBoardCoordinatesToBoardCornersMap(
                board, numberOfBoardsAlongHeight, numberOfBoardsAlongWidth);
        //Collapse all Tiles at the Edges between SubBoards => Shelling them
        System.out.println("Collapsing Tiles along Splitlines ...");
        if (Configuration.getInstance().isSimpleMap()){
            BoardCollapsingTileService.collapseAll(getListOfCoordinatesForSplitLines(boardToCornerMap), board);
        } else {
            BoardCollapsingTileService.collapseAllEasy(getListOfCoordinatesForSplitLines(boardToCornerMap), board);
        }
        return boardToCornerMap;
    }

    private static HashMap<PairOfCoordinates, BoardCornerCoordinates> getBoardCoordinatesToBoardCornersMap(
            Board board, int numberOfBoardsAlongHeight, int numberOfBoardsAlongWidth) {
        HashMap<PairOfCoordinates, BoardCornerCoordinates> boardToCornerMap = new HashMap<>();
        for (int y = 0; y < numberOfBoardsAlongHeight; y++) {
            int minY = y * MINIMAL_BOARD_HEIGHT;
            int maxY = Math.min(((y + 1) * MINIMAL_BOARD_HEIGHT), board.getHeight() - 1);
            for (int x = 0; x < numberOfBoardsAlongWidth; x++) {
                int minX = x * MINIMAL_BOARD_HEIGHT;
                int maxX = Math.min(((x + 1) * MINIMAL_BOARD_WIDTH), board.getWidth() - 1);
                boardToCornerMap.put(new PairOfCoordinates(x, y), new BoardCornerCoordinates(
                        new PairOfCoordinates(minX, minY), new PairOfCoordinates(maxX, minY),
                        new PairOfCoordinates(minX, maxY), new PairOfCoordinates(maxX, maxY)));
            }
        }
        return boardToCornerMap;
    }

    private static ArrayList<PairOfCoordinates> getListOfCoordinatesForSplitLines(HashMap<PairOfCoordinates, BoardCornerCoordinates> boardToCornerMap) {
        ArrayList<PairOfCoordinates> listOfCoordinatesForSplitLines = new ArrayList<>();
        for (BoardCornerCoordinates corners : boardToCornerMap.values()) {
            listOfCoordinatesForSplitLines.addAll(getListOfCoordinatesForSplitLine(corners));
        }
        return listOfCoordinatesForSplitLines;
    }

    private static ArrayList<PairOfCoordinates> getListOfCoordinatesForSplitLine(BoardCornerCoordinates corners) {
        int minX = corners.MinXMinY().x();
        int minY = corners.MinXMinY().y();
        int maxX = corners.MaxXMaxY().x();
        int maxY = corners.MaxXMaxY().y();
        ArrayList<PairOfCoordinates> listOfCoordinatesOfShell =
                new ArrayList<>(MINIMAL_BOARD_HEIGHT * 2 + MINIMAL_BOARD_WIDTH * 2);
        for (int x = minX; x <= maxX; x++) {
            listOfCoordinatesOfShell.add(new PairOfCoordinates(x, minY));
            listOfCoordinatesOfShell.add(new PairOfCoordinates(x, maxY));
        }
        for (int y = minY; y <= maxY; y++) {
            listOfCoordinatesOfShell.add(new PairOfCoordinates(minX, y));
            listOfCoordinatesOfShell.add(new PairOfCoordinates(maxX, y));
        }
        return listOfCoordinatesOfShell;
    }
}
