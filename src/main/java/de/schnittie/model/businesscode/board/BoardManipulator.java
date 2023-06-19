package de.schnittie.model.businesscode.board;

import de.schnittie.model.businesscode.Configuration;
import de.schnittie.model.businesscode.MapGenerationException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class BoardManipulator {
    private final Board board;
    private final HashMap<Integer, Pair> directionChangeMap = Configuration.getDirectionChanges();
    private final Random random = new Random();

    public BoardManipulator(int width, int height) throws MapGenerationException {
        board = new Board(width, height);
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
        BoardPropagaionService.propagate(x, y, board);
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

    public BoardTO getBoardTO() {
        return board.getBoardTO();
    }

    public int getWIDTH() {
        return board.getWIDTH();
    }

    public int getHEIGHT() {
        return board.getHEIGHT();
    }
}


