package de.schnittie.model.businesscode.board;

import de.schnittie.model.businesscode.Configuration;
import de.schnittie.model.businesscode.MapGenerationException;
import de.schnittie.model.businesscode.tile.Tile;
import de.schnittie.model.businesscode.tile.TileDataProvider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class BoardManipulator {
    private final Board board;
    private final HashMap<Integer, Pair> directionChangeMap = Configuration.getDirectionChanges();
    private final Random random = new Random();
    private final TileDataProvider tileDataProvider = TileDataProvider.getInstance();

    public BoardManipulator(int width, int height) throws MapGenerationException {
        board = new Board(width, height, tileDataProvider);
    }

    public void fill() throws MapGenerationException {
        while (true) {
            Pair randomTileWithLowEntropy = getRandomTileWithLowEntropy();
            if (randomTileWithLowEntropy.x() == -10) {
                //this means all Tiles are collapsed
                return;
            }
            collapseATile(randomTileWithLowEntropy.x(), randomTileWithLowEntropy.y());
        }

    }

    private void collapseATile(int x, int y) throws MapGenerationException {
        board.getTile(x, y).collapse();
        propagate(x, y);
    }

    private void propagateOneTile(
            int x, int y, int direction, List<Integer> newTileContent,
            HashMap<Pair, ArrayList<Integer>> propagationResultLists) {
        try {
            ArrayList<Integer> propagationResult = board.getTile(x, y).propagate(direction, newTileContent);
            if (propagationResult != null) {
                propagationResultLists.put(new Pair(x, y), propagationResult);
            }
        } catch (MapGenerationException e) {
            try {
                System.out.println("I need to control damage");
                controlDamage(x, y);
            } catch (MapGenerationException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    private void propagate(int x, int y, List<Integer> newTileContent) {
        HashMap<Pair, ArrayList<Integer>> propagationResultLists = new HashMap<>();
        for (int directionID : directionChangeMap.keySet()) {
            Pair directionChange = directionChangeMap.get(directionID);
            int wouldBeX = directionChange.x() + x;
            int wouldBeY = directionChange.y() + y;
            if (wouldBeX >= 0 && wouldBeX <= board.getWIDTH() - 1 && wouldBeY >= 0 && wouldBeY <= board.getHEIGHT() - 1) {
                propagateOneTile(wouldBeX, wouldBeY, directionID, newTileContent, propagationResultLists);
            }
        }
        for (Pair pair : propagationResultLists.keySet()) {
            propagate(pair.x(), pair.y(), propagationResultLists.get(pair));
        }
    }

    private void propagate(int x, int y) {
        propagate(x, y, board.getTile(x, y).getPossibleTileContentLeft());
    }

    private Pair getRandomTileWithLowEntropy() {
        ArrayList<Pair> lowestEntropyTiles = new ArrayList<>();
        for (int x = 0; x < board.getWIDTH(); x++) {
            for (int y = 0; y < board.getHEIGHT(); y++) {
                if (!board.getTile(x, y).isCollapsed()) {
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
        int damageSize = (getWIDTH() * getHEIGHT() >= 1000 ? 10 : 5);
        int damageAreaBorderMinX = Math.max(problemTileX - damageSize, 0);
        int damageAreaBorderMinY = Math.max(problemTileY - damageSize, 0);
        int damageAreaBorderMaxX = Math.min(problemTileX + damageSize, getWIDTH());
        int damageAreaBorderMaxY = Math.min(problemTileY + damageSize, getHEIGHT());

        for (int x = damageAreaBorderMinX + 1; x < damageAreaBorderMaxX - 1; x++) {
            for (int y = damageAreaBorderMinY + 1; y < damageAreaBorderMaxY - 1; y++) {
                board.setTile(x, y, new Tile(tileDataProvider));
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

    public BoardTO getBoardTO() {
        return board.getBoardTO().setFilePathMap(Configuration.getFilePathMap());
    }

    public int getWIDTH() {
        return board.getWIDTH();
    }

    public int getHEIGHT() {
        return board.getHEIGHT();
    }
}


