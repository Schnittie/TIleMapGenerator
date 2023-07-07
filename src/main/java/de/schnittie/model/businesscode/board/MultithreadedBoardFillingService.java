package de.schnittie.model.businesscode.board;

import de.schnittie.model.businesscode.board.splitting.BoardFusionFactory;
import de.schnittie.model.businesscode.board.splitting.BoardSplittingService;

import java.util.ArrayList;
import java.util.HashMap;

public class MultithreadedBoardFillingService {

    //Takes an empty Board and returns a filled board
    public static Board generateBoard(Board board) {
        HashMap<PairOfCoordinates, Board> coordinatesSubBoardMap =
                BoardSplittingService.splitBoardIntoSmallerShelledBoards(board);
        ArrayList<BoardFillingServiceThread> listOfFillingThreads = new ArrayList<>(coordinatesSubBoardMap.size());

        for (Board subBoard : coordinatesSubBoardMap.values()) {
            listOfFillingThreads.add(new BoardFillingServiceThread(subBoard));
        }
        ArrayList<Thread> listOfThreads = new ArrayList<>(listOfFillingThreads.size());
        for (BoardFillingServiceThread thread: listOfFillingThreads){
            Thread currentThread =new Thread(thread);
            listOfThreads.add(currentThread);
            currentThread.start();
        }

        for(Thread t: listOfThreads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        //wenn alle Threads fertig sind
        return BoardFusionFactory.fuseMapOfBoardsIntoOneBoard(coordinatesSubBoardMap);
    }
}
