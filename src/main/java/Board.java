import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Board {
    private final Tile[][] board;
    private final int WIDTH; //x
    private final int HEIGHT;//y
    private int amountCollapsed = 0;

    public Board(int width, int height) throws MapGenerationException {
        WIDTH = width;
        HEIGHT = height;
        board = new Tile[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                board[x][y] = new Tile();
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
        System.out.println(Math.divideExact(++amountCollapsed * 100, HEIGHT*WIDTH) + "%");
        board[x][y].collapse();
        propagate(x, y);
    }

    private PropagationResultLists propagateOneTile(int x, int y, EDirection direction,
                                                    ArrayList<ETileContent> newTileContent, PropagationResultLists propagationResultLists) {
        ArrayList<Board.Pair> toCollapse = propagationResultLists.toCollapse();
        HashMap<Board.Pair, ArrayList<ETileContent>> toPropagate = propagationResultLists.toPropagate();
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

    private boolean doCoordinatesFitForDirection(int x, int y, EDirection direction) {
        switch (direction) {
            case ABOVE -> {
                return y >= 0;
            }
            case BELOW -> {
                return y <= HEIGHT - 1;
            }
            case RIGHT -> {
                return x <= WIDTH - 1;
            }
            case LEFT -> {
                return x >= 0;
            }
            case TOPLEFT -> {
                return doCoordinatesFitForDirection(x, y, EDirection.ABOVE) && doCoordinatesFitForDirection(x, y, EDirection.LEFT);
            }
            case TOPRIGHT -> {
                return doCoordinatesFitForDirection(x, y, EDirection.ABOVE) && doCoordinatesFitForDirection(x, y, EDirection.RIGHT);
            }
            case DOWNLEFT -> {
                return doCoordinatesFitForDirection(x, y, EDirection.BELOW) && doCoordinatesFitForDirection(x, y, EDirection.LEFT);
            }
            case DOWNRIGHT -> {
                return doCoordinatesFitForDirection(x, y, EDirection.BELOW) && doCoordinatesFitForDirection(x, y, EDirection.RIGHT);
            }
            default -> {
                return false;
            }
        }
    }

    private void propagate(int x, int y, ArrayList<ETileContent> newTileContent) throws MapGenerationException {
        ArrayList<Pair> toCollapse = new ArrayList<>(8);
        HashMap<Pair, ArrayList<ETileContent>> toPropagate = new HashMap<>(8);
        PropagationResultLists propagationResultLists = new PropagationResultLists(toCollapse, toPropagate);
        propagationResultLists = propagateOneTile(x - 1, y, EDirection.LEFT, newTileContent, propagationResultLists);
        propagationResultLists = propagateOneTile(x + 1, y, EDirection.RIGHT, newTileContent, propagationResultLists);
        propagationResultLists = propagateOneTile(x, y - 1, EDirection.ABOVE, newTileContent, propagationResultLists);
        propagationResultLists = propagateOneTile(x, y + 1, EDirection.BELOW, newTileContent, propagationResultLists);
        propagationResultLists = propagateOneTile(x - 1, y - 1, EDirection.TOPLEFT, newTileContent, propagationResultLists);
        propagationResultLists = propagateOneTile(x - 1, y + 1, EDirection.DOWNLEFT, newTileContent, propagationResultLists);
        propagationResultLists = propagateOneTile(x + 1, y - 1, EDirection.TOPRIGHT, newTileContent, propagationResultLists);
        propagationResultLists = propagateOneTile(x + 1, y + 1, EDirection.DOWNRIGHT, newTileContent, propagationResultLists);
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

    record Pair(int x, int y) {
        @Override
        public String toString() {
            return (x + " " + y);
        }
    }

    public void print() throws IOException {
        System.out.println("-----------------");
        StringBuilder stringBuilder2 = new StringBuilder();
        for (int y = 0; y < HEIGHT; y++) {
            StringBuilder stringBuilder = new StringBuilder();
            for (int x = 0; x < WIDTH; x++) {
                stringBuilder.append(" ");
                stringBuilder.append(board[x][y].isCollapsed() ? board[x][y].getContent().getCharacter() : "U");
                stringBuilder.append(" ");
            }
            System.out.println(stringBuilder);
            stringBuilder2.append(stringBuilder);
            stringBuilder2.append("\n");
        }
        File file = new File("map.txt");
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        writer.write(stringBuilder2.toString());
        writer.close();
    }

    public Tile[][] getBoard() {
        return board;
    }
}
