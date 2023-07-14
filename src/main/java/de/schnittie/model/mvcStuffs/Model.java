package de.schnittie.model.mvcStuffs;

import de.schnittie.model.logic.board.Board;
import de.schnittie.model.logic.board.BoardImageFactory;
import de.schnittie.model.logic.board.MultithreadedBoardFillingUtility;
import de.schnittie.view.ModelListener;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Model {

    private List<ModelListener> listeners = new ArrayList<>();
    private HashMap<File, Integer> tilesToAdd = new HashMap<>();
    private BufferedImage lastMap;

    public BufferedImage generateMap(int width, int height) {
        long timeBefore = System.currentTimeMillis();
        Board board = new Board(width, height);
        board = MultithreadedBoardFillingUtility.generateBoard(board);
        System.out.println("Finished generating Map in " + (System.currentTimeMillis() - timeBefore) + " Milliseconds");
        lastMap = BoardImageFactory.renderBoardImage(board.getBoardTO());
        notifyListeners(new NewMapEvent(lastMap));
        return lastMap;
    }

    private void notifyListeners(MapGeneratorEvent event) {
        for (ModelListener listener : listeners) {
            listener.update(event);
        }
    }

    public void addListener(ModelListener listener) {
        listeners.add(listener);
    }

    public void removeListener(ModelListener listener) {
        listeners.remove(listener);
    }

    public void addTiles(String filepath, int rotation) {
        tilesToAdd.put(new File(filepath), rotation);
    }
    public void generateMap() {
    }
}
