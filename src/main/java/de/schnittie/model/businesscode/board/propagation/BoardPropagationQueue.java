package de.schnittie.model.businesscode.board.propagation;

import de.schnittie.model.businesscode.MapGenerationException;
import de.schnittie.model.businesscode.board.PairOfCoordinates;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class BoardPropagationQueue {
    private Queue<PairOfCoordinates> queueOfTilesToPropagate = new LinkedList<>();
    private HashMap<PairOfCoordinates, BoardPropagationInstructionForOneTile> coordinatesInstructionMap = new HashMap<>();
    public void enqueue(PairOfCoordinates coordinates, int direction, List<Integer> newTileContent) throws MapGenerationException {
        if (queueOfTilesToPropagate.contains(coordinates)){
            coordinatesInstructionMap.get(coordinates).addInstruction(direction, newTileContent);
        }
        else {
            queueOfTilesToPropagate.add(coordinates);
            coordinatesInstructionMap.put(coordinates, new BoardPropagationInstructionForOneTile(direction, newTileContent));
        }
    }
    public BoardPropagationToDo dequeue(){
        if (queueOfTilesToPropagate.isEmpty()){
            return null;
        }
        PairOfCoordinates dequeuedPair = queueOfTilesToPropagate.poll();
        BoardPropagationToDo dequeuedToDo = new BoardPropagationToDo(dequeuedPair, coordinatesInstructionMap.get(dequeuedPair));
        coordinatesInstructionMap.remove(dequeuedPair);
        return dequeuedToDo;
    }
}
