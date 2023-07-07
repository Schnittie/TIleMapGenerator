package de.schnittie.model.mvcStuffs;

import de.schnittie.model.businesscode.board.Board;
import de.schnittie.model.businesscode.board.BoardFillingServiceThread;
import de.schnittie.model.businesscode.board.BoardImageFactory;
import de.schnittie.model.businesscode.board.MultithreadedBoardFillingService;
import de.schnittie.view.ModelListener;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Model {

    private List<ModelListener> listeners = new ArrayList<>();
    private BoardFillingServiceThread boardFillingServiceThread;
    private HashMap<File, Integer> tilesToAdd = new HashMap<>();
    private BufferedImage lastMap;

    public BufferedImage generateMap() {
        long timeBefore = System.currentTimeMillis();
        Board board = new Board(100,100);
        MultithreadedBoardFillingService.generateBoard(board);
        System.out.println(System.currentTimeMillis()-timeBefore);
        lastMap = BoardImageFactory.generateBoardImage(board.getBoardTO());
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

    public void generateRules() {
        //TODO where to generate these rules to?
        //TileCreation.addTiles(tilesToAdd);
    }

    public BufferedImage getLastMap() {
        return lastMap;
    }
}
