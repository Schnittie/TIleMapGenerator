package de.schnittie.model.businesscode.board.propagation;

import de.schnittie.model.businesscode.Configuration;
import de.schnittie.model.businesscode.MapGenerationException;
import de.schnittie.model.businesscode.board.Board;
import de.schnittie.model.businesscode.board.BoardImageFactory;
import de.schnittie.model.businesscode.board.PairOfCoordinates;

import java.util.HashMap;
import java.util.List;

public class BoardPropagationService {

    public static void propagate(int x, int y, Board board) {
        try {
            BoardPropagationQueue boardPropagationQueue = new BoardPropagationQueue();
            addNewEntriesToQueue(new PairOfCoordinates(x, y), boardPropagationQueue, board, board.getTile(x, y).getPossibleTileContentLeft());
            propagate(boardPropagationQueue, board);
        } catch (MapGenerationException e) {
            System.out.println("damage");
            BoardImageFactory.generateBoardImage(board.getBoardTO());
            throw new RuntimeException();
//            try {
//                System.out.println("I need to control damage");
//                e.printStackTrace();
//                BoardDamageControlService.controlDamage(x, y, board);
//            } catch (MapGenerationException ex) {
//                throw new RuntimeException(ex);
//            }
        }
    }

    private static void propagate(BoardPropagationQueue boardPropagationQueue, Board board) throws MapGenerationException {
        BoardPropagationToDo boardPropagationToDo = boardPropagationQueue.dequeue();
        while (boardPropagationToDo != null) {
            propagate(boardPropagationToDo, boardPropagationQueue, board);
            boardPropagationToDo = boardPropagationQueue.dequeue();
        }
    }

    private static void propagate(BoardPropagationToDo boardPropagationToDo, BoardPropagationQueue boardPropagationQueue, Board board) throws MapGenerationException {
        boolean propagationRecieverHasChangedPossibilites = false;
        for (int direction : boardPropagationToDo.instruction().getAffectedDirections()) {
            if (board.getTile(boardPropagationToDo.coordinates().x(), boardPropagationToDo.coordinates().y()).
                    propagate(direction, boardPropagationToDo.instruction().getNewTileContentForDirection(direction))){
                propagationRecieverHasChangedPossibilites = true;
            }
        }
        if (propagationRecieverHasChangedPossibilites) {
            addNewEntriesToQueue(boardPropagationToDo.coordinates(), boardPropagationQueue, board, board.getTile(boardPropagationToDo.coordinates().x(), boardPropagationToDo.coordinates().y()).getPossibleTileContentLeft());
        }
    }

    private static void addNewEntriesToQueue(PairOfCoordinates coordinates, BoardPropagationQueue boardPropagationQueue, Board board, List<Integer> newContentOfPropagationSender) throws MapGenerationException {
        HashMap<Integer, PairOfCoordinates> directionChangeMap = Configuration.getInstance().getDirectionChanges();
        for (int directionID : directionChangeMap.keySet()) {
            PairOfCoordinates directionChange = directionChangeMap.get(directionID);
            int wouldBeX = directionChange.x() + coordinates.x();
            int wouldBeY = directionChange.y() + coordinates.y();
            if (wouldBeX >= 0 && wouldBeX <= board.getWIDTH() - 1 && wouldBeY >= 0 && wouldBeY <= board.getHEIGHT() - 1) {
                boardPropagationQueue.enqueue(new PairOfCoordinates(wouldBeX, wouldBeY), directionID, newContentOfPropagationSender);

            }
        }
    }
}
