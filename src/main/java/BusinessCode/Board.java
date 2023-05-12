package BusinessCode;

import DataBase.DBinteractions;

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
        int numberOfPossibleTiles = dBinteractions.getNumberOfTiles();
        int[] possibleTileIDs = dBinteractions.getPossibleTileIDs(numberOfPossibleTiles);
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                board[x][y] = new Tile(numberOfPossibleTiles, possibleTileIDs);
            }

        }
    }

    public void fill() throws MapGenerationException {
        while (true) {
            Pair randomTileWithLowEntropy = getRandomTileWithLowEntropy();
            if (randomTileWithLowEntropy.x() == -10) {
                return;
            }
            collapseATile(randomTileWithLowEntropy.x(), randomTileWithLowEntropy.y());
        }

    }

    private void collapseATile(int x, int y) throws MapGenerationException {
        System.out.println(Math.divideExact(++amountCollapsed * 100, HEIGHT * WIDTH) + "%");
        board[x][y].collapse();
        propagate(x, y);
    }

    private PropagationResultLists propagateOneTile(int x, int y, int direction,
                                                    List<Integer> newTileContent, PropagationResultLists propagationResultLists) {
        ArrayList<Pair> toCollapse = propagationResultLists.toCollapse();
        HashMap<Pair, ArrayList<Integer>> toPropagate = propagationResultLists.toPropagate();
        PropagationResponseEntity response = board[x][y].propagate(direction, newTileContent);
        if (response.isHasCollapsed()) {
            toCollapse.add(new Pair(x, y));
        } else if (response.isHasChangedPossibility()) {
            toPropagate.put(new Pair(x, y), response.getNewPossibilities());
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
            if (wouldBeX >= 0 && wouldBeX <= WIDTH - 1 && wouldBeY >= 0 && wouldBeY <= HEIGHT - 1) {
                propagationResultLists = propagateOneTile(wouldBeX, wouldBeY, directionID, newTileContent, propagationResultLists);
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

    public Tile[][] getBoard() {
        return board;
    }
}
