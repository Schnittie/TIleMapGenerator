package de.schnittie.model.logic.board;

import de.schnittie.model.logic.board.splitting.BoardFusionUtility;
import de.schnittie.model.logic.board.splitting.BoardSplittingUtility;

import java.util.ArrayList;
import java.util.HashMap;

public class MultithreadedBoardFillingUtility {

    //Takes an empty Board and returns a filled board
    public static Board generateBoard(Board board) {
        HashMap<PairOfCoordinates, Board> coordinatesSubBoardMap =
                BoardSplittingUtility.splitBoardIntoSmallerShelledBoards(board);
        ArrayList<BoardFillingServiceThread> listOfFillingThreads = new ArrayList<>(coordinatesSubBoardMap.size());

        for (Board subBoard : coordinatesSubBoardMap.values()) {
            listOfFillingThreads.add(new BoardFillingServiceThread(subBoard));
        }
        ArrayList<Thread> listOfThreads = new ArrayList<>(listOfFillingThreads.size());
        for (BoardFillingServiceThread thread : listOfFillingThreads) {
            Thread currentThread = new Thread(thread);
            listOfThreads.add(currentThread);
            currentThread.start();
        }
        System.out.println("Collapsing SubBoards...");

        for(Thread t: listOfThreads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println("Fusing SubBoards together...");
        return BoardFusionUtility.fuseMapOfBoardsIntoOneBoard(coordinatesSubBoardMap);
    }
}
