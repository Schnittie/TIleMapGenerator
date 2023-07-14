package de.schnittie.model.logic.board.propagation;

import de.schnittie.model.logic.MapGenerationException;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class BoardPropagationInstructionForOneTile {
    private HashMap<Integer, List<Integer>> directionNewTileContentMap = new HashMap<>(4);


    BoardPropagationInstructionForOneTile(int direction, List<Integer> newTileContent) {
        directionNewTileContentMap.put(direction, newTileContent);
    }

    public void addInstruction(int direction, List<Integer> newTileContent) throws MapGenerationException {
       if(directionNewTileContentMap.containsKey(direction)){
           List<Integer> existingNewTileContent = directionNewTileContentMap.get(direction);

           existingNewTileContent.retainAll(newTileContent);
           if (existingNewTileContent.isEmpty()){
               throw new MapGenerationException();
           }
           directionNewTileContentMap.replace(direction, existingNewTileContent);
       }
       else {
           directionNewTileContentMap.put(direction, newTileContent);
       }
    }
    public Set<Integer> getAffectedDirections(){
        return directionNewTileContentMap.keySet();
    }

    public List<Integer> getNewTileContentForDirection(int direction){
        return directionNewTileContentMap.get(direction);
    }


}
