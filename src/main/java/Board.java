import DataBase.DBinteractions;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class Board {
    private final Tile[][] board;
    private final int WIDTH; //x
    private final int HEIGHT;//y
    private int amountCollapsed = 0;
    private DBinteractions dBinteractions = DBinteractions.getInstance();
    private final HashMap<Integer, Pair> directionChangeMap = dBinteractions.getDirectionChanges();

    public Board(int width, int height) throws MapGenerationException {
        WIDTH = width;
        HEIGHT = height;
        board = new Tile[WIDTH][HEIGHT];
        int[] possibleTileIDs = dBinteractions.getPossibleTileIDs();
        int numberOfPossibleTiles = dBinteractions.getNumberOfTiles();
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                board[x][y] = new Tile(numberOfPossibleTiles, possibleTileIDs);
            }

        }
    }

    public void fill() throws MapGenerationException {
        while (true) {
            Pair randomTileWithLowEntropy = getRandomTileWithLowEntropy();
            if (randomTileWithLowEntropy.x == -10) {
                return;
            }
            collapseATile(randomTileWithLowEntropy.x, randomTileWithLowEntropy.y);
        }

    }

    private void collapseATile(int x, int y) throws MapGenerationException {
        System.out.println(Math.divideExact(++amountCollapsed * 100, HEIGHT * WIDTH) + "%");
        board[x][y].collapse();
        propagate(x, y);
    }

    private PropagationResultLists propagateOneTile(int x, int y, int direction,
                                                    List<Integer> newTileContent, PropagationResultLists propagationResultLists) {
        ArrayList<Board.Pair> toCollapse = propagationResultLists.toCollapse();
        HashMap<Board.Pair, ArrayList<Integer>> toPropagate = propagationResultLists.toPropagate();
        if (doCoordinatesFitForDirection(x, y, direction)) {
            PropagationResponseEntity response = board[x][y].propagate(direction, newTileContent);
            if (response.isHasCollapsed()) {
                toCollapse.add(new Pair(x, y));
            } else if (response.isHasChangedPossibility()) {
                toPropagate.put(new Pair(x, y), response.getNewPossibilities());
            }
        }
        return new PropagationResultLists(toCollapse, toPropagate);
    }

    private boolean doCoordinatesFitForDirection(int x, int y, int direction) {
        Pair directionChange = directionChangeMap.get(direction);
        int wouldBeX = directionChange.x() + x;
        int wouldBeY = directionChange.y() + y;
        return (wouldBeX >= 0 && wouldBeX <= WIDTH - 1 && wouldBeY >= 0 && wouldBeY <= HEIGHT);
    }

    private void propagate(int x, int y, List<Integer> newTileContent) throws MapGenerationException {
        ArrayList<Pair> toCollapse = new ArrayList<>();
        HashMap<Pair, ArrayList<Integer>> toPropagate = new HashMap<>();
        PropagationResultLists propagationResultLists = new PropagationResultLists(toCollapse, toPropagate);

        for (int directionID : directionChangeMap.keySet()) {
            Pair directionChange = directionChangeMap.get(directionID);
            int wouldBeX = directionChange.x() + x;
            int wouldBeY = directionChange.y() + y;
            if (wouldBeX >= 0 && wouldBeX <= WIDTH - 1 && wouldBeY >= 0 && wouldBeY <= HEIGHT){
                propagationResultLists = propagateOneTile(x,y, directionID, newTileContent, propagationResultLists);
            }
        }

        toPropagate = propagationResultLists.toPropagate();
        toCollapse = propagationResultLists.toCollapse();
        for (Pair pair : toPropagate.keySet()) {
            propagate(pair.x(), pair.y(), toPropagate.get(pair));
        }
        for (Pair pair : toCollapse) {
            collapseATile(pair.x(), pair.y());
        }
    }

    private void propagate(int x, int y) throws MapGenerationException {
        propagate(x, y, board[x][y].getPossibleTileContentLeft());
    }

    private Pair getRandomTileWithLowEntropy() {
        ArrayList<Pair> lowestEntropyTiles = new ArrayList<>();
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                if (!board[x][y].isCollapsed()) {
                    lowestEntropyTiles.add(new Pair(x, y));
                }
            }
        }
        if (lowestEntropyTiles.isEmpty()) {
            return new Pair(-10, -10);
        }
        Random random = new Random();
        return lowestEntropyTiles.get(random.nextInt(lowestEntropyTiles.size()));
    }

    public record Pair(int x, int y) {
        @Override
        public String toString() {
            return (x + " " + y);
        }
    }

    public Tile[][] getBoard() {
        return board;
    }
}
