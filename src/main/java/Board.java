import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Board {
    private final Tile[][] board;
    private final int WIDTH = 50; //x
    private final int HEIGHT = 50;//y

    public Board() throws MapGenerationException {
        board = new Tile[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                board[x][y] = new Tile();
            }

        }
//        for (int y = 0; y < HEIGHT; y++) {
//            board[0][y].collapse(RuleList.MOUNTAINID);
//            board[WIDTH - 1][y].collapse(RuleList.OCEANID);
//            propagate(WIDTH - 1, y);
//            propagate(0, y);
//        }
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
        //returns the amount of collapsed tiles
        board[x][y].collapse();
//        if (!checkIfPlacedTileIsRight(x, y)) {
//            throw new IllegalArgumentException();
//        }
        propagate(x, y);
    }

    private PropagationResultLists propagateOneTile(int x, int y, EDirection direction,
                                                    ArrayList<ETileContent> newTileContent, PropagationResultLists propagationResultLists) {
        ArrayList<Board.Pair> toCollapse = propagationResultLists.toCollapse();
        HashMap<Board.Pair, ArrayList<ETileContent>> toPropagate = propagationResultLists.toPropagate();
        if (doCoordinatesFitForDirection(x, y, direction)) {
            PropagationResponseEntity response = board[x][y].propagate(direction, newTileContent);
            if (response.isHasCollapsed()) {
                toCollapse.add(new Pair(x , y));
            } else if (response.isHasChangedPossibility()) {
                toPropagate.put(new Pair(x , y), response.getNewPossibilities());
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
        int lowestEntropy = ETileContent.values().length;
        ArrayList<Pair> lowestEntropyTiles = new ArrayList<>();
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                if (!board[x][y].isCollapsed() && board[x][y].getPossibleTileStatesLeft() <= lowestEntropy) {
                    if (board[x][y].getPossibleTileStatesLeft() < lowestEntropy) {
                        //lowestEntropyTiles = new ArrayList<>();
                        //lowestEntropy = board[x][y].getPossibleTileStatesLeft();
                    }
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

    private boolean checkIfPlacedTileIsRight(int x, int y) {
        if (!board[x][y].isCollapsed()) {
            return true;
        }
        if (((x > 0 && board[x-1][y].isCollapsed()) && !board[x - 1][y].getContent().getRuleList().canThisBeHere(EDirection.LEFT, board[x][y].getPossibleTileContentLeft()))) {
            return false;
        }
        if (((x < WIDTH - 1 && board[x + 1][y].isCollapsed()) && !board[x + 1][y].getContent().getRuleList().canThisBeHere(EDirection.RIGHT, board[x][y].getPossibleTileContentLeft()))) {
            return false;
        }
        if (((y > 0 && board[x][y - 1].isCollapsed()) && !board[x][y - 1].getContent().getRuleList().canThisBeHere(EDirection.ABOVE, board[x][y].getPossibleTileContentLeft()))) {
            return false;
        }
        if (((y < HEIGHT - 1 && board[x][y + 1].isCollapsed()) && !board[x][y + 1].getContent().getRuleList().canThisBeHere(EDirection.BELOW, board[x][y].getPossibleTileContentLeft()))) {
            return false;
        }
        if (((x > 0 && y > 0 && board[x - 1][y - 1].isCollapsed()) && !board[x - 1][y - 1].getContent().getRuleList().canThisBeHere(EDirection.LEFT, board[x][y].getPossibleTileContentLeft()))) {
            return false;
        }
        if (((x < WIDTH - 1 && y > 0 && board[x + 1][y - 1].isCollapsed()) && !board[x + 1][y - 1].getContent().getRuleList().canThisBeHere(EDirection.RIGHT, board[x][y].getPossibleTileContentLeft()))) {
            return false;
        }
        if (((x > 0 && y < HEIGHT - 1 && board[x - 1][y + 1].isCollapsed()) && !board[x - 1][y + 1].getContent().getRuleList().canThisBeHere(EDirection.ABOVE, board[x][y].getPossibleTileContentLeft()))) {
            return false;
        }
        if (((x < WIDTH - 1 && y < HEIGHT - 1 && board[x + 1][y + 1].isCollapsed()) && !board[x + 1][y + 1].getContent().getRuleList().canThisBeHere(EDirection.BELOW, board[x][y].getPossibleTileContentLeft()))) {
            return false;
        }
        return true;
    }

    public String print() throws IOException {
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
        return stringBuilder2.toString();
    }
}
