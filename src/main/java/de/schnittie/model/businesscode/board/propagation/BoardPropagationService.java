package de.schnittie.model.businesscode.board.propagation;

import de.schnittie.model.businesscode.Configuration;
import de.schnittie.model.businesscode.MapGenerationException;
import de.schnittie.model.businesscode.board.Board;
import de.schnittie.model.businesscode.damageControll.BoardDamageControlService;
import de.schnittie.model.businesscode.board.PairOfCoordinates;
import de.schnittie.model.businesscode.tile.tileObjects.Tile;
import de.schnittie.model.businesscode.tile.tileObjects.TileCollapsed;

import java.util.HashMap;
import java.util.List;

public class BoardPropagationService {
    // everything is static - maybe this is a Util, not a service?
    public static void startPropagation(PairOfCoordinates coordinates, Board board){
        startPropagation(coordinates.x(),coordinates.y(),board);
    }
    public static void startPropagation(int x, int y, Board board) {
        try {
            BoardPropagationQueue boardPropagationQueue = new BoardPropagationQueue();
            addNewEntriesToQueue(new PairOfCoordinates(x, y), boardPropagationQueue, board, board.getTile(x, y).getPossibleTileContentLeft());
            workThroughPropagationQueue(boardPropagationQueue, board);
        } catch (MapGenerationException e) {
            try {
                System.out.println("I need to control damage");
                BoardDamageControlService.controlDamage(x, y, board);
            } catch (MapGenerationException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    private static void workThroughPropagationQueue(BoardPropagationQueue boardPropagationQueue, Board board) throws MapGenerationException {
        BoardPropagationToDo boardPropagationToDo = boardPropagationQueue.dequeue();
        while (boardPropagationToDo != null) {
            propagate(boardPropagationToDo, boardPropagationQueue, board);
            boardPropagationToDo = boardPropagationQueue.dequeue();
        }
    }

    private static void propagate(BoardPropagationToDo boardPropagationToDo, BoardPropagationQueue boardPropagationQueue,
                                  Board board) throws MapGenerationException {
        boolean propagationRecieverHasChangedPossibilites = false;
        Tile tile = board.getTile(boardPropagationToDo.coordinates().x(), boardPropagationToDo.coordinates().y());
        if (tile.isCollapsed()){
            return;
        }
        for (int direction : boardPropagationToDo.instruction().getAffectedDirections()) {
            if (tile.propagate(direction, boardPropagationToDo.instruction().getNewTileContentForDirection(direction))){
                propagationRecieverHasChangedPossibilites = true;
            }
        }
        if (propagationRecieverHasChangedPossibilites) {
            if (tile.isCollapsed()){
                board.setTile(boardPropagationToDo.coordinates().x(), boardPropagationToDo.coordinates().y(),
                        new TileCollapsed(tile.getContent()));
            }
            addNewEntriesToQueue(boardPropagationToDo.coordinates(), boardPropagationQueue, board, board.getTile(
                    boardPropagationToDo.coordinates().x(), boardPropagationToDo.coordinates().y()).getPossibleTileContentLeft());
        }
    }

    private static void addNewEntriesToQueue(PairOfCoordinates coordinates, BoardPropagationQueue boardPropagationQueue,
                                             Board board, List<Integer> newContentOfPropagationSender) throws MapGenerationException {
        HashMap<Integer, PairOfCoordinates> directionChangeMap = Configuration.getInstance().getDirectionChanges();
        for (int directionID : directionChangeMap.keySet()) {
            PairOfCoordinates directionChange = directionChangeMap.get(directionID);
            int wouldBeX = directionChange.x() + coordinates.x();
            int wouldBeY = directionChange.y() + coordinates.y();
            if (wouldBeX >= 0 && wouldBeX <= board.getWidth() - 1 && wouldBeY >= 0 && wouldBeY <= board.getHeight() - 1) {
                boardPropagationQueue.enqueue(new PairOfCoordinates(wouldBeX, wouldBeY), directionID, newContentOfPropagationSender);

            }
        }
    }
}
