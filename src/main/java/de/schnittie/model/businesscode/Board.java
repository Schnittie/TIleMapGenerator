package de.schnittie.model.businesscode;

import de.schnittie.model.database.DBinteractions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class Board {
    private final Tile[][] board;
    private final int WIDTH; //x
    private final int HEIGHT;//y
    private final DBinteractions dBinteractions = DBinteractions.getInstance();
    private final HashMap<Integer, Pair> directionChangeMap = dBinteractions.getDirectionChanges();
    private final int numberOfPossibleTiles = dBinteractions.getNumberOfTiles();
    private final int[] possibleTileIDs = dBinteractions.getPossibleTileIDs(numberOfPossibleTiles);
    private final Random random = new Random();

    public Board(int width, int height) throws MapGenerationException {
        WIDTH = width;
        HEIGHT = height;
        board = new Tile[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                board[x][y] = new Tile(numberOfPossibleTiles, possibleTileIDs, random);
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
        board[x][y].collapse();
        propagate(x, y);
    }

    private void propagateOneTile(
            int x, int y, int direction, List<Integer> newTileContent,
            HashMap<Pair, ArrayList<Integer>> propagationResultLists) {
        try {
            ArrayList<Integer> propagationResult = board[x][y].propagate(direction, newTileContent);
            if (propagationResult != null) {
                propagationResultLists.put(new Pair(x, y), propagationResult);
            }
        } catch (MapGenerationException e) {
            try {
                System.out.println("I need to control damage");
                controlDamage(x, y);
                throw new MapGenerationException();
            } catch (MapGenerationException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    private void propagate(int x, int y, List<Integer> newTileContent) throws MapGenerationException {
        HashMap<Pair, ArrayList<Integer>> propagationResultLists = new HashMap<>();
        for (int directionID : directionChangeMap.keySet()) {
            Pair directionChange = directionChangeMap.get(directionID);
            int wouldBeX = directionChange.x() + x;
            int wouldBeY = directionChange.y() + y;
            if (wouldBeX >= 0 && wouldBeX <= WIDTH - 1 && wouldBeY >= 0 && wouldBeY <= HEIGHT - 1) {
                propagateOneTile(wouldBeX, wouldBeY, directionID, newTileContent, propagationResultLists);
            }
        }
        for (Pair pair : propagationResultLists.keySet()) {
            propagate(pair.x(), pair.y(), propagationResultLists.get(pair));
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
        System.out.println(lowestEntropyTiles.size() + " tiles left");
        return lowestEntropyTiles.get(random.nextInt(lowestEntropyTiles.size()));
    }

    public void controlDamage(int problemTileX, int problemTileY) throws MapGenerationException {
        int damageSize = (getWIDTH() * getHEIGHT() >= 1000 ?  10 : 5);
        int damageAreaBorderMinX = Math.max(problemTileX - damageSize, 0);
        int damageAreaBorderMinY = Math.max(problemTileY - damageSize, 0);
        int damageAreaBorderMaxX = Math.min(problemTileX + damageSize, getWIDTH());
        int damageAreaBorderMaxY = Math.min(problemTileY + damageSize, getHEIGHT());

        for (int x = damageAreaBorderMinX + 1; x < damageAreaBorderMaxX - 1; x++) {
            for (int y = damageAreaBorderMinY + 1; y < damageAreaBorderMaxY - 1; y++) {
                board[x][y] = new Tile(numberOfPossibleTiles, possibleTileIDs, random);
            }
        }
        for (int x = damageAreaBorderMinX; x < damageAreaBorderMaxX; x++) {
            propagate(x, damageAreaBorderMinY);
            propagate(x, damageAreaBorderMaxY - 1);
        }
        for (int y = damageAreaBorderMinY; y < damageAreaBorderMaxY; y++) {
            propagate(damageAreaBorderMinX, y);
            propagate(damageAreaBorderMaxX - 1, y);
        }
    }

    public Tile[][] getBoard() {
        return board;
    }


    public int getWIDTH() {
        return WIDTH;
    }

    public int getHEIGHT() {
        return HEIGHT;
    }
}


