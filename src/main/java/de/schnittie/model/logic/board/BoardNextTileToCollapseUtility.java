package de.schnittie.model.logic.board;

import de.schnittie.model.logic.tile.tileObjects.Tile;

import java.util.ArrayList;
import java.util.Random;
public class BoardNextTileToCollapseUtility {
    private static final Random random = new Random();
    public static final int MAX_ENTROPY = 10000;

    public static PairOfCoordinates getNextTile(Board board) {
        return getRandomNonCollapsedTile(board);
    }

    public static PairOfCoordinates getRandomNonCollapsedTile(Board board) {
        ArrayList<PairOfCoordinates> lowestEntropyTiles = new ArrayList<>();
        for (int x = 0; x < board.getWidth(); x++) {
            for (int y = 0; y < board.getHeight(); y++) {
                Tile currentTile = board.getTile(x, y);
                if (!currentTile.isCollapsed()) {
                    if (currentTile.getPossibleTileStatesLeft() == 1) {
                        board.setTile(x, y, currentTile.collapse());
                    } else {
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

