package de.schnittie.model.businesscode.board;

import de.schnittie.model.businesscode.Configuration;
import de.schnittie.model.businesscode.MapGenerationException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BoardPropagaionService {
    private static final HashMap<Integer, Pair> directionChangeMap = Configuration.getDirectionChanges();
    public static void propagateOneTile(
            int x, int y, int direction, List<Integer> newTileContent,
            HashMap<Pair, ArrayList<Integer>> propagationResultLists, Board board) {
        try {
            ArrayList<Integer> propagationResult = board.getTile(x, y).propagate(direction, newTileContent);
            if (propagationResult != null) {
                propagationResultLists.put(new Pair(x, y), propagationResult);
            }
        } catch (MapGenerationException e) {
            try {
                System.out.println("I need to control damage");
                BoardDamageControlService.controlDamage(x,y, board);
            } catch (MapGenerationException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public static void propagate(int x, int y, List<Integer> newTileContent, Board board) {
        HashMap<Pair, ArrayList<Integer>> propagationResultLists = new HashMap<>();
        for (int directionID : directionChangeMap.keySet()) {
            Pair directionChange = directionChangeMap.get(directionID);
            int wouldBeX = directionChange.x() + x;
            int wouldBeY = directionChange.y() + y;
            if (wouldBeX >= 0 && wouldBeX <= board.getWIDTH() - 1 && wouldBeY >= 0 && wouldBeY <= board.getHEIGHT() - 1) {
                propagateOneTile(wouldBeX, wouldBeY, directionID, newTileContent, propagationResultLists, board);
            }
        }
        for (Pair pair : propagationResultLists.keySet()) {
            propagate(pair.x(), pair.y(), propagationResultLists.get(pair),board);
        }
    }

    public static void propagate(int x, int y, Board board) {
        propagate(x, y, board.getTile(x, y).getPossibleTileContentLeft(), board);
    }
}
