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
        //TODO: ask artur about this (Generating the board images gets called multiple times)
//        ExecutorService executorService = ExecutorServiceProvider.getLinkedBlockingQueueExecutor();
//        for (Thread t : listOfThreads) {
//            executorService.submit(t);
//        }
//        executorService.shutdown();
//        boolean finishedThread = false;
//        try {
//            finishedThread = !executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
//        } catch (InterruptedException e) {
//            throw new RuntimeException("Unexpected Interruption");
//        }
//        if (finishedThread) System.out.println("Threads didn't finish");

        System.out.println("Fusing SubBoards together...");
        return BoardFusionFactory.fuseMapOfBoardsIntoOneBoard(coordinatesSubBoardMap);
    }
}
