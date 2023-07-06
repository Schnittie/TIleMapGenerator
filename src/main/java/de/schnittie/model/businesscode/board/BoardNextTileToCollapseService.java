package de.schnittie.model.businesscode.board;

import de.schnittie.model.businesscode.tile.tileObjects.Tile;

import java.util.ArrayList;
import java.util.Random;

public class BoardNextTileToCollapseService {
    private static final Random random = new Random();
    public static final int MAX_ENTROPY = 100;

    public static PairOfCoordinates getNextTile(Board board){
        //return getNextTileWithLowestEntropy(board);
        return getRandomNonCollapsedTile(board);
    }

    public static PairOfCoordinates getRandomNonCollapsedTile(Board board) {
        ArrayList<PairOfCoordinates> lowestEntropyTiles = new ArrayList<>();
        for (int x = 0; x < board.getWidth(); x++) {
            for (int y = 0; y < board.getHeight(); y++) {
                if (!board.getTile(x, y).isCollapsed()) {
                    lowestEntropyTiles.add(new PairOfCoordinates(x, y));
                }
            }
        }
        if (lowestEntropyTiles.isEmpty()) {
            return new PairOfCoordinates(-10, -10);
        }
        System.out.println(lowestEntropyTiles.size() + " tiles left");
        return lowestEntropyTiles.get(random.nextInt(lowestEntropyTiles.size()));
    }

    public static PairOfCoordinates getNextTileWithLowestEntropy(Board board) {
        ArrayList<PairOfCoordinates> lowestEntropyTiles = new ArrayList<>();
        int minEntropy = MAX_ENTROPY;
        for (int x = 0; x < board.getWidth(); x++) {
            for (int y = 0; y < board.getHeight(); y++) {
                Tile tile = board.getTile(x, y);
                if (!tile.isCollapsed()) {
                    if (tile.getPossibleTileStatesLeft() == minEntropy) {
                        lowestEntropyTiles.add(new PairOfCoordinates(x, y));
                    } else if (tile.getPossibleTileStatesLeft() < minEntropy) {
                        minEntropy = tile.getPossibleTileStatesLeft();
                        lowestEntropyTiles = new ArrayList<>();
                        lowestEntropyTiles.add(new PairOfCoordinates(x, y));
                    }
                }
            }
        }
        if (lowestEntropyTiles.isEmpty()) {
            return new PairOfCoordinates(-10, -10);
        }
        return lowestEntropyTiles.get(random.nextInt(lowestEntropyTiles.size()));
    }
}

